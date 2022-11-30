/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.fiscalprinter.command.FSReadCommStatus;
import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterTime;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.JposFiscalPrinterDate;

public class DIOReadCommStatus extends DIOItem {

    public DIOReadCommStatus(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception
    {
        String[] lines = (String[])object;
        DIOUtils.checkObjectMinLength(lines, 5);
        FSReadCommStatus command = service.getPrinter().fsReadCommStatus();
        service.getPrinter().check(command.getResultCode());
        lines[0] = String.valueOf(command.getStatus());
        lines[1] = String.valueOf(command.getReadStatus());
        lines[2] = String.valueOf(command.getQueueSize());
        lines[3] = String.valueOf(command.getDocumentNumber());
        JposFiscalPrinterDate jposDate = new JposFiscalPrinterDate(
                command.getDocumentDate(), command.getDocumentTime());
        lines[4] = jposDate.toString();
    }
}
