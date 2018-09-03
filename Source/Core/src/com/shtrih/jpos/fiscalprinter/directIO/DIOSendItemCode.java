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

public class DIOSendItemCode extends DIOItem {

    public DIOSendItemCode(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        String[] lines = (String[])object;
        String GTIN = lines[0];
        String serial = lines[1];
        GS1Barcode barcode = new GS1Barcode(GTIN, serial);
        getPrinter().check(getPrinter().sendItemCode(barcode));
    }

}
