package com.example.theotokos;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;


public class CopticActivity extends AppCompatActivity {

    private PDFView pdfView;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private DataCache dataCache;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_coptic);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Objects.requireNonNull(getSupportActionBar()).hide();
        pdfView = findViewById(R.id.pdfView);
        dataCache = DataCache.getInstance(this);
        progressBar = findViewById(R.id.progressBar);

    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = dataCache.getUser();
        String filename = "Secondary.pdf";

        if(user.getLevel().equals( "حضانة"))
            filename = "Ners1.pdf";
        else if(user.getLevel().equals( "أولى ابتدائى") || user.getLevel().equals( "ثانية ابتدائى"))
            filename = "Primary1.pdf";
        else if(user.getLevel().equals( "ثالثة ابتدائى") || user.getLevel().equals( "رابعة ابتدائى"))
            filename = "Primary3.pdf";
        else if(user.getLevel().equals( "خامسة ابتدائى") || user.getLevel().equals( "سادسة ابتدائى"))
            filename = "Primary5.pdf";
        else if(user.getLevel().equals("ثانوى")||user.getLevel().equals("اعدادى")||user.getLevel().equals( "جامعيين و خريجين"))
            filename = "Secondary.pdf";

        boolean isExists = loadPdfFromLocalStorage(filename);

        if(!isExists) {
            StorageReference pdfRef = FirebaseStorage.getInstance().getReference().child(filename);

            auth = FirebaseAuth.getInstance();
            String finalFilename = filename;
            auth.signInAnonymously().addOnSuccessListener(authResult -> {
                // Load PDF from Google Storage
                File localFile = new File(CopticActivity.this.getCacheDir(), finalFilename);

                pdfRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    savePdfToLocalStorage(localFile, finalFilename);
                    pdfView.fromFile(localFile)
                            .defaultPage(0)
                            .enableAnnotationRendering(true)
                            .load();
                    Toast.makeText(CopticActivity.this, "file downloaded successfully", Toast.LENGTH_SHORT).show();
                }).addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    // Update the progress bar with the calculated progress
                    progressBar.setProgress((int) progress);
                    progressBar.setVisibility(View.VISIBLE);
                });
            }).addOnFailureListener(e -> Toast.makeText(CopticActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        }

    }
    private void savePdfToLocalStorage(File pdfFile, String filename) {
        ContentResolver resolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename); // Replace with your desired file name
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        Uri uri = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
        }
        if (uri != null) {
            try (OutputStream outputStream = resolver.openOutputStream(uri)) {
                FileInputStream inputStream = new FileInputStream(pdfFile);
                IOUtils.copy(inputStream, outputStream);
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {

            Toast.makeText(this, "Failed to download PDF", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean loadPdfFromLocalStorage(String filename) {
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+filename; // Replace with the actual file path

        File pdfFile = new File(filePath);

        if (pdfFile.exists()) {
            // Load the PDF directly into the PDFView
            pdfView.fromFile(pdfFile)
                    .defaultPage(0)
                    .enableAnnotationRendering(true)
                    .load();
            return true;
        } else {
            Toast.makeText(this, "PDF not Downloaded", Toast.LENGTH_SHORT).show();
            return false;
            // Handle case where file doesn't exist (optional)
            // You can display a message or offer to download the PDF
        }
    }
}