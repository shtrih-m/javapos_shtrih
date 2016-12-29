/*
 * To change this template, choose Tools | Templates
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
import com.shtrih.jpos.fiscalprinter.SmFptrConst;

public class DIOReadDayStatus {

    private FiscalPrinterImpl service;

    public DIOReadDayStatus(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);
        int dayStatus = SmFptrConst.SMFPTR_DAY_STATUS_UNKNOWN;
        int mode = service.readPrinterStatus().getMode();
        switch (mode) {
            case PrinterConst.ECRMODE_24NOTOVER:
                dayStatus = SmFptrConst.SMFPTR_DAY_STATUS_OPENED;
                break;

            case PrinterConst.ECRMODE_24OVER:
                dayStatus = SmFptrConst.SMFPTR_DAY_STATUS_EXPIRED;
                break;

            case PrinterConst.ECRMODE_CLOSED:
                dayStatus = SmFptrConst.SMFPTR_DAY_STATUS_CLOSED;
                break;
        }
        data[0] = dayStatus;
    }
}
