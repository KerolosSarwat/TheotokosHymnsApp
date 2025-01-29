package com.example.theotokos;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private DataCache dataCache;
    private User user;
    private List<String> attendanceList;
    ImageView qrCodeImageView;
    private AttendanceAdapter attendanceAdapter;
    private RecyclerView attendanceRecyclerView;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TermsPagerAdapter pagerAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tabLayout = findViewById(R.id.termsTabs);
        viewPager = findViewById(R.id.viewPager);

        dataCache = DataCache.getInstance(this);
        user = dataCache.getUser();

         // Replace with your method to get the Degree object

        if (NetworkUtils.isNetworkConnected(this))
        {
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
            try {
                usersRef.child(user.getCode()).get().addOnSuccessListener(dataSnapshot -> {
                    user = dataSnapshot.getValue(User.class);
                    dataCache.saveUser(user);
                    Log.e( "onCreate: degrees", ""+user.getDegree().getFirstTerm().getAgbya());
                });
            }catch (Exception ex){
            Log.e("onCreate: ", ex.getMessage());
            }
        }

//        //qrCodeImageView = findViewById(R.id.qrCodeImageView);
//        attendanceRecyclerView = findViewById(R.id.attendanceRecyclerView);

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
        ImageButton qrButton = findViewById(R.id.QrButton);

        Degree degree = user.getDegree();
        setupViewPager(degree);
        qrButton.setOnClickListener(v -> showQRCodeDialog(user.getCode()));
        fetchAttendance();

    }

    private void setupViewPager(Degree degree) {
        pagerAdapter = new TermsPagerAdapter(ProfileActivity.this, degree);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {

            switch (position) {
                case 0:
//                    tabLayout.getTabAt(0).setText("الترم الأول ("+ user.getDegree().getFirstTerm().getResult()+")");

                    tab.setText("الترم الأول ("+ user.getDegree().getFirstTerm().getResult()+")");
                    break;
                case 1:
//                    tabLayout.getTabAt(1).setText("الترم الثانى ("+ user.getDegree().getSecondTerm().getResult()+")");

                    tab.setText("الترم الثانى ("+ user.getDegree().getSecondTerm().getResult()+")");
                    break;
                case 2:
//                    tabLayout.getTabAt(2).setText("الترم الثالث ("+ user.getDegree().getThirdTerm().getResult()+")");

                    tab.setText("الترم الثالث ("+ user.getDegree().getThirdTerm().getResult()+")");
                    break;
            }
        }).attach();
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

    private void showQRCodeDialog(String qrCodeContent) {
        // Create a dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_qr_code);

        ImageView qrCodeImage = dialog.findViewById(R.id.qr_code_image);
        Button closeButton = dialog.findViewById(R.id.close_button);

        // Generate QR code
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(qrCodeContent, BarcodeFormat.QR_CODE, 600, 600);
            qrCodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        // Set close button listener
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
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
                    int count = 0;
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        attendanceDates.add(childSnapshot.getKey());
                        count++;
                    }
                    TextView attendanceCount= findViewById(R.id.attendanceCount);
                    attendanceCount.setText(""+count);
                    //attendanceAdapter.submitList(attendanceDates);

                    // Process the attendance dates here
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}