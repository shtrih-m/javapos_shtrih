/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.util.MethodParameter;

public class FontNumber {

    private final int value;

    public FontNumber(int value) throws Exception {
        this.value = value;
    }

    public boolean isEqual(FontNumber src) {
        return src.getValue() == getValue();
    }

    public static boolean isValidValue(int value) {
        return true;
    }

    public int getValue() {
        return value;
    }

    public static FontNumber getNormalFont() throws Exception {
        return new FontNumber(PrinterConst.FONT_NUMBER_NORMAL);
    }

    public static FontNumber getDoubleFont() throws Exception {
        return new FontNumber(PrinterConst.FONT_NUMBER_DOUBLE);
    }

    public static FontNumber getSmallBoldFont() throws Exception {
        return new FontNumber(PrinterConst.FONT_NUMBER_BOLD_SMALL);
    }

    public static FontNumber getBoldFont() throws Exception {
        return new FontNumber(PrinterConst.FONT_NUMBER_BOLD);
    }

    public static FontNumber getItalicFont() throws Exception {
        return new FontNumber(PrinterConst.FONT_NUMBER_ITALIC);
    }

    public static FontNumber getTimesFont() throws Exception {
        return new FontNumber(PrinterConst.FONT_NUMBER_TIMES);
    }
}
