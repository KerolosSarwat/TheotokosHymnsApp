package com.example.theotokos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AgbyaAdapter extends RecyclerView.Adapter<AgbyaAdapter.AgbyaViewHolder> {

    private List<Agbya> agbyaList;
    private Context context;

    public AgbyaAdapter(List<Agbya> agbyaList, Context context) {
        this.agbyaList = agbyaList;
        this.context = context;
    }

    @NonNull
    @Override
    public AgbyaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new AgbyaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgbyaViewHolder holder, int position) {
        Agbya agbya = agbyaList.get(position);
        // Bind data to the ViewHolder's views
        holder.titleTextView.setText(agbya.getTitle());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AgbyaDetailsActivity.class);
            intent.putExtra("agbyaTitle", agbya.getTitle());
            intent.putExtra("agbyaContent", agbya.getTitle());

            intent.putExtra("agbyaDescription", agbya.getTitle());

            context.startActivity(intent);
        });
        //holder.contentTextView.setText(agbya.getContent());
    }

    @Override
    public int getItemCount() {
        return agbyaList.size();
    }

    public class AgbyaViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;

        public AgbyaViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            //contentTextView = itemView.findViewById(R.id.contentTextView);
        }
    }
    public void submitList(List<Agbya> newAgbyaList) {
        agbyaList.clear();
        agbyaList.addAll(newAgbyaList);
        notifyDataSetChanged();
    }
}