package com.example.theotokos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawingView extends View {
    // Drawing tools
    private Path userPath;
    private Path perfectPath;
    private Paint userPaint, perfectPaint, canvasPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private Bitmap guideBitmap;
    private Bitmap letterBitmap;

    // Touch handling
    private float lastX, lastY;
    private static final float TOUCH_TOLERANCE = 4;

    // Guide points
    private PointF startPoint, endPoint;
    private boolean showGuides = true;

    // Evaluation
    private List<PointF> deviationPoints = new ArrayList<>();
    private static final float DEVIATION_THRESHOLD = 15f;
    private Paint guidePaint;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        // User path initialization
        userPath = new Path();

        // User paint setup
        userPaint = new Paint();
        userPaint.setColor(Color.parseColor("#4285F4")); // Google blue
        userPaint.setAntiAlias(true);
        userPaint.setStrokeWidth(25);
        userPaint.setStyle(Paint.Style.STROKE);
        userPaint.setStrokeJoin(Paint.Join.ROUND);
        userPaint.setStrokeCap(Paint.Cap.ROUND);

        // Perfect path paint
        perfectPaint = new Paint();
        perfectPaint.setColor(Color.GREEN);
        perfectPaint.setStrokeWidth(5);
        perfectPaint.setStyle(Paint.Style.STROKE);
        perfectPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
        perfectPaint.setAntiAlias(true);

        // Canvas paint
        canvasPaint = new Paint(Paint.DITHER_FLAG);

        // Guide paint
        guidePaint = new Paint();
        guidePaint.setColor(Color.RED);
        guidePaint.setStyle(Paint.Style.FILL);
        guidePaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw background
        canvas.drawColor(Color.WHITE);

        // Draw guide image if available
        if (guideBitmap != null) {
            canvas.drawBitmap(guideBitmap, 0, 0, null);
        }

        // Draw letter bitmap if available
        if (letterBitmap != null) {
            canvas.drawBitmap(letterBitmap, 0, 0, null);
        }

        // Draw perfect path
        if (perfectPath != null) {
            canvas.drawPath(perfectPath, perfectPaint);
        }

        // Draw user's path
        canvas.drawPath(userPath, userPaint);

        // Draw guide points if enabled
        if (showGuides) {
            drawGuidePoints(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                userPath.moveTo(touchX, touchY);
                lastX = touchX;
                lastY = touchY;
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(touchX - lastX);
                float dy = Math.abs(touchY - lastY);

                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    userPath.quadTo(lastX, lastY, (touchX + lastX)/2, (touchY + lastY)/2);
                    lastX = touchX;
                    lastY = touchY;
                }
                break;

            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(userPath, userPaint);
                break;

            default:
                return false;
        }

        invalidate();
        return true;
    }

    private void drawGuidePoints(Canvas canvas) {
        if (startPoint != null) {
            canvas.drawCircle(startPoint.x, startPoint.y, 15, guidePaint);
        }
        if (endPoint != null) {
            canvas.drawCircle(endPoint.x, endPoint.y, 15, guidePaint);
        }
    }

    public void setGuidePoints(PointF start, PointF end) {
        this.startPoint = start;
        this.endPoint = end;
        invalidate();
    }

    public void setLetter(String letter) {
        this.perfectPath = getPerfectPathForLetter(letter);
        invalidate();
    }

    private Path getPerfectPathForLetter(String letter) {
        Path path = new Path();
        // Basic implementation - should be replaced with actual letter paths
        switch (letter.toLowerCase()) {
            case "a":
                path.moveTo(100, 300);
                path.lineTo(150, 100);
                path.lineTo(200, 300);
                path.moveTo(125, 200);
                path.lineTo(175, 200);
                break;
            case "b":
                path.moveTo(100, 100);
                path.lineTo(100, 300);
                path.addCircle(125, 150, 50, Path.Direction.CW);
                path.addCircle(125, 250, 50, Path.Direction.CW);
                break;
            default:
                // Simple line as default
                path.moveTo(100, 200);
                path.lineTo(200, 200);
                break;
        }
        return path;
    }

    public TracingResult evaluateDrawing() {
        float deviationScore = calculateDeviation();
        boolean isPerfect = deviationScore < 0.1f;
        boolean isPassing = deviationScore < 0.3f;

        return new TracingResult(isPerfect, isPassing, deviationScore, deviationPoints);
    }

    private float calculateDeviation() {
        // Simplified path comparison
        // In a real implementation, you would use PathMeasure to compare the paths
        deviationPoints.clear();
        float totalDeviation = 0;
        int samplePoints = 20;

        if (perfectPath == null || userPath.isEmpty()) {
            return 1.0f; // Max deviation if no comparison possible
        }

        // This is a placeholder - real implementation would be more complex
        for (int i = 0; i < samplePoints; i++) {
            float fraction = i / (float) samplePoints;
            // Compare points along both paths
            // In reality you'd need to measure actual path distances
            totalDeviation += 0.02f; // Dummy value
        }

        return Math.min(totalDeviation, 1.0f);
    }

    public void setLetterBitmap(Bitmap bitmap) {
        this.letterBitmap = bitmap;
        invalidate();
    }

    public void setGuideBitmap(Bitmap bitmap) {
        this.guideBitmap = bitmap;
        invalidate();
    }

    public void clearCanvas() {
        userPath.reset();
        if (drawCanvas != null) {
            drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
        invalidate();
    }

    public void setShowGuides(boolean show) {
        this.showGuides = show;
        invalidate();
    }

    public static class TracingResult {
        public final boolean isPerfect;
        public final boolean isPassing;
        public final float deviationScore;
        public final List<PointF> deviationPoints;

        public TracingResult(boolean isPerfect, boolean isPassing,
                             float deviationScore, List<PointF> deviationPoints) {
            this.isPerfect = isPerfect;
            this.isPassing = isPassing;
            this.deviationScore = deviationScore;
            this.deviationPoints = deviationPoints;
        }
    }
}