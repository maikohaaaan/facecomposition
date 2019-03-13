package com.example.pmmodel.facecoversion;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.content.res.Resources;
import android.graphics.BitmapFactory;


public class SelectParts extends AppCompatActivity {

    private String spinnerpartsitem[] = {"Eye", "nose", "mouth", "face"};
    private TextView textViewparts;
    private String spinnerpersonitem[] = {"石原さとみ", "深田恭子", "ブラットピッド", "田中圭","大泉洋"};
    private TextView textViewperson;
    String parts;
    String photo2;
    Uri photo2uri;
    String takephoto1=null;
    String uploadphoto1=null;
    //選択した芸能人のBitmapを受け取る用
    //takephotoclassから受け取ったビットマップ情報を受け取る
    //Intent intent = getIntent();
   // String photo1=getIntent().getStringExtra("takephotodata");
    //Bundle b = intent.getExtras();
    //Bitmap photo1 = (Bitmap) b.get("takephotodata");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_parts);

       // photo1=getIntent().getStringExtra("takephotodata");
        photo2 =null;
        textViewparts = findViewById(R.id.textparts);
       // Spinner spinnerparts = findViewById(R.id.spinner_parts);
        Spinner spinnerperson = findViewById(R.id.spinner_person);
        //選択した芸能人を受け取るための変数
        //final String person = (String) spinnerperson.getSelectedItem();

        // ArrayAdapte　交換したいパーツを選択
        /*ArrayAdapter<String> adapter
                = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,spinnerpartsitem);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        */

        //交換したい芸能人を選択
        ArrayAdapter<String> adapterperson
                = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,spinnerpersonitem);

       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // spinner に adapter をセット
        //spinnerparts.setAdapter(adapter);
        spinnerperson.setAdapter(adapterperson);

        if(getIntent().getStringExtra("takephotodata")!=null) {
            takephoto1=getIntent().getStringExtra("takephotodata");
        }else{
            uploadphoto1=getIntent().getStringExtra("uploadphotodata");
        }

        // リスナーを登録 確認のため一度ページに表示させる用
        spinnerperson.setOnItemSelectedListener(new OnItemSelectedListener() {
            //　アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                Spinner spinnerparts = (Spinner)parent;
                parts = (String)spinnerparts.getSelectedItem();
                textViewparts.setText(parts);
            }

            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        // リスナーを登録 芸能人編　名前を受け取って然るべき画像のbitmapを渡す　
        spinnerperson.setOnItemSelectedListener(new OnItemSelectedListener() {
            //　アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                Spinner spinnerperson = (Spinner)parent;
                String person = (String)spinnerperson.getSelectedItem();
                if(person.equals("石原さとみ")){
                    //photo2uri=Uri.parse("android.resource://com.example.pmmodel.facecoversion/drawable/satomi.jpg");
                    //photo2=photo2uri.toString();
                    photo2uri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + getResources().getResourcePackageName(R.drawable.satomi)
                            + '/' + getResources().getResourceTypeName(R.drawable.satomi) + '/' + getResources().getResourceEntryName(R.drawable.satomi) );
                    photo2=photo2uri.toString();
                    //photo2=context.getResources().openRawResource(R.drawable.satomi);
                   // photo2=this.getContext().getResources(),R.drawable.satomi;
                    //Resources r1 = getResources();
                    //photo2 = BitmapFactory.decodeResource(r1, R.drawable.satomi);


                }else if(person.equals("深田恭子")){
                    photo2uri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + getResources().getResourcePackageName(R.drawable.kyoko)
                            + '/' + getResources().getResourceTypeName(R.drawable.kyoko) + '/' + getResources().getResourceEntryName(R.drawable.kyoko) );
                    photo2=photo2uri.toString();

                }else if(person.equals("ブラットピット")){
                    photo2uri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + getResources().getResourcePackageName(R.drawable.burapi)
                            + '/' + getResources().getResourceTypeName(R.drawable.burapi) + '/' + getResources().getResourceEntryName(R.drawable.burapi) );
                    photo2=photo2uri.toString();

                }else if(person.equals("田中圭")){
                    photo2uri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + getResources().getResourcePackageName(R.drawable.keytanaka)
                            + '/' + getResources().getResourceTypeName(R.drawable.keytanaka) + '/' + getResources().getResourceEntryName(R.drawable.keytanaka) );
                    photo2=photo2uri.toString();

                }else if(person.equals("大泉洋")){
                    photo2uri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + getResources().getResourcePackageName(R.drawable.you)
                            + '/' + getResources().getResourceTypeName(R.drawable.you) + '/' + getResources().getResourceEntryName(R.drawable.you) );
                    photo2=photo2uri.toString();
                }
            }

            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        Button next = findViewById(R.id.button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectParts.this, CompositeImageActivity.class);  //インテントの作成
                if(uploadphoto1 == null) {
                    intent.putExtra("photo1", takephoto1);
                }else {
                    intent.putExtra("photo1", uploadphoto1);
                }
                intent.putExtra("photo2",photo2);
                //intent.putExtra("parts",parts);//撮った写真のビットマップを次のアクティビティに渡す 次ページで選択するため必要なし
                startActivity(intent);
            }
        });

    }
}
