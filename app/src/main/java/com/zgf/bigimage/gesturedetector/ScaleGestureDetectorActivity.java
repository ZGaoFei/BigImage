package com.zgf.bigimage.gesturedetector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zgf.bigimage.R;

public class ScaleGestureDetectorActivity extends AppCompatActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, ScaleGestureDetectorActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_gesture_detector);
    }
}
