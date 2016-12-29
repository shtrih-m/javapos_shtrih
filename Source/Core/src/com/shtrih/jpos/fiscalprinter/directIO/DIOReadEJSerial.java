/*
 * DIOReadEJSerial.java
 *
 * Created on 4 Март 2010 г., 13:41
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
import com.shtrih.fiscalprinter.command.ReadEJSerialNumber;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOReadEJSerial {

    private final FiscalPrinterImpl service;

    /** Creates a new instance of DIOReadSerial */
    public DIOReadEJSerial(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkObjectMinLength((String[]) object, 1);
        String[] serial = (String[]) object;
        serial[0] = "";
        LongPrinterStatus status = service.readLongStatus();
        if (status.getRegistrationNumber() > 0) {
            ReadEJSerialNumber command = new ReadEJSerialNumber();
            command.setPassword(service.printer.getSysPassword());
            service.printer.execute(command);
            serial[0] = String.valueOf(command.getSerial());
        }
    }
}
