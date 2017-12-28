package bilgi.sesliplayer;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MuzikPlayer extends Activity{
    public TextView bitisZaman,suankiZaman;
    public MediaPlayer mediaPlayer;
    private ImageButton playbutton;
    private ListView listView;
    public int sonindex=0;
    public int loadedIndex = 0;
    public Boolean playBtn = false;

    Toolbar toolbar;
    private SeekBar seekbar;
    Context context;
    MuzikList muzikList;

    public MuzikPlayer(Context context, MuzikList muzikList){
        //mediaPlayer = MediaPlayer.create(context, Uri.parse(path));
        mediaPlayer = new MediaPlayer();
        this.context = context;
        this.muzikList = muzikList;

        //mediaPlayer.setDataSource(filepath);

        toolbar = (Toolbar) ((Activity)context).findViewById(R.id.toolbar);
        bitisZaman = (TextView) ((Activity)context).findViewById(R.id.bitisSuresi);
        suankiZaman = (TextView) ((Activity)context).findViewById(R.id.suankiZaman);
        seekbar = (SeekBar) ((Activity)context).findViewById(R.id.seekBar2);
        playbutton = (ImageButton) ((Activity)context).findViewById(R.id.playButton);
        listView = (ListView) ((Activity)context).findViewById(R.id.music_list);

        playbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!playBtn) {
                    System.out.println("basla");
                    basla();
                } else {
                    System.out.println("durdur");
                    durdur();

                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mHandler.removeCallbacks(suankiTimer);
                playBtn = false;
                playbutton.setBackgroundResource(R.drawable.play);
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            public void onStopTrackingTouch(SeekBar seekBar) {
                setYuzdelikPozisyon(seekBar.getProgress());
                basla();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                MuzikPlayer.this.durdur();
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
    }

    private Handler mHandler = new Handler();
    long time = System.currentTimeMillis();
    private Runnable suankiTimer = new Runnable() {
        public void run() {
            String suankiZamanStr = getSuankiZaman();
            if(!suankiZamanStr.equals("")){
                suankiZaman.setText(suankiZamanStr);
                seekbar.setProgress(getSuankiYuzdelikZaman());

                long newTime = System.currentTimeMillis();
                if(newTime >= time+1000){
                    time = newTime;
                    MainActivity.mainActivity.contentView.setTextViewText(R.id.nTime, suankiZamanStr+" - "+getBitisZaman());
                    MainActivity.mainActivity.mNotificationManager.notify(1, MainActivity.mainActivity.notification);
                }
            }

            mHandler.postDelayed(suankiTimer, 100);
        }
    };

    public void basla()
    {
        Log.d("musicstate", "music basla");
        if(!mediaPlayer.isPlaying()){
            //toolbar.setTitle();
            playbutton.setBackgroundResource(R.drawable.stop);
            playBtn = true;
            mediaPlayer.start();
            mHandler.removeCallbacks(suankiTimer);
            mHandler.postDelayed(suankiTimer, 100);
            bitisZaman.setText(getBitisZaman());
            MainActivity.mainActivity.muzikBaslat();
        }
    }
    public void durdur()
    {
        Log.d("musicstate", "music durdur");
        if(mediaPlayer.isPlaying()){
            playbutton.setBackgroundResource(R.drawable.play);
            playBtn = false;
            mHandler.removeCallbacks(suankiTimer);
            mediaPlayer.pause();
            MainActivity.mainActivity.muzikDurdur();
        }

    }

    static public Muzik lastmuzik;
    public void yukle(Muzik muzik){
        Log.d("musicstate", "music yukle");
        lastmuzik = muzik;

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(context, Uri.parse(muzik.dosyaYolu));
            mediaPlayer.prepare();

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

        toolbar.setTitle(muzik.gorunurBaslik);
        MainActivity.mainActivity.contentView.setTextViewText(R.id.nTitle, muzik.gorunurBaslik);

        muzikList.scrollTo(muzik);
    }

    public String getSuankiZaman(){
        StringBuilder strBuilder = new StringBuilder();
        if(mediaPlayer.isPlaying()){
            long sure = mediaPlayer.getCurrentPosition();
            long second = (sure / 1000) % 60;
            long minute = (sure / (1000 * 60)) % 60;
            long hour = sure / (1000 * 60 * 60);

            if(hour > 0){
                strBuilder.append(hour+":");
                if(minute < 10)
                    strBuilder.append("0");
                strBuilder.append(minute+":");
                if(second < 10)
                    strBuilder.append("0");
                strBuilder.append(second);
            }
            else if(minute > 0){
                strBuilder.append(minute+":");
                if(second < 10)
                    strBuilder.append("0");
                strBuilder.append(second);
            }
            else{
                strBuilder.append(second+"s");
            }
        }
        return strBuilder.toString();
    }


    public String getBitisZaman(){
        return lastmuzik.getGorunurSure();
    }


    public int getSuankiYuzdelikZaman(){
        int intSure = 0;
        if(mediaPlayer.isPlaying()){
            intSure = (100*mediaPlayer.getCurrentPosition())/mediaPlayer.getDuration();
        }

        return intSure;
    }

    public MediaPlayer getPlayer(){
        return mediaPlayer;
    }

    public void setYuzdelikPozisyon(int pos) {
        mediaPlayer.seekTo((mediaPlayer.getDuration() * pos) / 100);
    }

}
