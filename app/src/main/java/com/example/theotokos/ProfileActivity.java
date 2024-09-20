package com.example.theotokos;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class ProfileActivity extends AppCompatActivity {

    private DataCache dataCache;
    private User user;
    ImageView qrCodeImageView;
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
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        getSupportActionBar().hide();
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView fullname = findViewById(R.id.fullnameTextView);
        fullname.setText(user.getFullName());
        generateQRCode(user.getCode());
        TextView phone = findViewById(R.id.phoneTextView);
        phone.setText(user.getPhoneNumber());
        TextView address = findViewById(R.id.AddressTextView);
        address.setText(user.getAddress());
        TextView church = findViewById(R.id.churchTextView);
        church.setText(user.getChurch());
        TextView birthdate = findViewById(R.id.birthdateTextView);
        birthdate.setText(user.getBirthdate());
    }

    public void generateQRCode(String data) {
        try {
            int size = 1024; // Adjust the size as needed
            BitMatrix matrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size);
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    bitmap.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            // Display the bitmap in an ImageView
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}