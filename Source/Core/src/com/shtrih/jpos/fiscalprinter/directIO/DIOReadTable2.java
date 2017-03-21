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

public class DIOReadTable2 {

    private final FiscalPrinterImpl service;

    /** Creates a new instance of DIOReadTable */
    public DIOReadTable2(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        String[] params = (String[])object;
        DIOUtils.checkObjectMinLength(params, 4);

        int tableNumber = Integer.valueOf(params[0]).intValue();
        int rowNumber = Integer.valueOf(params[1]).intValue();
        int fieldNumber = Integer.valueOf(params[2]).intValue();
        
        String[] fieldValue = new String[1];
        service.printer.check(service.printer.readTable(tableNumber, rowNumber,
                fieldNumber, fieldValue));
        params[3] = service.encodeText(service.encodeText(fieldValue[0]));
    }
}
