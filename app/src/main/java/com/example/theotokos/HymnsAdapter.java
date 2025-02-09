package com.example.theotokos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HymnsAdapter extends RecyclerView.Adapter<HymnsAdapter.HymnViewHolder> {

    private List<Hymn> hymnList;

    public HymnsAdapter(List<Hymn> hymnList) {
        this.hymnList = hymnList;
    }

    @NonNull
    @Override
    public HymnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_hymns, parent, false);
        return new HymnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HymnViewHolder holder, int position) {
        Hymn hymn = hymnList.get(position);
        //holder.hymnTitle.setText(hymn.getTitle());
        holder.hymnCopticContent.setText(hymn.getCopticContent());
        holder.hymnArabicContent.setText(hymn.getArabicContent());
    }

    @Override
    public int getItemCount() {
        return hymnList.size();
    }

    public static class HymnViewHolder extends RecyclerView.ViewHolder {
        TextView hymnTitle, hymnCopticContent, hymnArabicContent;

        public HymnViewHolder(@NonNull View itemView) {
            super(itemView);
            //hymnTitle = itemView.findViewById(R.id.hymn_title);
//            hymnCopticContent = itemView.findViewById(R.id.copticHymnContent);
//            hymnArabicContent = itemView.findViewById(R.id.arabicHymnContent);
        }
    }
}