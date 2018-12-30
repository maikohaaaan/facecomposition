package com.example.pmmodel.facecoversion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SelectParts extends AppCompatActivity {

    private String spinnerpartsitem[] = {"Eye", "nose", "mouth", "face"};
    private TextView textViewparts;
    //private String spinnerperson[] = {"石原さとみ", "深田恭子", "ブラットピッド", "田中圭"};
    //private TextView textViewperson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_parts);

        textViewparts = findViewById(R.id.textparts);
        Spinner spinnerparts = findViewById(R.id.spinner_parts);

        // ArrayAdapter
        ArrayAdapter<String> adapter
                = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,spinnerpartsitem);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // spinner に adapter をセット
        spinnerparts.setAdapter(adapter);

        // リスナーを登録
        spinnerparts.setOnItemSelectedListener(new OnItemSelectedListener() {
            //　アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                Spinner spinnerparts = (Spinner)parent;
                String item = (String)spinnerparts.getSelectedItem();
                textViewparts.setText(item);
            }

            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });
    }
}
