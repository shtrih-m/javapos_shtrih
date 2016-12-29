/*
 * DIOReadPaymentName.java
 *
 * Created on 23 Октябрь 2009 г., 14:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOReadPaymentName {
    private final FiscalPrinterImpl service;

    /** Creates a new instance of DIOReadPaymentName */
    public DIOReadPaymentName(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);
        DIOUtils.checkObjectMinLength((String[]) object, 1);

        int rowNumber = data[0];
        String[] fieldValue = new String[1];
        service.printer.check(service.printer.readTable(
                PrinterConst.SMFP_TABLE_PAYTYPE, rowNumber, 1, fieldValue));
        ((String[]) (object))[0] = service.encodeText(fieldValue[0]);
    }
}
