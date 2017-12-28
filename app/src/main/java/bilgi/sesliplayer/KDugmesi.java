package bilgi.sesliplayer ;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;

public class KDugmesi extends BroadcastReceiver {

	Context context;
	SesCevirici sescevirici;
	void init(Context context, SesCevirici sescevirici) { //ihtiyaç kalmadı sescevirici artık static
	    this.context = context;
		this.sescevirici = sescevirici;
	}

	private KeyEvent event;

	@Override
	public void onReceive(Context context, Intent intent) {
        Log.d("kulaklik",""+intent.getAction());
        Log.d("kulaklik","1 "+AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        Log.d("kulaklik","2 "+Intent.ACTION_HEADSET_PLUG);
        Log.d("kulaklik","3 "+AudioManager.ACTION_HEADSET_PLUG);
		String intentAction = intent.getAction();

		if(intent.getAction().equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY) || intent.getAction().equals(Intent.ACTION_HEADSET_PLUG) || intent.getAction().equals(AudioManager.ACTION_HEADSET_PLUG)) {
			Log.d("kulaklik", "Headset jack");
			int state = intent.getIntExtra("state", -1);
			switch(state) {
				case(0):
					Log.d("kulaklik", "Headset unplugged");
					MainActivity.mainActivity.muzikPlayer.durdur();
					break;
				case(1):
					Log.d("kulaklik", "Headset plugged");
					break;
				default:
					Log.d("kulaklik", "Error");
			}
		}

		if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
			return;
		}
		event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
		if (event == null) {
			return;
		}
		try {
			int action = event.getAction();

			switch(action) {

				case KeyEvent.ACTION_UP :
					Log.d("TEST", "BUTTON UP");
					MainActivity.sescevirici.cevir();
					break;
				case KeyEvent.ACTION_DOWN :
				case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE :
					Log.d("TEST", "BUTTON DOWN");
					break;
			}
		} catch (Exception e) {
			Log.d("TEST", "THIS IS NOT GOOD");
			Log.e("TEST", e.toString());
		}


		///////////////////////////////////////////////////////////////
		isOrderedBroadcast();
	}
}
