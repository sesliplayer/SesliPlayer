package  bilgi.sesliplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Erkan on 3.11.2017.
 */

public class MusicNotificationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("nofi", "Received Cancelled Event");

        switch(intent.getAction()){
            case "sesliplayer.launch":
                try{
                    Intent appintent = new Intent(MainActivity.mainActivity, MainActivity.class);
                    appintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    MainActivity.mainActivity.startActivity(appintent);
                }
                catch (Exception e){
                    Log.e("notifi", e.toString());
                }
                break;
            case "sesliplayer.play":
                if(MainActivity.mainActivity.muzikPlayer.playBtn==true){
                    MainActivity.mainActivity.muzikPlayer.durdur();
                }
                else {
                    MainActivity.mainActivity.muzikPlayer.basla();
                }
                break;
            case "sesliplayer.next":
                MainActivity.mainActivity.muzikIleri();
                break;
            case "sesliplayer.previous":
                MainActivity.mainActivity.muzikGeri();
                break;
            case "sesliplayer.mic":
                MainActivity.mainActivity.sescevirici.cevir();
                break;
        }
    }
}
