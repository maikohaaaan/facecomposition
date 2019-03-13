package com.example.pmmodel.facecoversion;
//package com.google.firebase.example.mlkit;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;
import com.google.firebase.FirebaseApp;
import android.graphics.Bitmap;
import android.util.Log;
import java.util.List;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.app.Activity;
import android.view.View;

public class milit extends Activity implements View.OnClickListener {

    Button btn1,btn2,btn3;
    Intent int1,int2,int3;
    int eyes = 3;

    private TextView textView;
    private Testclass test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milit);
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text_view);

        btn1 = (Button) findViewById(R.id.send_button);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.send_button2);
        btn2.setOnClickListener(this);
        btn3 = (Button) findViewById(R.id.send_button3);
        btn3.setOnClickListener(this);

        test = new Testclass();
        String str =  test.compare(eyes);
        textView.setText(str);


        //大泉洋の写真をビットマップに変換 → inputimageに保存
        Resources r = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(r, R.drawable.you);

        FirebaseVisionImage inputimage = FirebaseVisionImage.fromBitmap(bitmap);
        // [END image_from_bitmap]

        //ブラピの写真をビットマップに変換 → inputimage2に保存
        Resources r2 = getResources();
        Bitmap bitmap2 = BitmapFactory.decodeResource(r2, R.drawable.burapi);

        FirebaseVisionImage inputimage2 = FirebaseVisionImage.fromBitmap(bitmap2);

        /*
        //アプリ内保存イメージ利用のため　変換したいデータ

        private void imageFromPath1 (Context context, Uri uri){
            // [START image_from_path]
            FirebaseVisionImage inputimage1;
            try {
                inputimage1 = FirebaseVisionImage.fromFilePath(context, uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // [END image_from_path]
        }


        //アプリ内保存の芸能人イメージデータ
        private void imageFromPath2 (Context context, Uri uri){
            // [START image_from_path]
            FirebaseVisionImage inputimage2;
            try {
                inputimage2 = FirebaseVisionImage.fromFilePath(context, uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // [END image_from_path]
        }
        */
        //取得のオプション設定　どこの位置をとるとか、制度を優先するとか・・・
        // [START set_detector_options]
        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .setMinFaceSize(0.15f)
                        .enableTracking()
                        .build();


        // 指定したオプションを渡してインスタンスを取得
        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(options);
//

        // [START run_detector] とりあえず左目の座標を取得　inputimage1
        Task<List<FirebaseVisionFace>> result =
                detector.detectInImage(inputimage)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionFace>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionFace> faces) {
                                        // Task completed successfully
                                        // [START_EXCLUDE]
                                        // [START get_face_info]
                                        for (FirebaseVisionFace face : faces) {
                                            Rect bounds = face.getBoundingBox();
                                            float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                                            float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees

                                            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                                            // nose available):
                                            FirebaseVisionFaceLandmark leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR);
                                            if (leftEar != null) {
                                                FirebaseVisionPoint leftEarPos = leftEar.getPosition();
                                            }

                                            // If classification was enabled:
                                            if (face.getSmilingProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                                float smileProb = face.getSmilingProbability();
                                            }
                                            if (face.getRightEyeOpenProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                                float rightEyeOpenProb = face.getRightEyeOpenProbability();
                                            }

                                            // If face tracking was enabled:
                                            if (face.getTrackingId() != FirebaseVisionFace.INVALID_ID) {
                                                int id = face.getTrackingId();
                                            }
                                            List<FirebaseVisionPoint> leftEyeContour =
                                                    face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints();
                                            Log.i("example","input1画像の値取得の成功");

                                            //ArrayAdapter adaptereye = new ArrayAdapter<FirebaseVisionPoint>(this, android.R.layout.simple_list_item_1, leftEyeContour);
                                            //ListView View = (ListView) findViewById(R.id.listview);
                                            //View.setAdapter(adaptereye);

                                        }
                                        // [END get_face_info]
                                        // [END_EXCLUDE]
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });
        //とりあえず左目の座標を取得　inputimage2
        Task<List<FirebaseVisionFace>> result2 =
                detector.detectInImage(inputimage2)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionFace>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionFace> faces2) {
                                        // Task completed successfully
                                        // [START_EXCLUDE]
                                        // [START get_face_info]
                                        for (FirebaseVisionFace face : faces2) {
                                            Rect bounds = face.getBoundingBox();
                                            float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                                            float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees

                                            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                                            // nose available):
                                            FirebaseVisionFaceLandmark leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR);
                                            if (leftEar != null) {
                                                FirebaseVisionPoint leftEarPos = leftEar.getPosition();
                                            }

                                            // If classification was enabled:
                                            if (face.getSmilingProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                                float smileProb = face.getSmilingProbability();
                                            }
                                            if (face.getRightEyeOpenProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                                float rightEyeOpenProb = face.getRightEyeOpenProbability();
                                            }

                                            // If face tracking was enabled:
                                            if (face.getTrackingId() != FirebaseVisionFace.INVALID_ID) {
                                                int id = face.getTrackingId();
                                            }
                                            List<FirebaseVisionPoint> leftEyeContour =
                                                    face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints();
                                            Log.i("example","input2画像の値取得の成功");
                                            //ArrayAdapter adaptereye = new ArrayAdapter<FirebaseVisionPoint>(this, android.R.layout.simple_list_item_1, leftEyeContour);
                                            //ListView View = (ListView) findViewById(R.id.listview);
                                            //View.setAdapter(adaptereye);

                                        }
                                        // [END get_face_info]
                                        // [END_EXCLUDE]
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });

    }
    public void onClick(View v) {
        String originpath = "origin.jpg"; String selectphoto = "burapi.jpg";
        String position_origin = "[" +
                "{ID:1,parts:eyes,X:240,Y:214},{ID:2,parts:eyes,X:370,Y:210},{ID:3,parts:eyess,X:241,Y:230},{ID:4,parts:eyes,X:382,Y:235}," +
                "{ID:5,parts:nose,X:282,Y:219},{ID:6,parts:nose,X:320,Y:220},{ID:7,parts:nose,X:283,Y:275},{ID:8,parts:nose,X:325,Y:276}," +
                "{ID:9,parts:mouth,X:27" +
                "0,Y:296},{ID:10,parts:mouth,X:338,Y:295},{ID:11,parts:mouth,X:271,Y:310},{ID:12,parts:mouth,X:337,Y:312}" +
                "]";

        String position_select = "[" +
                "{ID:1,parts:eyes,X:80,Y:113},{ID:2,parts:eyes,X:208,Y:110},{ID:3,parts:eyess,X:81,Y:130},{ID:4,parts:eyes,X:210,Y:128}," +
                "{ID:5,parts:nose,X:120,Y:119},{ID:6,parts:nose,X:168,Y:117},{ID:7,parts:nose,X:151,Y:185},{ID:8,parts:nose,X:170,Y:128}," +
                "{ID:9,parts:mouth,X:110,Y:193},{ID:10,parts:mouth,X:178,Y:190},{ID:11,parts:mouth,X:151,Y:225},{ID:12,parts:mouth,X:190,Y:198}" +
                "]";

        switch (v.getId()){
            case R.id.send_button:
                int1 = new Intent(getApplication(),saveimage.class);
                int1.putExtra("Eyes", eyes);
                startActivity(int1);
                break;
            case R.id.send_button2:
                int2 = new Intent(getApplication(),finalimage.class);
                int2.putExtra("originpath",originpath);
                int2.putExtra("selectphoto",selectphoto);
                int2.putExtra("position_origin",position_origin);
                int2.putExtra("position_select",position_select);
                startActivity(int2);
                break;
            case R.id.send_button3:
                int3 = new Intent(getApplication(), Photocut.class);
                int3.putExtra("originpath",originpath);
                int3.putExtra("selectphoto",selectphoto);
                int3.putExtra("position_origin",position_origin);
                int3.putExtra("position_select",position_select);
                startActivity(int3);
                break;

        }}

}
