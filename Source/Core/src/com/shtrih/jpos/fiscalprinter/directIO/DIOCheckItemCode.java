/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.jpos.DIOUtils;
import com.shtrih.fiscalprinter.GS1Barcode;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOCheckItemCode extends DIOItem {

    public DIOCheckItemCode(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        String[] lines = (String[])object;
        String barcode = lines[0];
        long quantity = 1000;
        boolean isSale = true;
        if (lines.length > 3)
        {
            isSale = !(lines[1].equalsIgnoreCase("0"));
            quantity = Long.parseLong(lines[2]);
        }
        getPrinter().checkItemCode(barcode, isSale, quantity);
    }

}
