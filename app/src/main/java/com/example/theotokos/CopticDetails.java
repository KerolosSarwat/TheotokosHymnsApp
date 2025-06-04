package com.example.theotokos;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class CopticDetails extends AppCompatActivity {
    private DrawingView drawingView;
    private LinearLayout lettersLayout;  // Added reference

    private String currentLetter;

    // Coptic letters (Unicode values)
    private final String[] COPTIC_LETTERS = {
            "Ⲁ", "Ⲃ", "Ⲅ", "Ⲇ", "Ⲉ", "Ⲋ", "Ⲍ", "Ⲏ", "Ⲑ",
            "Ⲓ", "Ⲕ", "Ⲗ", "Ⲙ", "Ⲛ", "Ⲝ", "Ⲟ", "Ⲡ", "Ⲣ",
            "Ⲥ", "Ⲧ", "Ⲩ", "Ⲫ", "Ⲭ", "Ⲯ", "Ⲱ", "Ϣ", "Ϥ", "Ϧ"
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coptic_details);
        lettersLayout = findViewById(R.id.letters_layout);
        drawingView = findViewById(R.id.drawing_view);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupLetterButtons();
        setupControlButtons();
    }

    private void setupLetterButtons() {
        LinearLayout lettersLayout = findViewById(R.id.letters_layout);

        for (String letter : COPTIC_LETTERS) {
            Button btn = new Button(this);
            btn.setText(letter);
            btn.setTextSize(24);
            btn.setOnClickListener(v -> {
                currentLetter = letter;
                loadLetterTemplate(letter);
                Toast.makeText(this,"Practice letter: " + letter, Toast.LENGTH_SHORT).show();
            });

            lettersLayout.addView(btn);
        }
    }

    private void setupControlButtons() {
        Button clearBtn = findViewById(R.id.clear_btn);
        clearBtn.setOnClickListener(v -> drawingView.clearCanvas());

        Button checkBtn = findViewById(R.id.check_btn);
        checkBtn.setOnClickListener(v -> checkDrawing());
    }

    private void loadLetterTemplate(String letter) {
        CopticLetter copticLetter = CopticLetterDatabase.getLetter(letter);

        // Get dimensions from the drawing view
        int width = drawingView.getWidth();
        int height = drawingView.getHeight();

        // Load SVG as bitmap
        Bitmap letterBitmap = copticLetter.getBitmap(this, width, height);

        // Set to drawing view
        drawingView.setLetterBitmap(letterBitmap);
        drawingView.setGuidePoints(copticLetter.getStartPoint(), copticLetter.getEndPoint());
        drawingView.clearCanvas();
    }

    private void checkDrawing() {
        if (currentLetter == null) {
            Toast.makeText(this, "Please select a letter first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Here you would implement your drawing recognition logic
        // For now we'll just show a placeholder message
        Toast.makeText(this,"Checking your " + currentLetter + " drawing...",Toast.LENGTH_SHORT).show();

        // In a real app, you would:
        // 1. Compare the drawn path with the template
        // 2. Calculate accuracy
        // 3. Provide feedback
    }
}