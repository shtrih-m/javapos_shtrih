/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

/**
 * @author V.Kravtsov
 */
import com.shtrih.barcode.PrinterBarcode;

import java.util.Vector;

import com.shtrih.fiscalprinter.*;
import com.shtrih.jpos.fiscalprinter.PrintItem;
import jpos.JposConst;
import jpos.JposException;
import jpos.FiscalPrinterConst;

import com.shtrih.util.MathUtils;
import com.shtrih.util.Localizer;
import com.shtrih.util.StringUtils;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.MethodParameter;
import com.shtrih.fiscalprinter.command.FSCloseReceipt;
import com.shtrih.fiscalprinter.command.AmountItem;
import com.shtrih.fiscalprinter.command.CloseRecParams;
import com.shtrih.fiscalprinter.command.EndFiscalReceipt;
import com.shtrih.fiscalprinter.command.PriceItem;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.command.PrinterStatus;
import com.shtrih.jpos.fiscalprinter.PackageAdjustment;
import com.shtrih.jpos.fiscalprinter.PackageAdjustments;
import com.shtrih.fiscalprinter.command.FSReceiptDiscount;
import com.shtrih.fiscalprinter.command.TLVList;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.jpos.fiscalprinter.receipt.template.TemplateLine;
import com.shtrih.fiscalprinter.command.TextLine;
import java.util.Vector;

import static jpos.FiscalPrinterConst.FPTR_PS_FISCAL_RECEIPT_ENDING;

public class FSSalesReceipt extends CustomReceipt implements FiscalReceipt {

    private FSSaleReceiptItem lastItem = null;
    private long total = 0;
    private int receiptType = 0;
    private int discountAmount = 0;
    private boolean isOpened = false;
    private boolean disablePrint = false;
    private String voidDescription = "";
    private final Vector items = new Vector();
    private final Vector endingItems = new Vector();
    private final Vector recItems = new Vector();
    private final long[] payments = new long[16]; // payment amounts
    private final FSDiscounts discounts = new FSDiscounts();
    private Vector<String> messages = new Vector<String>();
    private final ReceiptTemplate receiptTemplate;
    private static CompositeLogger logger = CompositeLogger.getLogger(FSSalesReceipt.class);
    private boolean subtotalPrinted = false;

    public FSSalesReceipt(ReceiptContext context, int receiptType) throws Exception {
        super(context);
        this.receiptType = receiptType;
        receiptTemplate = new ReceiptTemplate(context);
    }

    public boolean isSaleReceipt() {
        return receiptType == PrinterConst.SMFP_RECTYPE_SALE;
    }

    public void printRecMessage(int station, FontNumber font, String message)
            throws Exception {
        addTextItem(message);
    }

    public void printNormal(int station, String data) throws Exception {
        addTextItem(data);
    }

    public SMFiscalPrinter getDevice() throws Exception {
        return getPrinter().getPrinter();
    }

    public boolean isOpened() {
        return isOpened;
    }

    private void clearReceipt() throws Exception {
        discountAmount = 0;
        total = 0;
        isOpened = false;
        lastItem = null;
        items.clear();
        recItems.clear();
        discounts.clear();
        for (int i = 0; i < payments.length; i++) {
            payments[i] = 0;
        }
        voidDescription = "";
        disablePrint = false;
        messages.clear();
        cancelled = false;
    }

    public void beginFiscalReceipt(boolean printHeader) throws Exception {
        clearReceipt();
        getDevice().printFSHeader();
        subtotalPrinted = false;
    }

    public void openReceipt(boolean isSale) throws Exception {
        if (!isOpened) {
            isOpened = true;
            if (!isSale)
            {
                if (receiptType == PrinterConst.SMFP_RECTYPE_SALE){
                    receiptType = PrinterConst.SMFP_RECTYPE_BUY;
                }
            }
            getPrinter().openReceipt(receiptType);
            getPrinter().waitForPrinting();
        }
    }

    public void printRecItem(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        openReceipt(true);
        printSale(price, quantity, unitPrice, getParams().department,
                vatInfo, description, unitName);
    }

    // pack discounts
    public void addItemsDiscounts() throws Exception {
        String discountText = "";
        if (discounts.size() == 1) {
            discountText = discounts.get(0).getText();
        }
        long discountTotal = discounts.getTotal();
        if (discountTotal < 0) {
            FSSaleReceiptItem saleItem = getLastItem();

            FSDiscount discount = new FSDiscount();
            discount.setAmount(discountTotal);
            discount.setText("");
            discount.setTax1(0);
            discount.setTax2(0);
            discount.setTax3(0);
            discount.setTax4(0);
            saleItem.addDiscount(discount);
            return;
        }

        long itemsTotal = getSubtotal() + discounts.getTotal();
        if (itemsTotal <= 0) {
            return;
        }

        for (int i = 0; i < recItems.size(); i++) {
            Object recItem = recItems.get(i);
            if (recItem instanceof FSSaleReceiptItem) {
                FSSaleReceiptItem item = (FSSaleReceiptItem) recItem;
                if (item.getTotal() > 0) {
                    long itemAmount = item.getTotalWithVoids();
                    if (itemAmount > 0) {
                        long discountAmount = itemAmount;
                        if (discountAmount > discountTotal) {
                            discountAmount = discountTotal;
                        }

                        if (discountAmount > 0) {
                            FSDiscount discount = new FSDiscount();
                            discount.setAmount(discountAmount);
                            discount.setText(discountText);
                            discount.setTax1(0);
                            discount.setTax2(0);
                            discount.setTax3(0);
                            discount.setTax4(0);
                            item.addDiscount(discount);

                            discountTotal -= discountAmount;
                            if (discountTotal == 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void printTemplateHeader() throws Exception {
        if (getParams().ReceiptTemplateEnabled) {
            String[] lines = receiptTemplate.getHeaderLines();
            for (String line : lines) {
                getDevice().printText(line);
            }
        }
    }

    public void printTemplateTrailer() throws Exception {
        if (getParams().ReceiptTemplateEnabled) {
            String[] lines = receiptTemplate.getTrailerLines();
            for (String line : lines) {
                getDevice().printText(line);
            }
        }
    }

    public void updateReceiptItems() throws Exception {
        for (int i = 0; i < items.size(); i++) {
            Object object = items.get(i);
            if (object instanceof FSSaleReceiptItem) {
                FSSaleReceiptItem item = (FSSaleReceiptItem) object;
                item.updatePrice();
                FSSaleReceiptItem splitItem = item.getSplittedItem();
                if (splitItem != null) {
                    items.insertElementAt(splitItem, i + 1);
                    i++;
                }
            }
        }
        int pos = 1;
        for (int i = 0; i < items.size(); i++) {
            Object object = items.get(i);
            if (object instanceof FSSaleReceiptItem) {
                FSSaleReceiptItem item = (FSSaleReceiptItem) object;
                item.setPos(pos);
                pos++;
            }
        }
    }

    public void printReceiptItems() throws Exception {
        boolean isHeaderPrinted = false;
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            if (item instanceof FSSaleReceiptItem) {
                if (!isHeaderPrinted) {
                    isHeaderPrinted = true;
                    printTemplateHeader();
                }
                printFSSale((FSSaleReceiptItem) item);
            }
            if (item instanceof FSTextReceiptItem) {
                printFSText((FSTextReceiptItem) item);
            }
            if (item instanceof PrinterBarcode) {
                getDevice().printBarcode((PrinterBarcode) item);
                continue;
            }

            if (item instanceof FSDiscount) {
                printTotalDiscount((FSDiscount) item);
            }

            if (item instanceof FSTLVItem) {
                FSTLVItem tlvItem = (FSTLVItem) item;
                getDevice().fsWriteTLV(tlvItem.getData());

                if (getParams().FSPrintTags) {
                    TLVParser reader = new TLVParser();
                    reader.read(tlvItem.getData());
                    Vector<String> lines = reader.getPrintText();
                    messages.addAll(lines);
                }
            }

            if (item instanceof PrintItem) {
                PrintItem printItem = (PrintItem) item;
                printItem.print(getPrinter().getPrinter());
            }
        }
        printTemplateTrailer();
    }

    public void printEndingItems() throws Exception {
        for (int i = 0; i < endingItems.size(); i++) {
            Object item = endingItems.get(i);
            if (item instanceof FSTextReceiptItem) {
                printFSText((FSTextReceiptItem) item);
            }
            if (item instanceof PrinterBarcode) {
                getDevice().printBarcode((PrinterBarcode) item);
            }
        }
    }

    public void correctPayments() throws Exception {
        long paidAmount = 0;
        for (int i = 1; i < payments.length; i++) {
            if (paidAmount + payments[i] > getSubtotal()) {
                payments[i] = getSubtotal() - paidAmount;
            }
            paidAmount += payments[i];
        }
    }

    public void endFiscalReceipt(boolean printHeader) throws Exception {
        subtotalPrinted = false;
        if (isOpened) {
            if (cancelled) {
                try {
                    PrinterStatus status = getDevice().waitForPrinting();
                    if (!status.getPrinterMode().isReceiptOpened()) {
                        getPrinter().openReceipt(receiptType);
                    }
                    getPrinter().waitForPrinting();
                    getDevice().cancelReceipt();
                    String docName = getDevice().getReceiptName(receiptType);
                    getDevice().printReceiptHeader(docName);
                    getPrinter().printText(voidDescription);
                    getPrinter().printText(getParams().receiptVoidText);
                } catch (Exception e) {
                    logger.error("Cancel receipt: " + e.getMessage());
                }
                getFiscalDay().cancelFiscalRec();
                clearReceipt();
            } else {
                correctPayments();
                int discountAmount = 0;
                if (getDevice().getCapDiscount()) {
                    addItemsDiscounts();
                } else if (discounts.getTotal() < 100) {
                    discountAmount = (int) discounts.getTotal();
                    discounts.clear();
                } else {
                    addItemsDiscounts();
                }
                updateReceiptItems();
                printReceiptItems();

                if (disablePrint) {
                    getDevice().disablePrint();
                }

                if (getDevice().getCapFSCloseReceipt()) {
                    FSCloseReceipt closeReceipt = new FSCloseReceipt();
                    closeReceipt.setSysPassword(getDevice().getUsrPassword());
                    for (int i = 0; i < payments.length; i++) {
                        closeReceipt.setPayment(i, payments[i]);
                    }
                    closeReceipt.setTaxValue(0, getParams().taxValue[0]);
                    closeReceipt.setTaxValue(1, getParams().taxValue[1]);
                    closeReceipt.setTaxValue(2, getParams().taxValue[2]);
                    closeReceipt.setTaxValue(3, getParams().taxValue[3]);
                    closeReceipt.setTaxValue(4, getParams().taxValue[4]);
                    closeReceipt.setTaxValue(5, getParams().taxValue[5]);
                    closeReceipt.setDiscount(discountAmount);
                    closeReceipt.setTaxSystem(getParams().taxSystem);
                    closeReceipt.setText(getParams().closeReceiptText);
                    getDevice().execute(closeReceipt);
                } else {
                    CloseRecParams closeParams = new CloseRecParams();
                    closeParams.setSum1(payments[0]);
                    closeParams.setSum2(payments[1]);
                    closeParams.setSum3(payments[2]);
                    closeParams.setSum4(payments[3]);
                    closeParams.setTax1(0);
                    closeParams.setTax2(0);
                    closeParams.setTax3(0);
                    closeParams.setTax4(0);
                    closeParams.setDiscount(discountAmount);
                    closeParams.setText(getParams().closeReceiptText);
                    getDevice().closeReceipt(closeParams);
                }
                getFiscalDay().closeFiscalRec();
                if (!disablePrint) {
                    for (int i = 0; i < messages.size(); i++) {
                        getDevice().printText(messages.get(i));
                    }
                }
            }
        }
        printEndingItems();
    }

    public void printTotalDiscount(FSDiscount discount)
            throws Exception {
        if (!getDevice().getCapDiscount()) {
            return;
        }
        FSReceiptDiscount item = new FSReceiptDiscount();
        item.setSysPassword(getDevice().getUsrPassword());
        if (discount.getAmount() > 0) {
            item.setDiscount(Math.abs(discount.getAmount()));
            item.setCharge(0);
        } else {
            item.setDiscount(0);
            item.setCharge(Math.abs(discount.getAmount()));
        }
        item.setTax1(discount.getTax1());
        item.setName(discount.getText());
        if (getDevice().getCapDisableDiscountText()) {
            if (!getDevice().isDiscountInHeader()) {
                item.setName("//");
                String s = StringUtils.amountToString(Math.abs(discount.getAmount()));
                getDevice().printLines("ОКРУГЛЕНИЕ", "=" + s);
            }
        }
        getDevice().fsReceiptDiscount(item);
    }

    public void printFSText(FSTextReceiptItem item) throws Exception {
        if (!item.preLine.isEmpty()) {
            getDevice().printText(item.preLine);
        }

        if (!item.text.isEmpty()) {
            getDevice().printText(item.text);
        }

        if (!item.postLine.isEmpty()) {
            getDevice().printText(item.postLine);
        }
    }

    public void printFSSale(FSSaleReceiptItem item) throws Exception {

        String preLine = item.getPreLine();
        if (preLine.length() > 0) {
            getDevice().printText(preLine);
        }

        if (getParams().ReceiptTemplateEnabled) {
            String[] lines = receiptTemplate.getReceiptItemLines(item);
            for (int i = 0; i < lines.length; i++) {
                getDevice().printText(lines[i]);
            }
            /*
             lines = receiptTemplate.getAdjustmentLines(item);
             for (int i = 0; i < lines.length; i++) {
             getDevice().printLine(PrinterConst.SMFP_STATION_REC,
             lines[i], getParams().discountFont);
             }
             */
            item.setText("//" + item.getText());
        } else if (!getParams().FSCombineItemAdjustments) {
            printRecItemAsText(item);
            item.setText("//" + item.getText());
        }

        PriceItem priceItem = item.getPriceItem();
        if (!item.getIsStorno()) 
        {
            switch (receiptType)
            { 
                case PrinterConst.SMFP_RECTYPE_SALE:
                    getDevice().printSale(priceItem);
                    break;
                    
                case PrinterConst.SMFP_RECTYPE_RETSALE:
                    getDevice().printVoidSale(priceItem);
                    break;
                    
                case PrinterConst.SMFP_RECTYPE_BUY:
                    getDevice().printRefund(priceItem);
                    break;
                    
                case PrinterConst.SMFP_RECTYPE_RETBUY:
                    getDevice().printVoidRefund(priceItem);
                    break;
                default:
                    getDevice().printSale(priceItem);
            }
        } else {
            getDevice().printVoidItem(priceItem);
        }

        String postLine = item.getPostLine();

        if (postLine.length()
                > 0) {
            getDevice().printText(postLine);
        }
    }

    public void printRecRefund(String description, long amount, int vatInfo)
            throws Exception {
        openReceipt(false);
        printSale(amount, 1000, amount, getParams().department, vatInfo,
                description, "");
    }

    public void addTextItem(String text) throws Exception {
        FSTextReceiptItem item = new FSTextReceiptItem();
        item.text = text;
        item.preLine = getPreLine();
        item.postLine = getPostLine();
        if (getContext().getPrinterState().getValue() == FiscalPrinterConst.FPTR_PS_FISCAL_RECEIPT_ENDING) {
            endingItems.add(item);
        } else {
            items.add(item);
        }
    }

    public void printRecTotal(long total, long payment, long payType,
            String description) throws Exception {
        checkTotal(getSubtotal(), total);
        addPayment(payment, payType);
        clearPrePostLine();
    }

    public void printRecItemVoid(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        openReceipt(false);

        if (isSaleReceipt()) {
            printStorno(price, quantity, unitPrice, getParams().department,
                    vatInfo, description);
        } else {
            printSale(price, quantity, unitPrice, getParams().department,
                    vatInfo, description, unitName);
        }
    }

    private String formatStrings(String line1, String line2) throws Exception {
        int len;
        String S = "";
        len = getDevice().getMessageLength() - line2.length();

        for (int i = 0; i < len; i++) {
            if (i < line1.length()) {
                S = S + line1.charAt(i);
            } else {
                S = S + " ";
            }
        }
        return S + line2;
    }

    public void printRecSubtotal(long amount) throws Exception {
        checkTotal(getSubtotal(), amount);
        if (!getDevice().isSubtotalInHeader()) {
            if (!subtotalPrinted) {
                if (getParams().subtotalTextEnabled) {
                    addTextItem(formatStrings(getParams().subtotalText,
                            "=" + StringUtils.amountToString(getSubtotal())));
                }
                subtotalPrinted = true;
            }
        }
    }

    public long getItemPercentAdjustmentAmount(long amount) throws Exception {
        double d = getLastItem().getAmount() * amount;
        return MathUtils.round(d / 10000.0);
    }

    public void checkDiscountsEnabled() throws Exception {
        if (!getParams().FSDiscountEnabled) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL,
                    "Adjustments forbidden with FS");
        }
    }

    public void printRecItemAdjustment(int adjustmentType, String description,
            long amount, int vatInfo) throws Exception {
        checkDiscountsEnabled();
        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:
                printDiscount(amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:
                printDiscount(-amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_DISCOUNT:
                amount = getItemPercentAdjustmentAmount(amount);
                printDiscount(amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_SURCHARGE:
                amount = getItemPercentAdjustmentAmount(amount);
                printDiscount(-amount, vatInfo, description);
                break;

            default:
                throw new JposException(JposConst.JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + "adjustmentType");
        }
    }

    public void printRecSubtotalAdjustment(int adjustmentType,
            String description, long amount) throws Exception {
        checkDiscountsEnabled();
        checkAdjustment(adjustmentType, amount);
        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:
                printTotalDiscount(amount, 0, description);
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:
                printTotalDiscount(-amount, 0, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_DISCOUNT:
                amount = MathUtils.round(((double) getSubtotal() * amount) / 10000.0);
                printTotalDiscount(amount, 0, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_SURCHARGE:
                amount = MathUtils.round(((double) getSubtotal() * amount) / 10000.0);
                printTotalDiscount(-amount, 0, description);
                break;

            default:
                throw new JposException(JposConst.JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + "AdjustmentType");
        }
    }

    public void printRecVoidItem(String description, long amount, int quantity,
            int adjustmentType, long adjustment, int vatInfo) throws Exception {
        openReceipt(false);
        long total = Math.round(amount * quantity / 1000.0);
        if (isSaleReceipt()) {
            printStorno(total, quantity, amount, getParams().department, vatInfo,
                    description);
        } else {
            printSale(total, quantity, amount, getParams().department, vatInfo,
                    description, "");
        }
    }

    public void printRecRefundVoid(String description, long amount, int vatInfo)
            throws Exception {
        openReceipt(false);

        printStorno(amount, 1000, 0, getParams().department, vatInfo,
                description);

    }

    public void printRecPackageAdjustment(int adjustmentType,
            String description, String vatAdjustment) throws Exception {
        printRecPackageAdjustment2(adjustmentType, vatAdjustment, 1);
    }

    public void printRecPackageAdjustVoid(int adjustmentType,
            String vatAdjustment) throws Exception {
        printRecPackageAdjustment2(adjustmentType, vatAdjustment, -1);
    }

    public void printRecPackageAdjustment2(int adjustmentType,
            String vatAdjustment, int factor) throws Exception {
        checkDiscountsEnabled();
        PackageAdjustments adjustments = new PackageAdjustments();
        adjustments.parse(vatAdjustment);
        checkAdjustments(adjustmentType, adjustments);
        PackageAdjustment adjustment;

        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:

                adjustments.parse(vatAdjustment);
                checkAdjustments(FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT,
                        adjustments);

                for (int i = 0; i < adjustments.size(); i++) {
                    adjustment = adjustments.getItem(i);
                    printDiscount(-adjustment.amount * factor, adjustment.vat, "");
                }
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:

                adjustments.parse(vatAdjustment);
                checkAdjustments(FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE,
                        adjustments);

                for (int i = 0; i < adjustments.size(); i++) {
                    adjustment = adjustments.getItem(i);
                    printDiscount(adjustment.amount * factor, adjustment.vat, "");
                }
                break;

            default:
                throw new JposException(JposConst.JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + "adjustmentType");
        }
    }

    public void printRecSubtotalAdjustVoid(int adjustmentType, long amount)
            throws Exception {
        checkDiscountsEnabled();
        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:
                printTotalDiscount(amount, 0, "");
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:
                printTotalDiscount(-amount, 0, "");
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_DISCOUNT:
                double d = getSubtotal() * amount;
                amount = MathUtils.round(d / 10000.0);
                printTotalDiscount(amount, 0, "");
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_SURCHARGE:
                d = getSubtotal() * amount;
                amount = MathUtils.round(d / 10000.0);
                printTotalDiscount(amount, 0, "");
                break;

            default:
                throw new JposException(JposConst.JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + "adjustmentType");
        }
    }

    public void printRecItemAdjustmentVoid(int adjustmentType,
            String description, long amount, int vatInfo) throws Exception {
        checkDiscountsEnabled();
        checkAdjustment(adjustmentType, amount);
        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:
                printDiscount(-amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:
                printDiscount(amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_DISCOUNT:
                amount = getItemPercentAdjustmentAmount(amount);
                printDiscount(-amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_SURCHARGE:
                amount = getItemPercentAdjustmentAmount(amount);
                printDiscount(amount, vatInfo, description);
                break;

            default:
                throw new JposException(JposConst.JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + "adjustmentType");
        }
    }

    public void printRecItemRefund(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        openReceipt(false);
        printSale(amount, quantity, unitAmount, getParams().department, vatInfo,
                description, unitName);

    }

    public void printRecItemRefundVoid(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        openReceipt(true);
        printStorno(amount, quantity, unitAmount, getParams().department, vatInfo,
                description);

    }

    public void printRecVoid(String description) throws Exception {
        voidDescription = description;
        cancelled = true;
    }

    public void addPayment(long payment, long payType) throws Exception {
        MethodParameter.checkRange(payType, 0, 15, "payType");
        payments[(int) payType] += payment;
    }

    public long getPaymentAmount() {
        long result = 0;
        for (int i = 0; i < payments.length; i++) {
            result += payments[i];
        }
        return result;
    }

    public boolean isPayed() throws Exception {
        return getPaymentAmount() >= (getSubtotal() - discountAmount);
    }

    public void clearPrePostLine() {
        getParams().clearPrePostLine();
    }

    public String getPreLine() throws Exception {
        String result = getParams().preLine;
        getParams().preLine = "";
        return result;
    }

    public String getPostLine() throws Exception {
        String result = getParams().postLine;
        getParams().postLine = "";
        return result;
    }

    public void addTotal(long amount) throws Exception {
        total += amount;
        logger.debug("Add amount: " + amount + ", total: " + total);
    }

    public long correctQuantity(long price, long quantity, long unitPrice) 
    {
        if (!getParams().quantityCorrectionEnabled){
            return quantity; 
        }
        
        for (;;) {
            double d = unitPrice * quantity;
            long amount = MathUtils.round(d / 1000.0);
            if (amount >= price) {
                break;
            }
            quantity = quantity + 1;
        }
        return quantity;
    }

    public void printStorno(long price, long quantity, long unitPrice,
            int department, int vatInfo, String description) throws Exception {
        if (unitPrice == 0) {
            if (price != 0) {
                quantity = 1000;
                unitPrice = price;
            }
        } else if (quantity == 0) {
            quantity = 1000;
        }
        quantity = correctQuantity(price, quantity, unitPrice);
        doPrintSale(price, quantity, unitPrice, department, vatInfo, description, "", true);
    }

    public void printSale(long price, long quantity, long unitPrice,
            int department, int vatInfo, String description,
            String unitName) throws Exception {
        if (unitPrice == 0) {
            if (price != 0) {
                quantity = 1000;
                unitPrice = price;
            }
        } else if (quantity == 0) {
            quantity = 1000;
        }
        quantity = correctQuantity(price, quantity, unitPrice);
        doPrintSale(price, quantity, unitPrice, department, vatInfo,
                description, unitName, false);
    }

    public void doPrintSale(long price, long quantity, long unitPrice,
            int department, int vatInfo, String description, String unitName,
            boolean isStorno) throws Exception {
        logger.debug(
                "price: " + price
                + ", quantity: " + quantity
                + ", unitPrice: " + unitPrice);

        double d = unitPrice * Math.abs(quantity);
        long amount = getParams().itemTotalAmount == null ?  MathUtils.round((d / 1000.0)) : getParams().itemTotalAmount;

        /*
         if (isStorno) {
         for (int i = 0; i < recItems.size(); i++) {
         Object recItem = recItems.get(i);
         if (recItem instanceof FSSaleReceiptItem) {
         FSSaleReceiptItem item = (FSSaleReceiptItem) recItem;
         if (item.getTotal() > 0) {
         if (item.getTax1() == vatInfo) {
         if (item.getTotalWithVoids() >= price) {
         item.addVoidAmount(price);
         break;
         }
         }
         }
         }
         }
         }
         */
        FSSaleReceiptItem item = null;
        if (getParams().combineReceiptItems) {
            item = findItem(unitPrice, description);
            if (item != null) {
                if (isStorno) {
                    if (item.getQuantity() >= quantity) {
                        item.setQuantity(item.getQuantity() - quantity);
                    } else {
                        item = null;
                    }
                } else {
                    item.setQuantity(item.getQuantity() + quantity);
                }
            }
        }
        if (item == null) {
            item = new FSSaleReceiptItem();
            item.setPrice(unitPrice);
            item.setUnitPrice(unitPrice);
            item.setQuantity(quantity);
            item.setDepartment(department);
            item.setTax1(vatInfo);
            item.setTax2(0);
            item.setTax3(0);
            item.setTax4(0);
            item.setText(description);
            item.setPreLine(getPreLine());
            item.setPostLine(getPostLine());
            item.setUnitName(unitName);
            item.setIsStorno(isStorno);
            
            item.setTotalAmount(getParams().itemTotalAmount);
            getParams().itemTotalAmount = null;
            
            items.add(item);
            recItems.add(item);

        }
        if (!isStorno) {
            lastItem = item;
        }

        if (isStorno) {
            addTotal(-amount);
        } else {
            addTotal(amount);
        }
        clearPrePostLine();
        if (getParams().FSReceiptItemDiscountEnabled) {
            if ((price > 0) && (amount - price) > 0) {
                printDiscount(amount - price, vatInfo, "");
            }
        }
    }

    FSSaleReceiptItem findItem(long price, String text) {
        for (int i = 0; i < items.size(); i++) {
            Object object = items.get(i);
            if (object instanceof FSSaleReceiptItem) {
                FSSaleReceiptItem item = (FSSaleReceiptItem) object;
                if ((item.getPrice() == price) && (item.getText().equalsIgnoreCase(text)) && (!item.getIsStorno())) {
                    return item;
                }
            }
        }
        return null;
    }

    public FSSaleReceiptItem getLastItem() throws Exception {
        if (lastItem == null) {
            throw new Exception("No receipt item available");
        }
        return lastItem;
    }

    public void printDiscount(long amount, int tax1, String text)
            throws Exception {
        logger.debug("printDiscount: " + amount);

        FSDiscount item = new FSDiscount();
        item.setAmount(amount);
        item.setTax1(tax1);
        item.setTax2(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setTax3(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setTax4(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setText(text);

        boolean added = false;
        for (int i = (items.size() - 1); i >= 0; i--) {
            Object object = items.get(i);
            if (object instanceof FSSaleReceiptItem) {
                FSSaleReceiptItem recitem = (FSSaleReceiptItem) object;
                if ((recitem.getTax1() == tax1) && (!recitem.getIsStorno())) {
                    if (amount < 0) {
                        recitem.addDiscount(item);
                        added = true;
                        break;
                    } else if (recitem.getTotal() >= amount) {
                        recitem.addDiscount(item);
                        added = true;
                        break;
                    }
                }
            }
        }
        // split discount
        long discountAmount = amount;
        if ((!added) && (discountAmount > 0)) {
            for (int i = (items.size() - 1); i >= 0; i--) {
                Object object = items.get(i);
                if (object instanceof FSSaleReceiptItem) {
                    FSSaleReceiptItem recitem = (FSSaleReceiptItem) object;
                    if ((recitem.getTax1() == tax1) && (!recitem.getIsStorno())) {
                        long itemDiscount = 0;
                        if (discountAmount > recitem.getTotal()) {
                            itemDiscount = recitem.getTotal();
                        } else {
                            itemDiscount = discountAmount;
                        }

                        FSDiscount discount = new FSDiscount();
                        discount.setAmount(itemDiscount);
                        discount.setTax1(tax1);
                        discount.setTax2(PrinterConst.SMFPTR_TAX_NOTAX);
                        discount.setTax3(PrinterConst.SMFPTR_TAX_NOTAX);
                        discount.setTax4(PrinterConst.SMFPTR_TAX_NOTAX);
                        discount.setText(text);
                        recitem.addDiscount(discount);
                        discountAmount -= itemDiscount;
                        if (discountAmount == 0) {
                            break;
                        }
                    }
                }
            }
        }
        addTotal(-amount);
    }

    public void printTotalDiscount(long amount, int tax1, String text)
            throws Exception {
        if (amount > getSubtotal()) {
            throw new Exception("Discount amount more than receipt total");
        }

        FSDiscount item = new FSDiscount();
        item.setAmount(amount);
        item.setTax1(tax1);
        item.setTax2(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setTax3(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setTax4(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setText(text);
        if (getParams().subAdjustmentOrder == PrinterConst.ADJUSTMENT_ORDER_CORRECT) {
            items.add(item);
        }
        discounts.add(item);
        addTotal(-amount);
    }

    public long getSubtotal() throws Exception {
        return total;
    }

    private void checkPercents(long amount) throws Exception {
        if ((amount < 0) || (amount > 10000)) {
            throw new JposException(JposConst.JPOS_E_EXTENDED,
                    FiscalPrinterConst.JPOS_EFPTR_BAD_ITEM_AMOUNT);
        }
    }

    public void checkAdjustment(int adjustmentType, long amount)
            throws Exception {
        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:
            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_DISCOUNT:
            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_SURCHARGE:
                checkPercents(amount);
                break;

            default:
                throw new JposException(JposConst.JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + "adjustmentType");
        }
    }

    private void checkAdjustments(int adjustmentType,
            PackageAdjustments adjustments) throws Exception {
        PackageAdjustment adjustment;
        for (int i = 0; i < adjustments.size(); i++) {
            adjustment = adjustments.getItem(i);
            checkAdjustment(adjustmentType, adjustment.amount);

        }
    }

    public class FSTLVItem {

        private final byte[] data;

        public FSTLVItem(byte[] data) {
            this.data = data;
        }

        public byte[] getData() {
            return data;
        }
    }

    public void disablePrint() throws Exception {
        disablePrint = true;
    }

    public boolean getDisablePrint() {
        return disablePrint;
    }

    public void fsWriteTLV(byte[] data) throws Exception {
        items.add(new FSTLVItem(data));
    }

    private void fsWriteTag2(int tagId, String tagValue) throws Exception {
        fsWriteTLV(getDevice().getTLVData(tagId, tagValue));
    }

    public void fsWriteTag(int tagId, String tagValue) throws Exception {
        fsWriteTag2(tagId, tagValue);
    }

    public void fsWriteCustomerEmail(String text) throws Exception {
        if (!text.isEmpty()) {
            fsWriteTag2(1008, text);
        }
    }

    public void fsWriteCustomerPhone(String text) throws Exception {
        if (!text.isEmpty()) {
            fsWriteTag2(1008, text);
        }
    }

    public void setDiscountAmount(int amount) throws Exception {
        getDevice().checkDiscountMode(2);
        discountAmount = amount;
    }

    public String getTaxLetter(int tax) {
        String taxLetters = "АБВГДЕ";
        if ((tax < 1) || (tax > 6)) {
            tax = 4;
        }
        return "_" + taxLetters.charAt(tax - 1);
    }

    public void printRecItemAsText(FSSaleReceiptItem item) throws Exception {

        int tax = item.getTax1();
        if (tax == 0) {
            tax = 4;
        }
        if (item.getIsStorno()) {
            getDevice().printText("СТОРНО");
        }

        String line = String.valueOf(item.getPos());
        line = StringUtils.left(line, 4) + item.getText();
        getDevice().printText(line);

        String amountText = StringUtils.amountToString(item.getAmount()) + getTaxLetter(tax);
        if (item.getIsStorno()) {
            amountText = "-" + amountText;
        }

        line = getParams().quantityToStr(item.getQuantity(), item.getUnitName()) + " X "
                + StringUtils.amountToString(item.getPrice()) + " =" + amountText;
        String line1 = "";
        /*
         int department = item.getDepartment();
         if ((department > 0) && (department <= 16)) {
         line1 = getDevice().getDepartmentName(item.getDepartment());
         }
         */
        getDevice().printLines(line1, line);
        printTotalAndTax(item);
    }

    protected void printTotalAndTax(FSSaleReceiptItem item) throws Exception {
        String line;
        int tax = item.getTax1();
        if (tax == 0) {
            tax = 4;
        }
        double taxRate = getDevice().getTaxRate(tax) / 10000.0;
        long taxAmount = (long) ((item.getTotal()) * taxRate / (1 + taxRate) + 0.5);

        line = getDevice().getTaxName(tax);
        line = formatLines(line, StringUtils.amountToString(taxAmount));
        getDevice().printText(line);
    }

    public String formatLines(String line1, String line2) throws Exception {
        return StringUtils.alignLines(line1, line2, getPrinter().getTextLength());
    }

    public void printBarcode(PrinterBarcode barcode) throws Exception {
        items.add(barcode);
    }

    public void printGraphics(PrinterGraphics graphics) throws Exception {
        items.add(graphics);
    }
    
}
