package com.example.pmmodel.facecoversion;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;


public class SelectPhoto extends AppCompatActivity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        FirebaseApp.initializeApp(this);

        findViewById(R.id.button_takephoto).setOnClickListener(this);
        findViewById(R.id.button_selectphoto).setOnClickListener(this);
       // findViewById(R.id.button_selectphoto).setOnClickListener(this);
    }

    //ボタンが押された時の処理
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button_selectphoto:
                startCompositeImageActivityViaOpenDoc();
                break;
            case R.id.button_takephoto:
                Intent intent = new Intent(this, takephoto.class);  //インテントの作成
                startActivity(intent);
                break;
        }
    }

    private static final int OPEN_DOC1=0;
    private static final int OPEN_DOC2=1;

    private String firstImagePath=null;
    //private String secondImagePath=null;

    public void startCompositeImageActivityViaOpenDoc(){
        Intent intentGallery;
        intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
        intentGallery.setType("image/*");

        if(firstImagePath==null) {
            Intent intent = Intent.createChooser(intentGallery, "１枚目の画像");
            startActivityForResult(intent, OPEN_DOC1);
        }
        //else if(secondImagePath==null) {
        //    Intent intent = Intent.createChooser(intentGallery, "２枚目の画像");
        //    startActivityForResult(intent, OPEN_DOC2);
        //}
        else{
            Intent intent = new Intent(this, SelectParts.class);
            intent.putExtra("uploadphotodata",firstImagePath);
            //intent.putExtra("img2",secondImagePath);
            firstImagePath=null;
        //    secondImagePath=null;

            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == OPEN_DOC1) {
            if(resultCode != RESULT_OK) {
                return ;
            }
            Uri resultUri = data.getData();
            if(resultUri == null) {
                Toast.makeText(this, "Error.Try again.", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                //UriをString型にしたものを飛ばしたらOK
                firstImagePath=resultUri.toString();
                startCompositeImageActivityViaOpenDoc();
            }
        }
       // else if(requestCode == OPEN_DOC2) {
       //     if(resultCode != RESULT_OK) {
       //         return ;
       //     }
       //     Uri resultUri = data.getData();
       //     if(resultUri == null) {
       //         Toast.makeText(this, "Error.Try again.", Toast.LENGTH_LONG).show();
       //         return;
       //     }
       //     else {
       //         secondImagePath=resultUri.toString();
       //         startCompositeImageActivityViaOpenDoc();
       //     }
        //}
    }

}

