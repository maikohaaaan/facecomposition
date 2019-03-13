package com.example.pmmodel.facecoversion;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class finalimage extends AppCompatActivity {

    //positionクラス定義
    private Photocalc photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //検証用関数定義
        Integer num = 0;
        ImageView testImage = new ImageView(this);
        TextView text = new TextView(this);
        RelativeLayout.LayoutParams lptest;


        //ダミーレイアウト定義と表示
        RelativeLayout mainLayout = new RelativeLayout(this);
        setContentView(mainLayout);
        //ビュー定義
        AssetManager assets = getResources().getAssets();
        ImageView originImage = new ImageView(this); ImageView selectImage = new ImageView(this);
        ImageView eyesImage = new ImageView(this); ImageView noseImage = new ImageView(this);
        ImageView mouthImage = new ImageView(this);
        //画像関数定義
        Rect rect; Bitmap bitmap; BitmapRegionDecoder regionDecoder;
        ArrayList<Integer> triming = new ArrayList<Integer>();
        ArrayList<Integer> position = new ArrayList<Integer>();
        RelativeLayout.LayoutParams lpeyes; RelativeLayout.LayoutParams lpnose; RelativeLayout.LayoutParams lpmouth;
        int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
        //Intent取得
        Intent intent = getIntent();
        //元画像パス名と選択画像ファイル名
        String originpath = ""; originpath = intent.getStringExtra("originpath");
        String selectphoto = ""; selectphoto = intent.getStringExtra("selectphoto");
        //パーツ座標取得(JSON)
        String position_origin = ""; position_origin = intent.getStringExtra("position_origin");
        String position_select = ""; position_select = intent.getStringExtra("position_select");

        //元画像取込
        try (InputStream istream = assets.open(originpath)){
            bitmap = BitmapFactory.decodeStream(istream);
            originImage.setImageBitmap(bitmap);
        } catch (Exception e) { e.printStackTrace(); }

        //トリミングposition計算クラス呼出(0-4eyes,5-7nose,8-11mouth)
        photo = new Photocalc(); triming =  photo.trim(position_select);

        //選択画像取込➡︎トリミング(eyes/nose/mouth)
        try (InputStream istream = assets.open(selectphoto)){
            bitmap = BitmapFactory.decodeStream(istream);
            selectImage.setImageBitmap(bitmap);
            regionDecoder = BitmapRegionDecoder.newInstance(istream,false);

            rect = new Rect(triming.get(0),triming.get(1), triming.get(2), triming.get(3));
            bitmap = regionDecoder.decodeRegion(rect, null);
            eyesImage.setImageBitmap(bitmap);

            rect = new Rect(triming.get(4),triming.get(5), triming.get(6), triming.get(7));
            bitmap = regionDecoder.decodeRegion(rect, null);
            noseImage.setImageBitmap(bitmap);

            rect = new Rect(triming.get(8),triming.get(9), triming.get(10), triming.get(11));
            bitmap = regionDecoder.decodeRegion(rect, null);
            mouthImage.setImageBitmap(bitmap);

        } catch (Exception e) { e.printStackTrace(); }

        //画像サイズ設定（width,height）
        lpeyes = new RelativeLayout.LayoutParams(WC,WC);
        lpnose = new RelativeLayout.LayoutParams(WC,WC);
        lpmouth = new RelativeLayout.LayoutParams(WC,WC);
        lptest = new RelativeLayout.LayoutParams(WC, WC);


        //画像表示位置,サイズ設定 [元画像座標](0-1eyes,2-3nose,4-5mouth), [元画像サイズ](6-7eyes,8-9nose,10-11mouth)
        photo = new Photocalc(); position =  photo.position(position_origin);
        lpeyes.leftMargin = position.get(0); lpeyes.topMargin = position.get(1);
        lpnose.leftMargin = position.get(2); lpnose.topMargin = position.get(3);
        lpmouth.leftMargin = position.get(4); lpmouth.topMargin = position.get(5);
        lptest.leftMargin = 245; lptest.topMargin = 130;


        //画像表示
        mainLayout.addView(originImage);
        //mainLayout.addView(selectImage);
        //mainLayout.addView(eyesImage);
        //mainLayout.addView(noseImage);
        //mainLayout.addView(mouthImage);

        mainLayout.addView(eyesImage,lpeyes);
        mainLayout.addView(noseImage,lpnose);
        mainLayout.addView(mouthImage,lpmouth);
        mainLayout.addView(text);

    }}

//画像サイズ取得
//text.setText("width:"+String.valueOf(bitmap.getWidth()));

//トースト
//num = triming.get(4);
//Toast toast = Toast.makeText(this, String.format("X1：%d", num), Toast.LENGTH_LONG);
//toast.setGravity(Gravity.TOP, 0, 100);toast.show();

//testImage.setImageBitmap(bitmap);