package com.example.pmmodel.facecoversion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Photocut extends AppCompatActivity {

    private boolean showCanvas;
    private ArrayList<String> url;
    private TestCanvasView canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photocut);
        Button btnsave = findViewById(R.id.btnsave);
        Button btnback = findViewById(R.id.btnback);
        canvas = this.findViewById(R.id.canvasview);

        //画像関数定義
        url = new ArrayList<String>();
        //Intent取得
        Intent intent = getIntent();
        //元画像パス名と選択画像ファイル名
        String originpath = ""; originpath = intent.getStringExtra("originpath");
        String selectphoto = ""; selectphoto = intent.getStringExtra("selectphoto");
        //パーツ座標取得(JSON)
        String position_origin = ""; position_origin = intent.getStringExtra("position_origin");
        String position_select = ""; position_select = intent.getStringExtra("position_select");
        url.add(originpath); url.add(selectphoto); url.add(position_origin); url.add(position_select);

        //画像結合クラス呼出
        canvas.showCanvas(url);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }});

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }});

    }
}

//Toast toast = Toast.makeText(this, String.format("X1：%d", num), Toast.LENGTH_LONG);
//toast.setGravity(Gravity.TOP, 0, 100);toast.show();