package com.example.pmmodel.facecoversion;

import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import android.graphics.Bitmap;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.graphics.BitmapFactory;
import android.app.Activity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

public class CompositeImageActivity extends Activity implements CheckBox.OnCheckedChangeListener{

    ImageView origImgView1;
    ImageView origImgView2;
    ImageView compImgView1;
    ImageView compImgView2;
    CheckBox leftEyeCheck;
    CheckBox rightEyeCheck;
    CheckBox noseCheck;
    CheckBox lipCheck;


    FaceDetector detector=new FaceDetector();
    ImageComposer composer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composite_image);
        FirebaseApp.initializeApp(this);

        origImgView1=(ImageView)findViewById(R.id.original_image1);
        origImgView2=(ImageView)findViewById(R.id.original_image2);
        compImgView1=(ImageView)findViewById(R.id.composite_image1);
        compImgView2=(ImageView)findViewById(R.id.composite_image2);

        leftEyeCheck=(CheckBox)findViewById(R.id.checkbox_left_eye);
        rightEyeCheck=(CheckBox)findViewById(R.id.checkbox_right_eye);
        noseCheck=(CheckBox)findViewById(R.id.checkbox_nose);
        lipCheck=(CheckBox)findViewById(R.id.checkbox_lip);

        leftEyeCheck.setOnCheckedChangeListener(this);
        rightEyeCheck.setOnCheckedChangeListener(this);
        noseCheck.setOnCheckedChangeListener(this);
        lipCheck.setOnCheckedChangeListener(this);

        //ここに前のSelectpartsクラスから値を渡せばよさそう！　ビットマップでわたすのはありかな？
        String img_path1=getIntent().getStringExtra("photo1");
        String img_path2=getIntent().getStringExtra("photo2");

        if (img_path1!=null && img_path2!=null){
            initImages(img_path1,img_path2);
        }
        else {//test
            AssetManager assets = getResources().getAssets();
            Bitmap bitmap1 = null;
            Bitmap bitmap2 = null;
            try (InputStream istream = assets.open("burapi.jpg")) {
                bitmap1 = BitmapFactory.decodeStream(istream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try (InputStream istream = assets.open("origin.jpg")) {
                bitmap2 = BitmapFactory.decodeStream(istream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            initImages(bitmap1, bitmap2);
        }
    }
    private void drawCompImages() {
        FaceDetector.FacePartsPosition face1=detector.resultMap.get("orig1");
        FaceDetector.FacePartsPosition face2=detector.resultMap.get("orig2");
        if(face1!=null && face2 !=null) {

            composer.reset();
            if (leftEyeCheck.isChecked()) {
                composer.replace(
                        face1.leftEye,
                        face2.leftEye,
                        0.2f
                );
            }
            if (rightEyeCheck.isChecked()) {
                composer.replace(
                        face1.rightEye,
                        face2.rightEye,
                        0.2f
                );
            }
            if (noseCheck.isChecked()) {
                composer.replace(
                        face1.nose,
                        face2.nose
                );
            }
            if (lipCheck.isChecked()) {
                composer.replace(
                        face1.lip,
                        face2.lip
                );
            }
            origImgView1.setImageBitmap(composer.origGuideBitmap1);
            origImgView2.setImageBitmap(composer.origGuideBitmap2);

            compImgView1.setImageBitmap(composer.compBitmap1);
            compImgView2.setImageBitmap(composer.compBitmap2);
        }
    }
    private void initImages(String img1, String img2) {
        initImages(Uri.parse(img1),Uri.parse(img2));
    }
    private void initImages(Uri img1, Uri img2) {
        try {
            initImages(
                    BitmapFactory.decodeStream(getContentResolver().openInputStream(img1)),
                    BitmapFactory.decodeStream(getContentResolver().openInputStream(img2))
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
    顔認識速度を保つために画像のサイズに上限を設けます
     */
    private Bitmap sizeLimit(Bitmap bitmap){
        int sizeLimit=640;
        int w=bitmap.getWidth();
        int h=bitmap.getHeight();
        if (w>sizeLimit || h>sizeLimit){
            float scale= (float)sizeLimit / Math.max(w,h);
            return Bitmap.createScaledBitmap(bitmap,(int)(w*scale),(int)(h*scale),true);
        }
        else
            return bitmap;
    }

    private void initImages(final Bitmap bitmap1, final Bitmap bitmap2){
        if(bitmap1==null || bitmap2==null){
            Toast.makeText(this,"画像がNULLです",Toast.LENGTH_SHORT).show();
        }
        else {
            Bitmap bm1=sizeLimit(bitmap1);
            Bitmap bm2=sizeLimit(bitmap2);

            origImgView1.setImageBitmap(bm1);
            origImgView2.setImageBitmap(bm2);
            composer = new ImageComposer(bm1, bm2);

            runFaceDetection(bm1, bm2, new DetectionCompletedCallback() {
                @Override
                public void onDetected() {
                    Toast.makeText(CompositeImageActivity.this, "顔検出成功", Toast.LENGTH_SHORT).show();
                    drawCompImages();
                }
            });
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        drawCompImages();
    }

    private interface DetectionCompletedCallback {
        void onDetected();
    }

    private void runFaceDetection(Bitmap bitmap1, Bitmap bitmap2, final DetectionCompletedCallback callback){
        if(bitmap1!=null && bitmap2!=null) {
            detector.resultMap.clear();
            detector.detect(bitmap1, "orig1",new FaceDetector.FaceDetectedCallback() {
                @Override
                public void onDetected(FaceDetector.FacePartsPosition face) {
                    if(detector.resultMap.containsKey("orig1") &&detector.resultMap.containsKey("orig2") ){
                        callback.onDetected();
                    }
                }
                @Override
                public void onError(String msg) {
                    Toast.makeText(CompositeImageActivity.this,msg,Toast.LENGTH_SHORT).show();
                }
            });
            detector.detect(bitmap2, "orig2",new FaceDetector.FaceDetectedCallback() {
                @Override
                public void onDetected(FaceDetector.FacePartsPosition face) {
                    if(detector.resultMap.containsKey("orig1") &&detector.resultMap.containsKey("orig2") ){
                        callback.onDetected();
                    }
                }
                @Override
                public void onError(String msg) {
                    Toast.makeText(CompositeImageActivity.this,msg,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}