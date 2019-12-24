package com.zgf.bigimage.gesturedetector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;

import com.zgf.bigimage.R;

public class GestureDetectorActivity extends AppCompatActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, GestureDetectorActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_detector);

        initView();
    }

    private void initView() {
        GestureDetector detector;
    }
}
