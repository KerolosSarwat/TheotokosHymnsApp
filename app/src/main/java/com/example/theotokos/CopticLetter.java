package com.example.theotokos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.DrawableRes;
import java.util.List;
import java.util.Collections;

public class CopticLetter {
    private final String character;
    private final int svgResourceId;
    private final PointF startPoint;
    private final PointF endPoint;
    private final Path path;
    private final List<PointF> strokePoints;

    public CopticLetter(String character,
                        @DrawableRes int svgResourceId,
                        PointF startPoint,
                        PointF endPoint,
                        Path path,
                        List<PointF> strokePoints) {

        if (character == null || startPoint == null || endPoint == null) {
            throw new IllegalArgumentException("Character and points cannot be null");
        }

        this.character = character;
        this.svgResourceId = svgResourceId;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.path = path != null ? path : new Path();
        this.strokePoints = strokePoints != null ? strokePoints : Collections.emptyList();
    }

    public Bitmap getBitmap(Context context, int width, int height) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }

        return SvgUtils.getBitmapFromDrawable(context, svgResourceId, width, height);
    }

    // Getters
    public String getCharacter() { return character; }
    public Path getPath() { return new Path(path); } // Return defensive copy
    public PointF getStartPoint() { return new PointF(startPoint.x, startPoint.y); }
    public PointF getEndPoint() { return new PointF(endPoint.x, endPoint.y); }
    public List<PointF> getStrokePoints() { return Collections.unmodifiableList(strokePoints); }
}