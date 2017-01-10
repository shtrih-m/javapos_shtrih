/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.util.CompositeLogger;

public class CashInReceipt extends CustomReceipt implements FiscalReceipt {

    private long total = 0; // receipt total
    private long payment = 0; // paied total
    private static CompositeLogger logger = CompositeLogger.getLogger(CashInReceipt.class);

    public CashInReceipt(ReceiptContext context) {
        super(context);
    }

    public void printRecCash(long amount) throws Exception {
        logger.debug("printRecCash");
        total += amount;
    }

    public void endFiscalReceipt(boolean printHeader) throws Exception {
        logger.debug("endFiscalReceipt");
        getPrinter().getPrinter().printCashIn(total);
    }

    public void printRecTotal(long total, long payment, long payType,
            String description) throws Exception {
        logger.debug("printRecTotal");
        getPrinter().printPreLine();
        getPrinter().printPostLine();
        this.payment += payment;
    }

    public boolean isPayed() {
        return payment >= total;
    }

    public boolean getCapAutoCut() throws Exception {
        return getModel().getCapCashInAutoCut();
    }
}
