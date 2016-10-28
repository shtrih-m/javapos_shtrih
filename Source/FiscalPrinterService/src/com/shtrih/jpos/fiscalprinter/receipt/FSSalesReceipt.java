/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Vector;

import jpos.JposConst;
import jpos.JposException;
import jpos.FiscalPrinterConst;

import com.shtrih.util.MathUtils;
import com.shtrih.util.CompositeLogger;
import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.AmountItem;
import com.shtrih.fiscalprinter.command.CloseRecParams;
import com.shtrih.fiscalprinter.command.PriceItem;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.command.PrinterStatus;
import com.shtrih.fiscalprinter.receipt.PrinterAmount;
import com.shtrih.fiscalprinter.receipt.ReceiptItems;
import com.shtrih.fiscalprinter.receipt.SaleReceiptItem;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.jpos.fiscalprinter.PackageAdjustment;
import com.shtrih.jpos.fiscalprinter.PackageAdjustments;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.util.Localizer;
import com.shtrih.util.MethodParameter;
import com.shtrih.util.StringUtils;
import com.shtrih.util.SysUtils;
import static jpos.FiscalPrinterConst.FPTR_PS_MONITOR;
import static jpos.FiscalPrinterConst.JPOS_EFPTR_BAD_ITEM_AMOUNT;
import static jpos.FiscalPrinterConst.JPOS_EFPTR_NEGATIVE_TOTAL;
import static jpos.JposConst.JPOS_E_EXTENDED;

public class FSSalesReceipt extends CustomReceipt implements FiscalReceipt {

    private FSSaleReceiptItem lastItem = null;
    private long total = 0;
    private int receiptType = 0;
    private boolean isOpened = false;
    private String voidDescription = "";
    private final Vector items = new Vector();
    private final Vector recItems = new Vector();
    private final long[] payments = new long[5]; // payment amounts
    private final FSDiscounts discounts = new FSDiscounts();

    private static CompositeLogger logger = CompositeLogger.getLogger(FSSalesReceipt.class);

    public FSSalesReceipt(ReceiptContext context, int receiptType) {
        super(context);
        this.receiptType = receiptType;
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

    public void clearReceipt() throws Exception {
        total = 0;
        isOpened = false;
        lastItem = null;
        items.clear();
        recItems.clear();
        discounts.clear();
        for (int i = 0; i < 5; i++) {
            payments[i] = 0;
        }
        voidDescription = "";
    }

    public void beginFiscalReceipt(boolean printHeader) throws Exception {
        clearReceipt();
    }

    public void openReceipt(int receiptType) throws Exception {
        if (!isOpened) {
            isOpened = true;
            this.receiptType = receiptType;
        }
    }

    public void printRecItem(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {

        openReceipt(PrinterConst.SMFP_RECTYPE_SALE);
        printSale(price, quantity, unitPrice, getParams().department, vatInfo,
                description);
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

    public void printReceiptItems() throws Exception {
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            if (item instanceof FSSaleReceiptItem) {
                printFSSale((FSSaleReceiptItem) item);
            }
            if (item instanceof FSTextReceiptItem) {
                printFSText((FSTextReceiptItem) item);
            }
            if (item instanceof FSTLVItem) {
                FSTLVItem tlvItem = (FSTLVItem) item;
                SMFiscalPrinter printer = getPrinter().getPrinter();
                printer.fsWriteTLV(tlvItem.getData());
            }
        }
    }

    public void correctPayments() throws Exception {
        long paidAmount = 0;
        for (int i = 1; i <= 3; i++) {
            if (paidAmount + payments[i] > getSubtotal()) {
                payments[i] = getSubtotal() - paidAmount;
            }
            paidAmount += payments[i];
        }
    }

    public void endFiscalReceipt(boolean printHeader) throws Exception {
        if (isOpened) {
            if (cancelled) {
                PrinterStatus status = getDevice().waitForPrinting();
                if (status.getPrinterMode().isReceiptOpened()) {
                    getDevice().cancelReceipt();
                }
                getPrinter().printText(voidDescription);
                getPrinter().printText(getParams().receiptVoidText);
                getFiscalDay().cancelFiscalRec();
                clearReceipt();
            } else {
                getPrinter().openReceipt(receiptType);
                getPrinter().waitForPrinting();

                addItemsDiscounts();
                printReceiptItems();

                correctPayments();
                CloseRecParams closeParams = new CloseRecParams();
                closeParams.setSum1(payments[0]);
                closeParams.setSum2(payments[1]);
                closeParams.setSum3(payments[2]);
                closeParams.setSum4(payments[3]);
                closeParams.setTax1(0);
                closeParams.setTax2(0);
                closeParams.setTax3(0);
                closeParams.setTax4(0);
                closeParams.setDiscount(0);
                closeParams.setText(getParams().closeReceiptText);
                getPrinter().getPrinter().closeReceipt(closeParams);
                getFiscalDay().closeFiscalRec();
            }
        }
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

    public void printFSDiscount(FSDiscount discount)
            throws Exception {
        AmountItem item = new AmountItem();
        item.setAmount(Math.abs(discount.getAmount()));
        item.setText(discount.getText());
        item.setTax1(discount.getTax1());
        item.setTax2(discount.getTax2());
        item.setTax3(discount.getTax3());
        item.setTax4(discount.getTax4());
        if (discount.getAmount() > 0) {
            getDevice().printDiscount(item);
        } else {
            getDevice().printCharge(item);
        }
    }

    public void printFSSale(FSSaleReceiptItem item) throws Exception {
        String preLine = item.getPreLine();
        if (preLine.length() > 0) {
            getDevice().printText(preLine);
        }

        long quantity = item.getItem().getQuantity();
        item.getItem().setQuantity(Math.abs(quantity));
        if (quantity > 0) {
            if (isSaleReceipt()) {
                getDevice().printSale(item.getItem());
            } else {
                getDevice().printVoidSale(item.getItem());
            }
        } else {
            getDevice().printVoidItem(item.getItem());
        }

        String postLine = item.getPostLine();
        if (postLine.length() > 0) {
            getDevice().printText(postLine);
        }
        // Print discounts
        FSDiscounts discounts = item.getDiscounts();
        for (int i = 0; i < discounts.size(); i++) {
            FSDiscount discount = (FSDiscount) discounts.get(i);
            printFSDiscount(discount);
        }
    }

    public void printRecRefund(String description, long amount, int vatInfo)
            throws Exception {
        openReceipt(PrinterConst.SMFP_RECTYPE_RETSALE);
        printSale(amount, 1000, amount, getParams().department, vatInfo,
                description);
    }

    public void addTextItem(String text) throws Exception {
        FSTextReceiptItem item = new FSTextReceiptItem();
        item.text = text;
        item.preLine = getPreLine();
        item.postLine = getPostLine();
        items.add(item);
    }

    public void printRecTotal(long total, long payment, long payType,
            String description) throws Exception {
        checkTotal(getSubtotal(), total);
        addPayment(payment, payType);
        clearPrePostLine();
    }

    public void printRecItemVoid(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        openReceipt(PrinterConst.SMFP_RECTYPE_RETSALE);

        if (isSaleReceipt()) {
            printStorno(price, quantity, unitPrice, getParams().department,
                    vatInfo, description);
        } else {
            printSale(price, quantity, unitPrice, getParams().department,
                    vatInfo, description);
        }
    }

    private String formatStrings(String line1, String line2) throws Exception {
        int len;
        String S = "";
        len = getModel().getTextLength(getParams().getFont())
                - line2.length();

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
        addTextItem(formatStrings(getParams().subtotalText,
                "=" + StringUtils.amountToString(getSubtotal())));
    }

    public long getItemPercentAdjustmentAmount(long amount) throws Exception {
        double d = getLastItem().getItem().getAmount() * amount;
        return MathUtils.round(d / 10000);
    }

    public void printRecItemAdjustment(int adjustmentType, String description,
            long amount, int vatInfo) throws Exception {
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

        checkAdjustment(adjustmentType, amount);
        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:
                printTotalDiscount(amount, 0, description);
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:
                printTotalDiscount(-amount, 0, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_DISCOUNT:
                amount = MathUtils.round(((double) getSubtotal() * amount) / 10000);
                printTotalDiscount(amount, 0, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_SURCHARGE:
                amount = MathUtils.round(((double) getSubtotal() * amount) / 10000);
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
        openReceipt(PrinterConst.SMFP_RECTYPE_RETSALE);

        if (isSaleReceipt()) {
            printStorno(amount, quantity, 0, getParams().department, vatInfo,
                    description);
        } else {
            printSale(amount, quantity, 0, getParams().department, vatInfo,
                    description);
        }

    }

    public void printRecRefundVoid(String description, long amount, int vatInfo)
            throws Exception {
        openReceipt(PrinterConst.SMFP_RECTYPE_RETSALE);

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
        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:
                printTotalDiscount(amount, 0, "");
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:
                printTotalDiscount(-amount, 0, "");
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_DISCOUNT:
                double d = getSubtotal() * amount;
                amount = MathUtils.round(d / 10000);
                printTotalDiscount(amount, 0, "");
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_SURCHARGE:
                d = getSubtotal() * amount;
                amount = MathUtils.round(d / 10000);
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
        openReceipt(PrinterConst.SMFP_RECTYPE_RETSALE);
        printSale(amount, quantity, unitAmount, getParams().department, vatInfo,
                description);

    }

    public void printRecItemRefundVoid(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        openReceipt(PrinterConst.SMFP_RECTYPE_SALE);
        printStorno(amount, quantity, unitAmount, getParams().department, vatInfo,
                description);

    }

    public void printRecVoid(String description) throws Exception {
        voidDescription = description;
        cancelled = true;
    }

    public void addPayment(long payment, long payType) throws Exception {
        MethodParameter.checkRange(payType, 0, 3, "payType");
        payments[(int) payType] += payment;
    }

    public long getPaymentAmount() {
        return payments[0] + payments[1] + payments[2] + payments[3];
    }

    public boolean isPayed() throws Exception {
        return getPaymentAmount() >= getSubtotal();
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

    public long correctQuantity(long price, long quantity, long unitPrice) {
        for (;;) {
            double d = unitPrice * quantity;
            long amount = MathUtils.round(d / 1000);
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
            quantity = 1000;
            unitPrice = price;
        } else if (quantity == 0) {
            quantity = 1000;
        }
        quantity = correctQuantity(price, quantity, unitPrice);
        doPrintSale(price, -quantity, unitPrice, department, vatInfo, description);
    }

    public void printSale(long price, long quantity, long unitPrice,
            int department, int vatInfo, String description) throws Exception {
        if (unitPrice == 0) {
            quantity = 1000;
            unitPrice = price;
        } else if (quantity == 0) {
            quantity = 1000;
        }
        quantity = correctQuantity(price, quantity, unitPrice);
        doPrintSale(price, quantity, unitPrice, department, vatInfo, description);
    }

    public void doPrintSale(long price, long quantity, long unitPrice,
            int department, int vatInfo, String description) throws Exception {
        logger.debug(
                "price: " + price
                + ", quantity: " + quantity
                + ", unitPrice: " + unitPrice);

        long factor = 1;
        if (quantity < 0) {
            factor = -1;
        }
        double d = unitPrice * Math.abs(quantity);
        long amount = MathUtils.round((d / 1000));

        if (quantity < 0) {
            for (int i = 0; i < recItems.size(); i++) {
                Object recItem = recItems.get(i);
                if (recItem instanceof FSSaleReceiptItem) {
                    FSSaleReceiptItem item = (FSSaleReceiptItem) recItem;
                    if (item.getTotal() > 0) {
                        if (item.getItem().getTax1() == vatInfo) {
                            if (item.getTotalWithVoids() >= price) {
                                item.addVoidAmount(price);
                                break;
                            }
                        }
                    }
                }
            }
        }

        PriceItem item = new PriceItem();
        item.setPrice(unitPrice);
        item.setQuantity(quantity);
        item.setDepartment(department);
        item.setTax1(vatInfo);
        item.setTax2(0);
        item.setTax3(0);
        item.setTax4(0);
        item.setText(description);
        lastItem = new FSSaleReceiptItem(item, getPreLine(), getPostLine());
        items.add(lastItem);
        recItems.add(lastItem);
        addTotal(amount * factor);
        clearPrePostLine();

        if ((amount - price) > 0) {
            printDiscount(amount - price, 0, "");
        }
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
        if (amount > getLastItem().getTotal()) {
            throw new Exception("Discount amount more than receipt item amount");
        }

        FSDiscount item = new FSDiscount();
        item.setAmount(amount);
        item.setTax1(tax1);
        item.setTax2(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setTax3(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setTax4(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setText(text);
        getLastItem().addDiscount(item);
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
        discounts.add(item);
        total -= amount;
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

    public void fsWriteTLV(byte[] data) throws Exception {
        items.add(new FSTLVItem(data));
    }
}

