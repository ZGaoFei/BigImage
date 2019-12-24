package com.zgf.bigimage.bitmapfactory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.print.PrinterId;
import android.widget.ImageView;

import com.zgf.bigimage.R;

public class BitmapFactoryActivity extends AppCompatActivity {

    private ImageView imageView;
    private ImageView imageView2;

    public static void start(Context context) {
        context.startActivity(new Intent(context, BitmapFactoryActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_factory);

        imageView = findViewById(R.id.image_view);
        imageView2 = findViewById(R.id.image_view2);

//        loadBitmap();

        toRoundCorner();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.kongque);
        Bitmap roundCorner = toRoundCorner(bitmap, 25);
        imageView.setImageBitmap(roundCorner);
    }

    private void loadBitmap() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.kongque);
        imageView.setImageBitmap(bitmap);
    }

    private void toRoundCorner() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.kongque).copy(Bitmap.Config.ARGB_8888, true);
        RoundedDrawable drawable = new RoundedDrawable(bitmap, 25, 0);
        imageView2.setImageDrawable(drawable);
    }

    public Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap roundCornerBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundCornerBitmap);
        int color = 0xff424242;// int color = 0xff424242;
        Paint paint = new Paint();
        paint.setColor(color);
        // 防止锯齿
        paint.setAntiAlias(true);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float roundPx = pixels;
        // 相当于清屏
        canvas.drawARGB(0, 0, 0, 0);
        // 先画了一个带圆角的矩形
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 再把原来的bitmap画到现在的bitmap！！！注意这个理解
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return roundCornerBitmap;
    }

    /**
     * 抄自imageloader设置圆角方法，必须设置图片的宽高
     */
    public static class RoundedDrawable extends Drawable {
        protected final float cornerRadius;
        protected final int margin;
        protected final RectF mRect = new RectF();
        protected final RectF mBitmapRect;
        protected final BitmapShader bitmapShader;
        protected final Paint paint;

        public RoundedDrawable(Bitmap bitmap, int cornerRadius, int margin) {
            this.cornerRadius = (float) cornerRadius;
            this.margin = margin;
            this.bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            this.mBitmapRect = new RectF((float) margin, (float) margin, (float) (bitmap.getWidth() - margin), (float) (bitmap.getHeight() - margin));
            this.paint = new Paint();
            this.paint.setAntiAlias(true);
            this.paint.setShader(this.bitmapShader);
        }

        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            this.mRect.set((float) this.margin, (float) this.margin, (float) (bounds.width() - this.margin), (float) (bounds.height() - this.margin));
            Matrix shaderMatrix = new Matrix();
            shaderMatrix.setRectToRect(this.mBitmapRect, this.mRect, Matrix.ScaleToFit.FILL);
            this.bitmapShader.setLocalMatrix(shaderMatrix);
        }

        public void draw(Canvas canvas) {
            canvas.drawRoundRect(this.mRect, this.cornerRadius, this.cornerRadius, this.paint);
        }

        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        public void setAlpha(int alpha) {
            this.paint.setAlpha(alpha);
        }

        public void setColorFilter(ColorFilter cf) {
            this.paint.setColorFilter(cf);
        }
    }
}
