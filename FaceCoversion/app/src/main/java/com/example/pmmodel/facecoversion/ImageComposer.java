package com.example.pmmodel.facecoversion;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class ImageComposer {
    private Bitmap origBitmap1;
    private Bitmap origBitmap2;
    public Bitmap origGuideBitmap1;
    public Bitmap origGuideBitmap2;
    public Bitmap compBitmap1;
    public Bitmap compBitmap2;
    public ImageComposer(Bitmap bitmap1,Bitmap bitmap2){
        this.origBitmap1=bitmap1;
        this.origBitmap2=bitmap2;
        reset();
    }
    public void replace(Rect rect1,Rect rect2,float inflateRate) {
        Rect r1=new Rect(rect1);
        Rect r2=new Rect(rect2);

        r1.inset((int)(-r1.width()*inflateRate),(int)(-r1.height()*inflateRate));
        r2.inset((int)(-r2.width()*inflateRate),(int)(-r2.height()*inflateRate));
        replace(r1,r2);
    }
    public void replace(Rect rect1,Rect rect2){
        Canvas compCanvas1 =new Canvas(compBitmap1);
        Canvas compCanvas2=new Canvas(compBitmap2);

        compCanvas1.drawBitmap(origBitmap2,rect2,rect1, null);
        compCanvas2.drawBitmap(origBitmap1,rect1,rect2, null);

        Canvas origGuideCanvas1 =new Canvas(origGuideBitmap1);
        Canvas origGuideCanvas2=new Canvas(origGuideBitmap2);

        Paint paint=new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        origGuideCanvas1.drawRect(rect1,paint);
        origGuideCanvas2.drawRect(rect2,paint);

    }

    public void reset() {
        origGuideBitmap1 = origBitmap1.copy(Bitmap.Config.ARGB_8888,true);
        origGuideBitmap2 = origBitmap2.copy(Bitmap.Config.ARGB_8888,true);
        compBitmap1 = origBitmap1.copy(Bitmap.Config.ARGB_8888,true);
        compBitmap2 = origBitmap2.copy(Bitmap.Config.ARGB_8888,true);
    }
}
