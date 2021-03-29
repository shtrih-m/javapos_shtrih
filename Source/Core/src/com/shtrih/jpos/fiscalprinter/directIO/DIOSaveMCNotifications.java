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
import com.shtrih.fiscalprinter.command.*;
import com.shtrih.fiscalprinter.GS1Barcode;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOSaveMCNotifications extends DIOItem {

    public DIOSaveMCNotifications(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        String[] lines = (String[])object;
        DIOUtils.checkObjectMinLength(lines, 1);
        getService().saveMCNotifications(lines[0]);
    }

}
