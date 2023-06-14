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
import com.shtrih.fiscalprinter.command.ItemCode;

public class DIOSetItemCode extends DIOItem {

    public DIOSetItemCode(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        String[] lines = (String[])object;
        String barcode = lines[0];
        boolean volumeAccounting = false;
        if (lines.length > 1){
            volumeAccounting = lines[1].equals("1");
        }
        ItemCode itemCode = new ItemCode(barcode.getBytes(), volumeAccounting);
        service.addItemCode(itemCode);
    }

}
