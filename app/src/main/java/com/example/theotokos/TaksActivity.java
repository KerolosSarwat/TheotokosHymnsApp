package com.example.theotokos;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
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
import java.util.List;
import java.util.Objects;

public class TaksActivity extends AppCompatActivity {

    private RecyclerView taksRecyclerView;
    private TaksAdapter taksAdapter;
    private FirebaseFirestore db;
    private DataCache dataCache;
    private ProgressBar progressBar;

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
        Objects.requireNonNull(getSupportActionBar()).hide();
        dataCache = DataCache.getInstance(this);
        taksRecyclerView = findViewById(R.id.taksRecyclerView);
        progressBar = findViewById(R.id.progressindicator);

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
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
        else if(user.getLevel().equals("ثانوى")||user.getLevel().equals("اعدادى")||user.getLevel().equals("جامعيين و خريجين"))
            fetchTaksata(7);


        taksAdapter = new TaksAdapter(new ArrayList<>(), this);
        taksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taksRecyclerView.setAdapter(taksAdapter);
        progressBar.setVisibility(View.GONE);
    }

    private void fetchTaksata(int level) {
        db.collection("taks").whereArrayContains("ageLevel", level)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Taks> taksList = new ArrayList<>();

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
                    taksAdapter.submitList(taksList);
                });

    }
}