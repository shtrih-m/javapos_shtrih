/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOWaitPrint extends DIOItem {

    public DIOWaitPrint(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        getPrinter().waitForPrinting();
    }
}
