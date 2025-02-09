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

public class TaksAdapter extends RecyclerView.Adapter<TaksAdapter.TaksViewHolder> {

    private List<Taks> taksList;
    private Context context;

    public TaksAdapter(List<Taks> taksList, Context context) {
        this.taksList = taksList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new TaksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaksViewHolder holder, int position) {
        Taks taks = taksList.get(position);
        // Bind data to the ViewHolder's views
        taks.setContent(taks.getContent().replace("*", "\n"));
        holder.titleTextView.setText(taks.getTitle());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TaksDetailsActivity.class).putExtra("taks", taks);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return taksList.size();
    }

    public class TaksViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;

        public TaksViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            //contentTextView = itemView.findViewById(R.id.contentTextView);
        }
    }
    public void submitList(List<Taks> newTaksList) {
        taksList.clear();
        taksList.addAll(newTaksList);
        notifyDataSetChanged();
    }
}