package com.example.bottomnavigationview;
import android.net.Uri;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    AppCompatImageButton btnPlayPause, btnforward, btnrevind;
    TextView songname, songCurrentTime, songTotalTime;
    ImageView imageView;
    MediaPlayer player=new MediaPlayer();
    SeekBar seekBar;
    int CurrentSongIndex = 0;
    ArrayList<String> musicpaths = new ArrayList<String>();
    ArrayList<String> musicnames = new ArrayList<String>();
    public MainFragment() {


    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        btnforward=rootView.findViewById(R.id.btnforward);
        btnPlayPause=rootView.findViewById(R.id.btnPlayPause);
        btnrevind=rootView.findViewById(R.id.btnrevind);
        songname=rootView.findViewById(R.id.songname);
        songCurrentTime=rootView.findViewById(R.id.songCurrentTime);
        songTotalTime=rootView.findViewById(R.id.songTotalTime);
        imageView=rootView.findViewById(R.id.imageView);
        seekBar=rootView.findViewById(R.id.seekBar);

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        String song1 = "android.resource://" + getActivity().getPackageName() + "/raw/riverflowsinyou";
        String song2 = "android.resource://" + getActivity().getPackageName() + "/raw/orange";
        String song3 = "android.resource://" + getActivity().getPackageName() + "/raw/interstellar";
        String song4 = "android.resource://" + getActivity().getPackageName() + "/raw/polaris";
        String song5 = "android.resource://" + getActivity().getPackageName() + "/raw/a_thousand_years";
        String song6 = "android.resource://" + getActivity().getPackageName() + "/raw/all_of_me";
        String song7 = "android.resource://" + getActivity().getPackageName() + "/raw/dandelions";
        String song8 = "android.resource://" + getActivity().getPackageName() + "/raw/moonlight_sonata";
        String song9 = "android.resource://" + getActivity().getPackageName() + "/raw/thoseeyes";
        String song10 = "android.resource://" + getActivity().getPackageName() + "/raw/vampire";

        musicpaths.add(song1);
        musicpaths.add(song2);
        musicpaths.add(song3);
        musicpaths.add(song4);
        musicpaths.add(song5);
        musicpaths.add(song6);
        musicpaths.add(song7);
        musicpaths.add(song8);
        musicpaths.add(song9);
        musicpaths.add(song10);


        musicnames.add("Rivers flows in you");
        musicnames.add("Orange");
        musicnames.add("Interstellar");
        musicnames.add("Polaris");
        musicnames.add("A Thousand years");
        musicnames.add("All of me");
        musicnames.add("Dandelions");
        musicnames.add("Moonlight sonata");
        musicnames.add("Those eyes");
        musicnames.add("Vampire");


        String currentsong = musicpaths.get(CurrentSongIndex);
        Uri audioURI = Uri.parse(currentsong);

        try {
            player.setDataSource(getContext(), audioURI);
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(player.getDuration());
                    updateSeekBarProgress();

                    String totaltime = createTimerLabel(player.getDuration());
                    songTotalTime.setText(totaltime);

                    String currentSongName = musicnames.get(CurrentSongIndex);
                    songname.setText(currentSongName); // Set the initial song name
                }
            });

            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player.isPlaying()) {
                    player.pause();
                    imageView.clearAnimation();
                    btnPlayPause.setImageResource(R.drawable.play);
                } else {
                    player.start();
                    RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setDuration(20000); // 20 seconds
                    rotateAnimation.setRepeatCount(Animation.INFINITE);
                    imageView.startAnimation(rotateAnimation);
                    btnPlayPause.setImageResource(R.drawable.pause);
                }
            }
        });
        btnforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentSongIndex++;
                if (CurrentSongIndex >= musicpaths.size()) {
                    CurrentSongIndex = 0;
                }
                playSong(CurrentSongIndex);
            }
        });
        btnrevind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentSongIndex--;
                if (CurrentSongIndex < 0) {
                    CurrentSongIndex = musicpaths.size() - 1;
                }
                playSong(CurrentSongIndex);
            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                CurrentSongIndex++;
                if (CurrentSongIndex >= musicpaths.size()) {
                    CurrentSongIndex = 0;
                }
                player.stop();
                player.reset();
                playSong(CurrentSongIndex);
            }
        });

        return rootView;
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
        player = null;
    }


    public void playSong(int song) {
        if (player.isPlaying()) {
            player.stop();
            player.reset();
        }

        String songPath = musicpaths.get(song);
        Uri audioURI = Uri.parse(songPath);

        try {
            player.setDataSource(getContext(), audioURI);
            player.prepare();
            player.start();

            String currentSongName = musicnames.get(song);
            songname.setText(currentSongName);

            RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(20000); // 20 seconds
            rotateAnimation.setRepeatCount(Animation.INFINITE);
            imageView.startAnimation(rotateAnimation);
            btnPlayPause.setImageResource(R.drawable.pause);
        } catch (IOException e) {
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            songCurrentTime.setText(currentTime);
                        }
                    });
                }
            }
        }, 0, 900); // Update SeekBar and TextView every 900 milliseconds
    }
    ArrayList<String> passmusicnames(){
        return musicnames;
    }
    ArrayList<String> passmusicpaths(){
        return musicpaths;
    }

    public void playSong(String songpath,int song) {
        if (player.isPlaying()) {
            player.stop();
            player.reset();
        }


        Uri audioURI = Uri.parse(songpath);

        try {
            player.setDataSource(getContext(), audioURI);
            player.prepare();
            player.start();

            String currentSongName = musicnames.get(song);
            songname.setText(currentSongName);

            RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(20000); // 20 seconds
            rotateAnimation.setRepeatCount(Animation.INFINITE);
            imageView.startAnimation(rotateAnimation);
            btnPlayPause.setImageResource(R.drawable.pause);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
public void setarray(){
    String song1 = "android.resource://" + getActivity().getPackageName() + "/raw/riverflowsinyou";
    String song2 = "android.resource://" + getActivity().getPackageName() + "/raw/orange";
    String song3 = "android.resource://" + getActivity().getPackageName() + "/raw/interstellar";
    String song4 = "android.resource://" + getActivity().getPackageName() + "/raw/polaris";

    musicpaths.add(song1);
    musicpaths.add(song2);
    musicpaths.add(song3);
    musicpaths.add(song4);


    musicnames.add("Rivers flows in you");
    musicnames.add("Orange");
    musicnames.add("Interstellar");
    musicnames.add("Polaris");
}
}