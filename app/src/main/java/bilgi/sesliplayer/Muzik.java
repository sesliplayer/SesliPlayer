package bilgi.sesliplayer;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Erkan on 29.11.2017.
 */

public class Muzik {
    public String dosyaYolu;
    public String dosyaIsmi;
    public String gorunurBaslik;
    public String sarkici;
    public int sarkisure;
    public int mid;

    static ArrayList<Muzik> muzikler=new ArrayList<Muzik>();

    static Map<String, List<Muzik>> categorySarkici = new HashMap<>();
    static Map<String, List<Muzik>> categoryDosyayolu = new HashMap<>();


    public Muzik( int mid ,String dosyaYolu, String dosyaIsmi, String gorunurBaslik, String sarkici, int sarkisure){
        this.dosyaYolu=dosyaYolu;
        this.dosyaIsmi=dosyaIsmi;
        this.gorunurBaslik=gorunurBaslik;
        this.sarkici=sarkici;
        this.sarkisure=sarkisure;
        this.mid=mid;

        if (sarkici.equalsIgnoreCase("")){
            this.sarkici="Bilinmeyen Kaynak";
        }
        List<Muzik> caregorySarkiciList = Muzik.categorySarkici.get(this.sarkici);
        if(caregorySarkiciList != null){
            caregorySarkiciList.add(this);
        }
        else {
            List<Muzik> newCaregorySarkiciList = new ArrayList<Muzik>();
            Muzik.categorySarkici.put(this.sarkici, newCaregorySarkiciList);
            newCaregorySarkiciList.add(this);
        }


        String regex = ".*\\/"; //dosya yollarini guruplandirmak icin dosya ismi ve uzantisi kaldiriliyor

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.dosyaYolu);

        while (matcher.find()) {
            List<Muzik> caregoryDosyayoluList = Muzik.categoryDosyayolu.get(matcher.group(0));
            if(caregoryDosyayoluList != null){
                caregoryDosyayoluList.add(this);
            }
            else {
                List<Muzik> newCaregoryDosyayoluList = new ArrayList<Muzik>();
                Muzik.categoryDosyayolu.put(matcher.group(0), newCaregoryDosyayoluList);
                newCaregoryDosyayoluList.add(this);
            }
        }
    }

    public static void clear(){
        muzikler.clear();
        categorySarkici.clear();
        categoryDosyayolu.clear();
    }

    public static List<String> getMuzikIsimleri(){
        ArrayList muzikIsimleri = new ArrayList();
        for(Muzik muzik : Muzik.muzikler){
            muzikIsimleri.add(muzik.gorunurBaslik);
        }

        return muzikIsimleri;
    }

    public String getGorunurSure(){
        long sure = this.sarkisure;
        long second = (sure / 1000) % 60;
        long minute = (sure / (1000 * 60)) % 60;
        long hour = sure / (1000 * 60 * 60);

        StringBuilder strBuilder = new StringBuilder();
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

        return strBuilder.toString();
    }

    public static void muziklerShuffle(){
        Collections.shuffle(muzikler);
    }

    @Override
    public int hashCode() {
        return mid;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Muzik other = (Muzik) obj;
        if (mid != other.mid)
            return false;
        return true;
    }
}
