package bilgi.sesliplayer;

import android.app.ProgressDialog;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MuzikEkleActivity extends AppCompatActivity implements OnCheckedChangeListener {
    List<String> dirs;
    String dosyaismi = "";
    String dosyayolu = "";
    List<List<String>> files;
    String gorunurbaslik = "";
    ListView lv;
    String mid = "";
    ImageButton muzikEkleBtn;
    MZEklePlanetAdapter plAdapter;
    ArrayList<MZEklePlanet> planetList;
    String rep = "";
    String sarkici = "";
    String sarkisuresi = "";
    ArrayList<Integer> secililer = new ArrayList();
    private veritabani v1;
    Toast waitMessage;
    Toast savedMessage;
    Handler mainHandler;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //muzikEkleContext = this;

        mainHandler = new Handler(MainActivity.mainActivity.getMainLooper());

        setContentView((int) R.layout.activity_muzik_ekle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        muzikEkleBtn = (ImageButton) findViewById(R.id.save);
        lv = (ListView) findViewById(R.id.list);
        setSupportActionBar(toolbar);

        final ProgressDialog filesearchdialog=new ProgressDialog(MuzikEkleActivity.this);
        filesearchdialog.setMessage("Müzikler Aranıyor...");
        filesearchdialog.setCancelable(false);
        filesearchdialog.setInverseBackgroundForced(false);
        filesearchdialog.show();



        new Thread ( new Runnable() {
            @Override
            public void run() {
                displayPlanetList();
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        filesearchdialog.hide();
                        filesearchdialog.cancel();
                    } // This is your code
                };
                mainHandler.post(myRunnable);
            }
        }).start();


        v1 = new veritabani(this);

        muzikEkleBtn.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            if (secililer.size() == 0) {
                Toast.makeText(MuzikEkleActivity.this, "Hiç Şarkı Seçilmedi", Toast.LENGTH_LONG).show();
                return;
            }

            waitMessage = Toast.makeText(MuzikEkleActivity.this, "Yükleniyor Lütfen Bekleyin",Toast.LENGTH_LONG);
            savedMessage = Toast.makeText(MuzikEkleActivity.this, "Kaydedildi", Toast.LENGTH_LONG);
            //waitMessage.show();

            final ProgressDialog dialog=new ProgressDialog(MuzikEkleActivity.this);
            dialog.setMessage("Yükleniyor\nLütfen Bekleyin...");
            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);

            try {
                dialog.show();
            }
            catch (Exception e){
                Log.i("dialog", "dialog zorla kapatıldı");
            }


            new Thread ( new Runnable() {
                @Override
                public void run() {
                    muzikEkleKaydet();

                    savedMessage.show();
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            dialog.hide();
                            dialog.cancel();
                            MainActivity.mainActivity.muzikPlayer.durdur();
                            MainActivity.mainActivity.initSesliPlayer();
                            finish();
                        } // This is your code
                    };
                    mainHandler.post(myRunnable);
                }
            }).start();
        }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String mesaj = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);
    }

    private void muzikEkleKaydet() {
        v1.getWritableDatabase().execSQL("Delete From muzikler");
        for (int i = 0; i < files.size(); i++) {
            if (secililer.indexOf(Integer.valueOf(i)) != -1) {
                for (int j = 0; j < ((List) files.get(i)).size(); j++) {
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    String path = ((String) dirs.get(i)) + "/" + ((String) ((List) files.get(i)).get(j));
                    try {
                        mmr.setDataSource(path);
                        int sarkisuresi = Integer.parseInt(mmr.extractMetadata(9));

                        System.out.println("muziksuresi : " + sarkisuresi/1000);
                        String artist = mmr.extractMetadata(2);
                        if (artist == null) {
                            artist = "";
                        }
                        sarkici = artist;
                        System.out.println("Muzik artist : " + artist);
                        dosyayolu = path;
                        dosyaismi = (String) ((List) files.get(i)).get(j);
                        System.out.println("dosya yolu : " + dosyayolu);
                        gorunurbaslik = dosyaismi.replace(".mp3", "");
                        System.out.println("gorunurbaslık :" + gorunurbaslik);
                        v1.muzikEkle(dosyayolu,dosyaismi,gorunurbaslik,sarkici,sarkisuresi);
                    } catch (Exception e) {
                        System.out.println(e.getStackTrace());
                    }
                }
            }
        }
    }

    private void displayPlanetList() {
        planetList = new ArrayList();

        MuzikTara muzikTara = new MuzikTara();
        dirs = muzikTara.getDirs();
        files = muzikTara.getFiles();
        for (int i = 0; i < dirs.size(); i++) {
            planetList.add(new MZEklePlanet((String) dirs.get(i), ((List) files.get(i)).size() + " Müzik Bulundu"));
        }

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                lv.setAdapter(plAdapter);
            }
        };
        mainHandler.post(myRunnable);
        plAdapter = new MZEklePlanetAdapter(planetList, this);
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int pos = lv.getPositionForView(buttonView);
        if (pos != -1) {
            ((MZEklePlanet) planetList.get(pos)).setSelected(isChecked);
            if (isChecked) {
                secililer.add(Integer.valueOf(pos));
            } else if (secililer.indexOf(Integer.valueOf(pos)) > -1) {
                secililer.remove(secililer.indexOf(Integer.valueOf(pos)));
            }
        }
    }

    /*public void veritabanitgoster() { //mainde de var
        SQLiteDatabase db = v1.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From muzikler ", null);
        int rowCount = cursor.getCount();
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                System.out.println("mid :" + cursor.getString(0) + " dosyayolu :" + cursor.getString(1) + " dosyaismi :" + cursor.getString(2) + " gorunurbaslik :" + cursor.getString(3) + " sarkici :" + cursor.getString(4) + " sarkisuresi :" + cursor.getString(5));
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
    }*/
}
