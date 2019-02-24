package com.example.pmmodel.facecoversion;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.io.InputStream;
import java.util.ArrayList;

public class TestCanvasView extends View {
    //関数定義
    private ArrayList<String> url; private String jsorigin;String jsselect;
    private Paint paint;
    private Photocalc photo;
    private Bitmap bmporigin = null; private Bitmap bmpselect = null;

    //url(0)origin , url(1)select , url(2)json_origin , url(3)json_select
    public void showCanvas(ArrayList<String> url){
        AssetManager assets = getResources().getAssets();
        try (InputStream istream = assets.open( url.get(0) )){
            bmporigin = BitmapFactory.decodeStream(istream);
        } catch (Exception e) { e.printStackTrace(); }

        try (InputStream istream = assets.open( url.get(1) )){
            bmpselect = BitmapFactory.decodeStream(istream);
        } catch (Exception e) { e.printStackTrace(); }

        jsorigin = url.get(2); jsselect = url.get(3);

        //重要:画像再描写(onDraw)
        invalidate(); }

    //コンストラクタ分からん
    public TestCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs); paint = new Paint(); }

    //画像処理
    @Override
    protected void onDraw(Canvas canvas) {

        //トリミングposition計算クラス呼出(0-4eyes,5-7nose,8-11mouth)
        ArrayList<Integer> triming = new ArrayList<Integer>();
        Rect reorigin; Rect reselect; Rect reeyes; Rect renose; Rect remouth;
        Rect deorigin; Rect deselect; Rect deeyes; Rect denose; Rect demouth;
        int WH = bmporigin.getWidth(); int HT = bmporigin.getHeight();
        int WHS = bmpselect.getWidth(); int HTS = bmpselect.getHeight();
        photo = new Photocalc(); triming =  photo.trim(jsselect);
        reorigin = new Rect(0,0,WH, HT); reselect = new Rect(0,0,WH, HT);
        reeyes = new Rect(triming.get(0),triming.get(1), triming.get(2), triming.get(3));
        renose = new Rect(triming.get(4),triming.get(5), triming.get(6), triming.get(7));
        remouth = new Rect(triming.get(8),triming.get(9), triming.get(10), triming.get(11));

        //画像表示位置,パーツサイズ設定 [元画像座標](0-1eyes,2-3nose,4-5mouth), [元画像サイズ](6-7eyes,8-9nose,10-11mouth)
        ArrayList<Integer> position = new ArrayList<Integer>(); ArrayList<Integer> resize = new ArrayList<Integer>();
        ArrayList<Integer> cont = new ArrayList<Integer>(); cont.add(9999);
        photo = new Photocalc(); position =  photo.position(jsorigin);

        //(0-1元画像,2-3選択画像)
        photo = new Photocalc(); resize =  photo.resize( WH, HT, cont);
        deorigin = new Rect(0,0, resize.get(0),resize.get(1));
        Rect rect = new Rect(0, 0, resize.get(0)+20, resize.get(1)+20); rect.offset(-10,-10);

        resize =  photo.resize( WHS, HTS, cont);
        deselect = new Rect(0,0, resize.get(2),resize.get(3));
        deselect.offset(0,50);

        position =  photo.resize( WH, HT, position);
        deeyes = new Rect(0, 0, position.get(6), position.get(7));
        deeyes.offset( position.get(0) , position.get(1));
        denose = new Rect(0, 0, position.get(8), position.get(9));
        denose.offset( position.get(2) , position.get(3));
        demouth = new Rect(0, 0, position.get(10), position.get(11));
        demouth.offset( position.get(4) , position.get(5));

        // 画像表示
        canvas.drawColor(Color.argb(50, 0, 0, 255));
        paint.setStrokeWidth(5); paint.setColor(Color.BLUE); paint.setStyle(Paint.Style.STROKE);
        canvas.drawBitmap(bmpselect, reselect, deselect, paint);
        canvas.translate(140,450);
        canvas.drawRect(rect, paint);
        canvas.drawBitmap(bmporigin, reorigin, deorigin, paint);
        canvas.drawBitmap(bmpselect, reeyes, deeyes, paint);
        canvas.drawBitmap(bmpselect, renose, denose, paint);
        canvas.drawBitmap(bmpselect, remouth, demouth, paint);
        paint.setStrokeWidth(1); paint.setTextSize(40); paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText("合成画像",-10,-20, paint);
    }
}