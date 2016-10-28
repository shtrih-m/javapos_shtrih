/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.printer.model;

/**
 * @author V.Kravtsov
 */
public interface NCR7167 {
    // ///////////////////////////////////////////////////////////////////
    // Barcode types
    // ///////////////////////////////////////////////////////////////////

    public static final int NCR7167_BARCODE_UPCA = 0;
    public static final int NCR7167_BARCODE_UPCE = 1;
    public static final int NCR7167_BARCODE_EAN13 = 2;
    public static final int NCR7167_BARCODE_EAN8 = 3;
    public static final int NCR7167_BARCODE_CODE39 = 4;
    public static final int NCR7167_BARCODE_ITF = 5;
    public static final int NCR7167_BARCODE_CODABAR = 6;
    public static final int NCR7167_BARCODE_CODE93 = 7;
    public static final int NCR7167_BARCODE_CODE128 = 8;
    public static final int NCR7167_BARCODE_PDF417 = 10;
    public static final int NCR7167_BARCODE_GS1_OMNI = 11;
    public static final int NCR7167_BARCODE_GS1_TRUNC = 12;
    public static final int NCR7167_BARCODE_GS1_LIMIT = 13;
    public static final int NCR7167_BARCODE_GS1_EXP = 14;
    public static final int NCR7167_BARCODE_GS1_STK = 15;
    public static final int NCR7167_BARCODE_GS1_STK_OMNI = 16;
    public static final int NCR7167_BARCODE_GS1_EXP_STK = 17;

    // ///////////////////////////////////////////////////////////////////
    // HRI position constants
    // ///////////////////////////////////////////////////////////////////

    public static final int NCR7167_HRI_NOTPRINTED = 0;
    public static final int NCR7167_HRI_ABOVE = 1;
    public static final int NCR7167_HRI_BELOW = 2;
    public static final int NCR7167_HRI_ABOVE_BELOW = 3;

    // ///////////////////////////////////////////////////////////////////
    // HRI pitch constants
    // ///////////////////////////////////////////////////////////////////

    public static final int NCR7167_HRI_PITCH_STANDARD = 0;
    public static final int NCR7167_HRI_PITCH_COMPRESSED = 1;

}
