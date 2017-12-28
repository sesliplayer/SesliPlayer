package bilgi.sesliplayer;
/**
 * Created by Bilgi on 11/28/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CategoryGrid {

    GridView grid;
    Context context;

    public CategoryGrid(Context c){
        context=c;

    }
    public void categoryDirs(){
        ArrayList categoryDataList = new ArrayList<CategoryData>();
        for(int i=0; i<Muzik.categoryDosyayolu.size(); i++){
            CategoryData categoryData = new CategoryData();
            categoryData.categoryName =  Muzik.categoryDosyayolu.keySet().toArray()[i].toString();
            categoryData.count = ((List)Muzik.categoryDosyayolu.values().toArray()[i]).size();
            categoryData.imageResource = R.drawable.klasor;
            categoryDataList.add(categoryData);
        }

        CategoryGridAdapter adapter = new CategoryGridAdapter(context, (ArrayList<CategoryData>)categoryDataList);
        grid=(GridView)((Activity)context).findViewById(R.id.categoryGrid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.mainActivity, "You Clicked at " +dosyaYollari[+ position], Toast.LENGTH_SHORT).show();
                MainActivity.mainActivity.muzikList.muzikListele((ArrayList)Muzik.categoryDosyayolu.values().toArray()[position]);
                ((Activity) context).finish();
            }
        });
    }


    public void categorySingers(){

        ArrayList categoryDataList = new ArrayList<CategoryData>();
        for(int i=0; i<Muzik.categorySarkici.size(); i++){
            CategoryData categoryData = new CategoryData();
            categoryData.categoryName =  Muzik.categorySarkici.keySet().toArray()[i].toString();
            categoryData.count = ((List)Muzik.categorySarkici.values().toArray()[i]).size();
            categoryData.imageResource = R.drawable.klasor;
            categoryDataList.add(categoryData);
        }

        CategoryGridAdapter adapter = new CategoryGridAdapter(context, (ArrayList<CategoryData>)categoryDataList);
        grid=(GridView)((Activity)context).findViewById(R.id.categoryGrid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.mainActivity, "You Clicked at " +sarkicilar[+ position], Toast.LENGTH_SHORT).show();
                MainActivity.mainActivity.muzikList.muzikListele((ArrayList)Muzik.categorySarkici.values().toArray()[position]);
                ((Activity) context).finish();
            }
        });
    }

    public void categoryAll(){
        MainActivity.mainActivity.muzikList.muzikListele(Muzik.muzikler);
        ((Activity) context).finish();
    }
}

class CategoryData{
    public String categoryName;
    public int count;
    public int imageResource;
}
