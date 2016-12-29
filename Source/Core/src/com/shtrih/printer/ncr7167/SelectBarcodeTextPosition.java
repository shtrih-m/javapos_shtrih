/*
 * SelectBarcodeTextPosition.java
 *
 * Created on 28 March 2008, 11:46
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
 * Select Printing Position for HRI Characters ASCII: GSHn Hexadecimal: 1D48n
 * Decimal: 29 72 n Value of n: Printing position 0 = Not printed 1 = Above the
 * bar code 2 = Belowthe bar code 3 = Both above and below the bar code Default:
 * 0 (Not printed)
 **********************************************************************/

public final class SelectBarcodeTextPosition extends NCR7167Command {
    private final int n;

    /**
     * Creates a new instance of SelectBarcodeTextPosition
     */
    public SelectBarcodeTextPosition(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public final NCR7167Command newInstance(NCR7167Command command) {
        SelectBarcodeTextPosition src = (SelectBarcodeTextPosition) command;
        return new SelectBarcodeTextPosition(src.getN());
    }

    public final String getText() {
        return "Select Printing Position for HRI Characters";
    }

    public void execute(NCR7167Printer printer) throws Exception {
        printer.barcodeTextPosition = n;
    }
}
