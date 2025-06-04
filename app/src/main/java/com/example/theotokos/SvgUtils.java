package com.example.theotokos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

public class SvgUtils {
    public static Bitmap getBitmapFromDrawable(Context context, @DrawableRes int drawableId,
                                               int width, int height) {
        // 1. Get the vector drawable
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable == null) {
            return null;
        }

        // 2. Create a bitmap of the specified size
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // 3. Create a canvas to draw on the bitmap
        Canvas canvas = new Canvas(bitmap);

        // 4. Set the drawable bounds and draw
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}