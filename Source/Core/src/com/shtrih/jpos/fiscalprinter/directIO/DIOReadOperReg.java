/*
 * DIOReadOperReg.java
 *
 * Created on 11 Март 2010 г., 17:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.command.OperationRegister;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOReadOperReg {

    private FiscalPrinterImpl service;

    /** Creates a new instance of DIOReadCashReg */
    public DIOReadOperReg(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);
        DIOUtils.checkObjectMinLength((String[]) object, 2);

        String[] lines = (String[]) object;
        int regNumber = data[0];
        long regValue = service.printer.readOperationRegister(regNumber);
        lines[0] = String.valueOf(regValue);
        lines[1] = OperationRegister.getName(data[0]);
    }

}
