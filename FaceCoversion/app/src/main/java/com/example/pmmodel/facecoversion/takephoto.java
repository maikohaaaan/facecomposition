package com.example.pmmodel.facecoversion;
import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;
import android.graphics.Bitmap;
import java.io.IOException;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class takephoto extends AppCompatActivity {

    private final static int RESULT_CAMERA = 1001;
    private final static int REQUEST_PERMISSION = 1002;
    private ImageView imageView;
    private Uri cameraUri;
    private String camerauris;
    private String filePath;
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debug","onCreate()");
        setContentView(R.layout.activity_takephoto);

        imageView = findViewById(R.id.image_view);

        Button cameraButton = findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Android 6, API 23以上でパーミッシンの確認
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermission();
                }
                else {
                    cameraIntent();
                }
            }
        });
        Button nextButton= findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v2){
                Intent intent = new Intent(getApplicationContext(), SelectParts.class);//インテントの作成
                intent.putExtra("takephotodata",camerauris);//撮った写真のuri(string)を次のアクティビティに渡す
                startActivity(intent);
                //startActivity(intent);//militに移動
            }
        });


    }

    private void cameraIntent(){
        Log.d("debug","cameraIntent()");

        // 保存先のフォルダーを作成するケース
//        File cameraFolder = new File(
//                Environment.getExternalStoragePublicDirectory(
//                        Environment.DIRECTORY_PICTURES),"IMG");
//        cameraFolder.mkdirs();

        // 保存先のフォルダーをカメラに指定した場合
        File cameraFolder = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM),"Camera");


        // 保存ファイル名
        String fileName = new SimpleDateFormat(
                "ddHHmmss", Locale.US).format(new Date());
        filePath = String.format("%s/%s.jpg", cameraFolder.getPath(),fileName);
        Log.d("debug","filePath:"+filePath);

        // capture画像のファイルパス
        File cameraFile = new File(filePath);
        cameraUri = FileProvider.getUriForFile(
                takephoto.this,
                getApplicationContext().getPackageName() + ".fileprovider",
                cameraFile);
        camerauris = cameraUri.toString();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(intent, RESULT_CAMERA);
        //撮影した画像をビットマップに変換してphotoに格納
        //try {
        //    photo = MediaStore.Images.Media.getBitmap(getContentResolver(), cameraUri);
        //}catch (IOException e) {
        //    e.printStackTrace();
        //}

        Log.d("debug","startActivityForResult()");
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent intent) {
        if (requestCode == RESULT_CAMERA) {

            if(cameraUri != null){
                imageView.setImageURI(cameraUri);

                registerDatabase(filePath);
            }
            else{
                Log.d("debug","cameraUri == null");
            }
        }
    }

    // アンドロイドのデータベースへ登録する
    private void registerDatabase(String file) {
        ContentValues contentValues = new ContentValues();
        ContentResolver contentResolver = takephoto.this.getContentResolver();
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put("_data", file);
        contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
    }

    // Runtime Permission check
    private void checkPermission(){
        // 既に許可している
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED){
            cameraIntent();
        }
        // 拒否していた場合
        else{
            requestPermission();
        }
    }

    // 許可を求める
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(takephoto.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);

        } else {
            Toast toast = Toast.makeText(this,
                    "許可されないとアプリが実行できません",
                    Toast.LENGTH_SHORT);
            toast.show();

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                    REQUEST_PERMISSION);

        }
    }

    // 結果の受け取り
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        Log.d("debug","onRequestPermissionsResult()");

        if (requestCode == REQUEST_PERMISSION) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraIntent();

            } else {
                // それでも拒否された時の対応
                Toast toast = Toast.makeText(this,
                        "これ以上なにもできません", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}