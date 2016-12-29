package com.shtrih.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class ImageReader {
    private static CompositeLogger logger = CompositeLogger.getLogger(ImageReader.class);

    private final String digest;
    private final byte[][] data;

    public ImageReader(String fileName) throws Exception {
        logger.debug("ImageReader(" + fileName + ")");
        File file = new File(fileName);
        digest = getFileDigest(file);

        data = convertImage(fileName);
    }

    public String getDigest() {
        return digest;
    }

    private String getFileDigest(File file) throws Exception {
        byte[] buffer = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(buffer);
        fis.close();

        MessageDigest md = MessageDigest.getInstance("md5");
        md.update(buffer);
        return Hex.toHex2(md.digest());
    }

    private byte[][] convertImage(String filePath) throws Exception {
        Bitmap srcbmp = BitmapFactory.decodeFile(filePath);
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

        // check max image width
//        if (bitmap.getWidth() > getModel().getMaxGraphicsWidth()) {
//            throw new Exception(Localizer.getString(Localizer.InvalidImageWidth) + ", "
//                    + String.valueOf(bitmap.getWidth()) + " > " + String.valueOf(getModel().getMaxGraphicsWidth()));
//        }
        // check max image height
//        int imageHeight = image.getFirstLine() + bitmap.getHeight() - 1;
//        if (imageHeight > getModel().getMaxGraphicsHeight()) {
//            throw new Exception(Localizer.getString(Localizer.InvalidImageHeight) + ", " + String.valueOf(imageHeight)
//                    + " > " + String.valueOf(getModel().getMaxGraphicsHeight()));
//        }

        int widthInBytes = (bitmap.getWidth() + 7) / 8;
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        byte[][] lines = new byte[height][widthInBytes];
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

        return lines;
    }

    private int height;
    private int width;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public byte[][] getData() {
        return data;
    }
}
