package com.shtrih.jpos.fiscalprinter.directIO;


import com.shtrih.fiscalprinter.command.FSPrintCorrectionReceipt2;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;


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
        if (params.length > 3){
            command.setPayment(0, (Long)params[3]);
        }
        if (params.length > 4){
            command.setPayment(1, (Long)params[4]);
        }
        if (params.length > 5){
            command.setPayment(2, (Long)params[5]);
        }
        if (params.length > 6){
            command.setPayment(3, (Long)params[6]);
        }
        if (params.length > 7){
            command.setPayment(4, (Long)params[7]);
        }
        if (params.length > 8){
            command.setTaxTotal(0, (Long)params[8]);
        }
        if (params.length > 9){
            command.setTaxTotal(1, (Long)params[9]);
        }
        if (params.length > 10){
            command.setTaxTotal(2, (Long)params[10]);
        }
        if (params.length > 11){
            command.setTaxTotal(3, (Long)params[11]);
        }
        if (params.length > 12){
            command.setTaxTotal(4, (Long)params[12]);
        }
        if (params.length > 13){
            command.setTaxTotal(5, (Long)params[13]);
        }
        if (params.length > 14){
            command.setTaxSystem((Integer)params[14]);
        }

        if(getParams().autoOpenShift)
            getPrinter().openFiscalDay();

        getPrinter().execute(command);

        service.printEndFiscal();

        if (params.length >= 18)
        {
            params[15] = command.getReceiptNumber();
            params[16] = command.getDocumentNumber();
            params[17] = command.getDocumentDigest();
        }
    }

}
