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
import com.shtrih.fiscalprinter.command.TextLine;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.jpos.fiscalprinter.PrintItem;
import java.util.Vector;

public class NullReceipt implements FiscalReceipt {

    private boolean cancelled = false;
    private ReceiptContext context = null;
    private final Vector<TextLine> messages = new Vector();

    public NullReceipt() {
    }

    public Vector<TextLine> getMessages(){
        return messages;
    }
    
    public NullReceipt(ReceiptContext context) {
        this.context = context;
    }

    public FptrParameters getParams() {
        return context.getParams();
    }

    public ReceiptPrinter getPrinter() {
        return context.getPrinter();
    }

    public boolean getCapAutoCut() throws Exception {
        return false;
    }

    public boolean isPayed() {
        return false;
    }

    public boolean isOpened() {
        return false;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void notOpenedException() throws Exception {
        throw new JposException(JposConst.JPOS_E_ILLEGAL, "Receipt is not opened");
    }

    public void beginFiscalReceipt(boolean printHeader) throws Exception {
        notOpenedException();
    }

    public void endFiscalReceipt(boolean printHeader) throws Exception {
        notOpenedException();
    }

    public void printRecItem(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        notOpenedException();
    }

    public void printRecItemAdjustment(int adjustmentType, String description,
            long amount, int vatInfo) throws Exception {
        notOpenedException();
    }

    public void printRecNotPaid(String description, long amount)
            throws Exception {
        notOpenedException();
    }

    public void printRecRefund(String description, long amount, int vatInfo)
            throws Exception {
        notOpenedException();
    }

    public void printRecSubtotal(long amount) throws Exception {
        notOpenedException();
    }

    public void printRecSubtotalAdjustment(int adjustmentType,
            String description, long amount) throws Exception {
        notOpenedException();
    }

    public void printRecTotal(long total, long payment, long payType,
            String description) throws Exception {
        notOpenedException();
    }

    public void printRecVoid(String description) throws Exception {
        cancelled = true;
    }

    public void printRecVoidItem(String description, long amount, int quantity,
            int adjustmentType, long adjustment, int vatInfo) throws Exception {
        notOpenedException();
    }

    public void printRecCash(long amount) throws Exception {
        notOpenedException();
    }

    public void printRecItemFuel(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName, long specialTax,
            String specialTaxName) throws Exception {
        notOpenedException();
    }

    public void printRecItemFuelVoid(String description, long price,
            int vatInfo, long specialTax) throws Exception {
        notOpenedException();
    }

    public void printRecPackageAdjustment(int adjustmentType,
            String description, String vatAdjustment) throws Exception {
        notOpenedException();
    }

    public void printRecPackageAdjustVoid(int adjustmentType,
            String vatAdjustment) throws Exception {
        notOpenedException();
    }

    public void printRecRefundVoid(String description, long amount, int vatInfo)
            throws Exception {
        notOpenedException();
    }

    public void printRecSubtotalAdjustVoid(int adjustmentType, long amount)
            throws Exception {
        notOpenedException();
    }

    public void printRecTaxID(String taxID) throws Exception {
        notOpenedException();
    }

    public void printRecItemVoid(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        notOpenedException();
    }

    public void printRecItemAdjustmentVoid(int adjustmentType,
            String description, long amount, int vatInfo) throws Exception {
        notOpenedException();
    }

    public void printRecItemRefund(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        notOpenedException();
    }

    public void printRecItemRefundVoid(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        notOpenedException();
    }

    public void printRecMessage(int station, FontNumber font, String message)
            throws Exception 
    {
        messages.add(new TextLine(station, font, message));
    }

    public void printNormal(int station, String data) throws Exception {
        if (context != null) {
            getPrinter().printText(getPrinter().getStation(station), data,
                    getParams().font);
        }
    }

    public boolean getDisablePrint() {
        return false;
    }

    public void disablePrint() throws Exception {
        notOpenedException();
    }

    public void fsWriteTLV(byte[] data) throws Exception {
        notOpenedException();
    }

    public void fsWriteTag(int tagId, String tagValue) throws Exception {
        notOpenedException();
    }

    public void fsWriteCustomerEmail(String text) throws Exception {
        notOpenedException();
    }

    public void fsWriteCustomerPhone(String text) throws Exception {
        notOpenedException();
    }
    
    public void setDiscountAmount(int amount) throws Exception{
    }
    
    public void printBarcode(PrinterBarcode barcode) throws Exception{
        getPrinter().getPrinter().printBarcode(barcode);
    }

    public void printGraphics(PrinterGraphics graphics) throws Exception{
        graphics.print(getPrinter().getPrinter());
    }
    
    public void setMessages(Vector<TextLine> messages){
    }
}
