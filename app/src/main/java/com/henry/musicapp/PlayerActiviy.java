package com.henry.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;

public class PlayerActiviy extends AppCompatActivity {

    Button btnplay,btnnext,btnbefore;
    TextView txtSongName, txtSongStart, txtSongEnd;
    SeekBar buscarMusica;

    BarVisualizer barVisualizer;
    ImageView imageView;
    String songName;

    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> misMusicas;

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

        buscarMusica = findViewById(R.id.seekBar);
        //barVisualizer = findViewById(R.id.wave);

        imageView = findViewById(R.id.idCentroImg);

        if(mediaPlayer != null){
            mediaPlayer.start();
            mediaPlayer.release();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        misMusicas = (ArrayList)bundle.getParcelableArrayList("musicas");
        String sNombre = intent.getStringExtra("songname");
        position = bundle.getInt("pos",0);
        txtSongName.setSelected(true);
        Uri uri = Uri.parse(misMusicas.get(position).toString());
        songName = misMusicas.get(position).getName();
        txtSongName.setText(songName);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mediaPlayer.isPlaying()){
                    btnplay.setBackgroundResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                }
                else{
                    btnplay.setBackgroundResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }

            }
        });
    }
}