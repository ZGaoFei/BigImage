package com.zgf.bigimage.matrix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zgf.bigimage.R;

public class MatrixActivity extends AppCompatActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, MatrixActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);
    }
}
