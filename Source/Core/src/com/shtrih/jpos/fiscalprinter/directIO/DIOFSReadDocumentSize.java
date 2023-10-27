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
import com.shtrih.fiscalprinter.command.FSReadDocumentSize;
import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterTime;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.JposFiscalPrinterDate;

public class DIOFSReadDocumentSize extends DIOItem {

    public DIOFSReadDocumentSize(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception
    {
        String[] lines = (String[])object;
        DIOUtils.checkObjectMinLength(lines, 2);
        
        FSReadDocumentSize command = new FSReadDocumentSize();
        command.password = service.getPrinter().getUsrPassword();
        service.getPrinter().execute(command);
        
        lines[0] = String.valueOf(command.documentNumber);
        lines[1] = String.valueOf(command.noticeNumber);
    }
}
