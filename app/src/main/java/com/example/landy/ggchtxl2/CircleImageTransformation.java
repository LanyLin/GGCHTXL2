package com.example.landy.ggchtxl2;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

public class CircleImageTransformation implements Transformation {
    private static final String KEY = "circleImageTransformation";
    @Override
    public Bitmap transform(Bitmap bitmap) {
        int mingedge = Math.min(bitmap.getWidth(),bitmap.getHeight());
        int dx = (bitmap.getWidth()-mingedge)/2;
        int dy = (bitmap.getHeight()-mingedge)/2;

        Shader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        matrix.setTranslate(-dx,-dy);
        shader.setLocalMatrix(matrix);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(shader);

        Bitmap output = Bitmap.createBitmap(mingedge,mingedge,bitmap.getConfig());
        Canvas canvas = new Canvas(output);
        canvas.drawOval(new RectF(0,0,mingedge,mingedge),paint);
        bitmap.recycle();
        return output;
    }

    @Override
    public String key() {
        return KEY;
    }
}
