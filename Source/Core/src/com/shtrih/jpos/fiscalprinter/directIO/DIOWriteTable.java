/*
 * DIOWriteTable.java
 *
 * Created on 23 Октябрь 2009 г., 14:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

/**
 * @author V.Kravtsov
 */

public class DIOWriteTable {

    private final FiscalPrinterImpl service;

    /** Creates a new instance of DIOWriteTable */
    public DIOWriteTable(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 3);
        DIOUtils.checkObjectMinLength((String[]) object, 1);

        int tableNumber = data[0];
        int rowNumber = data[1];
        int fieldNumber = data[2];
        String fieldValue = ((String[]) (object))[0];
        fieldValue = service.decodeText(fieldValue);
        service.printer.check(service.printer.writeTable(tableNumber,
                rowNumber, fieldNumber, fieldValue));
    }
}
