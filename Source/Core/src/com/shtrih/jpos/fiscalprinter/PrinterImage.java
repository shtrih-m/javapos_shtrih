/*
 * PrinterImage.java
 *
 * Created on March 21 2008, 6:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
// java

import java.io.File;
import java.util.Arrays;
import java.io.FileInputStream;
import java.security.MessageDigest;

import jpos.config.JposEntry;

import com.shtrih.util.Hex;
import com.shtrih.util.BitUtils;
import com.shtrih.util.Localizer;
import com.shtrih.util.ImageRender;
import com.shtrih.util.ImageRender;
import com.shtrih.util.CompositeLogger;

public class PrinterImage {

    private String fileName = "";
    private String digest = "";
    private int width = 0;
    private int height = 0;
    private int startPos = 0;
    private boolean isLoaded = false;
    public byte[][] lines = new byte[0][0]; // image data
    private static CompositeLogger logger = CompositeLogger.getLogger(PrinterImage.class);

    /**
     * Creates a new instance of PrinterImage
     */
    public PrinterImage(String fileName) {
        this.fileName = fileName;
        height = 0;
        startPos = 0;
        isLoaded = false;
    }

    public PrinterImage() {
        this.fileName = "";
        height = 0;
        startPos = 0;
        isLoaded = false;
    }

    public void assign(PrinterImage src) {
        fileName = src.fileName;
        digest = src.digest;
        width = src.width;
        height = src.height;
        startPos = src.startPos;
        isLoaded = src.isLoaded;
        lines = src.lines;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String value) {
        digest = value;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String value) {
        this.fileName = value;
    }

    public int getEndPos() {
        return startPos + height - 1;
    }

    public int getHeight() {
        return height;
    }

    public int getStartPos() {
        return startPos;
    }

    public boolean getIsLoaded() {
        return isLoaded;
    }

    public int getWidth() {
        return width;
    }

    public void setIsLoaded(boolean value) {
        this.isLoaded = value;
    }

    public void setHeight(int value) {
        this.height = value;
    }

    public void setStartPos(int value) {
        this.startPos = value;
    }

    // read image from file
    public void render(int maxWidth, boolean centerImage) throws Exception 
    {
        logger.debug("render(" + maxWidth + ", " + centerImage + ")");
        ImageRender render = new ImageRender();
        render.render(fileName);
        lines = render.getData();
        height = render.getHeight();
        width = render.getWidth();
        if (centerImage) {
            centerImage(maxWidth);
        }
        digest = getFileDigest(new File(fileName));
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

    private void centerImage(int maxWidth) {
        int offset = (maxWidth - width) / 16;
        for (int i = 0; i < height; i++) {
            byte[] b = new byte[offset + lines[i].length];
            Arrays.fill(b, (byte) 0);
            for (int j = 0; j < lines[i].length; j++) {
                b[offset + j] = lines[i][j];
            }
            lines[i] = b;
        }
    }
}