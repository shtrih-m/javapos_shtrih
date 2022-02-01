package com.shtrih.util;

import com.shtrih.jpos.fiscalprinter.PrinterImage;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.util.BitSet;
import java.io.FileInputStream;
import java.security.MessageDigest;

import com.shtrih.util.CompositeLogger;
import java.awt.Color;
import javax.imageio.ImageIO;

public class ImageRender {

    int width = 0;
    int height = 0;
    private byte[][] data;
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
        return data;
    }

    public void render(String fileName, int maxWidth, int maxHeight) throws Exception {
        logger.debug("render");

        File file = new File(fileName);
        BufferedImage image = ImageIO.read(file);
        if (image == null) {
            throw new Exception("Failed to read image");
        }
        width = image.getWidth();
        height = image.getHeight();
        if (width > maxWidth) {
            width = maxWidth;
        }
        if (height > maxHeight){
            height = maxHeight;
        }
        double scaleX = ((double) width) / (double)image.getWidth();
        double scaleY = ((double) height) / (double)image.getHeight();
        double scale = Math.min(scaleX, scaleY);
        width = (int) (scale * image.getWidth());
        height = (int) (scale * image.getHeight());
        
        BufferedImage img = new BufferedImage(width, height,
                BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);        
        
        try {
            g2.drawImage(image, 0, 0, width, height, Color.WHITE, null);
        } finally {
            g2.dispose();
        }

        image = img;
        width = image.getWidth();
        height = image.getHeight();
        data = new byte[height][];
        int[] pixels = new int[width];
        Raster raster = image.getData();
        for (int i = 0; i < height; i++) {
            raster.getPixels(0, i, width, 1, pixels);
            data[i] = pixelsToBits(pixels);
            data[i] = BitUtils.swap(data[i]);
        }
    }

    public static byte[] pixelsToBits(int[] pixels) {
        byte[] bytes = new byte[(pixels.length + 7) / 8];
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] == 0) {
                bytes[bytes.length - i / 8 - 1] |= 1 << (i % 8);
            }
        }
        return bytes;
    }
}
