package com.example.theotokos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private List<String> attendanceList ;

    public AttendanceAdapter(List<String> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String attendance = attendanceList.get(position);

        holder.dateTextView.setText(attendance);
        //holder.statusTextView.setText(attendance.getStatus());
    }
    @Override
    public int getItemCount() {
        return attendanceList.size();
    }
    public void submitList(List<String> newAttendanceList) {
        attendanceList.clear();
        attendanceList.addAll(newAttendanceList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dateTextView;
        TextView statusTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.dateTextView);
            //statusTextView = itemView.findViewById(R.id.statusTextView);
        }
    }

}