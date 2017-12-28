package bilgi.sesliplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MuzikListAdapter extends ArrayAdapter<Muzik> {
    private Context mContext;
    ArrayList<Muzik> muzikler;

    public MuzikListAdapter(Context c,ArrayList<Muzik> muzikler) {
        super(c, R.layout.single_music_list, muzikler);
        mContext = c;
        this.muzikler = muzikler;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.single_music_list, null);

        }

        Muzik muzik = getItem(position);

        if (muzik != null) {
            TextView music_name = (TextView) v.findViewById(R.id.music_name);
            TextView music_duration = (TextView) v.findViewById(R.id.music_duration);
            TextView music_singer = (TextView) v.findViewById(R.id.music_singer);

            music_name.setText(muzikler.get(position).gorunurBaslik);
            music_duration.setText(muzikler.get(position).getGorunurSure());
            music_singer.setText(muzikler.get(position).sarkici);

        }

        if(MuzikPlayer.lastmuzik != null){
            if(muzik.equals(MuzikPlayer.lastmuzik)){
                v.setBackgroundResource(R.drawable.muziklistbg);
            }
            else {
                v.setBackgroundResource(0);
            }
        }

        return v;
    }
}