package com.example.pmmodel.facecoversion;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Log;

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

import java.util.HashMap;
import java.util.List;

public class FaceDetector {
    public HashMap<String, FacePartsPosition> resultMap = new HashMap<>();

    public interface FaceDetectedCallback {
        void onDetected(FacePartsPosition face);

        void onError(String msg);
    }

    public class FacePartsPosition {
        Rect leftEye;
        Rect rightEye;
        Rect nose;
        Rect lip;
        Rect face;

        public FacePartsPosition(FirebaseVisionFace detected) {
            face = detected.getBoundingBox();
            leftEye = contourToRect(
//                    detected.getContour(FirebaseVisionFaceContour.LEFT_EYEBROW_TOP),
                    detected.getContour(FirebaseVisionFaceContour.LEFT_EYE));
            rightEye = contourToRect(
//                    detected.getContour(FirebaseVisionFaceContour.RIGHT_EYEBROW_TOP),
                    detected.getContour(FirebaseVisionFaceContour.RIGHT_EYE));
            nose = contourToRect(
                    detected.getContour(FirebaseVisionFaceContour.NOSE_BRIDGE),
                    detected.getContour(FirebaseVisionFaceContour.NOSE_BOTTOM));
            lip = contourToRect(
                    detected.getContour(FirebaseVisionFaceContour.LOWER_LIP_BOTTOM),
                    detected.getContour(FirebaseVisionFaceContour.UPPER_LIP_TOP));

        }

        private Rect contourToRect(FirebaseVisionFaceContour contourA, FirebaseVisionFaceContour contourB) {
            Rect a = contourToRect(contourA);
            Rect b = contourToRect(contourB);

            if (a.width() == 0)
                a.inset(-1, 0);
            if (b.width() == 0)
                b.inset(-1, 0);
            if (a.height() == 0)
                a.inset(0, -1);
            if (b.height() == 0)
                b.inset(0, -1);

            if (a != null && b != null) {
                a.union(b);
                return a;
            } else if (a != null) {
                return a;
            } else if (b != null) {
                return b;
            } else {
                return null;
            }
        }

        private Rect contourToRect(FirebaseVisionFaceContour contour) {
            if (contour != null && contour.getPoints().size() > 0) {
                int xMin = Integer.MAX_VALUE;
                int yMin = Integer.MAX_VALUE;
                int xMax = 0;
                int yMax = 0;

                for (FirebaseVisionPoint p : contour.getPoints()) {
                    if (p.getX() < xMin) xMin = p.getX().intValue();
                    if (p.getX() > xMax) xMax = p.getX().intValue();
                    if (p.getY() < yMin) yMin = p.getY().intValue();
                    if (p.getY() > yMax) yMax = p.getY().intValue();
                }
                return new Rect(xMin, yMin, xMax, yMax);
            } else {
                return null;
            }
        }
    }

    FirebaseVisionFaceDetector detector;

    public FaceDetector() {
        //取得のオプション設定　どこの位置をとるとか、制度を優先するとか・・・
        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.NO_CLASSIFICATIONS)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setMinFaceSize(0.15f)
                        .build();
        detector = FirebaseVision.getInstance().getVisionFaceDetector(options);
    }

    public void detect(Bitmap bitmap, final String id, final FaceDetectedCallback callback) {
        FirebaseVisionImage img = FirebaseVisionImage.fromBitmap(bitmap);
        Task<List<FirebaseVisionFace>> task = detector.detectInImage(img);
        task.addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> faces) {
                if (faces.size() == 0) {
                    Log.e(FaceDetector.class.getName(), "画像中に顔がありません");
                    callback.onError("画像中に顔がありません");
                } else {
                    if (faces.size() == 1) {
                        Log.i(FaceDetector.class.getName(), "画像中に１つの顔を検出しました" + faces.get(0).getBoundingBox().toString());
                    } else if (faces.size() > 1) {
                        Log.w(FaceDetector.class.getName(), "画像中に複数の顔を検出しました。１番目の顔のみを用います");
                    }
                    FacePartsPosition parts = new FacePartsPosition(faces.get(0));
                    resultMap.put(id, parts);
                    callback.onDetected(parts);
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(FaceDetector.class.getName(), "Firebasedでエラー発生");
                e.printStackTrace();
            }
        });
    }
}