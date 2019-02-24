package com.example.pmmodel.facecoversion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Photocalc {
    //コンストラクタ
    public Photocalc() {}

    //サンプルクラス
    public String compare(int number) {
        Integer nm = 2; String res = "2より低い";
        if (nm < number) { res = "2より大きい"; }
        return res; }

    //選択画像のパーツ別トリミングサイズ [eyes/nose/mouth]( left:X最小, top:Y最小, right:X最大, bottom:Y最大 )
    public ArrayList<Integer> trim(String position){
        //return定義
        ArrayList<Integer> res = new ArrayList<Integer>();

        //関数定義(partsチェック/トリミング位置(eyes,nose,mouth)/JSON変換
        String parts;
        Integer lfeyes = 0; Integer toeyes = 0; Integer rteyes = 0; Integer bmeyes = 0;
        Integer lfnose = 0; Integer tonose = 0; Integer rtnose = 0; Integer bmnose = 0;
        Integer lfmouth = 0; Integer tomouth = 0; Integer rtmouth = 0; Integer bmmouth = 0;
        Integer trimx = 0; Integer trimy = 0;
        String json = position;

        //トリミングサイズ探索
        try{ JSONArray array = new JSONArray(json);
            int count = array.length();
            for (int i=0; i<count; i++){
                JSONObject obj = array.getJSONObject(i);
                parts = obj.getString("parts");
                trimx = Integer.valueOf(obj.getString("X"));trimy = Integer.valueOf(obj.getString("Y"));

                if (parts.equals("eyes")){
                    if (lfeyes == 0){lfeyes = trimx;} if (toeyes == 0){toeyes = trimy;}
                    if (lfeyes > trimx){lfeyes = trimx;} if (toeyes > trimy){toeyes = trimy;}
                    if (rteyes < trimx){rteyes = trimx;} if (bmeyes < trimy){bmeyes = trimy;}
                }
                if (parts.equals("nose")){
                    if (lfnose == 0){lfnose = trimx;} if (tonose == 0){tonose = trimy;}
                    if (lfnose > trimx){lfnose = trimx;} if (tonose > trimy){tonose = trimy;}
                    if (rtnose < trimx){rtnose = trimx;} if (bmnose < trimy){bmnose = trimy;}
                }
                if (parts.equals("mouth")){
                    if (lfmouth == 0){lfmouth = trimx;} if (tomouth == 0){tomouth = trimy;}
                    if (lfmouth > trimx){lfmouth = trimx;} if (tomouth > trimy){tomouth = trimy;}
                    if (rtmouth < trimx){rtmouth = trimx;} if (bmmouth < trimy){bmmouth = trimy;}
                }

            } res.add(lfeyes);res.add(toeyes);res.add(rteyes);res.add(bmeyes);
            res.add(lfnose);res.add(tonose);res.add(rtnose);res.add(bmnose);
            res.add(lfmouth);res.add(tomouth);res.add(rtmouth);res.add(bmmouth);
        } catch (JSONException e) { e.printStackTrace(); }
        return res; }

    //元画像のパーツ別座標/サイズ [eyes/nose/mouth]( left:X最小, top:Y最小 ), [eyes/nose/mouth]( 横, 高さ )
    public ArrayList<Integer> position(String position) {
        //return定義
        ArrayList<Integer> res = new ArrayList<Integer>();

        //関数定義(座標位置(eyes,nose,mouth)/JSON変換
        String parts;
        Integer lfeyes = 0; Integer toeyes = 0; Integer rteyes = 0; Integer bmeyes = 0;
        Integer lfnose = 0; Integer tonose = 0; Integer rtnose = 0; Integer bmnose = 0;
        Integer lfmouth = 0; Integer tomouth = 0; Integer rtmouth = 0; Integer bmmouth = 0;
        Integer wheyes = 0; Integer hteyes = 0; Integer whnose = 0; Integer htnose = 0; Integer whmouth = 0; Integer htmouth = 0;
        Integer trimx = 0; Integer trimy = 0;
        String json = position;

        //座標位置探索
        try{ JSONArray array = new JSONArray(json);
            int count = array.length();
            for (int i=0; i<count; i++){
                JSONObject obj = array.getJSONObject(i);
                parts = obj.getString("parts");
                trimx = Integer.valueOf(obj.getString("X"));trimy = Integer.valueOf(obj.getString("Y"));

                if (parts.equals("eyes")){
                    if (lfeyes == 0){lfeyes = trimx;} if (toeyes == 0){toeyes = trimy;}
                    if (lfeyes > trimx){lfeyes = trimx;} if (toeyes > trimy){toeyes = trimy;}
                    if (rteyes < trimx){rteyes = trimx;} if (bmeyes < trimy){bmeyes = trimy;}
                }
                if (parts.equals("nose")){
                    if (lfnose == 0){lfnose = trimx;} if (tonose == 0){tonose = trimy;}
                    if (lfnose > trimx){lfnose = trimx;} if (tonose > trimy){tonose = trimy;}
                    if (rtnose < trimx){rtnose = trimx;} if (bmnose < trimy){bmnose = trimy;}
                }
                if (parts.equals("mouth")){
                    if (lfmouth == 0){lfmouth = trimx;} if (tomouth == 0){tomouth = trimy;}
                    if (lfmouth > trimx){lfmouth = trimx;} if (tomouth > trimy){tomouth = trimy;}
                    if (rtmouth < trimx){rtmouth = trimx;} if (bmmouth < trimy){bmmouth = trimy;}
                }

            } wheyes = rteyes - lfeyes; hteyes = bmeyes - toeyes;
            whnose = rtnose - lfnose; htnose = bmnose - tonose;
            whmouth = rtmouth - lfmouth; htmouth = bmmouth - tomouth;
            res.add(lfeyes);res.add(toeyes);res.add(lfnose);res.add(tonose);res.add(lfmouth);res.add(tomouth);
            res.add(wheyes);res.add(hteyes);res.add(whnose);res.add(htnose);res.add(whmouth);res.add(htmouth);
        } catch (JSONException e) { e.printStackTrace(); }
        return res; }

    //origin(800✖️800),selectリサイズ(300✖️300)
    public ArrayList<Integer> resize(int width, int hegiht, ArrayList<Integer> cont) {
        //return定義
        ArrayList<Integer> res = new ArrayList<Integer>();
        Integer setorigin = 800; Integer setselect = 300;
        double temp;
        Double rasetoriginwh = setorigin / Double.valueOf(width);
        Double rasetoriginht = setorigin / Double.valueOf(hegiht);
        Double rasetselectwh = setselect / Double.valueOf(width);
        Double rasetselectht = setselect / Double.valueOf(hegiht);

        if (cont.get(0) == 9999) {
            if (width > hegiht) {
                Double whorigin = width * rasetoriginwh; Double htorigin = hegiht * rasetoriginwh;
                temp = whorigin; int convert = (int) temp; res.add(convert);
                temp = htorigin; convert = (int) temp; res.add(convert);

                Double whselect = width * rasetselectwh; Double htselect = hegiht * rasetselectwh;
                temp = whselect; convert = (int) temp; res.add(convert);
                temp = htselect; convert = (int) temp; res.add(convert);
            }
            else {
                Double whorigin = width * rasetoriginht; Double htorigin = hegiht * rasetoriginht;
                temp = whorigin; int convert = (int) temp; res.add(convert);
                temp = htorigin; convert = (int) temp; res.add(convert);

                Double whselect = width * rasetselectht; Double htselect = hegiht * rasetselectht;
                temp = whselect; convert = (int) temp; res.add(convert);
                temp = htselect; convert = (int) temp; res.add(convert);
            }
        }
        // [元画像座標](0-1eyes,2-3nose,4-5mouth), [元画像サイズ](6-7eyes,8-9nose,10-11mouth)
        else {
            for (int i = 0; i < 12; i++) {
                if (width > hegiht) { temp = cont.get(i) * rasetoriginwh; int convert = (int) temp; res.add(i, convert); }
                else { temp = cont.get(i) * rasetoriginht; int convert = (int) temp; res.add(i, convert); }
            }
        }
        return res;
    }

}