package com.example.theotokos;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HymnViewHolder extends RecyclerView.ViewHolder {
    TextView hymnTitle, hymnCopticContent, hymnArabicContent;

    public HymnViewHolder(@NonNull View itemView) {
        super(itemView);
//        //hymnTitle = itemView.findViewById(R.id.hymn_title);
//        hymnCopticContent = itemView.findViewById(R.id.copticHymnContent);
//        hymnArabicContent = itemView.findViewById(R.id.arabicHymnContent);
    }
}