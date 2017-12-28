package bilgi.sesliplayer;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private veritabani v1;
    //ListView listview;
    private ImageButton ses;
    //List<String> muzikIsimleri;
    //List<Integer> id;
    //static public List<String> dosyayolu;
    MuzikPlayer muzikPlayer;
    Context context;
    Toolbar toolbar;
    public static SesCevirici sescevirici;
    int listPozisyon = 0;
    ImageButton ileriBtn;
    ImageButton GeriBtn;
    IntentFilter kulaklikFilter;
    IntentFilter aramaFilter;
    public static KDugmesi kDugmesi;
    public static CallReceiver callReceiver;
    static MainActivity mainActivity;
    NotificationCompat.Builder mBuilder;
    RemoteViews contentView;
    NotificationManager mNotificationManager;
    Notification notification;
    MuzikList muzikList;


    public final static String EXTRA_MESSAGE = "bilgi.myapplication.MESAJ";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        initSesliPlayer();
    }

    public void initSesliPlayer(){

        setContentView(R.layout.activity_main);
        getPermissions();

        muzikList = new MuzikList();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ses = (ImageButton) findViewById(R.id.ses);
        ileriBtn = (ImageButton) findViewById(R.id.ileriBtn);
        GeriBtn = (ImageButton) findViewById(R.id.geriBtn);
        setSupportActionBar(toolbar);
        context = this;
        muzikPlayer = new MuzikPlayer(context, muzikList);
        sescevirici = new SesCevirici(context, muzikPlayer);
        v1 = new veritabani(this);
        veritabanitgoster();

        ((AudioManager)getSystemService(AUDIO_SERVICE)).registerMediaButtonEventReceiver(new ComponentName(this, KDugmesi.class));//projedeki tek deprecated :(
        kDugmesi = new KDugmesi();
        kulaklikFilter = new IntentFilter(AudioManager.ACTION_HEADSET_PLUG); //ACTION_MEDIA_BUTTON ACTION_HEADSET_PLUG
        kulaklikFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        kulaklikFilter.addAction(Intent.ACTION_MEDIA_BUTTON);

        kulaklikFilter.setPriority(999);
        kDugmesi.init(this, sescevirici);
        this.registerReceiver(kDugmesi, kulaklikFilter);

        makeNotification();
        startService(new Intent(getBaseContext(), ClosingService.class));

        callReceiver = new CallReceiver();
        aramaFilter = new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        this.registerReceiver(callReceiver, aramaFilter);


        ses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sescevirici.cevir();
            }
        });
        GeriBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muzikGeri();
            }
        });
        ileriBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muzikIleri();
            }
        });

        muzikPlayer.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                muzikIleri();
            }
        });

        if(Muzik.muzikler.size()>0){
            muzikPlayer.yukle(Muzik.muzikler.get(0));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    public void muzikIleri()
    {
        if(MuzikPlayer.lastmuzik == null)
            return;

        muzikPlayer.durdur();

        int lastIndex = muzikList.muzikler.indexOf(MuzikPlayer.lastmuzik);

        if (lastIndex < muzikList.muzikler.size() - 1)
        {
            lastIndex++;
            muzikPlayer.yukle(muzikList.muzikler.get(lastIndex));
            muzikPlayer.basla();
        }
        else{
            lastIndex = 0;
            muzikPlayer.yukle(muzikList.muzikler.get(lastIndex));
            muzikPlayer.basla();
        }
    }

    public void muzikGeri()
    {
        if(MuzikPlayer.lastmuzik == null)
            return;

        muzikPlayer.durdur();

        int lastIndex = muzikList.muzikler.indexOf(MuzikPlayer.lastmuzik);

        if (lastIndex > 0)
        {
            lastIndex--;
            muzikPlayer.yukle(muzikList.muzikler.get(lastIndex));
            muzikPlayer.basla();
        }
        else{
            lastIndex = muzikList.muzikler.size() - 1;
            muzikPlayer.yukle(muzikList.muzikler.get(lastIndex));
            muzikPlayer.basla();
        }
    }

    public void muzikDurdur(){
        contentView.setImageViewResource(R.id.nPlay, R.drawable.playnotifi);
        mNotificationManager.notify(1, notification);
    }

    public void muzikBaslat(){
        contentView.setImageViewResource(R.id.nPlay, R.drawable.stopnotifi);
        mNotificationManager.notify(1, notification);
    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(kDugmesi);
        unregisterReceiver(callReceiver);
        super.onDestroy();
        muzikPlayer.durdur();
        muzikPlayer.getPlayer().release();
        mNotificationManager.cancelAll();
    }

    long time = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            long newTime = System.currentTimeMillis();
            if(time+700 >= newTime){
                finish();
            }
            else {
                time = newTime;
                Toast.makeText(MainActivity.this, "Uygulamayı kapatmak için geri butonuna arka arkaya basın.", Toast.LENGTH_SHORT).show();
                return false; //I have tried here true also
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_muzik_ekle) {
            Intent intent = new Intent(this, MuzikEkleActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_kategori) {
            Intent intent = new Intent(this, CategoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_shuffle) {
            muzikList.shuffle();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //veritabanındakı tabloyu tamamen gosterir
    public void veritabanitgoster()
    {
        Muzik.clear();
        String countQuery = "Select * From muzikler ";
        SQLiteDatabase db = v1.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                Muzik muzik = new Muzik(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5));
                Muzik.muzikler.add(muzik);

               // Log.i("db select", "id:" + cursor.getString(0)+"dosyayolu :" + cursor.getString(1) + "dosyaismi :" + cursor.getString(3));

            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();

        //ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Muzik.getMuzikIsimleri());
        //listview.setAdapter(adapter);

        muzikList.muzikListele(Muzik.muzikler);
    }

    public void getPermissions(){
        //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        if (    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.PROCESS_OUTGOING_CALLS}, 1);

        }
        //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 1);
        //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[3] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[4] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[5] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Çalışabilmem için bana izin vermelisin", Toast.LENGTH_SHORT).show();
                    //android.os.Process.killProcess(android.os.Process.myPid());
                    //System.exit(1);
                    finish();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void makeNotification(){

        Intent launchButton = new Intent("sesliplayer.launch");
        launchButton.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //launchButton.setFlags(0);
        PendingIntent pendingLaunchButton = PendingIntent.getBroadcast(this, 0, launchButton, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playButton = new Intent("sesliplayer.play");
        //playButton.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        playButton.setFlags(0);
        PendingIntent pendingPlayButton = PendingIntent.getBroadcast(this, 0, playButton, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextButton = new Intent("sesliplayer.next");
        //nextButton.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        nextButton.setFlags(0);
        PendingIntent pendingNextButton = PendingIntent.getBroadcast(this, 0, nextButton, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent previousButton = new Intent("sesliplayer.previous");
        //previousButton.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        previousButton.setFlags(0);
        PendingIntent pendingPreviousButton = PendingIntent.getBroadcast(this, 0, previousButton, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent micButton = new Intent("sesliplayer.mic");
        //micButton.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        micButton.setFlags(0);
        PendingIntent pendingMicButton = PendingIntent.getBroadcast(this, 0, micButton, PendingIntent.FLAG_UPDATE_CURRENT);


        contentView = new RemoteViews(getPackageName(), R.layout.custom_push);
        contentView.setImageViewResource(R.id.nIcon, R.mipmap.ic_launcher);
        contentView.setImageViewResource(R.id.nPlay, R.drawable.playnotifi);
        contentView.setImageViewResource(R.id.nNext, R.drawable.twonext);
        contentView.setImageViewResource(R.id.nPrevious, R.drawable.twoback);
        contentView.setImageViewResource(R.id.nMic, R.drawable.mic);
        contentView.setTextViewText(R.id.nTitle, "Sesli Player");


        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContent(contentView)
                .setAutoCancel(true);

        mBuilder.setCustomBigContentView(contentView);

        notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);

        contentView.setOnClickPendingIntent(R.id.nIcon, pendingLaunchButton);
        contentView.setOnClickPendingIntent(R.id.nPlay, pendingPlayButton);
        contentView.setOnClickPendingIntent(R.id.nNext, pendingNextButton);
        contentView.setOnClickPendingIntent(R.id.nPrevious, pendingPreviousButton);
        contentView.setOnClickPendingIntent(R.id.nMic, pendingMicButton);
        mNotificationManager.notify(1, notification);
    }

}
