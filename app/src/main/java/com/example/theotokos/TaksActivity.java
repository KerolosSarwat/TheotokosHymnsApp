package com.example.theotokos;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaksActivity extends AppCompatActivity {

    private DataCache dataCache;
    private ProgressBar progressBar;
    private ExpandableListView expandableListView;
    private CustomExpandableListAdapter adapter;
    private List<String> groupList;
    private Map<String, List<Taks>> childMap;
    private String[] studentLevels = {"حضانة", "أولى و تانية ابتدائى", "ثالثة و رابعة ابتدائى", "خامسة و سادسة ابتدائى", "اعدادى فما فوق"};
    private AutoCompleteTextView spStudentLevel, hymn;
    private String selectedLevel = "";
    private List<Taks> taksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taks);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }        dataCache = DataCache.getInstance(this);
        taksList = new ArrayList<>();
        expandableListView = findViewById(R.id.expandableListView);
        progressBar = findViewById(R.id.progressindicator);
        spStudentLevel = findViewById(R.id.spStudentLevel);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item , studentLevels);
        spStudentLevel.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        expandableListView.setVisibility(View.GONE);
        spStudentLevel.setOnItemClickListener((adapterView, view1, i, l) -> {
            selectedLevel = adapterView.getItemAtPosition(i).toString();
            //User user = dataCache.getUser();
            switch (selectedLevel) {
                case "حضانة":
                    fetchTaksata(0);
                    break;
                case  "أولى و تانية ابتدائى":
                    fetchTaksata(1);
                    break;
                case "ثالثة و رابعة ابتدائى":
                    fetchTaksata(3);
                    break;
                case "خامسة و سادسة ابتدائى":
                    fetchTaksata(5);
                    break;
                case  "اعدادى فما فوق":
                    fetchTaksata(7);
                    break;
            }
        });

        progressBar.setVisibility(View.GONE);
        expandableListView.setVisibility(View.VISIBLE);

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            // Get the selected Agbya object
            Taks selectedTaks = (Taks) adapter.getChild(groupPosition, childPosition);
            Intent intent = new Intent(this, TaksDetailsActivity.class).putExtra("taks", selectedTaks);
            this.startActivity(intent);

            return true;
        });
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
            List<Taks> firstTermItems = taksList.stream().filter(agb -> agb.getTerm() == 1).collect(Collectors.toList());
            List<Taks> secondTermItems = taksList.stream().filter(agb -> agb.getTerm() == 2).collect(Collectors.toList());
            List<Taks> thirdTermItems = taksList.stream().filter(agb -> agb.getTerm() == 3).collect(Collectors.toList());

            childMap.put(groupList.get(0), firstTermItems);
            childMap.put(groupList.get(1), secondTermItems);
            childMap.put(groupList.get(2), thirdTermItems);
        }catch (Exception exception){
            Log.e( "prepareData: ", exception.getMessage());
        }
    }
    private void fetchTaksata(int level) {
        FirebaseHelper.getTaksDatabase().whereArrayContains("ageLevel", level)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    taksList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            Taks taks = document.toObject(Taks.class);
                            if (taks != null)
                                taksList.add(taks);

                        } catch (Exception exception) {
                            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    Collections.sort(taksList, (s1, s2) -> {
                        int num1 = Integer.parseInt(s1.getTitle().split("-")[0].trim());
                        int num2 = Integer.parseInt(s2.getTitle().split("-")[0].trim());
                        return Integer.compare(num1, num2);
                    });
                    prepareData();
                    // Initialize the adapter
                    adapter = new CustomExpandableListAdapter<Taks>(this, groupList, childMap);

                    // Set the adapter to the ExpandableListView
                    expandableListView.setAdapter(adapter);
                });

    }
}