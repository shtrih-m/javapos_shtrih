/*
 * DIOExecuteCommand.java
 *
 * Created on 13 Май 2010 г., 17:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.SMFiscalPrinterImpl;
import com.shtrih.fiscalprinter.command.CommandInputStream;
import com.shtrih.fiscalprinter.command.PrinterCommand;
import com.shtrih.fiscalprinter.command.RawCommand;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.util.Hex;

public class DIOExecuteCommand {

    private final FiscalPrinterImpl service;

    /** Creates a new instance of DIOReadPaymentName */
    public DIOExecuteCommand(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);
        DIOUtils.checkObjectNotNull(object);

        int timeout = data[0];
        byte[] tx = (byte[]) ((Object[]) object)[0];
        RawCommand command = new RawCommand();
        command.setTxData(tx);
        command.setTimeout(timeout);
        service.printer.getDevice().send(command);
        service.printer.check(command.getResultCode());
        ((Object[]) object)[1] = command.getRxData();
    }
}
