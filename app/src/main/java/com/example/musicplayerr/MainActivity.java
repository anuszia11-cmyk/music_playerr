package com.example.musicplayerr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerr.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // Music Player UI components
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private TextView textCurrentTime, textTotalTime;
    private ImageView buttonPlay, buttonPause, buttonStop, buttonLogout;

    // API & List components
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<post> postList = new ArrayList<>();
    private ProgressBar loadingSpinner;

    private final Handler handler = new Handler(Looper.getMainLooper());

    // Thread for real-time SeekBar updates
    private final Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                textCurrentTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply saved theme before view creation to prevent flickering
        ThemeUtils.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();
        setupMediaPlayer();
        fetchData(); // Step 1: Initialize API Data Fetching
    }

    private void initializeUI() {
        seekBar = findViewById(R.id.seekBar);
        textCurrentTime = findViewById(R.id.textCurrentTime);
        textTotalTime = findViewById(R.id.textTotalTime);
        buttonPlay = findViewById(R.id.buttonPlay);
        buttonPause = findViewById(R.id.buttonPause);
        buttonStop = findViewById(R.id.buttonStop);
        buttonLogout = findViewById(R.id.buttonLogout);

        // Setup RecyclerView for API data display
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);

        // Music Controls
        buttonPlay.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.start();
                handler.post(updateSeekBar);
            }
        });

        buttonPause.setOnClickListener(v -> {
            if (mediaPlayer != null) mediaPlayer.pause();
        });

        buttonStop.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
                seekBar.setProgress(0);
                textCurrentTime.setText("0:00");
            }
        });

        if (buttonLogout != null) {
            buttonLogout.setOnClickListener(v -> logoutUser());
        }
    }

    private void setupMediaPlayer() {
        // Loading local raw resource (Ariana Grande - breathin)
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        if (mediaPlayer != null) {
            mediaPlayer.setOnPreparedListener(mp -> {
                seekBar.setMax(mp.getDuration());
                textTotalTime.setText(formatTime(mp.getDuration()));
            });
        }
    }

    /**
     * STEP 1: API Data Source Implementation
     * Uses Retrofit to fetch JSON data and syncs with SQLite for offline persistence.
     */
    private void fetchData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/") // Central Data URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getPosts().enqueue(new Callback<List<post>>() {
            @Override
            public void onResponse(Call<List<post>> call, Response<List<post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_SUCCESS", "Data received: " + response.body().size() + " items.");

                    postList.clear();
                    postList.addAll(response.body());
                    postAdapter.notifyDataSetChanged();

                    // Sync API data to Local SQLite Database
                    DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                    for (post p : response.body()) {
                        db.insertData(p.getId(), p.getTitle(), p.getBody());
                    }
                    Toast.makeText(MainActivity.this, "Feed Updated", Toast.LENGTH_SHORT).show();
                } else {
                    loadOfflineData();
                }
            }

            @Override
            public void onFailure(Call<List<post>> call, Throwable t) {
                Log.e("API_FAILURE", "Network error, switching to local storage.");
                loadOfflineData();
            }
        });
    }

    private void loadOfflineData() {
        DatabaseHelper db = new DatabaseHelper(this);
        List<post> offlineData = db.getAllPosts();
        if (!offlineData.isEmpty()) {
            postList.clear();
            postList.addAll(offlineData);
            postAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Offline Mode: Loaded from Cache", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_light_theme) {
            ThemeUtils.saveTheme(this, androidx.appcompat.R.style.Theme_AppCompat_Light_DarkActionBar);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            return true;
        } else if (id == R.id.action_dark_theme) {
            ThemeUtils.saveTheme(this, androidx.appcompat.R.style.Theme_AppCompat_DayNight_DarkActionBar);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            return true;
        } else if (id == R.id.action_webview) {
            startActivity(new Intent(this, WebContentActivity.class));
            return true;
        } else if (id == R.id.menu_logout) {
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        SharedPreferences prefs = getSharedPreferences("MusicAppPrefs", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("isLoggedIn", false).apply();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    private String formatTime(int milliseconds) {
        return String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateSeekBar);
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}