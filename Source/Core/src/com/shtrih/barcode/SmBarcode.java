package com.shtrih.barcode;

import java.util.Arrays;

/**
 * @author V.Kravtsov
 */
public class SmBarcode {

    private byte[][] data;
    private final int width;
    private final int height;
    private int firstLine = 1;
    private int hScale = 1;
    private int vScale = 1;

    public SmBarcode(byte[][] data, int width, int height) {
        this.data = data;
        this.width = width;
        this.height = height;
    }

    public void centerBarcode(int graphicsWidth) {
        if (graphicsWidth < width) {
            return;
        }

        int offset = ((graphicsWidth - width * hScale) / hScale) / 2;

        if (offset <= 0)
            return;

        for (int i = 0; i < height; i++) {
            byte[] b = new byte[(graphicsWidth + 7) / 8];
            Arrays.fill(b, (byte) 0);
            for (int j = 0; j < width; j++) {
                byte bit = (byte) Math.pow(2, j % 8);
                if ((data[i][getByteNumberForBit(j)] & bit) == bit) {
                    byte bitMapped = (byte) Math.pow(2, (offset + j) % 8);
                    int byteNumber = getByteNumberForBit(offset + j);
                    b[byteNumber] |= bitMapped;
                }
            }
            data[i] = b;
        }
    }

    private int getByteNumberForBit(int bitNumber){
        return  bitNumber / 8;
    }

    public int getWidth() {
        return width;
    }

    public int getWidthInBytes() {
        return (width + 7) / 8;
    }

    public int getHeight() {
        return height;
    }

    public byte[] getRow(int index) {
        return data[index];
    }

    public int getFirstLine() {
        return firstLine;
    }

    public int getHScale() {
        return hScale;
    }

    public int getVScale() {
        return vScale;
    }

    public void setFirstLine(int firstLine) {
        this.firstLine = firstLine;
    }

    public void setHScale(int hScale) {
        this.hScale = hScale;
    }

    public void setVScale(int vScale) {
        this.vScale = vScale;
    }

}
