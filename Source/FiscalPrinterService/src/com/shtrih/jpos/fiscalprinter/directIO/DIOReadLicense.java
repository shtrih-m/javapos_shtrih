/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

/**
 * @author V.Kravtsov
 */
public class DIOReadLicense {

    private FiscalPrinterImpl service;

    public DIOReadLicense(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        String[] out = (String[]) object;
        DIOUtils.checkObjectMinLength(out, 1);
        service.getPrinter().check(service.getPrinter().readLicense(out));
    }

}
