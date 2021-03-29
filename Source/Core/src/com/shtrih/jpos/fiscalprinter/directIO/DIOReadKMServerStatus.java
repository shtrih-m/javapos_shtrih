/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.jpos.DIOUtils;
import com.shtrih.fiscalprinter.GS1Barcode;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.FSReadKMServerStatus;
import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterTime;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.util.MethodParameter;

public class DIOReadKMServerStatus extends DIOItem {

    public DIOReadKMServerStatus(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        String[] params = (String[]) object;
        DIOUtils.checkObjectMinLength(params, 6);
        FSReadKMServerStatus command = new FSReadKMServerStatus();
        getPrinter().fsReadKMServerStatus(command);
        getPrinter().check(command.getResultCode());
        params[0] = String.valueOf(0);
        params[1] = String.valueOf(command.messageQuantity);
        params[2] = String.valueOf(command.messageNumber);
        params[3] = command.messageDate.toString();
        params[4] = command.messageTime.toString();
        params[5] = String.valueOf(command.freeMemoryPercents);
    }

}
