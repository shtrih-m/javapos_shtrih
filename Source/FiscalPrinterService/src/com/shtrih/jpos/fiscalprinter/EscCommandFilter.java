/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 * @author V.Kravtsov
 */
public class EscCommandFilter {

    public static final String STYLE_BOLD = "\u001B|bC";
    public static final String STYLE_UNDERLINE = "\u001B|uC";
    public static final String STYLE_ITALIC = "\u001B|iC";
    public static final String STYLE_NORMAL = "\u001B|1C";
    public static final String STYLE_DBLWIDE = "\u001B|2C";
    public static final String STYLE_DBLHIGH = "\u001B|3C";
    public static final String STYLE_DBLHIGHWIDE = "\u001B|4C";

    public void print(String line) {
        boolean isNormal = line.indexOf(STYLE_NORMAL) != -1;
        boolean isItalic = line.indexOf(STYLE_ITALIC) != -1;
        boolean isDoubleWide = line.indexOf(STYLE_DBLWIDE) != -1;
        boolean isDoubleHeight = line.indexOf(STYLE_DBLHIGHWIDE) != -1;

    }

}
