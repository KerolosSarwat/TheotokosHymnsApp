package com.example.theotokos;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    public static FirebaseHelper helper;
    private DataCache dataCache;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new FirebaseHelper();
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        navigationView = findViewById(R.id.navigation_menu);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataCache = DataCache.getInstance(this);
        getSupportActionBar().setTitle("مرحباً " + dataCache.getUser().getFullName().split(" ")[0]);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        navigationView.getMenu().findItem(R.id.nav_qrscanner).setOnMenuItemClickListener(menuItem -> {
            Intent qrIntent = new Intent(MainActivity.this, QrCodeScannerActivity.class);
            startActivity(qrIntent);
            return false;
        });
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
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Log out the user
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        dataCache.clearCache();
                        startActivity(loginIntent);
                    }
                })
                .setNegativeButton("لا", null)
                .show();
    }
}