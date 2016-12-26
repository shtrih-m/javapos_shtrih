/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.barcode;

import java.util.Arrays;
import org.apache.log4j.Logger;

/**
 *
 * @author V.Kravtsov
 */
public class SmBarcode {

    private final byte[][] data;
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
        int offset = (graphicsWidth - width * hScale) / (16 * hScale);
        for (int i = 0; i < height; i++) {
            byte[] b = new byte[offset + data[i].length];
            Arrays.fill(b, (byte) 0);
            for (int j = 0; j < data[i].length; j++) {
                b[offset + j] = data[i][j];
            }
            data[i] = b;
        }
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
