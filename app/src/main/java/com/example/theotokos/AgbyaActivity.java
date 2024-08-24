package com.example.theotokos;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AgbyaActivity extends AppCompatActivity {

    private RecyclerView agbyaRecyclerView;
    private AgbyaAdapter agbyaAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agbya);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        agbyaRecyclerView = findViewById(R.id.agbyaRecyclerView);
        agbyaAdapter = new AgbyaAdapter(new ArrayList<>(), this);
        agbyaRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        agbyaRecyclerView.setAdapter(agbyaAdapter);

        db = FirebaseFirestore.getInstance();
        fetchAgbyaData();
    }

    private void fetchAgbyaData() {
        db.collection("agbya")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Agbya> agbyaList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Agbya agbya = document.toObject(Agbya.class);
                        agbyaList.add(agbya);
                    }

                    // Update the RecyclerView adapter with the fetched data
                    agbyaAdapter.submitList(agbyaList);
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Log.e("Firestore", "Error getting documents.", e);
                });
    }
}