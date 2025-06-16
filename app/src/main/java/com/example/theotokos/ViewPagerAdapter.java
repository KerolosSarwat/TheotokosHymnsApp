package com.example.theotokos;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;

@Keep
public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    private List<String> imageUrls;
    private SparseBooleanArray loadedPositions = new SparseBooleanArray();
    private OnAllImagesLoadedListener allImagesLoadedListener;
    private int currentPosition = 0;

    public interface OnAllImagesLoadedListener {
        void onAllImagesLoaded();
        void onImageLoaded(int position);
        void onImageLoadFailed(int position);
    }

    public ViewPagerAdapter(List<String> imageUrls, OnAllImagesLoadedListener listener) {
        this.imageUrls = imageUrls;
        this.allImagesLoadedListener = listener;
        prefetchImages();
    }

    private void prefetchImages() {
        for (int i = 0; i < imageUrls.size(); i++) {
            final int position = i;
            Picasso.get()
                    .load(imageUrls.get(position))
                    .fetch(new Callback() {
                        @Override
                        public void onSuccess() {
                            loadedPositions.put(position, true);
                            checkAllImagesLoaded();
                        }

                        @Override
                        public void onError(Exception e) {
                            loadedPositions.put(position, false);
                        }
                    });
        }
    }

    private void checkAllImagesLoaded() {
        if (loadedPositions.size() == imageUrls.size()) {
            boolean allLoaded = true;
            for (int i = 0; i < loadedPositions.size(); i++) {
                if (!loadedPositions.valueAt(i)) {
                    allLoaded = false;
                    break;
                }
            }
            if (allLoaded && allImagesLoadedListener != null) {
                allImagesLoadedListener.onAllImagesLoaded();
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slider, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(imageUrls.get(position), position);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
        Button retryButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            progressBar = itemView.findViewById(R.id.progressBar);
            retryButton = itemView.findViewById(R.id.retryButton);
        }

        public void bind(String imageUrl, int position) {
            retryButton.setVisibility(View.GONE);

            if (loadedPositions.get(position, false)) {
                // Image already loaded from prefetch
                progressBar.setVisibility(View.GONE);
                Picasso.get().load(imageUrl).into(imageView);
                imageView.setVisibility(View.VISIBLE);
            } else {
                ImageLoaderUtils.loadImageWithProgress(imageUrl, imageView, progressBar,
                        new ImageLoaderUtils.OnImageLoadListener() {
                            @Override
                            public void onSuccess() {
                                loadedPositions.put(position, true);
                                checkAllImagesLoaded();
                                if (allImagesLoadedListener != null) {
                                    allImagesLoadedListener.onImageLoaded(position);
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                retryButton.setVisibility(View.VISIBLE);
                                if (allImagesLoadedListener != null) {
                                    allImagesLoadedListener.onImageLoadFailed(position);
                                }
                            }
                        });

                retryButton.setOnClickListener(v -> {
                    retryButton.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    ImageLoaderUtils.loadImageWithProgress(imageUrl, imageView, progressBar,
                            new ImageLoaderUtils.OnImageLoadListener() {
                                @Override
                                public void onSuccess() {
                                    loadedPositions.put(position, true);
                                    checkAllImagesLoaded();
                                }

                                @Override
                                public void onError(Exception e) {
                                    retryButton.setVisibility(View.VISIBLE);
                                }
                            });
                });
            }
        }
    }
    public List<String> getImageUrls() {
        return imageUrls;
    }
}