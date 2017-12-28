package bilgi.sesliplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryGridAdapter extends ArrayAdapter<CategoryData> {
    private Context mContext;
    ArrayList<CategoryData> categoryDataList;

    public CategoryGridAdapter(Context c, ArrayList<CategoryData> categoryDataList) {
        super(c, R.layout.single_category_item, categoryDataList);
        mContext = c;
        this.categoryDataList = categoryDataList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.single_category_item, null);
        }

        CategoryData categoryData = getItem(position);

        if (categoryData != null) {
            TextView textView = (TextView) v.findViewById(R.id.grid_text);
            TextView count_textView = (TextView) v.findViewById(R.id.grid_count);
            ImageView imageView = (ImageView)v.findViewById(R.id.grid_image);

            textView.setText(categoryData.categoryName);
            count_textView.setText(categoryData.count+" Adet");
            imageView.setImageResource(categoryData.imageResource);
        }

        return v;
    }
}