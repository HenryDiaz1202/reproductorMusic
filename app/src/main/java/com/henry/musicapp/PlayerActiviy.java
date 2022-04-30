package com.henry.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;

public class PlayerActiviy extends AppCompatActivity {

    Button btnplay, btnnext, btnbefore;
    TextView txtSongName, txtSongStart, txtSongEnd;
    SeekBar seekbuscarMusica;

    BarVisualizer barVisualizer;
    ImageView imageView;
    String songName;

    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> misMusicas;

    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_activiy);

        btnbefore = findViewById(R.id.btnAntes);
        btnnext = findViewById(R.id.btnDespues);
        btnplay = findViewById(R.id.btnPausa);

        txtSongName = findViewById(R.id.txtSong);
        txtSongStart = findViewById(R.id.txtStart);
        txtSongEnd = findViewById(R.id.txtEnd);

        seekbuscarMusica = findViewById(R.id.seekBar);
        //barVisualizer = findViewById(R.id.wave);

        imageView = findViewById(R.id.idCentroImg);

        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.release();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        misMusicas = (ArrayList) bundle.getParcelableArrayList("musicas");
        String sNombre = intent.getStringExtra("songname");
        position = bundle.getInt("pos", 0);
        txtSongName.setSelected(true);
        Uri uri = Uri.parse(misMusicas.get(position).toString());
        songName = misMusicas.get(position).getName();
        txtSongName.setText(songName);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        updateSeekBar = new Thread() {
            @Override
            public void run() {
                int totalDuracion = mediaPlayer.getDuration();
                int posicionActual = 0;
                while (posicionActual < totalDuracion) {
                    try {
                        sleep(500);
                        posicionActual = mediaPlayer.getCurrentPosition();
                        seekbuscarMusica.setProgress(posicionActual);

                    } catch (IllegalStateException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        seekbuscarMusica.setMax(mediaPlayer.getDuration());
        updateSeekBar.start();
        seekbuscarMusica.getProgressDrawable().setColorFilter(getResources().getColor(R.color.purple_700), PorterDuff.Mode.MULTIPLY);
        seekbuscarMusica.getThumb().setColorFilter(getResources().getColor(R.color.purple_700), PorterDuff.Mode.SRC_IN);

        seekbuscarMusica.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        String endTime = createTime(mediaPlayer.getDuration());
        txtSongEnd.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                txtSongStart.setText(currentTime);
                handler.postDelayed(this, delay);
            }
        }, delay);

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer.isPlaying()) {
                    btnplay.setBackgroundResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                } else {
                    btnplay.setBackgroundResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btnnext.performClick();
            }
        });

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position + 1) % misMusicas.size());
                Uri uri = Uri.parse(misMusicas.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                songName = misMusicas.get(position).getName();
                txtSongName.setText((songName));
                mediaPlayer.start();
                empezarAnimacion(imageView, 360f);
            }
        });

        btnbefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position - 1) < 0) ? (misMusicas.size() - 1) : position - 1;
                Uri uri = Uri.parse(misMusicas.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                songName = misMusicas.get(position).getName();
                txtSongName.setText((songName));
                mediaPlayer.start();
                empezarAnimacion(imageView, -360f);
            }
        });
    }

    int audio

    public void  empezarAnimacion(View view, Float grados){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"rotation",0f, grados);
        objectAnimator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator);
        animatorSet.start();
    }

    public String createTime(int duracion){
        String time = "";
        int min = duracion/1000/60;
        int sec = duracion/1000%60;

        time = time+min+":";
        if(sec<10){
            time+="0";
        }
        time+=sec;
        return time;
    }

}