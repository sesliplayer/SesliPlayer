package bilgi.sesliplayer;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Bilgi on 12/12/2017.
 */

public class MuzikList {

    ListView listView;

    MuzikListAdapter adapter;
    public ArrayList<Muzik> muzikler;

    public void muzikListele(ArrayList<Muzik> _muzikler){
        this.muzikler = _muzikler;

        adapter = new MuzikListAdapter(MainActivity.mainActivity, muzikler);

        listView= (ListView)(MainActivity.mainActivity).findViewById(R.id.music_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.mainActivity.muzikPlayer.yukle(muzikler.get(position));
                MainActivity.mainActivity.muzikPlayer.basla();
            }
        });
    }

    public void scrollTo(Muzik muzik){
        adapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(muzikler.indexOf(muzik));
    }

    public void shuffle(){
        Collections.shuffle(muzikler);
        adapter = new MuzikListAdapter(MainActivity.mainActivity, muzikler);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(muzikler.indexOf(MuzikPlayer.lastmuzik));
    }
}