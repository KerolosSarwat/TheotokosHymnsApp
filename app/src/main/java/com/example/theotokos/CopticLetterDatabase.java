package com.example.theotokos;

import android.graphics.Path;
import android.graphics.PointF;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CopticLetterDatabase {
    private static final Map<String, CopticLetter> letters = new HashMap<>();

    static {
        // Letter Ⲁ (Alpha)
        Path pathAlpha = new Path();
        pathAlpha.moveTo(100, 200);  // Start point
        pathAlpha.lineTo(400, 400);
        pathAlpha.lineTo(500, 300);   // End point

        List<PointF> strokePointsAlpha = new ArrayList<>();
        strokePointsAlpha.add(new PointF(100, 200));
        strokePointsAlpha.add(new PointF(400, 400));
        strokePointsAlpha.add(new PointF(500, 300));

        letters.put("Ⲁ", new CopticLetter(
                "Ⲁ",
                R.drawable.drawing,  // SVG in res/raw/
                new PointF(100, 200),
                new PointF(500, 300),
                pathAlpha,
                strokePointsAlpha
        ));

        // Letter Ⲃ (Beta)
        Path pathBeta = new Path();
        pathBeta.moveTo(100, 100);
        pathBeta.lineTo(100, 300);
        pathBeta.lineTo(200, 300);
        pathBeta.lineTo(200, 200);
        pathBeta.lineTo(100, 200);

        List<PointF> strokePointsBeta = new ArrayList<>();
        strokePointsBeta.add(new PointF(100, 100));
        strokePointsBeta.add(new PointF(100, 300));
        strokePointsBeta.add(new PointF(200, 300));
        strokePointsBeta.add(new PointF(200, 200));
        strokePointsBeta.add(new PointF(100, 200));

        letters.put("Ⲃ", new CopticLetter(
                "Ⲃ",
                R.drawable.b,
                new PointF(100, 100),
                new PointF(100, 200),
                pathBeta,
                strokePointsBeta
        ));

        // Add more letters following the same pattern...
    }

    public static CopticLetter getLetter(String character) {
        CopticLetter letter = letters.get(character);
        if (letter == null) {
            throw new IllegalArgumentException("No letter found for: " + character);
        }
        return letter;
    }

    public static List<String> getAllLetters() {
        return Collections.unmodifiableList(new ArrayList<>(letters.keySet()));
    }
}