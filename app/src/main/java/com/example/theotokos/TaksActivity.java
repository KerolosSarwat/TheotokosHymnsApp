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

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TaksActivity extends AppCompatActivity {

    private RecyclerView taksRecyclerView;
    private TaksAdapter taksAdapter;
    private FirebaseFirestore db;
    private DataCache dataCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_taks);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dataCache = DataCache.getInstance(this);
        taksRecyclerView = findViewById(R.id.taksRecyclerView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        db = FirebaseFirestore.getInstance();

        User user = dataCache.getUser();
        if(user.getLevel().equals( "حضانة"))
            fetchTaksata(0);
        else if(user.getLevel().equals( "أولى ابتدائى"))
            fetchTaksata(1);
        else if(user.getLevel().equals( "ثانية ابتدائى"))
            fetchTaksata(2);
        else if(user.getLevel().equals( "ثالثة ابتدائى"))
            fetchTaksata(3);
        else if(user.getLevel().equals( "رابعة ابتدائى"))
            fetchTaksata(4);
        else if(user.getLevel().equals( "خامسة ابتدائى"))
            fetchTaksata(5);
        else if(user.getLevel().equals( "سادسة ابتدائى"))
            fetchTaksata(6);
        else if(user.getLevel().equals("ثانوى")||user.getLevel().equals("اعدادى")||user.getLevel().equals( "جامعيين و خريجين"))
            fetchTaksata(7);


        taksAdapter = new TaksAdapter(new ArrayList<>(), this);
        taksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taksRecyclerView.setAdapter(taksAdapter);
    }

    private void fetchTaksata(int level) {
        db.collection("taks")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Taks> taksList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Taks taks = document.toObject(Taks.class);

                        if (taks.getAgeLevel().contains(level))
                            taksList.add(taks);
                    }

                    // Update the RecyclerView adapter with the fetched data
                    taksAdapter.submitList(taksList);
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Log.e("Firestore", "Error getting documents.", e);
                });
    }
}