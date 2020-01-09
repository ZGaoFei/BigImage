package com.zgf.bigimage.bigimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ScrollView;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class BigViewCopy extends View {
    private static final String TAG = "BigViewCopy";
    // 图片的宽高
    private float imageWidth;
    private float imageHeight;
    // 布局的宽高
    private float viewWidth;
    private float viewHeight;
    // 缩放比例
    private float scale = 1;
    private float currentScale = 1;

    // 判断最小的移动像素
    private int touchSlop;

    // 水平滑动
    private int currentX = 0;
    private int moveX = 0;
    private int left = 0;
    // 垂直滑动
    private int currentY = 0;
    private int moveY = 0;
    private int top = 0;

    private BitmapFactory.Options options;
    private BitmapRegionDecoder decoder;
    private Bitmap bitmap;
    // 绘制的区域
    private Rect rect;
    // 缩放
    private Matrix matrix;
    // 滑动器
    private Scroller scroller;

    public BigViewCopy(Context context) {
        this(context, null);
    }

    public BigViewCopy(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigViewCopy(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        options = new BitmapFactory.Options();

        rect = new Rect();
        matrix = new Matrix();
        scroller = new Scroller(getContext());

        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        // 获取TouchSlop的值
        touchSlop = configuration.getScaledPagingTouchSlop();
        Log.i(TAG, "touch slop: " + touchSlop);
    }

    public void setImageInputStream(InputStream stream) {
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream, null, options);
        imageWidth = options.outWidth;
        imageHeight = options.outHeight;
        Log.i(TAG, "width: " + imageWidth + " height: " + imageHeight);

        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;

        try {
            decoder = BitmapRegionDecoder.newInstance(stream, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (decoder == null) {
            return;
        }

        options.inBitmap = bitmap;
        // 缩放到屏幕的宽度
        matrix.setScale(currentScale, currentScale);
        // 取出当前view大小的部分显示
        bitmap = decoder.decodeRegion(rect, options);
        // 根据缩放绘制bitmap
        canvas.drawBitmap(bitmap, matrix, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        scale = viewWidth / imageWidth;
        Log.i(TAG, "scale: " + scale + " view width: " + viewWidth + " view height: " + viewHeight);
//        currentScale = scale;
        currentScale = 4;
        rect.left = 0;
        rect.top = 0;
        rect.right = w;
        rect.bottom = (int) (h / currentScale);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentX = (int) event.getRawX();
                currentY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                dispatchScrollHorizontal(event);
                dispatchScrollVertical(event);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private void dispatchScrollHorizontal(MotionEvent event) {
        moveX = (int) event.getRawX();
        int scrolldx = currentX - moveX;
        Log.i(TAG, "scroll dx: " + scrolldx);
        left += scrolldx;
        currentX = moveX;

        if (left >= 0 && left <= imageWidth - viewWidth / currentScale) {
            rect.left = left;
            rect.right = (int) (left + viewWidth / currentScale);

            invalidate();
        } else if (left < 0) {
            left = 0;
            rect.left = left;
            rect.right = (int) (left + viewWidth / currentScale);
        } else if (left > imageWidth - viewWidth / currentScale) {
            left = (int) (imageWidth - viewWidth / currentScale);
            rect.left = left;
            rect.right = (int) (left + viewWidth / currentScale);
        }
    }

    // 垂直滑动
    private void dispatchScrollVertical(MotionEvent event) {
        moveY = (int) event.getRawY();
        // 当前滑动的距离，手指落点与当前位置的差值
        int scrolldy = currentY - moveY;
        Log.i(TAG, "scroll dy: " + scrolldy);
        top += scrolldy;
        currentY = moveY;

        if (top > 0 && top < imageHeight - viewHeight / currentScale) {
            rect.top = top;
            rect.bottom = (int) (top + viewHeight / currentScale);

            invalidate();
        } else if (top < 0) {
            top = 0;
            rect.top = top;
            rect.bottom = (int) (top + viewHeight / currentScale);
        } else if (top >= imageHeight - viewHeight / currentScale) {
            top = (int) (imageHeight - viewHeight / currentScale);
            rect.top = top;
            rect.bottom = (int) (top + viewHeight / currentScale);
        }
    }

//    @Override
//    public void computeScroll() {
//        super.computeScroll();
//        if (!scroller.isFinished() && scroller.computeScrollOffset()) {
//            int currY = scroller.getCurrY();
//            rect.top = currY;
//            rect.bottom = (int) (currY + viewHeight / currentScale);
//
//            invalidate();
//        }
//    }
}
