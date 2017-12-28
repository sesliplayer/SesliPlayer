package bilgi.sesliplayer;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Arama {
	List<String> aramalar = new ArrayList<String>();
	List<Integer> puanlar = new ArrayList<Integer>();

	public Muzik ara() {
        for(int i_aramalar=0; i_aramalar<aramalar.size(); i_aramalar++){
            for(int i_isimler=0; i_isimler<Muzik.muzikler.size();i_isimler++){
                puanlar.add(0);
                puanlar.set(i_isimler, puanlar.get(i_isimler)-kelimeBenzerligi(aramalar.get(i_aramalar), Muzik.muzikler.get(i_isimler).gorunurBaslik));
                List<String> aramalarKelimeler = kelimeAyir(aramalar.get(i_aramalar));
                for(int i_aramaKelimeler=0; i_aramaKelimeler<aramalarKelimeler.size(); i_aramaKelimeler++){
                    List<String> isimlerKelimeler = kelimeAyir(Muzik.muzikler.get(i_isimler).gorunurBaslik);
                    for(int i_isimKelimeler=0; i_isimKelimeler<isimlerKelimeler.size(); i_isimKelimeler++){
                        if(aramalarKelimeler.get(i_aramaKelimeler).equalsIgnoreCase(isimlerKelimeler.get(i_isimKelimeler))){
                            puanlar.set(i_isimler, puanlar.get(i_isimler)+(2*((aramalar.size()-i_aramalar)+1000)));
                            if(i_aramaKelimeler>0 && i_isimKelimeler>0){
                                if(aramalarKelimeler.get(i_aramaKelimeler-1).equalsIgnoreCase(isimlerKelimeler.get(i_isimKelimeler-1))){
                                    puanlar.set(i_isimler, puanlar.get(i_isimler)+(1*((aramalar.size()-i_aramalar)+1000)));
                                }
                            }
                        }
                        //System.out.println(aramalarKelimeler.get(i_aramaKelimeler)+" x "+isimlerKelimeler.get(i_isimKelimeler) + "\t" + puanlar.get(i_isimler));
                    }
                }
            }
        }

		int en_yuksek_puan = Collections.max(puanlar);
		if (en_yuksek_puan >= 1000) {
			return Muzik.muzikler.get(puanlar.indexOf(en_yuksek_puan));
		}

		return null;
	}

	public void aramaEkle(String arama) {
		aramalar.add(arama);
	}

	public void isimlerTemizle() {
		puanlar.removeAll(puanlar);
	}

	public void aramalarTemizle() {
		aramalar.removeAll(aramalar);
		puanlar.removeAll(puanlar);
		System.out.println(aramalar.size());
	}

	public void temizle() {
		isimlerTemizle();
		aramalarTemizle();
	}

	List<String> kelimeAyir(String str) {
		Matcher matcher = Pattern.compile("(\\w+)", Pattern.DOTALL).matcher(harfKucult(str));
		List<String> kelimeler = new ArrayList();
		while (matcher.find()) {
			kelimeler.add(matcher.group(0));
		}
		return kelimeler;
	}

	public String harfKucult(String str) {
		char[] turkceHarfler = new char[]{'Ç', 'Ğ', 'İ', 'Ö', 'Ş', 'Ü', 'ç', 'ğ', 'i', 'ö', 'ş', 'ü', 'ı'};
		char[] kucukHarfler = new char[]{'c', 'g', 'i', 'o', 's', 'u', 'c', 'g', 'i', 'o', 's', 'u', 'i'};
		for (int i = 0; i < turkceHarfler.length; i++) {
			str = str.replace(turkceHarfler[i], kucukHarfler[i]);
		}
		return str.toLowerCase(Locale.ENGLISH);
	}

	public int kelimeBenzerligi(String a, String b) {
		int j;
		a = harfKucult(a);
		b = harfKucult(b);
		int[] costs = new int[(b.length() + 1)];
		for (j = 0; j < costs.length; j++) {
			costs[j] = j;
		}
		for (int i = 1; i <= a.length(); i++) {
			costs[0] = i;
			int nw = i - 1;
			for (j = 1; j <= b.length(); j++) {
				int min = Math.min(costs[j], costs[j - 1]) + 1;
				if (a.charAt(i - 1) != b.charAt(j - 1)) {
					nw++;
				}
				int cj = Math.min(min, nw);
				nw = costs[j];
				costs[j] = cj;
			}
		}
		return costs[b.length()];
	}
}
