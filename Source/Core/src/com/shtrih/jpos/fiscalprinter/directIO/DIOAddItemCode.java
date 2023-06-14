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
import com.shtrih.fiscalprinter.command.ItemCode;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOAddItemCode extends DIOItem {

    public DIOAddItemCode(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        Object[] params = (Object[])object;
        byte[] barcodeData = (byte[]) params[0];
        boolean volumeAccounting = false;
        if (params.length > 1)
        {
            volumeAccounting = (Boolean)params[1];
        }
        service.addItemCode(new ItemCode(barcodeData, volumeAccounting));
    }

}
