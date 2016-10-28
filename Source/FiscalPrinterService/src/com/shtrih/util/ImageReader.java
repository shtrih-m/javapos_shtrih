package com.shtrih.util;

import com.shtrih.jpos.fiscalprinter.PrinterImage;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.util.BitSet;
import java.io.FileInputStream;
import java.security.MessageDigest;

import net.sf.image4j.codec.bmp.BMPDecoder;
import com.shtrih.util.CompositeLogger;

public class ImageReader {

    private final String digest;
    private final byte[][] data;
    private final BufferedImage image;
    private static CompositeLogger logger = CompositeLogger.getLogger(ImageReader.class);

    public ImageReader(String fileName) throws Exception {
        logger.debug("ImageReader(" + fileName + ")");
        File file = new File(fileName);
        digest = getFileDigest(file);
        image = BMPDecoder.read(file);
        if (image == null) {
            throw new Exception("Failed to read image, " + fileName);
        }
        data = convertImage(image);
    }

    public String getDigest() {
        return digest;
    }

    public String getFileDigest(File file) throws Exception {
        byte[] buffer = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(buffer);
        fis.close();

        MessageDigest md = MessageDigest.getInstance("md5");
        md.update(buffer);
        return Hex.toHex2(md.digest());
    }

    private byte[][] convertImage(BufferedImage image) {
        image = indexToDirectColorModel(image);
        int width = image.getWidth();
        int height = image.getHeight();
        byte[][] data = new byte[height][];
        int[] pixels = new int[width];
        Raster raster = image.getData();
        for (int i = 0; i < height; i++) {
            raster.getPixels(0, i, image.getWidth(), 1, pixels);
            data[i] = pixelsToBits(pixels);
            data[i] = BitUtils.swap(data[i]);
        }
        return data;
    }

    public int getWidth() {
        if (image == null) {
            return 0;
        }
        return image.getWidth();
    }

    public int getHeight() {
        if (image == null) {
            return 0;
        }
        return image.getHeight();
    }

    private byte[] pixelsToBits(int[] pixels) {
        byte[] bytes = new byte[(pixels.length + 7) / 8];
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] == 0) {
                bytes[bytes.length - i / 8 - 1] |= 1 << (i % 8);
            }
        }
        return bytes;
    }

    public byte[][] getData() {
        return data;
    }

    public static BufferedImage indexToDirectColorModel(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

        Graphics2D g2 = result.createGraphics();
        g2.drawImage(image, null, null);
        return result;
    }

}
