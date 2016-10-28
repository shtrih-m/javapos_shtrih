/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.fiscalprinter.receipt.PrinterReceipt;
import com.shtrih.jpos.fiscalprinter.FiscalDay;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterState;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class ReceiptContext {

    private final ReceiptPrinter printer;
    private final FptrParameters params;
    private final FiscalDay fiscalDay;
    private final PrinterReceipt receipt;
    private final FiscalPrinterState printerState;
    private final FiscalPrinterImpl service;

    public ReceiptContext(ReceiptPrinter printer, FptrParameters params,
            FiscalDay fiscalDay, PrinterReceipt receipt,
            FiscalPrinterState printerState,
            FiscalPrinterImpl service) 
    {
        this.printer = printer;
        this.params = params;
        this.fiscalDay = fiscalDay;
        this.receipt = receipt;
        this.printerState = printerState;
        this.service = service;
    }

    public FiscalPrinterImpl getService(){
        return service;
    }
            
    public FiscalPrinterState getPrinterState() {
        return printerState;
    }

    public void setPrinterState(int printerState) {
        this.printerState.setValue(printerState);
    }

    public ReceiptPrinter getPrinter() {
        return printer;
    }

    public FptrParameters getParams() {
        return params;
    }

    public FiscalDay getFiscalDay() {
        return fiscalDay;
    }

    public PrinterReceipt getReceipt() {
        return receipt;
    }
}
