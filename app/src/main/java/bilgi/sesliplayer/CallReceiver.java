package bilgi.sesliplayer;

import android.content.Context;
import android.util.Log;

import java.util.Date;

/**
 * Created by Bilgi on 10.11.2017.
 */

public class CallReceiver extends PhonecallReceiver {

    boolean isPlaying = false;
    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        Log.d("arama"," arama basladi");
        try{
            isPlaying = MainActivity.mainActivity.muzikPlayer.playBtn;
            if(isPlaying){
                MainActivity.mainActivity.muzikPlayer.durdur();
            }

        }
        catch (Exception e){
            Log.e("arama", e.toString());
        }
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        Log.d("arama"," arama basladi");
        try{
            isPlaying = MainActivity.mainActivity.muzikPlayer.playBtn;
            if(isPlaying){
                MainActivity.mainActivity.muzikPlayer.durdur();
            }
        }
            catch (Exception e){
            Log.e("arama", e.toString());
        }
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        Log.d("arama"," arama bitti");
        try {
            if(isPlaying){
                MainActivity.mainActivity.muzikPlayer.basla();
            }
        }
            catch (Exception e){
            Log.e("arama", e.toString());
        }
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        Log.d("arama"," arama bitti");
        try{
            if(isPlaying){
                MainActivity.mainActivity.muzikPlayer.basla();
            }
        }
            catch (Exception e){
            Log.e("arama", e.toString());
        }
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        Log.d("arama"," arama kacti");
        try{
            if(isPlaying){
                MainActivity.mainActivity.muzikPlayer.durdur();
                MainActivity.mainActivity.muzikPlayer.basla();
            }
        }
            catch (Exception e){
            Log.e("arama", e.toString());
        }
    }

}