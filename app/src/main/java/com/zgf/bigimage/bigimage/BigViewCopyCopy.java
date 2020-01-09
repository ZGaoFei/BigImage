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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class BigViewCopyCopy extends View implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        ScaleGestureDetector.OnScaleGestureListener {
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
    // 放大倍数
    private int multiple = 2;

    private BitmapFactory.Options options;
    private BitmapRegionDecoder decoder;
    private Bitmap bitmap;
    // 绘制的区域
    private Rect rect;
    // 缩放
    private Matrix matrix;
    // 滑动器
    private Scroller scroller;

    private GestureDetector detector;
    private ScaleGestureDetector scaleGestureDetector;

    public BigViewCopyCopy(Context context) {
        this(context, null);
    }

    public BigViewCopyCopy(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigViewCopyCopy(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        options = new BitmapFactory.Options();

        rect = new Rect();
        matrix = new Matrix();
        scroller = new Scroller(getContext());

        detector = new GestureDetector(getContext(), this);
        scaleGestureDetector = new ScaleGestureDetector(getContext(), this);
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
        rect.left = 0;
        rect.top = 0;
        rect.right = (int) viewWidth;
        rect.bottom = (int) viewHeight;

        scale = viewWidth / imageWidth;
        Log.i(TAG, "scale: " + scale + " view width: " + viewWidth + " view height: " + viewHeight);
        currentScale = scale;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);

        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (!scroller.isFinished()) {
            scroller.forceFinished(true);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        rect.offset((int) distanceX, (int) distanceY);
        handlerBorder();
        invalidate();

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        scroller.fling(rect.left, rect.top, -(int) velocityX, -(int) velocityY, 0, (int) imageWidth, 0, (int) imageHeight);
        return false;
    }

    private void handlerBorder() {
        if (rect.left < 0) {
            rect.left = 0;
            rect.right = (int) (viewWidth / currentScale);
        }
        if (rect.right > imageWidth) {
            rect.right = (int) imageWidth;
            rect.left = (int) (imageWidth - viewWidth / currentScale);
        }
        if (rect.top < 0) {
            rect.top = 0;
            rect.bottom = (int) (viewHeight / currentScale);
        }
        if (rect.bottom > imageHeight) {
            rect.bottom = (int) imageHeight;
            rect.top = (int) (imageHeight - viewHeight / currentScale);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!scroller.isFinished() && scroller.computeScrollOffset()) {
            if (rect.top + viewHeight / currentScale < imageHeight) {
                rect.top = scroller.getCurrY();
                rect.bottom = (int) (rect.top + viewHeight / currentScale);
            }
            if (rect.bottom > imageHeight) {
                rect.top = (int) (imageHeight - viewHeight / currentScale);
                rect.bottom = (int) imageHeight;
            }

            invalidate();
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (currentScale > scale) {
            currentScale = scale;
        } else {
            currentScale = scale * multiple;
        }
        rect.right = (int) (rect.left + viewWidth / currentScale);
        rect.bottom = (int) (rect.top + viewHeight / currentScale);
        handlerBorder();
        invalidate();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();
        currentScale *= scaleFactor;
        if (currentScale > scale * multiple) {
            currentScale = scale * multiple;
        } else if (currentScale <= scale) {
            currentScale = scale;
        }
        rect.right = (int) (rect.left + viewWidth / currentScale);
        rect.bottom = (int) (rect.top + viewHeight / currentScale);
        invalidate();
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
}
