package com.example.theotokos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import java.util.Map;
import java.util.HashMap;

public class AgbyaActivity extends AppCompatActivity {

    private RecyclerView agbyaRecyclerView, secondRecyclerView, thirdRecyclerView;
    private AgbyaAdapter firstTermAdapter, secondTermAdapter, thirdTermAdapter ;
    private FirebaseFirestore db;
    private DataCache dataCache;
    private final List<String> studentLevels = Arrays.asList(new String[]{"حضانة", "أولى ابتدائى", "ثانية ابتدائى", "ثالثة ابتدائى", "رابعة ابتدائى", "خامسة ابتدائى", "سادسة ابتدائى", "اعدادى", "ثانوى", "جامعيين و خريجين"});
    private ProgressBar progressBar;
    private ExpandableListView expandableListView;
    private CustomExpandableListAdapter adapter;
    private List<String> groupList;
    private Map<String, List<Agbya>> childMap;
    private List<Agbya> agbyaList;
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
        agbyaList = new ArrayList<>();
        expandableListView = findViewById(R.id.expandableListView);
        Objects.requireNonNull(getSupportActionBar()).hide();
        dataCache = DataCache.getInstance(this);
//        agbyaRecyclerView = findViewById(R.id.agbyaRecyclerView);
//        secondRecyclerView = findViewById(R.id.secondRecyclerView);
//        thirdRecyclerView = findViewById(R.id.thirdRecyclerView);
        progressBar = findViewById(R.id.progressBar2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        db = FirebaseFirestore.getInstance();

        try {
            User user = dataCache.getUser();
            switch (user.getLevel()) {
                case "حضانة":
                    fetchAgbyaData(0);
                    break;
                case "أولى ابتدائى":
                    fetchAgbyaData(1);
                    break;
                case "ثانية ابتدائى":
                    fetchAgbyaData(2);
                    break;
                case "ثالثة ابتدائى":
                    fetchAgbyaData(3);
                    break;
                case "رابعة ابتدائى":
                    fetchAgbyaData(4);
                    break;
                case "خامسة ابتدائى":
                    fetchAgbyaData(5);
                    break;
                case "سادسة ابتدائى":
                    fetchAgbyaData(6);
                    break;
                case "ثانوى":
                case "اعدادى":
                case "جامعيين و خريجين":
                    fetchAgbyaData(7);
                    break;
            }
            expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {


                // Get the selected Agbya object
                Agbya selectedAgbya = (Agbya) adapter.getChild(groupPosition, childPosition);
                Intent intent = new Intent(this, AgbyaDetailsActivity.class).putExtra("agbya", selectedAgbya);
                this.startActivity(intent);

                return true;
            });
        }catch (Exception ex){
            Log.e( "Agbya Activity onResume: ", ex.getMessage());
        }




//        firstTermAdapter = new AgbyaAdapter(new ArrayList<>(), this);
//        secondTermAdapter = new AgbyaAdapter(new ArrayList<>(), this);
//        thirdTermAdapter = new AgbyaAdapter(new ArrayList<>(), this);
//        agbyaRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        agbyaRecyclerView.setAdapter(firstTermAdapter);
//        secondRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        secondRecyclerView.setAdapter(secondTermAdapter);
//        thirdRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        thirdRecyclerView.setAdapter(thirdTermAdapter);

        progressBar.setVisibility(View.INVISIBLE);
    }
    private void prepareData() {
        groupList = new ArrayList<>();
        childMap = new HashMap<>();

        // Add group headers (terms)
        groupList.add("الترم الأول");
        groupList.add("الترم الثانى");
        groupList.add("الترم الثالث");

        // Add child items for each group
        try {
            List<Agbya> firstTermItems = agbyaList.stream().filter(agb -> agb.getTerm() == 1).collect(Collectors.toList());
            List<Agbya> secondTermItems = agbyaList.stream().filter(agb -> agb.getTerm() == 2).collect(Collectors.toList());
            List<Agbya> thirdTermItems = agbyaList.stream().filter(agb -> agb.getTerm() == 3).collect(Collectors.toList());

            childMap.put(groupList.get(0), firstTermItems);
            childMap.put(groupList.get(1), secondTermItems);
            childMap.put(groupList.get(2), thirdTermItems);
        }catch (Exception exception){
            Log.e( "prepareData: ", exception.getMessage());
        }
    }

    private void fetchAgbyaData(int level) {
        List<Agbya> list = new ArrayList<>();
        db.collection("agbya").whereArrayContains("ageLevel", level)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    agbyaList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Agbya agbya = document.toObject(Agbya.class);
                        if ( agbya != null ) {
                            agbyaList.add(agbya);
                        }else {
                            String documentId = document.getId();
                            Log.e("Firestore", "Error converting document: " + documentId); // Use your logging mechanism
                        }
                    }
                    list.sort((s1, s2) -> {
                        int num1 = Integer.parseInt(s1.getTitle().split("-")[0].trim());
                        int num2 = Integer.parseInt(s2.getTitle().split("-")[0].trim());
                        return Integer.compare(num1, num2);
                    });
                    prepareData();
                    // Initialize the adapter
                    adapter = new CustomExpandableListAdapter(this, groupList, childMap);

                    // Set the adapter to the ExpandableListView
                    expandableListView.setAdapter(adapter);
                    // Update the RecyclerView adapter with the fetched data
//                    try {
//                        firstTermAdapter.submitList(agbyaList.stream().filter(agbya -> agbya.getTerm() == 1).collect(Collectors.toList()));
//                        secondTermAdapter.submitList(agbyaList.stream().filter(agbya -> agbya.getTerm() == 2).collect(Collectors.toList()));
//                        thirdTermAdapter.submitList(agbyaList.stream().filter(agbya -> agbya.getTerm() == 3).collect(Collectors.toList()));
//
//                    }catch (Exception ex){
//                        Log.e("fetchAgbyaData: ", ex.getMessage());
//                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Log.e("Firestore", "Error getting documents." + e.getMessage());
                });
    }
}