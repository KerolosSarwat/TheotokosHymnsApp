package com.example.theotokos;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class AgbyaDetailsActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView contentTextView;
    private TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agbya_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Objects.requireNonNull(getSupportActionBar()).hide();
        titleTextView = findViewById(R.id.detailTitle);
        contentTextView = findViewById(R.id.detailContent);
        descriptionTextView = findViewById(R.id.detailDescription);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Agbya agbya = (Agbya) getIntent().getSerializableExtra("agbya");
        int fontSize = SettingsFragment.getFontSize();
        if (agbya != null){
            titleTextView.setText(agbya.getTitle());
            contentTextView.setText(agbya.getContent());
            contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            descriptionTextView.setText(agbya.getDescription());
            descriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        }
        else {
            Log.e("onCreate: ", "Agbya is NULL" );
        }
    }
}