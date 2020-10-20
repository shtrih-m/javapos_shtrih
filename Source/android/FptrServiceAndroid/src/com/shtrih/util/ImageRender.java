package com.shtrih.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class ImageRender {

    private int width;
    private int height;
    private byte[][] lines;
    private static CompositeLogger logger = CompositeLogger.getLogger(ImageRender.class);

    public ImageRender() {
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public byte[][] getData() {
        return lines;
    }
 
    public void render(String fileName) throws Exception
    {
        Bitmap srcbmp = BitmapFactory.decodeFile(fileName);
        if (srcbmp == null) {
            throw new Exception("Failed to decode file");
        }

        Bitmap bitmap = Bitmap.createBitmap(srcbmp.getWidth(), srcbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        ColorMatrix ma = new ColorMatrix();
        ma.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(ma));
        canvas.drawBitmap(srcbmp, 0, 0, paint);
        int widthInBytes = (bitmap.getWidth() + 7) / 8;
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        lines = new byte[height][widthInBytes];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < widthInBytes; x++) {
                lines[y][x] = 0;
            }
            for (int x = 0; x < width; x++) {
                int pixel = bitmap.getPixel(x, y) & 0xFF;
                if (pixel < 128) {
                    byte b = (byte) (1 << (x % 8));
                    lines[y][x / 8] = (byte) (lines[y][x / 8] + b);
                }
            }
        }
    }
}

