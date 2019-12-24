package com.zgf.bigimage.gesturedetector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class GestureDetectorTestView extends View {
    private static final String TAG = "GestureDetectorTestView";

    private GestureDetector detector;

    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.i(TAG, "=======onSingleTapUp============");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            Log.i(TAG, "========onLongPress===========");
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i(TAG, "=========onScroll========== distanceX: " + distanceX + " distanceY: " + distanceY);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i(TAG, "=========onFling========== velocityX: " + velocityX + " velocityY: " + velocityY);
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
            Log.i(TAG, "=========onShowPress==========");
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.i(TAG, "=========onDown==========");
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i(TAG, "========onDoubleTap===========");
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.i(TAG, "=========onDoubleTapEvent==========");
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i(TAG, "==========onSingleTapConfirmed=========");
            return true;
        }
    };

    public GestureDetectorTestView(Context context) {
        this(context, null);
    }

    public GestureDetectorTestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureDetectorTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        detector = new GestureDetector(getContext(), simpleOnGestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);
    }
}
