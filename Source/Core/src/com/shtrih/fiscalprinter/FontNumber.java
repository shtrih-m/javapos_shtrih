package com.shtrih.fiscalprinter;

import com.shtrih.fiscalprinter.command.PrinterConst;

public class FontNumber {

    private final int value;

    public FontNumber(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FontNumber that = (FontNumber) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return value;
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
