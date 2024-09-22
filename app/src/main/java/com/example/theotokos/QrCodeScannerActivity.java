package com.example.theotokos;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class QrCodeScannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scanner);
        FirebaseApp.initializeApp(this);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QrCodeScannerActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                markAttendance(intentResult.getContents());
                //Toast.makeText(getBaseContext(), intentResult.getContents(), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void markAttendance(String studentId) {

        long timestamp = System.currentTimeMillis() / 1000; // Unix timestamp

        Map<String, Object> attendanceData = new HashMap<>();
        attendanceData.put(String.valueOf(java.time.LocalDate.now()), true); // Add timestamp as key and attendance status as value

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("attendance").child(studentId);
        ref.updateChildren(attendanceData).addOnSuccessListener(task->{
            Toast.makeText(this, "تم التسجيل بنجاح", Toast.LENGTH_LONG).show();

        }).addOnFailureListener(task -> {
            Toast.makeText(this, task.getMessage(), Toast.LENGTH_LONG).show();

        }); // Update the student's node with the key-value pair


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}