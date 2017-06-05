/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

/**
 *
 * @author V.Kravtsov
 */
import jpos.FiscalPrinterConst;
import jpos.JposConst;
import jpos.JposException;

import com.shtrih.util.CompositeLogger;

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
import static jpos.FiscalPrinterConst.FPTR_PS_MONITOR;
import static jpos.FiscalPrinterConst.JPOS_EFPTR_BAD_ITEM_AMOUNT;
import static jpos.FiscalPrinterConst.JPOS_EFPTR_NEGATIVE_TOTAL;
import static jpos.JposConst.JPOS_E_EXTENDED;

public class SalesReceipt extends CustomReceipt implements FiscalReceipt {

    private int receiptType = 0;
    private boolean isOpened = false;
    private boolean hasItems = false;
    private final long[] vatAmounts = new long[5];
    private final ReceiptItems items = new ReceiptItems();
    private static CompositeLogger logger = CompositeLogger.getLogger(SalesReceipt.class);

    public SalesReceipt(ReceiptContext context, int receiptType) {
        super(context);
        this.receiptType = receiptType;
    }

    public boolean isOpened() {
        return isOpened;
    }

    private void sleep(long millis) {
        try {
            SysUtils.sleep(millis);
        } catch (InterruptedException e) {
            // Restore the interrupted status
            logger.error("InterruptedException", e);
            Thread.currentThread().interrupt();
        }
    }

    public void openReceipt(int receiptType) throws Exception {
        if (!isOpened) {
            getPrinter().openReceipt(receiptType);
            this.receiptType = receiptType;
            isOpened = true;
        }
    }

    public void beginFiscalReceipt(boolean printHeader) throws Exception {
        isOpened = false;
        hasItems = false;
        getReceipt().reset();
        items.clear();
        for (int i = 0; i < 5; i++) {
            vatAmounts[i] = 0;
        }
        if (getParams().openReceiptType == SmFptrConst.SMFPTR_OPEN_RECEIPT_BEGIN) {
            openReceipt(receiptType);
        }
    }

    public void printRecItem(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {

        openReceipt(PrinterConst.SMFP_RECTYPE_SALE);
        getPrinter().printPreLine();
        // if unitPrice is zero then we use price and quantity = 1000
        long itemPrice = price;
        if (unitPrice == 0) {
            quantity = 1000;
        } else {
            if (quantity == 0) {
                quantity = 1000;
            }
            itemPrice = unitPrice;
        }
        description = getPrinter().printDescription(description);

        // receipt was just opened
        if (!hasItems) {
            printSale(itemPrice, quantity, getParams().department, vatInfo,
                    description);
            hasItems = true;
        } else {
            switch (receiptType) {
                case PrinterConst.SMFP_RECTYPE_SALE:
                    printSale(itemPrice, quantity, getParams().department, vatInfo,
                            description);
                    break;

                case PrinterConst.SMFP_RECTYPE_RETSALE:
                    printStorno(itemPrice, quantity, getParams().department,
                            vatInfo, description);
                    break;
                default:
                    throw new Exception("Invalid receipt type");
            }
        }
        double d = unitPrice * Math.abs(quantity);
        long amount = MathUtils.round((d / 1000.0));
        if (amount > price)
        {
            printDiscount(amount-price, vatInfo, "");
        }
        getPrinter().printPostLine();
    }

    public void endFiscalReceipt(boolean printHeader) throws Exception {
        PrinterStatus status = getPrinter().getPrinter().waitForPrinting();
        if (status.getPrinterMode().isReceiptOpened()) 
        {
            if (getReceipt().isCancelled()) {
                getPrinter().getPrinter().cancelReceipt();
                getFiscalDay().cancelFiscalRec();
            } else {
                getPrinter().checkZeroReceipt();
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
                getPrinter().getPrinter().closeReceipt(closeParams);
                getFiscalDay().closeFiscalRec();
                // Print may not respond for some time
                sleep(getParams().recCloseSleepTime);
            }
        } else if (getReceipt().isCancelled()) {
            getPrinter().printText(getParams().receiptVoidText);
        }
    }

    public void printRecRefund(String description, long amount, int vatInfo)
            throws Exception {
        openReceipt(PrinterConst.SMFP_RECTYPE_RETSALE);
        getPrinter().printPreLine();
        description = getPrinter().printDescription(description);

        // receipt was just opened
        if (!hasItems) {
            printSaleRefund(amount, 1000, getParams().department, vatInfo,
                    description);
            hasItems = true;
        } else {
            switch (receiptType) {
                case PrinterConst.SMFP_RECTYPE_SALE:
                    printStorno(amount, 1000, getParams().department, vatInfo,
                            description);
                    break;

                case PrinterConst.SMFP_RECTYPE_RETSALE:
                    printSaleRefund(amount, 1000, getParams().department,
                            vatInfo, description);
                    break;
            }
        }
    }

    public void printRecTotal(long total, long payment, long payType,
            String description) throws Exception {
        long receiptSubtotal = getPrinter().getSubtotal();
        checkTotal(receiptSubtotal, total);
        getReceipt().addPayment(payment, payType);
        getPrinter().printPreLine();
        getPrinter().printPostLine();
    }

    public void printRecItemVoid(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        openReceipt(PrinterConst.SMFP_RECTYPE_RETSALE);
        getPrinter().printPreLine();
        // if unitPrice is zero - use price and quantity = 1000
        if (unitPrice == 0) {
            quantity = 1000;
        } else {
            if (quantity == 0) {
                quantity = 1000;
            }
            price = unitPrice;
        }
        description = getPrinter().printDescription(description);
        if (!hasItems) {
            printSaleRefund(price, quantity, getParams().department, vatInfo,
                    description);
            hasItems = true;
        } else {
            switch (receiptType) {
                case PrinterConst.SMFP_RECTYPE_SALE:
                    printStorno(price, quantity, getParams().department,
                            vatInfo, description);
                    break;

                case PrinterConst.SMFP_RECTYPE_RETSALE:
                    printSaleRefund(price, quantity, getParams().department,
                            vatInfo, description);
                    break;
            }
        }
        getPrinter().printPostLine();
    }

    public void printRecSubtotal(long amount) throws Exception {
        
        long receiptSubtotal = getPrinter().getSubtotal();
        checkTotal(receiptSubtotal, amount);
        getPrinter().printPreLine();
        getPrinter().printStrings(getParams().subtotalText,
                "=" + StringUtils.amountToString(getPrinter().getSubtotal()));
        getPrinter().printPostLine();
    }

    public void printRecItemAdjustment(int adjustmentType, String description,
            long amount, int vatInfo) throws Exception {
        getPrinter().printPreLine();
        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:
                description = getPrinter().printDescription(description);
                printDiscount(amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:
                description = getPrinter().printDescription(description);
                printCharge(amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_DISCOUNT:
                description = getPrinter().printDescription(description);
                amount = getReceipt().getItemPercentAdjustmentAmount(amount);
                printDiscount(amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_SURCHARGE:
                description = getPrinter().printDescription(description);
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
        getPrinter().printPreLine();
        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:
                description = getPrinter().printDescription(description);
                printDiscount(amount, 0, description);
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:
                description = getPrinter().printDescription(description);
                printCharge(amount, 0, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_DISCOUNT:
                description = getPrinter().printDescription(description);
                amount = Math.round(getPrinter().getSubtotal() * amount / 10000.0);
                printDiscount(amount, 0, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_SURCHARGE:
                description = getPrinter().printDescription(description);
                amount = Math.round(getPrinter().getSubtotal() * amount / 10000.0);
                printCharge(amount, 0, description);
                break;

            default:
                throw new JposException(JposConst.JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + "AdjustmentType");
        }
        getPrinter().printPostLine();
    }

    public void printRecVoidItem(String description, long amount, int quantity,
            int adjustmentType, long adjustment, int vatInfo) throws Exception {
        openReceipt(PrinterConst.SMFP_RECTYPE_RETSALE);
        description = getPrinter().printDescription(description);
        if (!hasItems) {
            printSaleRefund(amount, quantity, getParams().department, vatInfo,
                    description);
            hasItems = true;
        } else {
            switch (receiptType) {
                case PrinterConst.SMFP_RECTYPE_SALE:
                    printStorno(amount, quantity, getParams().department,
                            vatInfo, description);
                    break;

                case PrinterConst.SMFP_RECTYPE_RETSALE:
                    printSaleRefund(amount, quantity, getParams().department,
                            vatInfo, description);
                    break;
            }
        }
    }

    public void printRecRefundVoid(String description, long amount, int vatInfo)
            throws Exception {
        openReceipt(PrinterConst.SMFP_RECTYPE_SALE);
        description = getPrinter().printDescription(description);

        if (!hasItems) {
            printSale(amount, 1000, getParams().department, vatInfo,
                    description);
            hasItems = true;
        } else {
            switch (receiptType) {
                case PrinterConst.SMFP_RECTYPE_SALE:
                    printStorno(amount, 1000, getParams().department, vatInfo,
                            description);
                    break;

                case PrinterConst.SMFP_RECTYPE_RETSALE:
                    printSale(amount, 1000, getParams().department, vatInfo,
                            description);
                    break;
            }
        }
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
                getPrinter().printText(PrinterConst.SMFP_STATION_REC,
                        description,
                        getPrinter().getPrinter().getParams().getFont());
                for (int i = 0; i < adjustments.size(); i++) {
                    adjustment = adjustments.getItem(i);
                    printDiscount(adjustment.amount, adjustment.vat, "");
                }
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:

                adjustments.parse(vatAdjustment);
                checkAdjustments(FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE,
                        adjustments);

                getPrinter().printText(description);
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

                getPrinter().printText("Void discount");
                for (int i = 0; i < adjustments.size(); i++) {
                    adjustment = adjustments.getItem(i);
                    printCharge(adjustment.amount, adjustment.vat, "");
                }
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:

                adjustments.parse(vatAdjustment);
                checkAdjustments(FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE,
                        adjustments);

                getPrinter().printText("Void charge");
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
        getPrinter().printPreLine();
        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:
                printDiscount(amount, 0, "");
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:
                printCharge(amount, 0, "");
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_DISCOUNT:
                amount = Math.round(getPrinter().getSubtotal() * amount / 10000.0);
                printCharge(amount, 0, "");
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_SURCHARGE:
                amount = Math.round(getPrinter().getSubtotal() * amount / 10000.0);
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
        getPrinter().printPreLine();
        switch (adjustmentType) {
            case FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT:
                description = getPrinter().printDescription(description);
                printCharge(amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_AMOUNT_SURCHARGE:
                description = getPrinter().printDescription(description);
                printDiscount(amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_DISCOUNT:
                description = getPrinter().printDescription(description);
                amount = getReceipt().getItemPercentAdjustmentAmount(amount);
                printCharge(amount, vatInfo, description);
                break;

            case FiscalPrinterConst.FPTR_AT_PERCENTAGE_SURCHARGE:
                description = getPrinter().printDescription(description);
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
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        openReceipt(PrinterConst.SMFP_RECTYPE_RETSALE);
        getPrinter().printPreLine();
        // if unitPrice is zero then we use price and quantity = 1000
        if (unitAmount == 0) {
            quantity = 1000;
        } else {
            if (quantity == 0) {
                quantity = 1000;
            }
            amount = unitAmount;
        }
        description = getPrinter().printDescription(description);

        // receipt was just opened
        if (!hasItems) {
            printSaleRefund(amount, quantity, getParams().department, vatInfo,
                    description);
            hasItems = true;
        } else {
            switch (receiptType) {
                case PrinterConst.SMFP_RECTYPE_SALE:
                    printStorno(amount, quantity, getParams().department,
                            vatInfo, description);
                    break;

                case PrinterConst.SMFP_RECTYPE_RETSALE:
                    printSaleRefund(amount, quantity, getParams().department,
                            vatInfo, description);
                    break;
            }
        }
        getPrinter().printPostLine();
    }

    public void printRecItemRefundVoid(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        openReceipt(PrinterConst.SMFP_RECTYPE_SALE);
        getPrinter().printPreLine();
        // if unitPrice is zero - use price and quantity = 1000
        if (unitAmount == 0) {
            quantity = 1000;
        } else {
            if (quantity == 0) {
                quantity = 1000;
            }
            amount = unitAmount;
        }
        description = getPrinter().printDescription(description);
        if (!hasItems) {
            printSale(amount, quantity, getParams().department, vatInfo,
                    description);
            hasItems = true;
        } else {
            switch (receiptType) {
                case PrinterConst.SMFP_RECTYPE_RETSALE:
                    printStorno(amount, quantity, getParams().department,
                            vatInfo, description);
                    break;

                case PrinterConst.SMFP_RECTYPE_SALE:
                    printSale(amount, quantity, getParams().department,
                            vatInfo, description);
                    break;
            }
        }
        getPrinter().printPostLine();
    }

    public void printRecVoid(String description) throws Exception {
        getPrinter().printText(description);
        getReceipt().cancel();
    }

    public boolean isPayed() {
        return getReceipt().isPayed();
    }

    public void printSale(long price, long quantity, int department,
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
        getPrinter().getPrinter().printSale(item);
        vatAmounts[vatInfo] = vatAmounts[vatInfo] + item.getAmount();
        getReceipt().printSale(item);
    }

    public void printSaleRefund(long price, long quantity, int department,
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
        getPrinter().getPrinter().printVoidSale(item);
        getReceipt().printSaleRefund(item);
    }

    public void printStorno(long price, int quantity, int department,
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
            getPrinter().getPrinter().printVoidItem(item);
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
                getPrinter().getPrinter().printVoidItem(item.getItem());
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
        getPrinter().getPrinter().printDiscount(item);
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
        getPrinter().getPrinter().printCharge(item);
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
