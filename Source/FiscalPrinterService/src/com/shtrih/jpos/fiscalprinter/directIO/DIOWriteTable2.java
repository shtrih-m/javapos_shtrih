/*
 * DIOWriteTable.java
 *
 * Created on 23 ќкт€брь 2009 г., 14:25
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

public class DIOWriteTable2 {

    private final FiscalPrinterImpl service;

    /** Creates a new instance of DIOWriteTable */
    public DIOWriteTable2(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        String[] params = (String[]) object;
        DIOUtils.checkObjectMinLength(params, 1);

        int tableNumber = Integer.valueOf(params[0]).intValue();
        int rowNumber = Integer.valueOf(params[1]).intValue();
        int fieldNumber = Integer.valueOf(params[2]).intValue();
        String fieldValue = params[2];
        fieldValue = service.decodeText(fieldValue);
        service.printer.check(service.printer.writeTable(tableNumber,
                rowNumber, fieldNumber, fieldValue));
    }
}
