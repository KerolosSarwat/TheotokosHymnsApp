package com.example.theotokos;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private DataCache dataCache;
    private User user;
    private List<String> attendanceList;
    ImageView qrCodeImageView;
    private AttendanceAdapter attendanceAdapter;
    private RecyclerView attendanceRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dataCache = DataCache.getInstance(this);
        user = dataCache.getUser();
        //qrCodeImageView = findViewById(R.id.qrCodeImageView);
        attendanceRecyclerView = findViewById(R.id.attendanceRecyclerView);


        getSupportActionBar().hide();
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView fullname = findViewById(R.id.fullnameTextView);
        fullname.setText(user.getFullName());
        TextView code = findViewById(R.id.codeTextView);
        code.setText(user.getCode());
        generateQRCode(user.getCode());
        TextView phone = findViewById(R.id.phoneTextView);
        phone.setText(user.getPhoneNumber());
        TextView address = findViewById(R.id.AddressTextView);
        address.setText(user.getAddress());
        TextView church = findViewById(R.id.churchTextView);
        church.setText(user.getChurch());
        TextView birthdate = findViewById(R.id.birthdateTextView);
        birthdate.setText(user.getBirthdate());
        TextView schoolLevel = findViewById(R.id.levelTextView);
        schoolLevel.setText(user.getLevel());

        fetchAttendance();
        attendanceAdapter = new AttendanceAdapter(new ArrayList<>());
        attendanceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendanceRecyclerView.setAdapter(attendanceAdapter);
        attendanceAdapter.notifyDataSetChanged();
    }

    public void generateQRCode(String data) {
        try {
            int size = 512; // Adjust the size as needed
            BitMatrix matrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size);
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    bitmap.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            // Display the bitmap in an ImageView
//            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void fetchAttendance(){

        String userId = user.getCode();
        List<String> attendanceDates = new ArrayList<>();

        DatabaseReference attendanceRef = FirebaseDatabase.getInstance().getReference("attendance").child(userId);
        attendanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        attendanceDates.add(childSnapshot.getKey());
                    }

                    attendanceAdapter.submitList(attendanceDates);
                    // Process the attendance dates here
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}