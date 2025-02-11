package com.example.theotokos;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


public class AgbyaActivity extends AppCompatActivity {

    private RecyclerView agbyaRecyclerView, secondRecyclerView, thirdRecyclerView;
    private AgbyaAdapter firstTermAdapter, secondTermAdapter, thirdTermAdapter ;
    private FirebaseFirestore db;
    private DataCache dataCache;
    private final List<String> studentLevels = Arrays.asList(new String[]{"حضانة", "أولى ابتدائى", "ثانية ابتدائى", "ثالثة ابتدائى", "رابعة ابتدائى", "خامسة ابتدائى", "سادسة ابتدائى", "اعدادى", "ثانوى", "جامعيين و خريجين"});
    private ProgressBar progressBar;
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
        secondRecyclerView = findViewById(R.id.secondRecyclerView);
        thirdRecyclerView = findViewById(R.id.thirdRecyclerView);
        progressBar = findViewById(R.id.progressBar2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        db = FirebaseFirestore.getInstance();

        try {
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
        }catch (Exception ex){
            Log.e( "Agbya Activity onResume: ", ex.getMessage());
        }



        firstTermAdapter = new AgbyaAdapter(new ArrayList<>(), this);
        secondTermAdapter = new AgbyaAdapter(new ArrayList<>(), this);
        thirdTermAdapter = new AgbyaAdapter(new ArrayList<>(), this);
        agbyaRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        agbyaRecyclerView.setAdapter(firstTermAdapter);
        secondRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        secondRecyclerView.setAdapter(secondTermAdapter);
        thirdRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        thirdRecyclerView.setAdapter(thirdTermAdapter);

        progressBar.setVisibility(View.INVISIBLE);
    }

    private void fetchAgbyaData(int level) {
        db.collection("agbya").whereArrayContains("ageLevel", level)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Agbya> agbyaList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Agbya agbya = document.toObject(Agbya.class);
                        if ( agbya != null ) {
                            //Log.e("Agbya Activity","Agbya Title: "+ agbya.getTitle());
                            agbyaList.add(agbya);
                        }else {
                            String documentId = document.getId();
                            Log.e("Firestore", "Error converting document: " + documentId); // Use your logging mechanism
                        }
                    }
                    agbyaList.sort((s1, s2) -> {
                        int num1 = Integer.parseInt(s1.getTitle().split("-")[0].trim());
                        int num2 = Integer.parseInt(s2.getTitle().split("-")[0].trim());
                        return Integer.compare(num1, num2);
                    });
                    // Update the RecyclerView adapter with the fetched data
                    try {
                        firstTermAdapter.submitList(agbyaList.stream().filter(agbya -> agbya.getTerm() == 1).collect(Collectors.toList()));
                        secondTermAdapter.submitList(agbyaList.stream().filter(agbya -> agbya.getTerm() == 2).collect(Collectors.toList()));
                        thirdTermAdapter.submitList(agbyaList.stream().filter(agbya -> agbya.getTerm() == 3).collect(Collectors.toList()));

                    }catch (Exception ex){
                        Log.e("fetchAgbyaData: ", ex.getMessage());
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Log.e("Firestore", "Error getting documents." + e.getMessage());
                });
    }
}