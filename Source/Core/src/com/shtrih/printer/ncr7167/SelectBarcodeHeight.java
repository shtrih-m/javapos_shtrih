/*
 * SelectBarcodeHeight.java
 *
 * Created on 28 March 2008, 11:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.printer.ncr7167;

/**
 *
 * @author V.Kravtsov
 */

/****************************************************************************
 * Select Bar Code Height ASCII : GShn Hexadecimal : 1D 68 n Decimal : 29 104 n
 * Value of n: Number of dots Range of n: 1 - 255 Default: 162
 **************************************************************************/

public class SelectBarcodeHeight extends NCR7167Command {
    private final int n;

    /**
     * Creates a new instance of SelectBarcodeHeight
     */
    public SelectBarcodeHeight(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public final NCR7167Command newInstance(NCR7167Command command) {
        SelectBarcodeHeight src = (SelectBarcodeHeight) command;
        return new SelectBarcodeHeight(src.getN());
    }

    public final String getText() {
        return "Select Bar Code Height";
    }

    public void execute(NCR7167Printer printer) throws Exception {
        printer.barcodeHeight = n;
    }
}
