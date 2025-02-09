package com.example.theotokos;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

import java.util.Objects;

public class TaksDetailsActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView contentTextView;
    private TextView homeworkTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_taks_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Objects.requireNonNull(getSupportActionBar()).hide();
        titleTextView = findViewById(R.id.detailTitle);
        contentTextView = findViewById(R.id.detailContent);
        homeworkTextView = findViewById(R.id.detailHomework);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Taks taks = (Taks) getIntent().getSerializableExtra("taks");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int fontSize = SettingsFragment.getFontSize(sharedPreferences);

        if (taks != null){
            titleTextView.setText(taks.getTitle());
            contentTextView.setText(taks.getContent());
            contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            homeworkTextView.setText(taks.getHomework());
            homeworkTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        }
        else {
            Log.e("onCreate: ", "Taks is NULL" );
        }
    }
}