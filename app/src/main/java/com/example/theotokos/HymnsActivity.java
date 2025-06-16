package com.example.theotokos;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import java.io.File;

public class HymnsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final List<Hymn> hymnList = new ArrayList<>();
    private OnDataFetchedListener listener;
    private FloatingActionButton fab;
    private DataCache dataCache;
    private ExoPlayer exoPlayer;
    private String[] studentLevels = {"حضانة", "أولى و تانية ابتدائى", "ثالثة و رابعة ابتدائى", "خامسة و سادسة ابتدائى", "اعدادى فما فوق"};
    private AutoCompleteTextView spStudentLevel, hymn;
    String selectedLevel = "";
    private Cache cache;
    private User user;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hymns);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }        EdgeToEdge.enable(this);
        hymn = findViewById(R.id.titlesSpinner);
        fab = findViewById(R.id.play_pause_btn);
        dataCache = DataCache.getInstance(this);
        spStudentLevel = findViewById(R.id.spStudentLevel);
        user = dataCache.getUser();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item , studentLevels);
        spStudentLevel.setAdapter(adapter);
        // Initialize ExoPlayer with caching
        initializeExoPlayerWithCache();
//         // Change icon to "pause"

        // Fetch the audio URL from Firestore (same as before)
//        fetchAudioUrlFromFirestore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchHymnData(0);
        spStudentLevel.setOnItemClickListener((adapterView, view1, i, l) -> {
            Toast.makeText(HymnsActivity.this, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
            selectedLevel = adapterView.getItemAtPosition(i).toString();
            switch (selectedLevel) {
                case "حضانة":
                    fetchHymnData(0);
                    break;
                case  "أولى و تانية ابتدائى":
                    fetchHymnData(1);
                    break;
                case "ثالثة و رابعة ابتدائى":
                    fetchHymnData(3);
                    break;
                case "خامسة و سادسة ابتدائى":
                    fetchHymnData(5);
                    break;
                case  "اعدادى فما فوق":
                    fetchHymnData(7);
                    break;
            }
        });
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
            hymn.setOnItemClickListener(this);
            fab.setOnClickListener(v -> togglePlayPause());
        }catch (Exception e){
            Log.e( "onCreate: ", e.getMessage());
        }

    }

    @NonNull
    private void fetchHymnData(int level) {
        List<String> hymnTitles = new ArrayList<>();

        FirebaseHelper.getHymnsDatabase().whereArrayContains("ageLevel", level)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Hymn> hymnsArray = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Hymn hymn = document.toObject(Hymn.class);


                        if (hymn.getAgeLevel() != null){
                            hymnTitles.add(hymn.getTitle());
                            hymnsArray.add(hymn);
                        }
                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hymnTitles);

                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    hymn.setAdapter(adapter);
                    listener.onDataFetched(hymnsArray);
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Toast.makeText(this, "خطأ برجاء المحاولة وقت لاحق", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedItem = adapterView.getItemAtPosition(i).toString();
        TextView hymnTitle = findViewById(R.id.HymnName);
        hymnTitle.setText(selectedItem);
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
                playAudio(hymn.getAudio());
                fab.setImageResource(R.drawable.baseline_pause_24);
                isPlaying = true;


            }
        }catch (Exception ex)
        {
            Log.e("onItemSelected: ", ex.getMessage() );
        }
    }


    interface OnDataFetchedListener {
        void onDataFetched(List<Hymn> hymns);
        void onDataFetchFailed(Exception e);
    }


    private void pauseAudio() {
        if (exoPlayer != null) {
            exoPlayer.pause();
        }
    }

    private void stopAudio() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }
    private void initializeExoPlayerWithCache() {
        // Create a cache directory
        File cacheDir = new File(getCacheDir(), "media");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        // Initialize the cache
        cache = new SimpleCache(cacheDir, new NoOpCacheEvictor());

        // Build the player
        exoPlayer = new ExoPlayer.Builder(this).build();
    }

    private void playAudio(String audioUrl) {
        // Create a MediaSource with caching
        DataSource.Factory cacheDataSourceFactory = new CacheDataSource.Factory()
                .setCache(cache)
                .setUpstreamDataSourceFactory(new DefaultDataSource.Factory(this));

        MediaSource mediaSource = new ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(audioUrl)));

        // Prepare the player
        exoPlayer.setMediaSource(mediaSource);
        exoPlayer.prepare();
        exoPlayer.play();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
        }
        if (cache != null) {
            cache.release();
        }
    }

    private void togglePlayPause() {
        if (exoPlayer != null) {
            if (isPlaying) {
                // Pause playback
                exoPlayer.pause();
                fab.setImageResource(R.drawable.baseline_play_arrow_24); // Change icon to "play"
            } else {
                // Start or resume playback
                exoPlayer.play();
                fab.setImageResource(R.drawable.baseline_pause_24); // Change icon to "pause"
            }
            isPlaying = !isPlaying; // Toggle the state
        }
    }
}