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
import com.shtrih.fiscalprinter.command.FSPrintCorrectionReceipt2;


public class DIOPrintCorrectionReceipt extends DIOItem {

    public DIOPrintCorrectionReceipt(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        Object[] params = (Object[]) object;
        FSPrintCorrectionReceipt2 command = new FSPrintCorrectionReceipt2();
        command.setSysPassword(service.getPrinter().getSysPassword());
        command.setCorrectionType((Integer)params[0]);
        command.setPaymentType((Integer)params[1]);
        command.setTotal((Long)params[2]);
        command.setPayments((long[])params[3]);
        command.setTaxTotals((long[])params[4]);
        command.setTaxSystem((Integer)params[5]);
        service.getPrinter().execute(command);
        
    }

}
