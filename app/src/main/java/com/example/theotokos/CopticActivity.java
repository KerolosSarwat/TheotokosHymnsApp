package com.example.theotokos;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;


public class CopticActivity extends AppCompatActivity {

    private PDFView pdfView;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
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
        pdfView = findViewById(R.id.pdfView);
    }

    @Override
    protected void onResume() {
        super.onResume();


        boolean isExists = loadPdfFromLocalStorage();

        if(!isExists) {
            StorageReference pdfRef = FirebaseStorage.getInstance().getReference().child("Primary1.pdf");

            auth = FirebaseAuth.getInstance();

            Log.d("onResume: ", "\t" + auth);
            auth.signInAnonymously().addOnSuccessListener(authResult -> {
                // Load PDF from Google Storage
                File localFile = new File(CopticActivity.this.getCacheDir(), "Primary1.pdf");

                pdfRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        pdfView.fromFile(localFile)
                                .defaultPage(0)
                                .enableAnnotationRendering(true)
                                .load();
                        savePdfToLocalStorage(localFile);
                        Toast.makeText(CopticActivity.this, "file downloaded successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }).addOnFailureListener(e -> Toast.makeText(CopticActivity.this, "Signin Field", Toast.LENGTH_SHORT).show());
        }

    }
    private void savePdfToLocalStorage(File pdfFile) {
        ContentResolver resolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Primary1.pdf"); // Replace with your desired file name
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS+"/Primary1.pdf");

        Uri uri = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
        }
        if (uri != null) {
            try (OutputStream outputStream = resolver.openOutputStream(uri)) {
                FileInputStream inputStream = new FileInputStream(pdfFile);
                IOUtils.copy(inputStream, outputStream);
                Log.d("Storage", "PDF saved successfully to: " + uri.toString());
            } catch (IOException e) {
                Log.e("Storage", "Error saving PDF to local storage: " + e.getMessage());
            }
        } else {
            Log.e("Storage", "Failed to insert PDF into Downloads: " + uri);
        }
    }
    private boolean loadPdfFromLocalStorage() {
        Uri uri = null;
        ContentResolver resolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Primary1.pdf"); // Replace with your desired file name
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS+"/Primary1.pdf");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
        }
        String filePath = uri.toString(); //Environment.getExternalStorageDirectory().getPath() + "/downloads/Primary1.pdf"; // Replace with the actual file path


        Log.d("loadPdfFromLocalStorage: ", filePath);
        File pdfFile = new File(filePath);
        if (pdfFile.exists()) {
            pdfView.fromFile(pdfFile)
                    .defaultPage(0)
                    .enableAnnotationRendering(true)
                    .load();
            return true;
        } else {
            return false;
            // Handle case where file doesn't exist
        }
    }
}