package bilgi.sesliplayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class veritabani extends SQLiteOpenHelper{
    private static final String VERİTABANİ_ADİ="muzikPlayer";
    private static final int SURUM=1;
    //SQLiteDatabase db;

    public veritabani(Context c)
    {
        super(c,VERİTABANİ_ADİ,null,SURUM);

        // TODO Auto-generated constructor stub
    }

    public void muzikEkle(String dosyayolu, String dosyaismi, String gorunurbaslik, String sarkici, int sarkisuresi){

        dosyayolu = dosyayolu.replace("'","''");
        dosyaismi = dosyaismi.replace("'","''");
        gorunurbaslik = gorunurbaslik.replace("'","''");
        sarkici = sarkici.replace("'","''");

        /*dosyayolu = android.database.DatabaseUtils.sqlEscapeString(dosyayolu);
        dosyaismi = android.database.DatabaseUtils.sqlEscapeString(dosyaismi);
        gorunurbaslik = android.database.DatabaseUtils.sqlEscapeString(gorunurbaslik);
        sarkici = android.database.DatabaseUtils.sqlEscapeString(sarkici);*/

        String quaerStr = "INSERT INTO muzikler(dosyayolu,dosyaismi,gorunurbaslik,sarkici,sarkisure) values('"+dosyayolu+"', '"+dosyaismi+"', '"+gorunurbaslik+"', '"+sarkici+"', '"+sarkisuresi+"');";

        this.getWritableDatabase().execSQL(quaerStr);
        //localdb.rawQuery("select 1",null);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //this.db = db;
        db.execSQL("Create Table muzikler(mid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,dosyayolu TEXT,dosyaismi TEXT,gorunurbaslik TEXT,sarkici TEXT,sarkisure INTEGER)");
        db.execSQL("Create Table kelimeler(kid TEXT,mid TEXT,kelimeler TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXIST muzikPlayer");
        db.execSQL("DROP TABLE IF EXIST foveriler");

        onCreate(db);
        // TODO Auto-generated method stub

    }
}
