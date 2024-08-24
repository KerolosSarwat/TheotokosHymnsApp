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

public class HymnsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore db;
    private final List<Hymn> hymnList = new ArrayList<>();
    private LinearLayout hymnContainer;
    private Spinner hymnSpinner;
    private TableLayout tableLayout;
    TextView hymnTitle, hymnCopticContent, hymnArabicContent, hymnCopticArabicContent;
    private OnDataFetchedListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        setContentView(R.layout.activity_hymns);

        hymnSpinner = findViewById(R.id.titlesSpinner);
        //tableLayout = findViewById(R.id.tableContent);
        db = FirebaseFirestore.getInstance();

        fetchHymnData();

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
            Log.d("Kirollos", "list length: "+hymnList.size() );
            hymnSpinner.setOnItemSelectedListener(this);
            //hymnCopticContent.setText(hymnList.get(1).getArabicContent());

        }catch (Exception e){
            Log.e( "onCreate: ", e.getMessage());
        }
    }

    @NonNull
    private void fetchHymnData() {
        List<String> hymnTitles = new ArrayList<>();

        db.collection("hymns")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Hymn> hymnsArray = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Hymn hymn = document.toObject(Hymn.class);

                        hymnTitles.add(hymn.getTitle());
                        hymnsArray.add(hymn);

                        Log.i("Title", "Data fethced to array: " +  hymnsArray);
                        //hymnsArray.add(new Hymn(document.get("copticArabicContent").toString(), document.get("arabicContent").toString(), document.get("copticContent").toString(), document.get("title").toString()));
                    }
                    Log.i("Kirollos", "Data fethced to list size " + hymnsArray.size());


                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hymnTitles);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    hymnSpinner.setAdapter(adapter);
                    listener.onDataFetched(hymnsArray);

                    Log.d("Kirollos", "Data Fetched successfully");

                    // Do something with the hymnList, like updating a RecyclerView
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Log.e("Firestore", "Error getting documents.", e);
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

                Log.d( "onItemSelected: ", ""+arabicArr);

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