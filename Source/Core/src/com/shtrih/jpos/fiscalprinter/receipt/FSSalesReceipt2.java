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

import com.shtrih.jpos.fiscalprinter.receipt.template.ParsingException;
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
import com.shtrih.fiscalprinter.command.FSReceiptDiscount;
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
import java.util.Locale;
import static jpos.FiscalPrinterConst.FPTR_PS_MONITOR;
import static jpos.FiscalPrinterConst.JPOS_EFPTR_BAD_ITEM_AMOUNT;
import static jpos.FiscalPrinterConst.JPOS_EFPTR_NEGATIVE_TOTAL;
import static jpos.JposConst.JPOS_E_EXTENDED;
import com.shtrih.fiscalprinter.TLVReader;
import com.shtrih.fiscalprinter.TLVInfo;

public class FSSalesReceipt2 extends CustomReceipt implements FiscalReceipt {

    protected PriceItem lastItem = null;
    protected long lastItemDiscountSum = 0;
    protected boolean lastItemFooterPrinted = false;
    private int receiptType = 0;
    private boolean isOpened = false;
    private boolean disablePrint = false;
    private String voidDescription = "";
    private final long[] payments = new long[5]; // payment amounts
    private Vector<String> messages = new Vector();
    private int discountAmount = 0;

    private static CompositeLogger logger = CompositeLogger.getLogger(FSSalesReceipt2.class);

    public FSSalesReceipt2(ReceiptContext context, int receiptType) {
        super(context);
        this.receiptType = receiptType;
    }

    public boolean isSaleReceipt() {
        return receiptType == PrinterConst.SMFP_RECTYPE_SALE;
    }

    public void printRecMessage(int station, FontNumber font, String message)
            throws Exception {
        printPreLine();
        getDevice().printText(station, message, font);
        printPostLine();
    }

    public void printNormal(int station, String data) throws Exception {
        printRecMessage(station, FontNumber.getNormalFont(), data);
    }

    public SMFiscalPrinter getDevice() throws Exception {
        return getPrinter().getPrinter();
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void clearReceipt() throws Exception {
        discountAmount = 0;
        isOpened = false;
        lastItem = null;
        lastItemDiscountSum = 0;
        lastItemFooterPrinted = false;
        for (int i = 0; i < 5; i++) {
            payments[i] = 0;
        }
        voidDescription = "";
        disablePrint = false;
        messages.clear();
        cancelled = false;
    }

    public void beginFiscalReceipt(boolean printHeader) throws Exception 
    {
        getPrinter().getPrinter().printFSHeader();
        
        clearReceipt();
        getPrinter().openReceipt(receiptType);
        getPrinter().waitForPrinting();
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

    protected void setLastItem(PriceItem lastItem) {
        this.lastItem = lastItem;
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
        if (isOpened) 
        {
            if (cancelled) {
                try {
                    PrinterStatus status = getDevice().waitForPrinting();
                    if (!status.getPrinterMode().isReceiptOpened()) {
                        getPrinter().openReceipt(receiptType);
                    }
                    getPrinter().waitForPrinting();
                    getDevice().cancelReceipt();
                    getPrinter().printText(voidDescription);
                    getPrinter().printText(getParams().receiptVoidText);
                } catch (Exception e) {
                    logger.error("Cancel receipt: " + e.getMessage());
                }
                getFiscalDay().cancelFiscalRec();
                clearReceipt();
            } else {
                correctPayments();
                if (lastItem != null && !lastItemFooterPrinted) {
                    printTotalAndTax(lastItem);
                }
                if (disablePrint) {
                    getDevice().disablePrint();
                }

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
                getPrinter().getPrinter().closeReceipt(closeParams);
                getFiscalDay().closeFiscalRec();

                if (!disablePrint) {
                    for (int i = 0; i < messages.size(); i++) {
                        getDevice().printText(messages.get(i));
                    }
                }
            }
        }
    }

    public void printRecRefund(String description, long amount, int vatInfo)
            throws Exception {
        openReceipt(PrinterConst.SMFP_RECTYPE_RETSALE);
        printSale(amount, 1000, amount, getParams().department, vatInfo,
                description);
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

    protected String formatStrings(String line1, String line2) throws Exception {
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
        if (lastItem != null && !lastItemFooterPrinted) {
            printTotalAndTax(lastItem);
        }
        checkTotal(getSubtotal(), amount);
        getDevice().printText(formatStrings(getParams().subtotalText,
                "=" + StringUtils.amountToString(getSubtotal())));
    }

    public long getItemPercentAdjustmentAmount(long amount) throws Exception {
        double d = getLastItem().getAmount() * amount;
        return MathUtils.round(d / 10000);
    }

    public void checkDiscountsEnabled() throws Exception {
        if (!getParams().FSDiscountEnabled) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL,
                    Localizer.getString("adjustments forbidden with FS"));
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
        if (lastItem != null && !lastItemFooterPrinted) {
            printTotalAndTax(lastItem);
        }
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
            printStorno(0, quantity, amount, getParams().department, vatInfo,
                    description);
        } else {
            printSale(0, quantity, amount, getParams().department, vatInfo,
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
        checkDiscountsEnabled();
        PackageAdjustments adjustments = new PackageAdjustments();
        adjustments.parse(vatAdjustment);
        checkAdjustments(adjustmentType, adjustments);
        PackageAdjustment adjustment;
        if (lastItem != null && !lastItemFooterPrinted) {
            printTotalAndTax(lastItem);
        }
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
        if (lastItem != null && !lastItemFooterPrinted) {
            printTotalAndTax(lastItem);
        }
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
        checkDiscountsEnabled();
        checkAdjustment(adjustmentType, amount);
        if (lastItem != null && !lastItemFooterPrinted) {
            printTotalAndTax(lastItem);
        }
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
        MethodParameter.checkRange(payType, 0, 15, "payType");
        payments[(int) payType] += payment;
    }

    public long getPaymentAmount() {
        return payments[0] + payments[1] + payments[2] + payments[3];
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

        printPreLine();

        PriceItem item = new PriceItem();
        item.setPrice(unitPrice);
        item.setQuantity(Math.abs(quantity));
        item.setDepartment(department);
        item.setTax1(vatInfo);
        item.setTax2(0);
        item.setTax3(0);
        item.setTax4(0);
        item.setText(description);
        if (lastItem != null && !lastItemFooterPrinted) {
            printTotalAndTax(lastItem);
        }
        lastItem = item;
        lastItemDiscountSum = 0;
        lastItemFooterPrinted = false;

        if (!getParams().FSCombineItemAdjustments) {
            printRecItemAsText(item, quantity);
            item.setText("//" + description);
        }

        if (quantity > 0) {
            if (isSaleReceipt()) {
                getDevice().printSale(item);
            } else {
                getDevice().printVoidSale(item);
            }
        } else {
            getDevice().printVoidItem(item);
        }
        if (getParams().FSReceiptItemDiscountEnabled) {
            double d = Math.abs(unitPrice) * Math.abs(quantity);
            long amount = MathUtils.round((d / 1000));
            if ((price > 0)&&(amount - price) > 0) {
                printDiscount(amount - price, 0, "");
            }
        }
        printPostLine();
    }

    public String formatLines(String line1, String line2) throws Exception {
        return StringUtils.alignLines(line1, line2, getPrinter().getTextLength());
    }

    public String getTaxLetter(int tax) {
        String taxLetters = "АБВГДЕ";
        if ((tax < 1) || (tax > 6)) {
            tax = 4;
        }
        return "_" + taxLetters.charAt(tax - 1);
    }

    public void printRecItemAsText(PriceItem item, long quantity) throws Exception {

        int tax = item.getTax1();
        if (tax == 0) {
            tax = 4;
        }

        getDevice().printText(item.getText());

        String line = StringUtils.quantityToStr(item.getQuantity()) + " X "
                + StringUtils.amountToString(item.getPrice());
        String line1 = "";
        if (quantity < 0) {
            line1 = "СТОРНО";
        }
        line = formatLines(line1, line);
        getDevice().printText(line);

        line = "";
        int department = item.getDepartment();
        if ((department > 0) && (department <= 16)) {
            line = getDevice().getDepartmentName(item.getDepartment());
        }
        line = formatLines(line, "=" + StringUtils.amountToString(item.getAmount())
                + getTaxLetter(tax));
        getDevice().printText(line);
    }

    protected void printTotalAndTax(PriceItem item) throws Exception {

        if (getParams().FSCombineItemAdjustments) {
            return;
        }

        if (lastItemDiscountSum != 0) {
            getDevice().printText(formatStrings(" ", "=" + StringUtils.amountToString(item.getAmount() - lastItemDiscountSum)));
        }

        String line;
        int tax = item.getTax1();
        if (tax == 0) {
            tax = 4;
        }
        double taxRate = getDevice().getTaxRate(tax) / 10000.0;
        long taxAmount = (long) ((item.getAmount() - lastItemDiscountSum) * taxRate / (1 + taxRate) + 0.5);

        line = getDevice().getTaxName(tax);
        line = formatLines(line, StringUtils.amountToString(taxAmount));
        getDevice().printText(line);
        lastItemFooterPrinted = true;
    }

    public void printPreLine() throws Exception {

        String preLine = getParams().preLine;
        if (preLine.length() > 0) {
            getDevice().printText(preLine);
        }
        getParams().clearPreLine();
    }

    public void printPostLine() throws Exception {
        String text = getParams().postLine;
        if (text.length() > 0) {
            getDevice().printText(text);
        }
        getParams().clearPostLine();
    }

    public PriceItem getLastItem() throws Exception {
        if (lastItem == null) {
            throw new Exception("No receipt item available");
        }
        return lastItem;
    }

    public void printDiscount(long amount, int tax1, String text)
            throws Exception {
        logger.debug("printDiscount: " + amount);

        AmountItem item = new AmountItem();
        item.setAmount(Math.abs(amount));
        item.setText(text);
        item.setTax1(tax1);
        item.setTax2(0);
        item.setTax3(0);
        item.setTax4(0);

        String line = formatStrings(text, "=" + StringUtils.amountToString(amount));

        if (amount > 0) {
            getDevice().printDiscount(item);
        } else {
            getDevice().printCharge(item);
        }
        lastItemDiscountSum += amount;
        if (!getParams().FSCombineItemAdjustments) {
            if (amount > 0) {
                getDevice().printText("СКИДКА");
                getDevice().printText(line);
            } else {
                getDevice().printText("НАДБАВКА");
                getDevice().printText(line);
            }
        }
    }

    public void printTotalDiscount(long amount, int tax1, String text)
            throws Exception {

        FSReceiptDiscount item = new FSReceiptDiscount();
        item.setSysPassword(getDevice().getUsrPassword());
        if (amount > 0) {
            item.setDiscount(Math.abs(amount));
            item.setCharge(0);
        } else {
            item.setDiscount(0);
            item.setCharge(Math.abs(amount));
        }
        item.setTax1(tax1);
        item.setName(text);
        getDevice().fsReceiptDiscount(item);
    }

    public long getSubtotal() throws Exception {
        return getDevice().getSubtotal();
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
        getDevice().check(getDevice().fsWriteTLV(data));

        TLVReader reader = new TLVReader();
        reader.read(data);
        Vector<String> lines = reader.getPrintText();
        messages.addAll(lines);
    }

    public void fsWriteTag(int tagId, String tagValue) throws Exception {
        getDevice().check(getDevice().fsWriteTag(tagId, tagValue));
        if (getParams().FSPrintTags) {
            messages.add(TLVInfo.getTagPrintName(tagId) + ": " + tagValue);
        }
    }

    public void fsWriteCustomerEmail(String text) throws Exception {
        if (!text.isEmpty()) {
            getDevice().check(getDevice().fsWriteTag(1008, text));
            if (getParams().FSPrintTags) {
                messages.add("Email покупателя: " + text);
            }
        }
    }

    public void fsWriteCustomerPhone(String text) throws Exception {
        if (!text.isEmpty()) {
            getDevice().check(getDevice().fsWriteTag(1008, text));
            if (getParams().FSPrintTags) {
                messages.add("Телефон покупателя: " + text);
            }
        }
    }

    public void setDiscountAmount(int amount) throws Exception {
        getDevice().checkDiscountMode(2);
        discountAmount = amount;
    }
}
