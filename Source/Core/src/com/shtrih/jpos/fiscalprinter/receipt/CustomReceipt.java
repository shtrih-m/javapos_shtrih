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
import com.shtrih.fiscalprinter.GS1Barcode;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.ItemCode;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.command.PrinterStatus;
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
import com.shtrih.jpos.fiscalprinter.JposPrinterStation;
import com.shtrih.util.Localizer;
import java.util.Vector;

public abstract class CustomReceipt implements FiscalReceipt {

    protected boolean ending = false;
    protected boolean cancelled = false;
    private final SMFiscalPrinter printer;
    private static CompositeLogger logger = CompositeLogger.getLogger(CustomReceipt.class);

    public CustomReceipt(SMFiscalPrinter printer) {
        this.printer = printer;
    }

    public FptrParameters getParams() {
        return printer.getParams();
    }
    
    public SMFiscalPrinter getPrinter() {
        return printer;
    }

    public PrinterModel getModel() throws Exception {
        return getPrinter().getModel();
    }

    public void printRecMessage(int station, FontNumber font, String message)
            throws Exception {
        getPrinter().printText(station, message, font);
    }

    public void printNormal(int station, String data) throws Exception {
        getPrinter().printText(getStation(station), data,
                getParams().font);
    }

    public void checkTotal(long recTotal, long appTotal) throws Exception {
        if (!getParams().checkTotalEnabled) {
            return;
        }

        if (!getParams().checkTotal) {
            return;
        }

        if (recTotal != appTotal) {
            logger.error("Totals compare failed!");
            logger.debug("Receipt total: " + recTotal);
            logger.debug("Application total: " + appTotal);

            throw new JposException(JPOS_E_EXTENDED,
                    JPOS_EFPTR_BAD_ITEM_AMOUNT);
        }
    }
    
    public void printBarcode(PrinterBarcode barcode) throws Exception {
        getPrinter().printBarcode(barcode);
    }

    public void printGraphics(PrinterGraphics graphics) throws Exception {
        graphics.print(getPrinter());
    }

    public long getSubtotal() throws Exception {
        long total = 0;
        PrinterStatus status = getPrinter().readPrinterStatus();
        if (status.getPrinterMode().isReceiptOpened()) {
            total = getPrinter().getSubtotal();
        }
        return total;
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

    public boolean isEnding() throws Exception{
        return ending;
    }
            
    public void notSupported() throws Exception {
        throw new JposException(JposConst.JPOS_E_ILLEGAL,
                "Receipt method is not supported");
    }

    public void beginFiscalReceipt(boolean printHeader) throws Exception {
        ending = false;
        cancelled = false;
    }

    public void endFiscalReceipt(boolean printHeader) throws Exception {
    }

    public void printRecItem(String description, long price, double quantity,
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
        ending = true;
    }

    public void printRecVoidItem(String description, long amount, double quantity,
            int adjustmentType, long adjustment, int vatInfo) throws Exception {
        notSupported();
    }

    public void printRecCash(long amount) throws Exception {
        notSupported();
    }

    public void printRecItemFuel(String description, long price, double quantity,
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

    public void printRecItemVoid(String description, long price, double quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        notSupported();
    }

    public void printRecItemAdjustmentVoid(int adjustmentType,
            String description, long amount, int vatInfo) throws Exception {
        notSupported();
    }

    public void printRecItemRefund(String description, long amount,
            double quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        notSupported();
    }

    public void printRecItemRefundVoid(String description, long amount,
            double quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        notSupported();
    }


    public void fsWriteTLV(byte[] data, boolean print) throws Exception {
    }

    public void fsWriteOperationTLV(byte[] data, boolean print) throws Exception{
    }

    public void setDiscountAmount(int amount) throws Exception {
    }

    public void setItemBarcode2(String barcode) throws Exception {
    }

    public void addItemCode(ItemCode itemCode) throws Exception{
    }
    
    public void printReceiptEnding() throws Exception{
    }
    
    public void accept(ReceiptVisitor visitor) throws Exception{
        visitor.visitCustomReceipt(this);
    }
    
    public void checkZeroReceipt() throws Exception {

        if (!getParams().getZeroReceiptEnabled()) {
            if (getSubtotal() == 0) {
                throw new JposException(JposConst.JPOS_E_ILLEGAL,
                        "Zero receipts sre disabled");
            }
        }
    }
    
    public int getStation(int station) throws Exception {
        // check valid stations
        JposPrinterStation printerStation = new JposPrinterStation(station);
        if (printerStation.isRecStation()
                && (!getModel().getCapRecPresent())) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.receiptStationNotPresent));
        }
        if (printerStation.isJrnStation()
                && (!getModel().getCapJrnPresent())) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.journalStationNotPresent));
        }
        if (printerStation.isSlpStation()
                && (!getModel().getCapSlpPresent())) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.slipStationNotPresent));
        }
        return printerStation.getStation();
    }
    
    public void printPreLine() throws Exception {
        if (getParams().preLine.length() > 0) {
            printText(getParams().preLine);
            getParams().preLine = "";
        }
    }
    
    public void printPostLine() throws Exception {
        if (getParams().postLine.length() > 0) {
            printText(getParams().postLine);
            getParams().postLine = "";
        }
    }
    
    public void printText(String text) throws Exception {
        getPrinter().printText(PrinterConst.SMFP_STATION_REC, text, 
            getPrinter().getParams().getFont());
    }

    public String[] parseText(String text) throws Exception {
        logger.debug("parseText: " + text);
        FontNumber font = getParams().getFont();
        return getPrinter().splitText(text, font);
    }
    
    public String printDescription(String description) throws Exception {
        String result = "";
        String[] lines = parseText(description);
        if (lines.length == 1) {
            result = lines[0];
        } else {
            for (int i = 0; i < lines.length - 1; i++) {
                printText(lines[i]);
            }
            result = lines[lines.length - 1];
        }
        return result;
    }

    private String formatStrings(String line1, String line2) throws Exception {
        int len;
        String S = "";
        len = getPrinter().getMessageLength() - line2.length();

        for (int i = 0; i < len; i++) {
            if (i < line1.length()) {
                S = S + line1.charAt(i);
            } else {
                S = S + " ";
            }
        }
        return S + line2;
    }


    public void printStrings(String line1, String line2) throws Exception {
        printText(formatStrings(line1, line2));
    }
}    
