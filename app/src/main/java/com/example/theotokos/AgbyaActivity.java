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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AgbyaActivity extends AppCompatActivity {

    private RecyclerView agbyaRecyclerView;
    private AgbyaAdapter agbyaAdapter;
    private FirebaseFirestore db;
    private DataCache dataCache;
    private final List<String> studentLevels = Arrays.asList(new String[]{"حضانة", "أولى ابتدائى", "ثانية ابتدائى", "ثالثة ابتدائى", "رابعة ابتدائى", "خامسة ابتدائى", "سادسة ابتدائى", "اعدادى", "ثانوى", "جامعيين و خريجين"});

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
        Objects.requireNonNull(getSupportActionBar()).hide();
        dataCache = DataCache.getInstance(this);
        agbyaRecyclerView = findViewById(R.id.agbyaRecyclerView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        db = FirebaseFirestore.getInstance();

        User user = dataCache.getUser();
        if(user.getLevel().equals( "حضانة"))
            fetchAgbyaData(0);
        else if(user.getLevel().equals( "أولى ابتدائى"))
            fetchAgbyaData(1);
        else if(user.getLevel().equals( "ثانية ابتدائى"))
            fetchAgbyaData(2);
        else if(user.getLevel().equals( "ثالثة ابتدائى"))
            fetchAgbyaData(3);
        else if(user.getLevel().equals( "رابعة ابتدائى"))
            fetchAgbyaData(4);
        else if(user.getLevel().equals( "خامسة ابتدائى"))
            fetchAgbyaData(5);
        else if(user.getLevel().equals( "سادسة ابتدائى"))
            fetchAgbyaData(6);
        else if(user.getLevel().equals("ثانوى")||user.getLevel().equals("اعدادى")||user.getLevel().equals( "جامعيين و خريجين"))
            fetchAgbyaData(7);


        agbyaAdapter = new AgbyaAdapter(new ArrayList<>(), this);
        agbyaRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        agbyaRecyclerView.setAdapter(agbyaAdapter);


    }

    private void fetchAgbyaData(int level) {
        db.collection("agbya")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Agbya> agbyaList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Agbya agbya = document.toObject(Agbya.class);

                        if (agbya.getAgeLevel().contains(level))
                            agbyaList.add(agbya);
                    }
                    agbyaList.sort(Comparator.comparing(Agbya::getTitle));
                    // Update the RecyclerView adapter with the fetched data
                    agbyaAdapter.submitList(agbyaList);
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Log.e("Firestore", "Error getting documents.", e);
                });
    }
}