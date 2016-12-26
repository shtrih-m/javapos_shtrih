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
public class DIOReadTrailerLine {
    private FiscalPrinterImpl service;

    /**
     * Creates a new instance of DIOPrintBarcode
     */
    public DIOReadTrailerLine(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        String[] lines = (String[]) object;
        DIOUtils.checkDataMinLength(data, 1);
        DIOUtils.checkObjectMinLength(lines, 1);
        lines[0] = service.getTrailerLine(data[0] - 1);
    }

}
