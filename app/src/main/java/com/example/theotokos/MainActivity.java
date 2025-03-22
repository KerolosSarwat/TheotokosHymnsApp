package com.example.theotokos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.viewpager2.widget.ViewPager2;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public ActionBarDrawerToggle actionBarDrawerToggle;
    private BottomNavigationView navigationView;
    private DataCache dataCache;
    private ViewPager2 viewPager;
//    private CircleIndicator3 indicator;
    private List<Integer> imageList;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        EdgeToEdge.enable(this);
         new FirebaseHelper();

        navigationView = findViewById(R.id.bottomNavView);
        viewPager = findViewById(R.id.viewPager);
        imageList = new ArrayList<>();
        imageList.add(R.drawable.church);
        imageList.add(R.drawable.oip);

        // Set up adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(imageList);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Auto-slide functionality
        autoSlideImages();
        navigationView.getMenu().getItem(0).setChecked(true);
        try {
            //user logined but not admin
            scheduleNotification();
            dataCache = DataCache.getInstance(this);
            if (!dataCache.getUser().isAdmin()){
                navigationView.getMenu().getItem(3).setVisible(false);
                navigationView.getMenu().removeItem(3);
            }
        }catch (NullPointerException ex){
            //user not logined yet
            navigationView.getMenu().getItem(4).setVisible(false);
            navigationView.getMenu().getItem(3).setVisible(false);
            navigationView.getMenu().removeItem(3);
            navigationView.getMenu().removeItem(4);
        }


        View hymnsImageView = findViewById(R.id.hymns);
        View agbyaImageView = findViewById(R.id.agbya);
        View copticImageView = findViewById(R.id.coptic);
        View taksImageView = findViewById(R.id.taks);
        View formView = findViewById(R.id.form);

        formView.setOnClickListener(v -> {
            // Handle the click event here
            Intent signIntent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(signIntent);
            // Replace this with your desired action
        });

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
            Intent profileIntent;
            if(dataCache.getUser() == null){
                profileIntent = new Intent(MainActivity.this, LoginActivity.class);
            }
            else
                profileIntent = new Intent(MainActivity.this, ProfileActivity.class);

            startActivity(profileIntent);
            return false;
        });
        try {
                navigationView.getMenu().findItem(R.id.attendance).setOnMenuItemClickListener(menuItem -> {
                    Intent profileIntent = new Intent(MainActivity.this, MyClass.class);
                    startActivity(profileIntent);
                    return true;
                });
        }catch (NullPointerException ex){
            Log.e("onResume: ", "Exception: "+ ex.getMessage() );
        }
    }
    private void autoSlideImages() {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (viewPager.getCurrentItem() == imageList.size() - 1) {
                    viewPager.setCurrentItem(0);
                } else {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 5000, 10000); // Delay and period in milliseconds
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
                    Intent loginIntent = new Intent(MainActivity.this, MainActivity.class);
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}