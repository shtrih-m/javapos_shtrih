/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.fiscalprinter.PrinterGraphics;
import jpos.JposConst;
import jpos.JposException;

import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.model.PrinterModel;
import com.shtrih.fiscalprinter.receipt.PrinterReceipt;
import com.shtrih.jpos.fiscalprinter.FiscalDay;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.util.CompositeLogger;
import static jpos.FiscalPrinterConst.FPTR_PS_MONITOR;
import static jpos.FiscalPrinterConst.JPOS_EFPTR_BAD_ITEM_AMOUNT;
import static jpos.JposConst.JPOS_E_EXTENDED;
import com.shtrih.fiscalprinter.command.TextLine;
import java.util.Vector;

public abstract class CustomReceipt implements FiscalReceipt {

    protected boolean cancelled = false;
    private final ReceiptContext context;
    private static CompositeLogger logger = CompositeLogger.getLogger(CustomReceipt.class);

    public CustomReceipt(ReceiptContext context) {
        this.context = context;
    }

    public ReceiptContext getContext() {
        return context;
    }

    public FiscalPrinterImpl getService() {
        return context.getService();
    }

    public void setPrinterState(int state) {
        context.setPrinterState(state);
    }

    public ReceiptPrinter getPrinter() {
        return context.getPrinter();
    }

    public FptrParameters getParams() {
        return context.getParams();
    }

    public FiscalDay getFiscalDay() {
        return context.getFiscalDay();
    }

    public PrinterReceipt getReceipt() {
        return context.getReceipt();
    }

    public PrinterModel getModel() throws Exception {
        return getPrinter().getPrinter().getModel();
    }

    public boolean getCapAutoCut() throws Exception {
        return false;
    }

    public boolean isPayed() throws Exception {
        return false;
    }

    public boolean isOpened() {
        return false;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void notSupported() throws Exception {
        throw new JposException(JposConst.JPOS_E_ILLEGAL,
                "Receipt method is not supported");
    }

    public void beginFiscalReceipt(boolean printHeader) throws Exception {
        cancelled = false;
    }

    public void endFiscalReceipt(boolean printHeader) throws Exception {
    }

    public void printRecItem(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        notSupported();
    }

    public void printRecItemAdjustment(int adjustmentType, String description,
            long amount, int vatInfo) throws Exception {
        notSupported();
    }

    public void printRecNotPaid(String description, long amount)
            throws Exception {
        notSupported();
    }

    public void printRecRefund(String description, long amount, int vatInfo)
            throws Exception {
        notSupported();
    }

    public void printRecSubtotal(long amount) throws Exception {
        notSupported();
    }

    public void printRecSubtotalAdjustment(int adjustmentType,
            String description, long amount) throws Exception {
        notSupported();
    }

    public void printRecTotal(long total, long payment, long payType,
            String description) throws Exception {
        notSupported();
    }

    public void printRecVoid(String description) throws Exception {
        cancelled = true;
    }

    public void printRecVoidItem(String description, long amount, int quantity,
            int adjustmentType, long adjustment, int vatInfo) throws Exception {
        notSupported();
    }

    public void printRecCash(long amount) throws Exception {
        notSupported();
    }

    public void printRecItemFuel(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName, long specialTax,
            String specialTaxName) throws Exception {
        notSupported();
    }

    public void printRecItemFuelVoid(String description, long price,
            int vatInfo, long specialTax) throws Exception {
        notSupported();
    }

    public void printRecPackageAdjustment(int adjustmentType,
            String description, String vatAdjustment) throws Exception {
        notSupported();
    }

    public void printRecPackageAdjustVoid(int adjustmentType,
            String vatAdjustment) throws Exception {
        notSupported();
    }

    public void printRecRefundVoid(String description, long amount, int vatInfo)
            throws Exception {
        notSupported();
    }

    public void printRecSubtotalAdjustVoid(int adjustmentType, long amount)
            throws Exception {
        notSupported();
    }

    public void printRecTaxID(String taxID) throws Exception {
        notSupported();
    }

    public void printRecItemVoid(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        notSupported();
    }

    public void printRecItemAdjustmentVoid(int adjustmentType,
            String description, long amount, int vatInfo) throws Exception {
        notSupported();
    }

    public void printRecItemRefund(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        notSupported();
    }

    public void printRecItemRefundVoid(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        notSupported();
    }

    public void printRecMessage(int station, FontNumber font, String message)
            throws Exception {
        getPrinter().printText(station, message, font);
    }

    public void printNormal(int station, String data) throws Exception{
        getPrinter().printText(getPrinter().getStation(station), data,
                getParams().font);
    }

    public void checkTotal(long recTotal, long appTotal) throws Exception {
        if (!getParams().checkTotalEnabled) {
            return;
        }

        if (!getService().getCheckTotal()) {
            return;
        }

        if (recTotal != appTotal) {
            logger.error("Totals compare failed!");
            logger.debug("Receipt total: " + recTotal);
            logger.debug("Application total: " + appTotal);

            setPrinterState(FPTR_PS_MONITOR);
            throw new JposException(JPOS_E_EXTENDED,
                    JPOS_EFPTR_BAD_ITEM_AMOUNT);
        }
    }

    public void disablePrint() throws Exception {
    }

    public boolean getDisablePrint() {
        return false;
    }

    public void fsWriteTLV(byte[] data) throws Exception {
    }

    public void fsWriteTag(int tagId, String tagValue) throws Exception {
    }

    public void fsWriteCustomerEmail(String text) throws Exception {
    }

    public void fsWriteCustomerPhone(String text) throws Exception {
    }

    public void setDiscountAmount(int amount) throws Exception{
    }
    
    public void printBarcode(PrinterBarcode barcode) throws Exception{
        getPrinter().getPrinter().printBarcode(barcode);
    }

    public void printGraphics(PrinterGraphics graphics) throws Exception{
        graphics.print(getPrinter().getPrinter());
    }

}
