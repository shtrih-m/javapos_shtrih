/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

/**
 * @author V.Kravtsov
 */
public class PrinterFont {

    private final FontNumber number;
    private final int charWidth;
    private final int charHeight;
    private final int paperWidth;

    public PrinterFont(FontNumber number, int charWidth, int charHeight, int paperWidth) {
        this.number = number;
        this.charWidth = charWidth;
        this.charHeight = charHeight;
        this.paperWidth = paperWidth;
    }

    public int getWidthInChars() {
        int result = 0;
        if (charWidth > 0) {
            result = paperWidth / charWidth;
        }
        return result;
    }

    public FontNumber getNumber() {
        return number;
    }

    public int getCharWidth() {
        return charWidth;
    }

    public int getCharHeight() {
        return charHeight;
    }

    public int getPaperWidth() {
        return paperWidth;
    }
}
