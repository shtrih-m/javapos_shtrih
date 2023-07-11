/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

/**
 *
 * @author V.Kravtsov
 */
import jpos.JposConst;
import jpos.JposException;
import jpos.FiscalPrinterConst;
import com.shtrih.util.CompositeLogger;

import com.shtrih.fiscalprinter.receipt.PrinterReceipt;
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
import com.shtrih.util.MathUtils;
import com.shtrih.util.StringUtils;
import com.shtrih.util.SysUtils;
import com.shtrih.util.Time;

import static jpos.FiscalPrinterConst.FPTR_PS_MONITOR;
import static jpos.FiscalPrinterConst.JPOS_EFPTR_BAD_ITEM_AMOUNT;
import static jpos.FiscalPrinterConst.JPOS_EFPTR_NEGATIVE_TOTAL;
import static jpos.JposConst.JPOS_E_EXTENDED;

public class SalesReceipt extends CustomReceipt implements FiscalReceipt {

    private int receiptType = 0;
    private boolean isOpened = false;
    private final long[] vatAmounts = new long[5];
    private final ReceiptItems items = new ReceiptItems();
    private final PrinterReceipt receipt = new PrinterReceipt();
    private static CompositeLogger logger = CompositeLogger.getLogger(SalesReceipt.class);

    public SalesReceipt(ReceiptContext context, int receiptType) {
        super(context);
        this.receiptType = receiptType;
    }

    public PrinterReceipt getReceipt(){
        return receipt;
    }
    
    public boolean isOpened() {
        return isOpened;
    }

    public void openReceipt(boolean isSale) throws Exception {
        if (!isOpened) {
            if (!isSale) {
                if (receiptType == PrinterConst.SMFP_RECTYPE_SALE) {
                    receiptType = PrinterConst.SMFP_RECTYPE_RETSALE;
                }
            }
            getPrinter().openReceipt(receiptType);

            isOpened = true;
        }
    }

    public void beginFiscalReceipt(boolean printHeader) throws Exception {
        isOpened = false;
        getReceipt().reset();
        items.clear();
        for (int i = 0; i < 5; i++) {
            vatAmounts[i] = 0;
        }
        if (getParams().openReceiptOnBegin) {
            openReceipt(true);
        }
    }

    public void printRecItem(String description, long price, double quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {

        openReceipt(true);
        printPreLine();
        // if unitPrice is zero then we use price and quantity = 1
        long itemPrice = price;
        if (unitPrice == 0) {
            quantity = 1;
        } else {
            if (quantity == 0) {
                quantity = 1;
            }
            itemPrice = unitPrice;
        }
        description = printDescription(description);
        printReceiptItem(description, itemPrice, quantity, vatInfo);

        long amount = Math.round(unitPrice * Math.abs(quantity));
        if (amount > price) {
            printDiscount(amount - price, vatInfo, "");
        }
        printPostLine();
    }

    public void printReceiptItem(String description, long price, double quantity,
            int vatInfo) throws Exception {
        switch (receiptType) {
            case PrinterConst.SMFP_RECTYPE_SALE:
                printSale(price, quantity, getParams().department, vatInfo,
                        description);
                break;

            case PrinterConst.SMFP_RECTYPE_BUY:
                printRefund(price, quantity, getParams().department, vatInfo,
                        description);
                break;

            case PrinterConst.SMFP_RECTYPE_RETSALE:
                printVoidSale(price, quantity, getParams().department,
                        vatInfo, description);
                break;

            case PrinterConst.SMFP_RECTYPE_RETBUY:
                printVoidRefund(price, quantity, getParams().department,
                        vatInfo, description);
                break;

            default:
                throw new Exception("Invalid receipt type");
        }
    }

    public void endFiscalReceipt(boolean printHeader) throws Exception {
        PrinterStatus status = getPrinter().waitForPrinting();
        if (status.getPrinterMode().isReceiptOpened()) {
            if (getReceipt().isCancelled()) {
                getPrinter().cancelReceipt();
                getFiscalDay().cancelFiscalRec();
            } else {
                checkZeroReceipt();
                long[] sum = getReceipt().getPayments();
                CloseRecParams closeParams = new CloseRecParams();
                closeParams.setSum1(sum[0]);
                closeParams.setSum2(sum[1]);
                closeParams.setSum3(sum[2]);
                closeParams.setSum4(sum[3]);
                closeParams.setTax1(0);
                closeParams.setTax2(0);
                closeParams.setTax3(0);
                closeParams.setTax4(0);
                closeParams.setDiscount(0);
                closeParams.setText(getParams().closeReceiptText);
                getPrinter().closeReceipt(closeParams);
                getFiscalDay().closeFiscalRec();
                // Print may not respond for some time
                Time.delay(getParams().recCloseSleepTime);
            }
        } else if (getReceipt().isCancelled()) {
            printText(getParams().receiptVoidText);
        }
    }

    public void printRecRefund(String description, long amount, int vatInfo)
            throws Exception {
        openReceipt(false);
        printPreLine();
        description = printDescription(description);
        printReceiptItem(description, amount, 1, vatInfo);
    }

    public void printRecTotal(long total, long payment, long payType,
            String description) throws Exception {
        long receiptSubtotal = getSubtotal();
        checkTotal(receiptSubtotal, total);
        getReceipt().addPayment(payment, payType);
        printPreLine();
        printPostLine();
    }

    public void printRecItemVoid(String description, long price, double quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        openReceipt(false);
        printPreLine();
        // if unitPrice is zero - use price and quantity = 1
        if (unitPrice == 0) {
            quantity = 1;
        } else {
            if (quantity == 0) {
                quantity = 1;
            }
            price = unitPrice;
        }
        description = printDescription(description);
        printReceiptItem(description, price, quantity, vatInfo);
        printPostLine();
    }

    public void printRecSubtotal(long amount) throws Exception {

        long receiptSubtotal = getSubtotal();
        checkTotal(receiptSubtotal, amount);
        printPreLine();
        printStrings(getParams().subtotalText,
                "=" + StringUtils.amountToString(getSubtotal()));
        printPostLine();
    }

    public void printRecItemAdjustment(int adjustmentType, String description,
            long amount, int vatInfo) throws Exception {
        printPreLine();
        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:
                description = printDescription(description);
                printDiscount(amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:
                description = printDescription(description);
                printCharge(amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_DISCOUNT:
                description = printDescription(description);
                amount = getReceipt().getItemPercentAdjustmentAmount(amount);
                printDiscount(amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_SURCHARGE:
                description = printDescription(description);
                amount = getReceipt().getItemPercentAdjustmentAmount(amount);
                printCharge(amount, vatInfo, description);
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
        printPreLine();
        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:
                description = printDescription(description);
                printDiscount(amount, 0, description);
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:
                description = printDescription(description);
                printCharge(amount, 0, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_DISCOUNT:
                description = printDescription(description);
                amount = Math.round(getSubtotal() * amount / 10000.0);
                printDiscount(amount, 0, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_SURCHARGE:
                description = printDescription(description);
                amount = Math.round(getSubtotal() * amount / 10000.0);
                printCharge(amount, 0, description);
                break;

            default:
                throw new JposException(JposConst.JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + "AdjustmentType");
        }
        printPostLine();
    }

    public void printRecVoidItem(String description, long amount, double quantity,
            int adjustmentType, long adjustment, int vatInfo) throws Exception {
        openReceipt(false);
        description = printDescription(description);
        printStorno(amount, quantity, getParams().department,
                vatInfo, description);
    }

    public void printRecRefundVoid(String description, long amount, int vatInfo)
            throws Exception {
        openReceipt(true);
        description = printDescription(description);
        printStorno(amount, 1, getParams().department, vatInfo,
                description);
    }

    public void printRecPackageAdjustment(int adjustmentType,
            String description, String vatAdjustment) throws Exception {

        PackageAdjustments adjustments = new PackageAdjustments();
        adjustments.parse(vatAdjustment);
        checkAdjustments(adjustmentType, adjustments);
        PackageAdjustment adjustment;
        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:

                adjustments.parse(vatAdjustment);
                checkAdjustments(FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT,
                        adjustments);
                printText(description);
                for (int i = 0; i < adjustments.size(); i++) {
                    adjustment = adjustments.getItem(i);
                    printDiscount(adjustment.amount, adjustment.vat, "");
                }
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:

                adjustments.parse(vatAdjustment);
                checkAdjustments(FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE,
                        adjustments);

                printText(description);
                for (int i = 0; i < adjustments.size(); i++) {
                    adjustment = adjustments.getItem(i);
                    printCharge(adjustment.amount, adjustment.vat, "");
                }
                break;

            default:
                throw new JposException(JposConst.JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + "adjustmentType");
        }
    }

    public void printRecPackageAdjustVoid(int adjustmentType,
            String vatAdjustment) throws Exception {
        PackageAdjustments adjustments = new PackageAdjustments();
        adjustments.parse(vatAdjustment);
        checkAdjustments(adjustmentType, adjustments);
        PackageAdjustment adjustment;

        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:

                adjustments.parse(vatAdjustment);
                checkAdjustments(FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT,
                        adjustments);

                printText("Void discount");
                for (int i = 0; i < adjustments.size(); i++) {
                    adjustment = adjustments.getItem(i);
                    printCharge(adjustment.amount, adjustment.vat, "");
                }
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:

                adjustments.parse(vatAdjustment);
                checkAdjustments(FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE,
                        adjustments);

                printText("Void charge");
                for (int i = 0; i < adjustments.size(); i++) {
                    adjustment = adjustments.getItem(i);
                    printDiscount(adjustment.amount, adjustment.vat, "");
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
        printPreLine();
        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:
                printDiscount(amount, 0, "");
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:
                printCharge(amount, 0, "");
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_DISCOUNT:
                amount = Math.round(getSubtotal() * amount / 10000.0);
                printCharge(amount, 0, "");
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_SURCHARGE:
                amount = Math.round(getSubtotal() * amount / 10000.0);
                printDiscount(amount, 0, "");
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
        printPreLine();
        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:
                description = printDescription(description);
                printCharge(amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:
                description = printDescription(description);
                printDiscount(amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_DISCOUNT:
                description = printDescription(description);
                amount = getReceipt().getItemPercentAdjustmentAmount(amount);
                printCharge(amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_SURCHARGE:
                description = printDescription(description);
                amount = getReceipt().getItemPercentAdjustmentAmount(amount);
                printDiscount(amount, vatInfo, description);
                break;

            default:
                throw new JposException(JposConst.JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + "adjustmentType");
        }
    }

    public void printRecItemRefund(String description, long amount,
            double quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        openReceipt(false);
        printPreLine();
        // if unitPrice is zero then we use price and quantity = 1
        if (unitAmount == 0) {
            quantity = 1;
        } else {
            if (quantity == 0) {
                quantity = 1;
            }
            amount = unitAmount;
        }
        description = printDescription(description);
        printReceiptItem(description, amount, quantity, vatInfo);
        printPostLine();
    }

    public void printRecItemRefundVoid(String description, long amount,
            double quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        openReceipt(true);
        printPreLine();
        // if unitPrice is zero - use price and quantity = 1
        if (unitAmount == 0) {
            quantity = 1;
        } else {
            if (quantity == 0) {
                quantity = 1;
            }
            amount = unitAmount;
        }
        description = printDescription(description);
        printStorno(amount, quantity, getParams().department,
                vatInfo, description);
        printPostLine();
    }

    public void printRecVoid(String description) throws Exception {
        printText(description);
        getReceipt().cancel();
    }

    public boolean isPayed() {
        return getReceipt().isPayed();
    }

    public void printSale(long price, double quantity, int department,
            int vatInfo, String description) throws Exception {
        PriceItem item = new PriceItem();
        item.setPrice(price);
        item.setQuantity(quantity);
        item.setDepartment(department);
        item.setTax1(vatInfo);
        item.setTax2(0);
        item.setTax3(0);
        item.setTax4(0);
        item.setText(description);
        getPrinter().printSale(item);
        vatAmounts[vatInfo] = vatAmounts[vatInfo] + item.getAmount();
        getReceipt().printSale(item);
    }

    public void printVoidSale(long price, double quantity, int department,
            int vatInfo, String description) throws Exception {
        PriceItem item = new PriceItem();
        item.setPrice(price);
        item.setQuantity(quantity);
        item.setDepartment(department);
        item.setTax1(vatInfo);
        item.setTax2(0);
        item.setTax3(0);
        item.setTax4(0);
        item.setText(description);
        getPrinter().printVoidSale(item);
        getReceipt().printSaleRefund(item);
    }

    public void printRefund(long price, double quantity, int department,
            int vatInfo, String description) throws Exception {
        PriceItem item = new PriceItem();
        item.setPrice(price);
        item.setQuantity(quantity);
        item.setDepartment(department);
        item.setTax1(vatInfo);
        item.setTax2(0);
        item.setTax3(0);
        item.setTax4(0);
        item.setText(description);
        getPrinter().printRefund(item);
        getReceipt().printSaleRefund(item);
    }

    public void printVoidRefund(long price, double quantity, int department,
            int vatInfo, String description) throws Exception {
        PriceItem item = new PriceItem();
        item.setPrice(price);
        item.setQuantity(quantity);
        item.setDepartment(department);
        item.setTax1(vatInfo);
        item.setTax2(0);
        item.setTax3(0);
        item.setTax4(0);
        item.setText(description);
        getPrinter().printVoidRefund(item);
        getReceipt().printSale(item);
    }

    public void printStorno(long price, double quantity, int department,
            int vatInfo, String description) throws Exception {
        long vatAmount = PrinterAmount.getAmount(price, quantity);
        if ((vatInfo == 0) || (vatAmount <= vatAmounts[vatInfo])) {
            PriceItem item = new PriceItem();
            item.setPrice(price);
            item.setQuantity(quantity);
            item.setDepartment(department);
            item.setTax1(vatInfo);
            item.setTax2(0);
            item.setTax3(0);
            item.setTax4(0);
            item.setText(description);
            getPrinter().printVoidItem(item);
            getReceipt().printStorno(item);
            vatAmounts[vatInfo] = vatAmounts[vatInfo] - vatAmount;
        } else {
            PriceItem item = new PriceItem();
            item.setPrice(price);
            item.setQuantity(quantity);
            item.setDepartment(department);
            item.setTax1(vatInfo);
            item.setTax2(0);
            item.setTax3(0);
            item.setTax4(0);
            item.setText(description);
            items.add(new SaleReceiptItem(item));
        }
    }

    public void printStornoItems() throws Exception {
        logger.debug("printStornoItems");

        int index = 0;
        long vatAmount = 0;
        while (index < items.size()) {
            SaleReceiptItem item = (SaleReceiptItem) items.get(index);
            logger.debug("item.getAmount() = "
                    + String.valueOf(item.getAmount()));
            logger.debug("vatAmounts[item.getTax1()] = "
                    + String.valueOf(vatAmounts[item.getTax1()]));

            if (item.getAmount() <= vatAmounts[item.getTax1()]) {
                getPrinter().printVoidItem(item.getItem());
                getReceipt().printStorno(item.getItem());
                vatAmounts[item.getTax1()] = vatAmounts[item.getTax1()]
                        - item.getAmount();
                items.remove(item);
            } else {
                index++;
            }
        }
    }

    public void printDiscount(long amount, int tax1, String text)
            throws Exception {
        logger.debug("printDiscount");
        AmountItem item = new AmountItem();
        item.setAmount(amount);
        item.setTax1(tax1);
        item.setTax2(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setTax3(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setTax4(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setText(text);
        getPrinter().printDiscount(item);
        getReceipt().printDiscount(item);
        vatAmounts[tax1] = vatAmounts[tax1] - amount;
        printStornoItems();
    }

    public void printCharge(long amount, int tax1, String text)
            throws Exception {
        logger.debug("printCharge");
        AmountItem item = new AmountItem();
        item.setAmount(amount);
        item.setTax1(tax1);
        item.setTax2(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setTax3(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setTax4(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setText(text);
        getPrinter().printCharge(item);
        getReceipt().printCharge(item);
        vatAmounts[tax1] = vatAmounts[tax1] + amount;
        printStornoItems();
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
}
