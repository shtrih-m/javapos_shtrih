/*
 * DIOOpenDrawer.java
 *
 * Created on 4 Март 2010 г., 14:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOOpenDrawer {

    private FiscalPrinterImpl service;

    /**
     * Creates a new instance of DIOPrintBarcode
     */
    public DIOOpenDrawer(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataNotNull(data);
        int drawerNumber = 0;
        if (data.length > 0) {
            drawerNumber = data[0];
        }
        service.printer.openCashDrawer(drawerNumber);
    }

}
