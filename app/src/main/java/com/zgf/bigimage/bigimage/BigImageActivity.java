package com.zgf.bigimage.bigimage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;

import com.zgf.bigimage.R;

import java.io.IOException;
import java.io.InputStream;

public class BigImageActivity extends AppCompatActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, BigImageActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);

        initView();
    }

    private void initView() {
//        BigViewCopy bigView = findViewById(R.id.big_image_copy);
        BigViewCopyCopy bigView = findViewById(R.id.big_image_copy);
//        MyBigView bigView = findViewById(R.id.big_image);
        AssetManager assets = getAssets();
        try {
            InputStream inputStream = assets.open("qmsht.png"); // 注意assets文件的位置
            bigView.setImageInputStream(inputStream);
//            bigView.setImage(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
