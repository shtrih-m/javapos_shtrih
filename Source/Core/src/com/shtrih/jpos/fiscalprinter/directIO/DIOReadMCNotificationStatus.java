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
import com.shtrih.fiscalprinter.command.FSReadMCNotificationStatus;
import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterTime;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.util.MethodParameter;

public class DIOReadMCNotificationStatus extends DIOItem {

    public DIOReadMCNotificationStatus(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        String[] params = (String[]) object;
        DIOUtils.checkObjectMinLength(params, 6);
        FSReadMCNotificationStatus command = new FSReadMCNotificationStatus();
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
