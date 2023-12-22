package com.example.bottomnavigationview;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageButton btnPlayPause, btnforward, btnrevind;
    TextView songname, songCurrentTime, songTotalTime;
    private ImageView imageView;
    MediaPlayer player;
    MainFragment mainFragment=new MainFragment();
    SeekBar seekBar;
    int CurrentSongIndex = 0;
    ArrayList<String> musicpaths = new ArrayList<String>();
    ArrayList<String> musicnames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);// yeah apna khud k toolbar set ho jayega



        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnforward = findViewById(R.id.btnforward);
        btnrevind = findViewById(R.id.btnrevind);
        seekBar = findViewById(R.id.seekBar);
        imageView = findViewById(R.id.imageView);
        songname = findViewById(R.id.songname);
        songCurrentTime = findViewById(R.id.songCurrentTime);
        songTotalTime = findViewById(R.id.songTotalTime);


        // Initialize the MediaPlayer here
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // Yeah ek class h jisse agar action bar open h toh close h toh open and open h toh close ho jayega
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);

        //Yeah drawer ko enable kar dega slide bhi kar sakte and swipe bhi
        drawerLayout.addDrawerListener(toggle);

        // yeah bata deta h ki navigation view open h yah close state ko sync kar deta h
        toggle.syncState();
        loadFragments(new MainFragment());


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                // Release and reset the MediaPlayer before setting new data source
                if (player.isPlaying() || player.isLooping()) {
                    player.stop();
                }
                player.reset();
                mainFragment.setarray();
                if (id == R.id.RiversFLowsinyou) {

                    mainFragment.playSong(0);
                } else if (id == R.id.Orange) {
                    mainFragment.playSong(1);
                } else if (id == R.id.Interstellar) {
                    mainFragment.playSong(2);
                } else if (id == R.id.Polaris) {
                    mainFragment.playSong(3);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadFragments(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, fragment);
        ft.commit();

    }
    private void playAudioFromResource(int resourceId) {
        try {
            // Set the data source for the MediaPlayer using the provided resource ID
            player.setDataSource(getApplicationContext(), Uri.parse("android.resource://" + getPackageName() + "/" + resourceId));
            player.prepare(); // Prepare the MediaPlayer for playback
            player.start(); // Start playing the audio
        } catch (IOException e) {
            Toast.makeText(this, "Error playing audio", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void updateSeekBarProgress() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (player != null && player.isPlaying()) {
                    int currentPosition = player.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                    String currentTime = createTimerLabel(currentPosition);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            songCurrentTime.setText(currentTime);
                        }
                    });
                }
            }
        }, 0, 900); // Update SeekBar and TextView every 900 milliseconds
    }
    public String createTimerLabel(int duration) {
        String timerLabel = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        timerLabel += min + ":";
        if (sec < 10) {
            timerLabel += "0";
        }
        timerLabel += sec;
        return timerLabel;
    }
}