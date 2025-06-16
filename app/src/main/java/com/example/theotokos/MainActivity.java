package com.example.theotokos;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.viewpager2.widget.ViewPager2;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements ViewPagerAdapter.OnAllImagesLoadedListener {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private BottomNavigationView navigationView;
    private DataCache dataCache;
    private ViewPager2 viewPager;
    private List<String> imageUrls;
    private Timer timer;
    private View hymnsImageView, agbyaImageView, copticImageView, taksImageView, formView;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            Log.e( "onCreate: ", "the main activity const.");
           var firebase = new FirebaseHelper();
            initializeViews();
            setupNavigation();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
        }

    private void initializeViews() {
        hymnsImageView = findViewById(R.id.hymns);
        agbyaImageView = findViewById(R.id.agbya);
        copticImageView = findViewById(R.id.coptic);
        taksImageView = findViewById(R.id.taks);
        formView = findViewById(R.id.form);
        navigationView = findViewById(R.id.bottomNavView);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setUserInputEnabled(false);
    }

    private void setupNavigation() {
        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            showLogoutConfirmationDialog();
            return false;
        });

        navigationView.getMenu().findItem(R.id.nav_settings).setOnMenuItemClickListener(menuItem -> {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
            return false;
        });

        navigationView.getMenu().findItem(R.id.nav_profile).setOnMenuItemClickListener(menuItem -> {
            Intent profileIntent = (dataCache.getUser() == null) ?
                    new Intent(MainActivity.this, LoginActivity.class) :
                    new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
            return false;
        });

        navigationView.getMenu().findItem(R.id.attendance).setOnMenuItemClickListener(menuItem -> {
            startActivity(new Intent(MainActivity.this, MyClass.class));
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

            fetchBannerImages();
            autoSlideImages();
            navigationView.getMenu().getItem(0).setChecked(true);
            handleUserLoginStatus();
            setupClickListeners();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }

    }

    private void handleUserLoginStatus() {
        dataCache = DataCache.getInstance(this);
        if (dataCache.getUser() != null && dataCache.getUser().isAdmin()) {
            navigationView.getMenu().getItem(3).setVisible(true);
        } else {
            navigationView.getMenu().getItem(3).setVisible(false);
            navigationView.getMenu().removeItem(3);
        }
    }

    private void setupClickListeners() {
        formView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SignupActivity.class)));
        hymnsImageView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HymnsActivity.class)));
        agbyaImageView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AgbyaActivity.class)));
        taksImageView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TaksActivity.class)));
    }

    private void autoSlideImages() {
        if (adapter == null || adapter.getImageUrls() == null || adapter.getImageUrls().isEmpty()) {
            Log.e("AutoSlide", "Adapter or image list is not initialized");
            return;
        }

        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable update = () -> {
            if (viewPager == null || adapter == null) return;

            int currentItem = viewPager.getCurrentItem();
            int itemCount = adapter.getItemCount();

            if (itemCount == 0) return;

            int nextItem = (currentItem == itemCount - 1) ? 0 : currentItem + 1;
            viewPager.setCurrentItem(nextItem, true);
        };

        // Cancel any existing timer
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 7000, 10000); // Delay and period in milliseconds
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("تسجيل خروج")
                .setMessage("هل تريد تسجيل خروج من التطبيق")
                .setPositiveButton("نعم", (dialog, which) -> {
                    dataCache.clearCache();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                })
                .setNegativeButton("لا", null)
                .show();
    }

    private void scheduleNotification() {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(this).enqueue(workRequest);
    }

    private void fetchBannerImages() {
        FirebaseHelper.getBannerDatabase().get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    imageUrls = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Banner banner = document.toObject(Banner.class);
                        if (banner != null && banner.getImageURL() != null) {
                            Log.e("fetchBannerImages: ", "" + banner.getDescription());
                            imageUrls.add(banner.getImageURL());
                        }
                    }
                    adapter = new ViewPagerAdapter(imageUrls, MainActivity.this);
                    viewPager.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseError", "Error fetching banner images", e);
                    Toast.makeText(this, "Failed to load images", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onAllImagesLoaded() {
        runOnUiThread(() -> {
            viewPager.setUserInputEnabled(true);
            //Toast.makeText(this, "All images loaded", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onImageLoaded(int position) {
        Log.d("ImageLoad", "Image loaded at position: " + position);
    }

    @Override
    public void onImageLoadFailed(int position) {
        Log.e("ImageLoad", "Failed to load image at position: " + position);
    }
}
