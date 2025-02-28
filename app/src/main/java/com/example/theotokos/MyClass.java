package com.example.theotokos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyClass extends AppCompatActivity {

    private RecyclerView studentsRecyclerView;
    private StudentAdapter studentAdapter;
    private List<User> studentList;
    private Button takeAttendanceButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_myclass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Objects.requireNonNull(getSupportActionBar()).hide();

        studentsRecyclerView = findViewById(R.id.studentsRecyclerView);
        takeAttendanceButton = findViewById(R.id.takeAttendanceButton);

        // Initialize the RecyclerView
        studentList = new ArrayList<>();
        studentAdapter = new StudentAdapter(studentList, this);
        studentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentsRecyclerView.setAdapter(studentAdapter);




    }

    @Override
    protected void onResume() {
        super.onResume();

        // Retrieve the instructor's level (e.g., from SharedPreferences or Intent)
        String instructorLevel = DataCache.getInstance(this).getUser().getLevel(); // Replace with actual instructor level

        // Fetch students from Firebase Realtime Database
        fetchStudents(instructorLevel);

        // Handle the "Take Attendance" button click
        takeAttendanceButton.setOnClickListener(v -> takeAttendance());
    }

    private void fetchStudents(String level) {
        DatabaseReference studentsRef = FirebaseDatabase.getInstance().getReference("users");
        studentsRef.orderByChild("level").equalTo(level).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear();
                for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
                    User student = studentSnapshot.getValue(User.class);
                    if (student != null) {
                        studentList.add(student);
                    }
                }
                studentAdapter.notifyDataSetChanged();
                takeAttendanceButton.setEnabled(true); // Enable the button after data is loaded
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void takeAttendance() {
        for (User student : studentList) {
            // Save attendance status for each student (e.g., to Firebase)
            boolean isPresent = student.isPresent();
            // Update the student's attendance in Firebase
            DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(student.getCode()); // Use a unique identifier like "code"
            studentRef.child("attendance").setValue(isPresent);
        }
        // Show a success message
        Toast.makeText(this, "Attendance saved successfully!", Toast.LENGTH_SHORT).show();
    }
}