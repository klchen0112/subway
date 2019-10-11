package com.klchen.subway;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.klchen.subway.Graph.DealGraph;
import com.klchen.subway.ui.main.MainFragment;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner1,spinner2,spinner3,spinner4;
    private Button btn01;
    private DealGraph dealGraph;
    private TextView textView;
    private ArrayAdapter<String> adapter2,adapter1,adapter3,adapter4;
    private List<String> stations1,stations2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        try {
            this.dealGraph = new DealGraph("http://106.14.117.206:8000/map.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
//        System.out.println(mitmes);


        spinner3 = (Spinner)super.findViewById(R.id.spinner3);
        adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, dealGraph.getLines());
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        spinner3.setSelection(0);
        stations1 = dealGraph.getStationByLine((String)spinner3.getSelectedItem());

        spinner1 = (Spinner)super.findViewById(R.id.spinner1);
        adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,stations1);
        spinner1.setAdapter(adapter1);
        spinner1.setSelection(0);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stations1.clear();
                stations1.addAll(dealGraph.getStationByLine((String)spinner3.getSelectedItem()));
                adapter1.notifyDataSetChanged();
                spinner1.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner4 = (Spinner)super.findViewById(R.id.spinner4);
        adapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, dealGraph.getLines());
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter4);

        spinner4.setSelection(1);
        stations2 = dealGraph.getStationByLine((String)spinner4.getSelectedItem());

        spinner2 = (Spinner)super.findViewById(R.id.spinner2);
        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,stations2);
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(0);

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stations2.clear();
                stations2.addAll(dealGraph.getStationByLine((String)spinner4.getSelectedItem()));
                adapter2.notifyDataSetChanged();
                spinner2.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        textView = findViewById(R.id.textView3);

        btn01 = findViewById(R.id.button2);

        btn01.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String sta1 = (String)spinner1.getSelectedItem();
                String sta2 = (String) spinner2.getSelectedItem();

                textView.setText(dealGraph.getPath(sta1,sta2));
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }

}
