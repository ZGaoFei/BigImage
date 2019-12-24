package com.zgf.bigimage.gesturedetector;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

public class ScaleGestureDetectorTestView extends AppCompatImageView implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = "ScaleGestureDetector";
    private static final int MAX_SCALE_TIME = 4;

    private ScaleGestureDetector detector;

    private Matrix matrix;
    private boolean isFirstLayout;
    private float baseScale;

    private ScaleGestureDetector.SimpleOnScaleGestureListener listener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.i(TAG, "=======onScale============");
            scale(detector);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            Log.i(TAG, "=======onScaleBegin============");
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
            Log.i(TAG, "=======onScaleEnd============");
        }
    };

    public ScaleGestureDetectorTestView(Context context) {
        this(context, null);
    }

    public ScaleGestureDetectorTestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleGestureDetectorTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        detector = new ScaleGestureDetector(getContext(), listener);

        matrix = new Matrix();

        setScaleType(ScaleType.MATRIX);

        isFirstLayout = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    private void scale(ScaleGestureDetector detector) {
        if (getDrawable() == null || matrix == null) {
            return;
        }

        float scaleFactor = detector.getScaleFactor();
        float scale = getScale();

        if ((scale < baseScale * MAX_SCALE_TIME && scaleFactor > 1.0f) || (scale > baseScale && scaleFactor < 1.0f)) {
            if (scale * scaleFactor < baseScale) {
                scaleFactor = baseScale / scale;
            }
            if (scale * scaleFactor > baseScale * MAX_SCALE_TIME) {
                scaleFactor = baseScale * MAX_SCALE_TIME / scale;
            }
            matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            borderAndCenterCheck();
            setImageMatrix(matrix);
        }
    }

    private float getScale() {
        float[] values = new float[9];
        matrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    /**
     * 获得图片放大缩小以后的宽和高
     */
    private RectF getMatrixRectF() {
        RectF rectF = new RectF();
        Drawable drawable = getDrawable();
        if (drawable != null) {
            rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    /**
     * 图片在缩放时进行边界控制
     */
    private void borderAndCenterCheck() {
        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        // 缩放时进行边界检测，防止出现白边
        if (rect.width() >= viewWidth) {
            if (rect.left > 0) {
                deltaX = -rect.left;
            }
            if (rect.right < viewWidth) {
                deltaX = viewWidth - rect.right;
            }
        }
        if (rect.height() >= viewHeight) {
            if (rect.top > 0) {
                deltaY = -rect.top;
            }
            if (rect.bottom < viewHeight) {
                deltaY = viewHeight - rect.bottom;
            }
        }
        // 如果宽度或者高度小于控件的宽或者高；则让其居中
        if (rect.width() < viewWidth) {
            deltaX = viewWidth / 2f - rect.right + rect.width() / 2f;

        }
        if (rect.height() < viewHeight) {
            deltaY = viewHeight / 2f - rect.bottom + rect.height() / 2f;
        }
        matrix.postTranslate(deltaX, deltaY);
    }

    @Override
    public void onGlobalLayout() {
        if (isFirstLayout) {
            isFirstLayout = false;
            // 获取控件的宽度和高度
            int viewWidth = getWidth();
            int viewHeight = getHeight();
            // 获取到ImageView对应图片的宽度和高度
            Drawable drawable = getDrawable();
            if (null == drawable) {
                return;
            }
            // 图片固有宽度
            int drawableWidth = drawable.getIntrinsicWidth();
            // 图片固有高度
            int drawableHeight = drawable.getIntrinsicHeight();
            // 接下来对图片做初始的缩放处理，保证图片能看全
            if (drawableWidth >= viewWidth && drawableHeight >= viewHeight) {
                // 图片宽度和高度都大于控件(缩小)
                baseScale = Math.min(viewWidth * 1.0f / drawableWidth, viewHeight * 1.0f / drawableHeight);
            } else if (drawableWidth > viewWidth && drawableHeight < viewHeight) {
                // 图片宽度大于控件,高度小于控件(缩小)
                baseScale = viewWidth * 1.0f / drawableWidth;
            } else if (drawableWidth < viewWidth && drawableHeight > viewHeight) {
                // 图片宽度小于控件,高度大于控件(缩小)
                baseScale = viewHeight * 1.0f / drawableHeight;
            } else {
                // 图片宽度小于控件,高度小于控件(放大)
                baseScale = Math.min(viewWidth * 1.0f / drawableWidth, viewHeight * 1.0f / drawableHeight);
            }
            // 将图片移动到手机屏幕的中间位置
            float distanceX = viewWidth / 2 - drawableWidth / 2;
            float distanceY = viewHeight / 2 - drawableHeight / 2;
            matrix.postTranslate(distanceX, distanceY);
            matrix.postScale(baseScale, baseScale, viewWidth / 2, viewHeight / 2);
            setImageMatrix(matrix);
        }
    }
}
