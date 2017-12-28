package bilgi.sesliplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Erkan on 15.05.2017.
 */


class MZEklePlanet {
    String name;
    String distance;
    boolean selected=false;

    public MZEklePlanet(String name, String distance) {
        super();
        this.name = name;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
public class MZEklePlanetAdapter extends ArrayAdapter<MZEklePlanet>
{
    private List<MZEklePlanet> planetList;
    private Context context;


    public MZEklePlanetAdapter(List<MZEklePlanet> planetList, Context context) {
        super(context, R.layout.single_listview_item,planetList);
        this.planetList=planetList;
        this.context=context;
        
    }

    private static class PlanetHolder{
        public TextView planetName;
        public TextView distView;
        public CheckBox chkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v=convertView;
        PlanetHolder holder = new PlanetHolder();

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v=inflater.inflate(R.layout.single_listview_item, null);
        holder.planetName=(TextView)v.findViewById(R.id.name);
        holder.distView=(TextView)v.findViewById(R.id.dlist);
        holder.chkBox=(CheckBox)v.findViewById(R.id.chk_box);

        holder.chkBox.setOnCheckedChangeListener((MuzikEkleActivity)context);


        MZEklePlanet p=planetList.get(position);
        holder.planetName.setText(p.getName());
        holder.distView.setText(""+p.getDistance());
        holder.chkBox.setChecked(p.isSelected());
        holder.chkBox.setTag(p);

        return v;
    }
}
