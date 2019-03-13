package com.example.pmmodel.facecoversion;
//package com.example.honmadaisuke.myapplication;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;

import java.io.InputStream;
import java.util.ArrayList;

public class saveimage extends AppCompatActivity {
    private int Eyes;   // パラメータ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saveimage);

        ArrayList test; int int1 = 0;
        ImageView imageView = findViewById(R.id.image_view);
        ImageView imageView2 = findViewById(R.id.image_view2);
        AssetManager assets = getResources().getAssets();

        // Intent を取得する
        Intent intent = getIntent();
        // キーを元にデータを取得する(データがない場合、第２引数の 0 が返る)
        Eyes = intent.getIntExtra("Eyes", 0);
        test = intent.getStringArrayListExtra("test");
        // 遷移した回数を +1 する
        Eyes++;

        // Eyesの数字表示する
        Toast toast2 = Toast.makeText(this, String.format("Eyes：%d", Eyes), Toast.LENGTH_LONG);
        toast2.setGravity(Gravity.TOP, 0, 300);
        toast2.show();
        Toast toast3 = Toast.makeText(this, String.format("int：%d", int1), Toast.LENGTH_LONG);
        toast3.setGravity(Gravity.TOP, 0, 100);
        toast3.show();


        // try-with-resources
        if (Eyes > 1 ){
            try (InputStream istream = assets.open("origin.jpg")){
                Bitmap bitmap = BitmapFactory.decodeStream(istream);
                imageView.setImageBitmap(bitmap); } catch (Exception e) { e.printStackTrace(); } }

        try (InputStream istream = assets.open("burapi.jpg")){
            Bitmap bitmap = BitmapFactory.decodeStream(istream);
            imageView2.setImageBitmap(bitmap); } catch (Exception e) {e.printStackTrace();}

    }}
