package com.example.theotokos;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class ImageLoaderUtils {
    public static void loadImageWithProgress(String url, ImageView imageView,
                                             ProgressBar progressBar, OnImageLoadListener listener) {
        progressBar.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);

        Picasso.get()
                .load(url)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        if (listener != null) {
                            listener.onSuccess();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        progressBar.setVisibility(View.GONE);
                        if (listener != null) {
                            listener.onError(e);
                        }
                    }
                });
    }

    public interface OnImageLoadListener {
        void onSuccess();
        void onError(Exception e);
    }
}