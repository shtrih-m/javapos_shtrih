package com.shtrih.jpos.fiscalprinter.receipt;

import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.GS1Barcode;
import com.shtrih.fiscalprinter.PrinterGraphics;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.TLVParser;
import com.shtrih.fiscalprinter.command.AmountItem;
import com.shtrih.fiscalprinter.command.CloseRecParams;
import com.shtrih.fiscalprinter.command.EndFiscalReceipt;
import com.shtrih.fiscalprinter.command.FSCloseReceipt;
import com.shtrih.fiscalprinter.command.FSReceiptDiscount;
import com.shtrih.fiscalprinter.command.FlexCommand;
import com.shtrih.fiscalprinter.command.PriceItem;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.jpos.fiscalprinter.PackageAdjustment;
import com.shtrih.jpos.fiscalprinter.PackageAdjustments;
import com.shtrih.jpos.fiscalprinter.PrintItem;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Localizer;
import com.shtrih.util.MathUtils;
import com.shtrih.util.MethodParameter;
import com.shtrih.util.StringUtils;
import com.shtrih.util.SysUtils;

import java.util.Vector;

import jpos.FiscalPrinterConst;
import jpos.JposConst;
import jpos.JposException;

import static com.shtrih.fiscalprinter.command.PrinterConst.SMFP_EFPTR_NOT_SUPPORTED;
import static com.shtrih.fiscalprinter.command.PrinterConst.SMFP_STATION_REC;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;

public class FSSalesReceipt extends CustomReceipt implements FiscalReceipt {

    private FSSaleReceiptItem lastItem = null;
    private int receiptType = 0;
    private int discountAmount = 0;
    private boolean isOpened = false;
    private String voidDescription = "";
    private final Vector items = new Vector();
    private final Vector endingItems = new Vector();
    private final long[] payments = new long[16]; // payment amounts
    private final FSDiscounts discounts = new FSDiscounts();
    private Vector<String> messages = new Vector<String>();
    private final ReceiptTemplate receiptTemplate;
    private static CompositeLogger logger = CompositeLogger.getLogger(FSSalesReceipt.class);
    private String barcode = null;

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
        isOpened = false;
        lastItem = null;
        items.clear();
        discounts.clear();
        for (int i = 0; i < payments.length; i++) {
            payments[i] = 0;
        }
        voidDescription = "";
        messages.clear();
        cancelled = false;

        getParams().itemTaxAmount = null;
        getParams().itemTotalAmount = null;
    }

    public void beginFiscalReceipt(boolean printHeader) throws Exception {
        clearReceipt();
        getDevice().printFSHeader();

        if (getParams().openReceiptOnBegin
                || (getParams().writeTagMode == SmFptrConst.WRITE_TAG_MODE_BEFORE_ITEMS)
                || (getParams().ReceiptTemplateEnabled)) {
            openReceipt(true);
        }
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

            AmountItem discount = new AmountItem();
            discount.setAmount(discountTotal);
            discount.setText("");
            discount.setTax1(0);
            discount.setTax2(0);
            discount.setTax3(0);
            discount.setTax4(0);
            saleItem.addDiscount(discount);
            return;
        }
        if (discountTotal < 100) {
            return;
        }

        long itemsTotal = getSubtotal() + discounts.getTotal();
        if (itemsTotal <= 0) {
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            Object recItem = items.get(i);
            if (recItem instanceof FSSaleReceiptItem) {
                FSSaleReceiptItem item = (FSSaleReceiptItem) recItem;
                if (item.getTotal() > 0) {
                    long itemAmount = item.getTotalWithVoids();
                    if (itemAmount > 0) {
                        long dscAmount = itemAmount;
                        if (dscAmount > discountTotal) {
                            dscAmount = discountTotal;
                        }

                        if (dscAmount > 0) {
                            AmountItem discount = new AmountItem();
                            discount.setAmount(dscAmount);
                            discount.setText(discountText);
                            discount.setTax1(0);
                            discount.setTax2(0);
                            discount.setTax3(0);
                            discount.setTax4(0);
                            item.addDiscount(discount);

                            discountTotal -= dscAmount;
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

    public void printTLVItems() throws Exception {
        for (int i = 0; i < items.size(); i++) {
            printTLVItem(items.get(i));
        }
    }

    public void printTLVItem(Object item) throws Exception {
        if (item instanceof FSTLVItem) {
            FSTLVItem tlvItem = (FSTLVItem) item;
            getDevice().fsWriteTLV(tlvItem.getData());

            if (getParams().FSPrintTags) {

                TLVParser reader = new TLVParser();
                reader.read(tlvItem.getData());
                Vector<String> lines = reader.getPrintText();

                if (getParams().FSTagsPlacement == 1) {
                    for (String line : lines) {
                        getDevice().printText(SMFP_STATION_REC, line, tlvItem.getFont());
                    }
                } else {
                    messages.addAll(lines);
                }
            }
        }

    }

    public void printReceiptItems() throws Exception {
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            if (item instanceof FSSaleReceiptItem) {
                printFSSaleNoTemplate((FSSaleReceiptItem) item);
            }
            if (item instanceof FSTextReceiptItem) {
                printFSText((FSTextReceiptItem) item);
            }
            if (item instanceof PrinterBarcode) {
                getDevice().printBarcode((PrinterBarcode) item);
                continue;
            }

            if (item instanceof AmountItem) {
                printTotalDiscount((AmountItem) item);
            }

            if (item instanceof PrintItem) {
                PrintItem printItem = (PrintItem) item;
                printItem.print(getPrinter().getPrinter());
            }
            if (getParams().writeTagMode == SmFptrConst.WRITE_TAG_MODE_IN_PLACE) {
                printTLVItem(item);
            }
        }
    }

    public void templatePrintReceiptItems() throws Exception {
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            if (item instanceof FSSaleReceiptItem) 
            {
                FSSaleReceiptItem sitem = (FSSaleReceiptItem) item;
                
                String itemText = sitem.getText();
                sitem.setText("//" + sitem.getText());

                printReceiptItem(sitem);
                sitem.setText(itemText);
            }
        }
        printTemplateHeader();
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            if (item instanceof FSSaleReceiptItem) {
                templatePrintTextItem((FSSaleReceiptItem) item);
            }
            if (item instanceof FSTextReceiptItem) {
                printFSText((FSTextReceiptItem) item);
            }
            if (item instanceof PrinterBarcode) {
                getDevice().printBarcode((PrinterBarcode) item);
                continue;
            }

            if (item instanceof AmountItem) {
                printTotalDiscount((AmountItem) item);
            }

            if (item instanceof PrintItem) {
                PrintItem printItem = (PrintItem) item;
                printItem.print(getPrinter().getPrinter());
            }
            if (getParams().writeTagMode == SmFptrConst.WRITE_TAG_MODE_IN_PLACE) {
                printTLVItem(item);
            }
        }
        printTemplateTrailer();
    }

    public void printEndingItems() throws Exception {
        getPrinter().waitForPrinting();
        for (int i = 0; i < endingItems.size(); i++) {
            Object item = endingItems.get(i);
            if (item instanceof FSTextReceiptItem) {
                printFSText((FSTextReceiptItem) item);
            }
            if (item instanceof PrinterBarcode) {
                getDevice().printBarcode((PrinterBarcode) item);
            }
        }
        endingItems.clear();
    }

    private void correctPayments() throws Exception {
        if (!getParams().paymentSumCorrectionEnabled) {
            return;
        }

        long paidAmount = 0;
        for (int i = 1; i < payments.length; i++) {
            if (paidAmount + payments[i] > getSubtotal()) {
                payments[i] = getSubtotal() - paidAmount;
            }
            paidAmount += payments[i];
        }
    }

    public void removeStornoItems() throws Exception {
        for (int i = items.size() - 1; i >= 0; i--) {
            Object object = items.get(i);
            if (object instanceof FSSaleReceiptItem) {
                FSSaleReceiptItem stornoItem = (FSSaleReceiptItem) object;
                if (stornoItem.getIsStorno()) {
                    FSSaleReceiptItem item = null;
                    for (int j = i - 1; j >= 0; j--) {
                        object = items.get(j);
                        if (object instanceof FSSaleReceiptItem) {
                            item = (FSSaleReceiptItem) object;
                            if ((item.getText().equalsIgnoreCase(stornoItem.getText()))
                                    && (item.getPrice() == stornoItem.getPrice())
                                    && (item.getQuantity() >= stornoItem.getQuantity())) {
                                break;
                            }
                        }
                    }
                    if (item == null) {
                        throw new Exception("Voided receipt item not found");
                    }
                    long resultAmount = item.getTotal() - stornoItem.getTotal();
                    item.setQuantity(item.getQuantity() - stornoItem.getQuantity());
                    item.setItemAmount(item.getItemAmount() - stornoItem.getItemAmount());

                    if (item.getQuantity() == 0) {
                        items.remove(item);
                        i--;
                    }
                    items.remove(stornoItem);
                }
            }
        }
    }

    public void processTLVItems() throws Exception {
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            if (item instanceof FSTLVItem) {
                FSTLVItem tlvItem = (FSTLVItem) item;
                tlvItem.setData(getDevice().processTLVBeforeReceipt(tlvItem.getData()));
            }
        }
    }

    /*
    88.Оборот по налогу А с продаж в чеке                       : 0.00
    89.Оборот по налогу А с покупок в чеке                      : 0.00
    90.Оборот по налогу А с возврата продаж в чеке              : 0.00
    91.Оборот по налогу А с возврата покупок в чеке             : 0.00
    92.Оборот по налогу Б с продаж в чеке                       : 0.00
    93.Оборот по налогу Б с покупок в чеке                      : 0.00
    94.Оборот по налогу Б с возврата продаж в чеке              : 0.00
    95.Оборот по налогу Б с возврата покупок в чеке             : 0.00
    96.Оборот по налогу В с продаж в чеке                       : 0.00
    97.Оборот по налогу В с покупок в чеке                      : 0.00
    98.Оборот по налогу В с возврата продаж в чеке              : 0.00
    99.Оборот по налогу В с возврата покупок в чеке             : 0.00
    100.Оборот по налогу Г с продаж в чеке                       : 0.00
    101.Оборот по налогу Г с покупок в чеке                      : 0.00
    102.Оборот по налогу Г с возврата продаж в чеке              : 0.00
    103.Оборот по налогу Г с возврата покупок в чеке             : 0.00    
    
    4192.Оборот по налогу 18/118 с продаж в чеке                  : 0.00
    4193.Оборот по налогу 18/118 с покупок в чеке                 : 0.00
    4194.Оборот по налогу 18/118 с возврата продаж в чеке         : 0.00
    4195.Оборот по налогу 18/118 с возврата покупок в чеке        : 0.00
    4196.Оборот по налогу 10/110 с продаж в чеке                  : 0.00
    4197.Оборот по налогу 10/110 с покупок в чеке                 : 0.00
    4198.Оборот по налогу 10/110 с возврата продаж в чеке         : 0.00
    4199.Оборот по налогу 10/110 с возврата покупок в чеке        : 0.00
     */
    public long calculateTaxAmount(int index, int tax, int discount) throws Exception {
        long result = 0;
        if ((index < 0) || (index > 5)) {
            throw new Exception("Invalid tax index, must be 0..5");
        }

        int taxRate = getDevice().getTaxRate(index + 1);
        int[] cashRegNumber = {88, 92, 96, 100, 4192, 4196};
        long amount = getDevice().readCashRegister(cashRegNumber[index] + receiptType);
        if ((tax - 1) == index) {
            amount = amount - discount;
        }
        if (taxRate == 0) {
            result = amount;
        } else 
        {
            double taxAmount = ((double)amount) * taxRate / (10000 + taxRate);
            result = Math.round(taxAmount);
        }
        return result;
    }

    public void endFiscalReceipt(boolean printHeader) throws Exception {
        if (isOpened) {
            processTLVItems();
            removeStornoItems();
            correctPayments();

            if (getDevice().getCapDiscount()) {
                for (int i = 0; i < discounts.size(); i++) {
                    items.add(discounts.get(i));
                }
                discounts.clear();
            } else if (discounts.getTotal() < 100) {
                discountAmount += (int) discounts.getTotal();
                discounts.clear();
            } else {
                addItemsDiscounts();
            }
            updateReceiptItems();
            if (getParams().writeTagMode == SmFptrConst.WRITE_TAG_MODE_BEFORE_ITEMS) {
                printTLVItems();
            }

            if (getParams().ReceiptTemplateEnabled) {
                templatePrintReceiptItems();
            } else {
                printReceiptItems();
            }

            if (getParams().writeTagMode == SmFptrConst.WRITE_TAG_MODE_AFTER_ITEMS) {
                printTLVItems();
            }

            if (!cancelled && getDevice().isCapFooterFlag()) {
                getDevice().setIsFooter(true);
                try {
                    printEndingItems();
                } finally {
                    getDevice().setIsFooter(false);
                }
            }

            if (cancelled) {
                getPrinter().waitForPrinting();
                getDevice().cancelReceipt();
                try {
                    getPrinter().waitForPrinting();
                    if (getParams().printVoidedReceipt) {
                        String docName = getDevice().getReceiptName(receiptType);
                        getDevice().printReceiptHeader(docName);
                        getPrinter().printText(voidDescription);
                        getPrinter().printText(getParams().receiptVoidText);
                    }
                } catch (Exception e) {
                    logger.error("Cancel receipt: " + e.getMessage());
                }
                getFiscalDay().cancelFiscalRec();
                clearReceipt();
            } else {
                FSCloseReceipt closeReceipt = new FSCloseReceipt();
                closeReceipt.setSysPassword(getDevice().getUsrPassword());
                for (int i = 0; i < payments.length; i++) {
                    closeReceipt.setPayment(i, payments[i]);
                }
                int taxNumber = (int) getParams().taxAmount[0];
                if (getParams().taxCalculation == SmFptrConst.TAX_CALCULATION_DRIVER) {
                    for (int i = 0; i < 6; i++) {
                        getParams().taxAmount[i] = calculateTaxAmount(i, taxNumber, discountAmount);
                    }
                }
                for (int i = 0; i < 6; i++) {
                    closeReceipt.setTaxValue(i, getParams().taxAmount[i]);
                }
                closeReceipt.setDiscount(discountAmount);
                closeReceipt.setTaxSystem(getParams().taxSystem);
                closeReceipt.setText(getParams().closeReceiptText);

                int rc = getDevice().fsCloseReceipt(closeReceipt);
                if (rc == SMFP_EFPTR_NOT_SUPPORTED) {
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

                    EndFiscalReceipt command = new EndFiscalReceipt();
                    command.setPassword(getDevice().getUsrPassword());
                    command.setParams(closeParams);
                    rc = getDevice().closeReceipt(command);
                }
                getDevice().check(rc);
                getFiscalDay().closeFiscalRec();
                
                try {
                    getDevice().waitForPrinting();
                    for (int i = 0; i < messages.size(); i++) {
                        getDevice().printText(messages.get(i));
                    }
                } catch (Exception e) {
                    logger.error("Receipt messages printing failed", e);
                }
            }
        }
        if (cancelled || (!getDevice().isCapFooterFlag())) 
        {
            try {
                printEndingItems();
            } catch (Exception e) {
                logger.error("Receipt ending items printing failed", e);
            }
        }
    }

    private void printTotalDiscount(AmountItem discount) throws Exception {
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

    private void printFSText(FSTextReceiptItem item) throws Exception {
        if (!item.preLine.isEmpty()) {
            getDevice().printText(SMFP_STATION_REC, item.preLine, item.font);
        }

        if (!item.text.isEmpty()) {
            getDevice().printText(SMFP_STATION_REC, item.text, item.font);
        }

        if (!item.postLine.isEmpty()) {
            getDevice().printText(SMFP_STATION_REC, item.postLine, item.font);
        }
    }

    public void printReceiptItem(FSSaleReceiptItem item) throws Exception
    {
        if (getDevice().getCapOperationTagsFirst()) 
        {
            printOperationTLV(item);
            getDevice().sendMarking(item.getBarcode());
        }
        
        PriceItem priceItem = item.getPriceItem();
        if (!item.getIsStorno()) {
            switch (receiptType) {
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
        if (!getDevice().getCapOperationTagsFirst()) 
        {
            printOperationTLV(item);
            getDevice().sendMarking(item.getBarcode());
        }
    }

    public void printTextTemplate(FSSaleReceiptItem item) throws Exception {
        if (!receiptTemplate.hasPreLine()) {
            String preLine = item.getPreLine();
            if (preLine.length() > 0) {
                getDevice().printText(preLine);
                item.setPreLine("");
            }
        }

        String[] lines = receiptTemplate.getReceiptItemLines(item);
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (!line.isEmpty()) {
                getDevice().printText(lines[i]);
            }
        }

        if (!receiptTemplate.hasPostLine()) {
            String postLine = item.getPostLine();
            if (postLine.length() > 0) {
                getDevice().printText(postLine);
                item.setPostLine("");
            }
        }
    }

    public void printFSSaleNoTemplate(FSSaleReceiptItem item) throws Exception 
    {
        if (!receiptTemplate.hasPreLine()) {
            String preLine = item.getPreLine();
            if (preLine.length() > 0) {
                getDevice().printText(preLine);
                item.setPreLine("");
            }
        }

        if (getParams().printReceiptItemAsText) {
            printRecItemAsText(item);
            item.setText("//" + item.getText());
        }

        printReceiptItem(item);
        
        long discountTotal = item.getDiscounts().getTotal();
        if (discountTotal != 0) {
            String text = "=" + StringUtils.amountToString(discountTotal);
            getDevice().printLines("СКИДКА", text);
        }

        if (!receiptTemplate.hasPostLine()) {
            String postLine = item.getPostLine();
            if (postLine.length() > 0) {
                getDevice().printText(postLine);
                item.setPostLine("");
            }
        }
    }

    public void printOperationTLV(FSSaleReceiptItem item) throws Exception {
        for (int i = 0; i < item.getTags().size(); i++) {
            FSOperationTLVItem tag = (FSOperationTLVItem) item.getTags().get(i);
            getDevice().check(getDevice().fsWriteOperationTLV(tag.getData()));
        }
    }
    
    public void templatePrintTextItem(FSSaleReceiptItem item) throws Exception {
        if (!receiptTemplate.hasPreLine()) {
            String preLine = item.getPreLine();
            if (preLine.length() > 0) {
                getDevice().printText(preLine);
                item.setPreLine("");
            }
        }

        String[] lines = receiptTemplate.getReceiptItemLines(item);
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (!line.isEmpty()) {
                getDevice().printText(lines[i]);
            }
        }

        if (!receiptTemplate.hasPostLine()) {
            String postLine = item.getPostLine();
            if (postLine.length() > 0) {
                getDevice().printText(postLine);
                item.setPostLine("");
            }
        }
    }

    public void printRecRefund(String description, long amount, int vatInfo)
            throws Exception {
        openReceipt(false);
        printSale(amount, 1000, amount, getParams().department, vatInfo,
                description, "");
    }

    private void addTextItem(String text, FontNumber font) throws Exception {
        FSTextReceiptItem item = new FSTextReceiptItem(text, getPreLine(), getPostLine(), font);
        if (getContext().getPrinterState().isEnding()) {
            endingItems.add(item);
        } else {
            items.add(item);
        }
    }

    private void addTextItem(String text) throws Exception {
        addTextItem(text, getParams().getFont());
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
        printStorno(price, quantity, unitPrice, getParams().department,
                vatInfo, description);
    }

    public void printRecSubtotal(long amount) throws Exception {
        checkTotal(getSubtotal(), amount);
        if (getParams().subtotalTextEnabled) {
            String text = StringUtils.alignLines(
                    getParams().subtotalText,
                    "=" + StringUtils.amountToString(getSubtotal()),
                    getDevice().getMessageLength(getParams().subtotalFont));
            addTextItem(text, getParams().subtotalFont);
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
        printStorno(total, quantity, amount, getParams().department, vatInfo,
                description);
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

    private long correctQuantity(long price, long quantity, long unitPrice) {
        if (!getParams().quantityCorrectionEnabled) {
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
        doPrintSale(price, quantity, unitPrice, department, vatInfo,
                description, "", true);

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

        doPrintSale(price, quantity, unitPrice, department, vatInfo, description, unitName, false);
    }

    public void doPrintSale(long price, long quantity, long unitPrice,
            int department, int vatInfo, String description, String unitName,
            boolean isStorno) throws Exception 
    {
        logger.debug(
                "price: " + price
                + ", quantity: " + quantity
                + ", unitPrice: " + unitPrice);

        
        if (vatInfo == 0) {
            vatInfo = 4;
        }
        
        double d = unitPrice * Math.abs(quantity);
        long amount = getParams().itemTotalAmount == null ? MathUtils.round((d / 1000.0)) : getParams().itemTotalAmount;
        FSSaleReceiptItem item = null;
        if (getParams().combineReceiptItems) {
            item = findItem(unitPrice, description);
            if (item != null) {
                item.setQuantity(item.getQuantity() + quantity);
                item.setItemAmount(item.getItemAmount() + price);
            }
        }
        if (price > amount){
            price = amount;
        }
        if (item == null) {
            item = new FSSaleReceiptItem();
            item.setItemAmount(price);
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
            item.setBarcode(barcode);
            double taxRate = getDevice().getTaxRate(vatInfo) / 10000.0;
            item.setTaxRate(taxRate);
            item.getReceiptFields().clear();
            item.getReceiptFields().putAll(getParams().getReceiptFields());
            getParams().getReceiptFields().clear();
            
            barcode = null;

            if (getParams().itemTotalAmount != null) {
                item.setTotalAmount(getParams().itemTotalAmount);
                getParams().itemTotalAmount = null;
            }
            if (getParams().itemTaxAmount != null) {
                item.setTaxAmount(getParams().itemTaxAmount);
                getParams().itemTaxAmount = null;
            }

            item.setPaymentType(getParams().paymentType);
            item.setSubjectType(getParams().subjectType);
            // Item unit
            if (getParams().itemUnit != null){
                item.setUnit(getParams().itemUnit);
                getParams().itemUnit = null;
            }
            // check MC
            if (getParams().checkItemCodeEnabled)
            {
                getDevice().checkItemCode(item.getBarcode(), isSaleReceipt(), item.getQuantity());
            }
            
            items.add(item);

            getParams().paymentType = 4;
            getParams().subjectType = 1;
        }
        lastItem = item;
        clearPrePostLine();
        if ((price > 0) && (amount - price) > 1) {
            printDiscount(amount - price, vatInfo, "");
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

        AmountItem item = new AmountItem();
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

                        AmountItem discount = new AmountItem();
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
    }

    public void printTotalDiscount(long amount, int tax1, String text)
            throws Exception {
        if (amount > getSubtotal()) {
            throw new Exception("Discount amount more than receipt total");
        }

        AmountItem item = new AmountItem();
        item.setAmount(amount);
        item.setTax1(tax1);
        item.setTax2(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setTax3(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setTax4(PrinterConst.SMFPTR_TAX_NOTAX);
        item.setText(text);
        discounts.add(item);
    }

    public long getSubtotal() throws Exception {
        long total = 0;
        for (int i = 0; i < items.size(); i++) {
            Object object = items.get(i);
            if (object instanceof FSSaleReceiptItem) {
                FSSaleReceiptItem item = (FSSaleReceiptItem) object;
                if (item.getIsStorno()) {
                    total -= item.getTotal();
                } else {
                    total += item.getTotal();
                }
            }
        }
        total -= discounts.getTotal();
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

        private byte[] data;
        private FontNumber font;

        public FSTLVItem(byte[] data, FontNumber font) {
            this.data = data;
            this.font = font;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        public byte[] getData() {
            return data;
        }

        public FontNumber getFont() {
            return font;
        }
    }

    public class FSOperationTLVItem {

        private final byte[] data;
        private FontNumber font;

        public FSOperationTLVItem(byte[] data, FontNumber font) {
            this.data = data;
            this.font = font;
        }

        public byte[] getData() {
            return data;
        }

        public FontNumber getFont() {
            return font;
        }
    }

    public void fsWriteTLV(byte[] data) throws Exception {
        items.add(new FSTLVItem(data, getParams().getFont()));
    }

    public void fsWriteOperationTLV(byte[] data) throws Exception {
        FSOperationTLVItem item = new FSOperationTLVItem(data, getParams().getFont());
        if (getDevice().getCapOperationTagsFirst()) {
            items.add(item);
        } else {
            getLastItem().getTags().add(item);
        }
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

    protected void printTotalAndTax(FSSaleReceiptItem item) throws Exception 
    {
        String line;
        line = getDevice().getTaxName(item.getTax1());
        line = formatLines(line, StringUtils.amountToString(item.getTaxAmount()));
        getDevice().printText(line);
    }

    public String formatLines(String line1, String line2) throws Exception {
        return StringUtils.alignLines(line1, line2, getPrinter().getTextLength());
    }

    public void printBarcode(PrinterBarcode barcode) throws Exception {
        if (getContext().getPrinterState().isEnding()) {
            endingItems.add(barcode);
        } else {
            items.add(barcode);
        }
    }

    public void printGraphics(PrinterGraphics graphics) throws Exception {
        items.add(graphics);
    }

    public void setItemBarcode(String barcode) throws Exception {
        this.barcode = barcode;
    }

}
