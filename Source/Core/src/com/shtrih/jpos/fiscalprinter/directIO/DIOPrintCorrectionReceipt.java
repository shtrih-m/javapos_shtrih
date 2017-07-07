/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.fiscalprinter.command.FSPrintCorrectionReceipt;

public class DIOPrintCorrectionReceipt extends DIOItem {

    public DIOPrintCorrectionReceipt(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        Object[] params = (Object[]) object;
        FSPrintCorrectionReceipt command = new FSPrintCorrectionReceipt();
        command.setSysPassword(service.getPrinter().getSysPassword());
        command.setOperationType((Integer) params[0]);
        command.setTotal((Long) params[1]);
        service.getPrinter().check(service.getPrinter().fsPrintCorrectionReceipt(command));
        service.printDocEnd();
    }

}
