/*
 * DIOWritePaymentName.java
 *
 * Created on 23 Октябрь 2009 г., 14:09
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

public class DIOWritePaymentName {
    private final FiscalPrinterImpl service;

    /** Creates a new instance of DIOWritePaymentName */
    public DIOWritePaymentName(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);
        DIOUtils.checkObjectMinLength((String[]) object, 1);

        int rowNumber = data[0];
        String fieldValue = ((String[]) (object))[0];
        fieldValue = service.decodeText(fieldValue);
        service.printer.check(service.printer.writeTable(
                PrinterConst.SMFP_TABLE_PAYTYPE, rowNumber, 1, fieldValue));
    }
}
