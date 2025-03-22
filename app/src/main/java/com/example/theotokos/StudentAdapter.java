package com.example.theotokos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> implements Filterable {

    private List<User> studentList;
    private List<User> studentListFiltered; // Filtered list

    private Context context;

    public StudentAdapter(List<User> studentList, Context context) {
        this.studentList = studentList;
        this.studentListFiltered = new ArrayList<>(studentList); // Initialize filtered list
        this.context = context;
    }
    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        User student = studentListFiltered.get(position);

        holder.fullNameTextView.setText(student.getFullName());
        holder.codeTextView.setText(student.getCode());
        holder.attendanceCheckBox.setChecked(student.isPresent());
        holder.attendanceCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            student.setPresent(isChecked); // Update the student's attendance status
            submitAttendanceToFirebase(student);
        });
    }

    @Override
    public int getItemCount() {
        return studentListFiltered.size();
    }
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<User> filteredList = new ArrayList<>(studentList);
                if (constraint == null || constraint.length() == 0) {
                    // If the search query is empty, show the full list
                    filteredList.addAll(studentList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (User student : studentList) {
                        if (student.getFullName().contains(filterPattern)) {
                            filteredList.add(student);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                studentListFiltered.clear();
                studentListFiltered.addAll((List<User>) results.values);
                notifyDataSetChanged();
            }
        };
    }
    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView fullNameTextView, codeTextView;
        CheckBox attendanceCheckBox;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            codeTextView = itemView.findViewById(R.id.codeTextView);
            attendanceCheckBox = itemView.findViewById(R.id.attendanceCheckBox);
        }
    }
    private void submitAttendanceToFirebase(User student) {
        // Get the current date
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        // Update the student's attendance dates
        if (student.isPresent()) {
            if(!student.getAttendanceDates().contains(currentDate))
                student.addAttendanceDate(currentDate);
            else
                Toast.makeText(context,"هذا الطالب مسجل بالفعل", Toast.LENGTH_SHORT).show();
        }

        // Get a reference to the student's node in Firebase
        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("users")
                .child(student.getCode()); // Use a unique identifier like "code"

        // Update the student's attendance data in Firebase
        studentRef.child("attendanceDates").setValue(student.getAttendanceDates())
                .addOnSuccessListener(aVoid -> {
                    // Show a success message
                    Toast.makeText(context, "Attendance saved for " + student.getFullName(), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Show an error message
                    Toast.makeText(context, "Failed to save attendance: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}