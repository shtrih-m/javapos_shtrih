/*
 * SelectBarcodeWidth.java
 *
 * Created on 28 March 2008, 11:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.printer.ncr7167;

/**
 *
 * @author V.Kravtsov
 */

/*********************************************************************
 * Select Bar Code Width ASCII : GSwn Hexadecimal : 1D77n Decimal : 29 119 n
 * Value of n: 1, 2, 3, 4, 5 Default: 3 for receipt; 2 for slip
 **********************************************************************/

public final class SelectBarcodeWidth extends NCR7167Command {
    private final int n;

    /**
     * Creates a new instance of SelectBarcodeWidth
     */
    public SelectBarcodeWidth(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public final NCR7167Command newInstance(NCR7167Command command) {
        SelectBarcodeWidth src = (SelectBarcodeWidth) command;
        return new SelectBarcodeWidth(src.getN());
    }

    public final String getText() {
        return "Select Bar Code Width";
    }

    public void execute(NCR7167Printer printer) throws Exception {
        printer.barWidth = n;
    }
}
