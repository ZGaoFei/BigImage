package com.zgf.bigimage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zgf.bigimage.bigimage.BigImageActivity;
import com.zgf.bigimage.bitmapfactory.BitmapFactoryActivity;
import com.zgf.bigimage.gesturedetector.GestureDetectorActivity;
import com.zgf.bigimage.gesturedetector.ScaleGestureDetectorActivity;
import com.zgf.bigimage.matrix.MatrixActivity;

public class MainActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        initView();
    }

    private void initView() {
        findViewById(R.id.tv_bitmap_factory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapFactoryActivity.start(context);
            }
        });

        findViewById(R.id.tv_gesture_detector).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestureDetectorActivity.start(context);
            }
        });

        findViewById(R.id.tv_scale_gesture_detector).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScaleGestureDetectorActivity.start(context);
            }
        });

        findViewById(R.id.tv_matrix).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatrixActivity.start(context);
            }
        });

        findViewById(R.id.tv_big_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigImageActivity.start(context);
            }
        });
    }

    private void toastShow(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
