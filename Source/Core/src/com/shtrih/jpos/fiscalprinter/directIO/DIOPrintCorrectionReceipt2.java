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


public class DIOPrintCorrectionReceipt2 extends DIOItem {

    public DIOPrintCorrectionReceipt2(FiscalPrinterImpl service) {
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
        command.setPayment(0, (Long)params[3]);
        command.setPayment(1, (Long)params[4]);
        command.setPayment(2, (Long)params[5]);
        command.setPayment(3, (Long)params[6]);
        command.setPayment(4, (Long)params[7]);
        command.setTaxTotal(0, (Long)params[8]);
        command.setTaxTotal(1, (Long)params[9]);
        command.setTaxTotal(2, (Long)params[10]);
        command.setTaxTotal(3, (Long)params[11]);
        command.setTaxTotal(4, (Long)params[12]);
        command.setTaxTotal(5, (Long)params[13]);
        command.setTaxSystem((Integer)params[14]);
        service.getPrinter().fsPrintCorrectionReceipt2(command);
        if (params.length >= 18)
        {
            params[15] = command.getReceiptNumber();
            params[16] = command.getDocumentNumber();
            params[17] = command.getDocumentDigest();
        }
    }

}
