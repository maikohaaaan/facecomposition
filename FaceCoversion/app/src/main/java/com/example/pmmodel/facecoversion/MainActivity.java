package com.example.pmmodel.facecoversion;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.Button).setOnClickListener(this);
    }

    //ボタンが押された時の処理
    public void onClick(View view){
        Intent intent = new Intent(this, SelectPhoto.class);  //インテントの作成
        startActivity(intent);                                 //画面遷移
    }
}