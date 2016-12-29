/*
 * DIOReadTable.java
 *
 * Created on 23 Октябрь 2009 г., 14:17
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

public class DIOReadTable {

    private final FiscalPrinterImpl service;

    /** Creates a new instance of DIOReadTable */
    public DIOReadTable(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 3);
        DIOUtils.checkObjectMinLength((String[]) object, 1);

        String[] fieldValue = new String[1];
        int tableNumber = data[0];
        int rowNumber = data[1];
        int fieldNumber = data[2];
        service.printer.check(service.printer.readTable(tableNumber, rowNumber,
                fieldNumber, fieldValue));
        ((String[]) (object))[0] = service.encodeText(fieldValue[0]);
    }
}
