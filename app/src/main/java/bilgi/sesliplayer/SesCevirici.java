package bilgi.sesliplayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SesCevirici extends Activity {
    Context context;
    ProgressDialog loadingDialog;
    MuzikPlayer mzPlayer;
    SpeechRecognizer speechRecognizer;
    ArrayList<String> speechResultSentence = new ArrayList<String>();


    public SesCevirici(Context mContext, MuzikPlayer mzPlayer) {
        context = mContext;
        this.mzPlayer = mzPlayer;
    }

    public void cevir() {
        mzPlayer.durdur();
        if (isInternetAvailable()) {
            System.out.println("Baglandim");
            loadingDialog = new ProgressDialog(context);
            loadingDialog.setCancelable(false);
            loadingDialog.setMessage("Lütfen Konuşun");
            loadingDialog.show();
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            speechRecognizer.setRecognitionListener(new RecognitionListener()
            {

                public void onRmsChanged(float rmsdB) {
                }

                public void onResults(Bundle results) {
                    System.out.println("sonuclar tamam");
                    loadingDialog.dismiss();
                    speechResultSentence = results.getStringArrayList("results_recognition");
                    String ciktiTest = "";
                    Iterator it = speechResultSentence.iterator();
                    while (it.hasNext()) {
                        String sonuc = (String) it.next();
                        System.out.println(sonuc);
                        ciktiTest = ciktiTest + sonuc + "\n";
                    }
                    Arama arama = new Arama();

                    it = speechResultSentence.iterator();
                    while (it.hasNext()) {
                        arama.aramaEkle((String) it.next());
                    }
                    Muzik muzik = arama.ara();
                    if (muzik == null) {
                        Toast.makeText(context, "Eşleşme Bulunamadı :(", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Log.i("sescevirici",muzik.dosyaYolu);
                        mzPlayer.yukle(muzik);
                        mzPlayer.basla();
                    }
                }

                public void onReadyForSpeech(Bundle params) {
                    System.out.println("Konuşmaya Hazırsın");
                }

                public void onPartialResults(Bundle partialResults) {
                }

                public void onEvent(int eventType, Bundle params) {
                }

                public void onError(int error) {
                    System.out.println("hata");
                    loadingDialog.dismiss();
                    Toast.makeText(context.getApplicationContext(), "Bir Hata Oluştu Lütfen Tekrar Deneyin", Toast.LENGTH_LONG).show();
                }

                public void onEndOfSpeech() {
                    System.out.println("kayit bitti");
                    loadingDialog.setMessage("Kayıt Bitti.Sonuçlar Getiriliyor");
                    loadingDialog.cancel();
                }

                public void onBufferReceived(byte[] buffer) {
                }

                public void onBeginningOfSpeech() {
                    System.out.println("kayit basladi");
                    loadingDialog.setMessage("Kayıt Başladı");
                }
            });
            Intent recognizerIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
            recognizerIntent.putExtra("calling_package", getClass().getPackage().getName());
            recognizerIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
            recognizerIntent.putExtra("android.speech.extra.MAX_RESULTS", 5);
            speechRecognizer.startListening(recognizerIntent);
            return;
        }
        Toast.makeText(context.getApplicationContext(), "Plese Connect to Internet", Toast.LENGTH_LONG).show();
    }

    protected void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
            speechRecognizer.cancel();
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net != null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


}
