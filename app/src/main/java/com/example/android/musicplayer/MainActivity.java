package com.example.android.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    boolean t=true,fin=true;
    SeekBar SR;
    Context context;
    TextView startCont;
    TextView endCont;
    TextView artistName;
    TextView songName;
    Timer timer;

    de.hdodenhof.circleimageview.CircleImageView profileImage;
    de.hdodenhof.circleimageview.CircleImageView play_btn;
    de.hdodenhof.circleimageview.CircleImageView next_btn;
    de.hdodenhof.circleimageview.CircleImageView previous_btn;
    de.hdodenhof.circleimageview.CircleImageView forward_btn;
    de.hdodenhof.circleimageview.CircleImageView backward_btn;
    ImageButton launch_btn;
    int actSongId=-1;

    ArrayList<SongObject> songs;
    CheckBox check;

    private MediaPlayer media;
    MediaPlayer.OnCompletionListener listener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
            timer.cancel();
            play_btn.setImageResource(R.drawable.play);
            t=true;fin=true;
            SR.setProgress(0);
        }
    };


    private AudioManager AM;
    AudioManager.OnAudioFocusChangeListener audioFocusListener = new AudioManager.OnAudioFocusChangeListener(){
        public void onAudioFocusChange(int focusChange){
            if(focusChange==AudioManager.AUDIOFOCUS_GAIN){
                if(media!=null)media.start();
            }else if(focusChange==AudioManager.AUDIOFOCUS_LOSS){
                if(media!=null)media.stop();
                releaseMediaPlayer();
            }else if(focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){
                if(media!=null)media.pause();

            }

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songs= new ArrayList<SongObject>();
        if(savedInstanceState!=null && savedInstanceState.containsKey("old")) {
            songs = savedInstanceState.getParcelableArrayList("old");
            Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show();
        }
        else {
            songs.add(new SongObject("Alaafassy", "Rahman ya Rahman", R.raw.alafassy, R.drawable.aafassy, 0));
            songs.add(new SongObject("AbouKhater", "Mubhiron fi Thikrayati", R.raw.aboukhater, R.drawable.aboukhater, 0));
            songs.add(new SongObject("AhmedZain", "ya nabi", R.raw.ahmadzen, R.drawable.ahmedzain, 0));
            songs.add(new SongObject("SamiYoucef", "whitout your smile", R.raw.samiyoucef, R.drawable.samiyoucef, 0));
            songs.add(new SongObject("kids song", "basmala", R.raw.basmala, R.drawable.bismillah, 0));
            songs.add(new SongObject("kids song", "basmala", R.raw.basmala, R.drawable.bismillah, 0));
            songs.add(new SongObject("kids song", "hourouf al hijaa", R.raw.hourouf, R.drawable.alifarnab, 0));
        }
        setContentView(R.layout.activity_main);
        context = this;
        AM = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        SR = (SeekBar) findViewById(R.id.seek);
        SR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                startCont.setText(milliSecondsToTimer(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        startCont = (TextView) findViewById(R.id.start_count);
        endCont=  (TextView)findViewById(R.id.end_count);
        artistName=  (TextView)findViewById(R.id.text_artist);
        songName=(TextView)findViewById(R.id.text_song);
        profileImage= (de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.image_profile);


        launch_btn =(ImageButton) findViewById(R.id.lanch_list);
        launch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timer!=null)timer.cancel();
                releaseMediaPlayer();

                Intent intent =new Intent(context,Main2Activity.class);
                intent.putParcelableArrayListExtra("list",songs);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent,0);
                }
            }
        });



        play_btn = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.play_btn);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(actSongId!=-1) {
                 forward_btn.setEnabled(true);
                 backward_btn.setEnabled(true);
                 if (t) {
                     if (fin) {
                         releaseMediaPlayer();
                         int check = AM.requestAudioFocus(audioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                         if (check == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                                 media = MediaPlayer.create(context, songs.get(actSongId).getAudioRessourceId());
                                 endCont.setText(milliSecondsToTimer(media.getDuration()));
                                 SR.setMax(media.getDuration());
                                 media.start();
                                 media.setOnCompletionListener(listener);

                         }

                     } else {
                         media.start();
                     }
                     timer = new Timer();
                     timer.scheduleAtFixedRate(new TimerTask() {

                         @Override
                         public void run() {

                             if(actSongId!=-1) {
                                 int position = media.getCurrentPosition();
                                 SR.setProgress(position);


                             }
                         }
                     },0,1000);
                     play_btn.setImageResource(R.drawable.pause);
                     t = false;

                 } else {
                     play_btn.setImageResource(R.drawable.play);
                     media.pause();
                     t = true;
                     fin = false;
                     timer.cancel();

                 }

             }else{
                 Toast.makeText(context,"load list of songs before",Toast.LENGTH_SHORT).show();
             }
            }

        });

        next_btn = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(actSongId!=-1) {
                    forward_btn.setEnabled(false);
                    backward_btn.setEnabled(false);
                    if(timer!=null)timer.cancel();
                    SR.setProgress(0);
                    play_btn.setImageResource(R.drawable.play);
                             if (media != null){
                                    media.reset();
                                    media.stop();
                             }
                    t = true; fin = true;
                    actSongId = findNextSong();
                    endCont.setText("00:00");
                    artistName.setText(songs.get(actSongId).getArtistName());
                    songName.setText(songs.get(actSongId).getSongName());
                    profileImage.setImageResource(songs.get(actSongId).getImageRessourceId());
                }else{
                    Toast.makeText(context,"load list of songs before",Toast.LENGTH_SHORT).show();
                }

            }
        });
        previous_btn=(de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.previous_btn);
        previous_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(actSongId!=-1){
                  forward_btn.setEnabled(false);
                  backward_btn.setEnabled(false);
                  if(timer!=null)timer.cancel();
                  play_btn.setImageResource(R.drawable.play);
                  SR.setProgress(0);
                     if (media != null){
                                    media.reset();
                                    media.stop();
                     }
                  t = true;fin = true;
                  actSongId = findPreviousSong();
                  endCont.setText("00:00");
                  artistName.setText(songs.get(actSongId).getArtistName());
                  songName.setText(songs.get(actSongId).getSongName());
                  profileImage.setImageResource(songs.get(actSongId).getImageRessourceId());
                }else{
                    Toast.makeText(context,"load list of songs before",Toast.LENGTH_SHORT).show();
                }

            }
        });
        forward_btn=(de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.forward_btn);
        forward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(actSongId!=-1 ) {

                    int ratio = media.getDuration() / 20;
                    int position = media.getCurrentPosition() + ratio;
                    if (position <= media.getDuration()) {
                        SR.setProgress(position);
                        startCont.setText(milliSecondsToTimer(position));
                        media.seekTo(position);
                    }

                }else{
                    Toast.makeText(context,"load list of songs before",Toast.LENGTH_SHORT).show();
                }

            }
        });
        backward_btn=(de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.backward_btn);
        backward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(actSongId!=-1) {

                    int ratio = media.getDuration() / 20;
                    int position = media.getCurrentPosition() - ratio;
                    if (position >= 0) {
                        SR.setProgress(position);
                        startCont.setText(milliSecondsToTimer(position));
                        media.seekTo(position);
                    }

                }else{
                    Toast.makeText(context,"load list of songs before",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    public int findNextSong(){
        boolean tt=false;
            for(int i=++actSongId;i<songs.size();i++){
                if(songs.get(i).isCoched()){
                                      actSongId=i;
                                      tt=true;
                                      break;
                                      }
            }

       if(!tt)
       {
           for(int i=0;i<actSongId;i++){
               if(songs.get(i).isCoched()){
                   actSongId=i;
                   tt=true;
                   break;
               }
           }
       }
       if(!tt)actSongId=-1;
        return actSongId;
    }
    public int findPreviousSong(){
        boolean tt=false;
        for(int i=--actSongId;i>=0;i--){
            if(songs.get(i).isCoched()){
                actSongId=i;
                tt=true;
                break;
            }
        }

        if(!tt)
        {
            for(int i= songs.size()-1;i>actSongId;i--){
                if(songs.get(i).isCoched()){
                    actSongId=i;
                    tt=true;
                    break;
                }
            }
        }
        if(!tt)actSongId=-1;
        return actSongId;
    }

    public void releaseMediaPlayer(){

        if(media !=null){
            media.release();
            media=null;
            fin=true;
            AM.abandonAudioFocus(audioFocusListener);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            actSongId=-1;
            songs =  data.getParcelableArrayListExtra("list");
            t=true;fin=true;
            actSongId=findNextSong();
            if(actSongId!=-1) {
                endCont.setText("00:00");
                artistName.setText(songs.get(actSongId).getArtistName());
                songName.setText(songs.get(actSongId).getSongName());
                profileImage.setImageResource(songs.get(actSongId).getImageRessourceId());
                SR.setProgress(0);
            }

        }
    }
    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";


        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);


        if(hours > 0){
            finalTimerString = hours + ":";
        }


        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        startCont.setText(finalTimerString);
        return finalTimerString;

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putParcelableArrayList("old", songs);
        super.onSaveInstanceState(outState, outPersistentState);

    }
   /** public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("Old", (Parcelable) songs);
        super.onSaveInstanceState(savedInstanceState);
    }**/
}
