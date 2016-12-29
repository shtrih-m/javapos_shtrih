/*
 * DIOReadSerial.java
 *
 * Created on 4 Март 2010 г., 13:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.command.LongPrinterStatus;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOReadSerial {

    private final FiscalPrinterImpl service;

    /** Creates a new instance of DIOReadSerial */
    public DIOReadSerial(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkObjectMinLength((String[]) object, 1);

        String[] serial = (String[]) object;
        LongPrinterStatus status = service.readLongStatus();
        serial[0] = status.getSerial();
    }
}
