/*
 * DIOReadCashReg.java
 *
 * Created on 11 Март 2010 г., 16:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.fiscalprinter.command.CashRegister;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOReadCashReg {

    private FiscalPrinterImpl service;

    /** Creates a new instance of DIOReadCashReg */
    public DIOReadCashReg(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);
        DIOUtils.checkObjectMinLength((String[]) object, 2);

        String[] lines = (String[]) object;
        CashRegister register = new CashRegister(data[0]);
        service.printer.check(service.printer.readCashRegister(register));
        lines[0] = String.valueOf(register.getValue());
        lines[1] = CashRegister.getName(data[0]);
    }
}
