package bilgi.sesliplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CategoryActivity extends AppCompatActivity {
    private Spinner spinner;
    private String[] categorys={"Filtre Seçin","Filtreleme Devre Dısı","Dosya Yoluna Gore","Sarkıcıya Gore"};
    private ArrayAdapter<String> catogoysadapter;
    CategoryGrid g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        g = new CategoryGrid(this);
        spinner = (Spinner) findViewById(R.id.spinner);
        catogoysadapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item1, categorys);
        catogoysadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(catogoysadapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(parent.getSelectedItem().toString().equals(categorys[1])){
                    g.categoryAll();
                }
                else if(parent.getSelectedItem().toString().equals(categorys[2])){
                    g.categoryDirs();
                }
                else if(parent.getSelectedItem().toString().equals(categorys[3])){
                    Log.e("aaaa", "naber");
                    g.categorySingers();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
