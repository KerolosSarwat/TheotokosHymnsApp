package com.example.theotokos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public ActionBarDrawerToggle actionBarDrawerToggle;
    private BottomNavigationView navigationView;
    private DataCache dataCache;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        scheduleNotification();
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        navigationView = findViewById(R.id.bottomNavView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        dataCache = DataCache.getInstance(this);
        //getSupportActionBar().setTitle("مرحباً " + dataCache.getUser().getFullName().split(" ")[0]);

        View hymnsImageView = findViewById(R.id.hymns);
        View agbyaImageView = findViewById(R.id.agbya);
        View copticImageView = findViewById(R.id.coptic);
        View taksImageView = findViewById(R.id.taks);
        hymnsImageView.setOnClickListener(v -> {
            // Handle the click event here
            Intent hymnsIntent = new Intent(MainActivity.this, HymnsActivity.class);
            startActivity(hymnsIntent);
            // Replace this with your desired action
        });
        agbyaImageView.setOnClickListener(view -> {
            Intent hymnsIntent = new Intent(MainActivity.this, AgbyaActivity.class);
            startActivity(hymnsIntent);
        });
        copticImageView.setOnClickListener(view -> {
            Intent copticIntent = new Intent(MainActivity.this, CopticActivity.class);
            startActivity(copticIntent);
        });

        taksImageView.setOnClickListener(view -> {
            Intent taksIntent = new Intent(MainActivity.this, TaksActivity.class);
            startActivity(taksIntent);
        });

        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            showLogoutConfirmationDialog();
            return false;
        });
        navigationView.getMenu().findItem(R.id.nav_settings).setOnMenuItemClickListener(menuItem -> {
            Intent loginIntent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(loginIntent);
            return false;
        });
        navigationView.getMenu().findItem(R.id.nav_profile).setOnMenuItemClickListener(menuItem -> {
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
            return false;
        });
        try {
            Log.e("onResume: ", ""+ dataCache.getUser().isAdmin );
            if (dataCache.getUser().isAdmin()) {
                navigationView.getMenu().findItem(R.id.attendance).setOnMenuItemClickListener(menuItem -> {
                    Intent profileIntent = new Intent(MainActivity.this, MyClass.class);
                    startActivity(profileIntent);
                    return true;
                });n
            }
        }catch (NullPointerException ex){
            dataCache.getUser().setAdmin(false);
        }


//        navigationView.getMenu().findItem(R.id.nav_qrscanner).setOnMenuItemClickListener(menuItem -> {
//            Intent qrIntent = new Intent(MainActivity.this, QrCodeScannerActivity.class);
//            startActivity(qrIntent);
//            return false;
//        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("تسجيل خروج")
                .setMessage("هل تريد تسجيل خروج من التطبيق")
                .setPositiveButton("نعم", (dialog, which) -> {
                    // Log out the user
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    dataCache.clearCache();
                    startActivity(loginIntent);
                })
                .setNegativeButton("لا", null)
                .show();
    }
    private void scheduleNotification() {
        // Create a work request to trigger the NotificationWorker after 10 seconds
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(10, TimeUnit.SECONDS) // 10 seconds from now
                .build();

        // Enqueue the work request
        WorkManager.getInstance(this).enqueue(workRequest);
    }
}