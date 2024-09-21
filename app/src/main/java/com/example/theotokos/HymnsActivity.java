package com.example.theotokos;

import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class HymnsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore db;
    private final List<Hymn> hymnList = new ArrayList<>();
    private Spinner hymnSpinner;
    private OnDataFetchedListener listener;
    private DataCache dataCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hymns);
        Objects.requireNonNull(getSupportActionBar()).hide();
        hymnSpinner = findViewById(R.id.titlesSpinner);
        db = FirebaseFirestore.getInstance();
        dataCache = DataCache.getInstance(this);
        User user = dataCache.getUser();
        if(user.getLevel().equals( "حضانة"))
            fetchHymnData(0);
        else if(user.getLevel().equals( "أولى ابتدائى"))
            fetchHymnData(1);
        else if(user.getLevel().equals( "ثانية ابتدائى"))
            fetchHymnData(2);
        else if(user.getLevel().equals( "ثالثة ابتدائى"))
            fetchHymnData(3);
        else if(user.getLevel().equals( "رابعة ابتدائى"))
            fetchHymnData(4);
        else if(user.getLevel().equals( "خامسة ابتدائى"))
            fetchHymnData(5);
        else if(user.getLevel().equals( "سادسة ابتدائى"))
            fetchHymnData(6);
        else if(user.getLevel().equals("ثانوى")||user.getLevel().equals("اعدادى")||user.getLevel().equals( "جامعيين و خريجين"))
            fetchHymnData(7);

        listener = new OnDataFetchedListener() {
            @Override
            public void onDataFetched(List<Hymn> hymns) {
                Log.d( "onDataFetched: ", "Start Copying " + hymns.size());
                hymnList.addAll(hymns);
            }

            @Override
            public void onDataFetchFailed(Exception e) {

            }
        };
        try {
            hymnSpinner.setOnItemSelectedListener(this);

        }catch (Exception e){
            Log.e( "onCreate: ", e.getMessage());
        }
    }

    @NonNull
    private void fetchHymnData(int level) {
        List<String> hymnTitles = new ArrayList<>();

        db.collection("hymns")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Hymn> hymnsArray = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Hymn hymn = document.toObject(Hymn.class);
                        if (hymn.getAgeLevel() != null && hymn.getAgeLevel().contains(level)){
                            hymnTitles.add(hymn.getTitle());
                            hymnsArray.add(hymn);
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hymnTitles);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    hymnSpinner.setAdapter(adapter);
                    listener.onDataFetched(hymnsArray);
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Toast.makeText(this, "خطأ برجاء المحاولة وقت لاحق", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String selectedItem = parent.getItemAtPosition(position).toString();

        Hymn hymn = hymnList.stream().filter(hy -> hy.getTitle().equals(selectedItem)).findFirst().orElse(null);

        try {
            // Determine the layout based on the selected item
            if (hymn != null) {
                String[] arabicArr= hymn.getArabicContent().split("\\|");

                String[] copticArr= hymn.getCopticContent().split("\\|");
                String[] copticArabicArr= hymn.getCopticArabicContent().split("\\|");

                TableLayout tableLayout = TableLayoutHelper.createTableLayout(this, arabicArr, copticArr, copticArabicArr);
                ScrollView scrollView = findViewById(R.id.scrollView2);
                scrollView.removeAllViews();
                scrollView.addView(tableLayout);

            }
        }catch (Exception ex)
        {
            Log.e("onItemSelected: ", ex.getMessage() );
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    interface OnDataFetchedListener {
        void onDataFetched(List<Hymn> hymns);
        void onDataFetchFailed(Exception e);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}