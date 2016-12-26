/*
 * SelectBarcodeTextPitch.java
 *
 * Created on 28 March 2008, 11:48
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
 * Select Pitch for HRI Characters ASCII: GSfn Hexadecimal: 1D66n Decimal: 29
 * 102 n Value of n: Pitch 0 = Standard Pitch at 15.2 CPI on 1 = Compressed
 * Pitch at 19 CPI on Default: 0 (Standard Pitch at 15.2 CPI)
 **********************************************************************/

public class SelectBarcodeTextPitch extends NCR7167Command {
    private final int n;

    /**
     * Creates a new instance of SelectBarcodeTextPitch
     */
    public SelectBarcodeTextPitch(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public final NCR7167Command newInstance(NCR7167Command command) {
        SelectBarcodeTextPitch src = (SelectBarcodeTextPitch) command;
        return new SelectBarcodeTextPitch(src.getN());
    }

    public final String getText() {
        return "Select Pitch for HRI Characters";
    }

    public void execute(NCR7167Printer printer) throws Exception {
        printer.barcodeTextPitch = n;
    }
}
