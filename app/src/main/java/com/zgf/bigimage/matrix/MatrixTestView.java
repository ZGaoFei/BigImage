package com.zgf.bigimage.matrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.zgf.bigimage.R;

public class MatrixTestView extends View {

    private Bitmap bitmap;

    private Matrix matrix;
    private Paint paint;

    public MatrixTestView(Context context) {
        this(context, null);
    }

    public MatrixTestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MatrixTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.kongque);

        matrix = new Matrix();
        setMatrix();
//        setOtherMatrix();
        setAnotherMatrix();

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    private void setMatrix() {
//        matrix.setTranslate(100, 100);
//        matrix.preTranslate(100, 100);
//        matrix.postTranslate(100, 100);

//        matrix.setRotate(30);
//        matrix.setRotate(30, 100, 100);
//        matrix.postRotate(30);
//        matrix.postRotate(30, 100, 100);
//        matrix.preRotate(30);
//        matrix.preRotate(30, 100, 100);

//        matrix.setScale(0.5f, 0.5f);
//        matrix.setScale(0.5f, 0.5f, 100, 100);
//        matrix.postScale(0.5f, 0.5f);
//        matrix.postScale(0.5f, 0.5f, 100, 100);
//        matrix.preScale(0.5f, 0.5f);
//        matrix.preScale(0.5f, 0.5f, 100, 100);

//        matrix.setSkew(0.3f, 0.3f);
//        matrix.setSkew(0.3f, 0.4f, 100, 100);
//        matrix.postSkew(0.3f, 0.3f);
//        matrix.postSkew(0.3f, 0.4f, 100, 100);
//        matrix.preSkew(0.3f, 0.3f);
//        matrix.preSkew(0.3f, 0.4f, 100, 100);
    }

    /**
     * 数组长度为8，表示当前的四个角对应的坐标，每两个数为一个坐标点
     * pointCount表示取其中的几个点，不能超过4
     */
    private void setOtherMatrix() {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        float[] src = new float[]{0, 0, 0, height, width, height, width, 0};
        float[] des = new float[]{0 + 150, 0, 0 + 120, height - 80, width - 50, height - 30, width - 150, 0 + 60};
        matrix.setPolyToPoly(src, 0, des, 0, 4);
    }

    private void setAnotherMatrix() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        float bWidth = bitmap.getWidth();
        float bHeight = bitmap.getHeight();
        RectF src = new RectF(0, 0, bWidth / 2, bHeight / 2);
        RectF dst = new RectF(0, 0, screenWidth, screenHeight);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.END);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);

        canvas.drawBitmap(bitmap, matrix, paint);
    }
}
