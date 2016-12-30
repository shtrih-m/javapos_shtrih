package com.shtrih.jpos.fiscalprinter.receipt;

import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.command.AmountItem;
import com.shtrih.fiscalprinter.command.PriceItem;
import com.shtrih.jpos.fiscalprinter.receipt.template.*;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.MathUtils;
import com.shtrih.util.StringUtils;

import java.util.Vector;

import static com.shtrih.fiscalprinter.command.PrinterConst.SMFP_STATION_REC;

public class FSTemplateReceipt extends FSSalesReceipt2 {

    private final static CompositeLogger logger = CompositeLogger.getLogger(FSTemplateReceipt.class);
    private String[] m_tableHeaderLines = null;
    private TemplateLine[] m_templateLines = null;
    private long m_position_count = 0;

    public FSTemplateReceipt(ReceiptContext context, int receiptType) throws Exception {
        super(context, receiptType);
        parseItemRowFormat();
    }

    /**
     * Parsing format lines, create format string and determine fields
     * @throws Exception
     */
    protected void parseItemRowFormat() throws Exception {
        m_tableHeaderLines = getDevice().getParams().ItemTableHeader.split("\\\\n");
        String[] itemLines = getDevice().getParams().ItemRowFormat.split("\\\\n");
        m_templateLines = new TemplateLine[itemLines.length];
        for (int i = 0; i < itemLines.length; i++) {
            TemplateLine templateLine = FormatLineParser.getLineFromString(itemLines[i]);
            templateLine.parseLine();
            m_templateLines[i] = templateLine;
        }
    }

    /**
     *
     * @param line template line to prepare
     * @param item item to use as format context
     * @return line to print
     * @throws Exception format/parsing exception may appear
     */
    private String getPrinterLine(TemplateLine line,TemplatePriceItem item) throws Exception {
        String[] fmts = new String[line.getTags().size()];
        Vector<Field> tags = line.getTags();
        for (int i = 0; i < tags.size(); i++) {
            Field f = tags.elementAt(i);

            if (f.width >= 0) {
                switch (f.justify) {
                    case CENTER:
                        fmts[i] = StringUtils.center(f.prefix + item.getTagContents(f), f.width);
                        break;
                    case LEFT:
                        fmts[i] = StringUtils.left(f.prefix + item.getTagContents(f), f.width);
                        break;
                    case RIGHT:
                        fmts[i] = StringUtils.right(f.prefix + item.getTagContents(f), f.width);
                        break;
                }
            } else {
                fmts[i] = f.prefix + item.getTagContents(f);
            }
        }
        String s = String.format(line.getOutputFmt(), (String[]) fmts);
        return s;
    }

    @Override
    public void clearReceipt() throws Exception {
        super.clearReceipt();
        m_position_count=0;
    }

    /**
     * Subtotal with font specified in parameters
     * @param amount subtotal amount
     * @throws Exception
     */
    @Override
    public void printRecSubtotal(long amount) throws Exception {
        if (lastItem != null && !lastItemFooterPrinted) {
            printTotalAndTax(lastItem);
        }
        checkTotal(getSubtotal(), amount);
        getDevice().printText(SMFP_STATION_REC, formatStrings(getDevice().getParams().subtotalFont, getParams().subtotalText,
                "=" + StringUtils.amountToString(getSubtotal())), getDevice().getParams().subtotalFont);
    }

    /**
     *
     * @param font font to use
     * @param line1 left side string
     * @param line2 right side string
     * @return strings separated by spaces
     * @throws Exception
     */
    protected String formatStrings(FontNumber font, String line1, String line2) throws Exception {
        int len;
        StringBuilder sb = new StringBuilder();
        len = getModel().getTextLength(font)
                - line2.length();

        for (int i = 0; i < len; i++) {
            if (i < line1.length()) {
                sb.append(line1.charAt(i));
            } else {
                sb.append(" ");
            }
        }
        sb.append(line2);
        return sb.toString();
    }

    /**
     * Prints discount only if it was made explicitly, skips empty discount messages
     * @param item discount item to print
     * @throws Exception
     */
    protected void doPrintDiscount(AmountItem item) throws Exception{
        long amount=item.getAmount();
        item.setAmount(Math.abs(amount));
        String line = formatStrings(getDevice().getParams().totalDiscountFont, item.getText(), "=" + StringUtils.amountToString(amount));

        if (amount > 0) {
            getDevice().printDiscount(item);
        } else {
            getDevice().printCharge(item);
        }
        lastItemDiscountSum += amount;
        if (!getParams().FSCombineItemAdjustments) {
            //не печатаем автоматически посчитанную скидку. Оставляем это для шаблона
            if (item.getText().equals("")){
                return;
            }
            if (amount > 0) {
                getDevice().printText(SMFP_STATION_REC, "СКИДКА", getDevice().getParams().totalDiscountFont);
            } else {
                getDevice().printText(SMFP_STATION_REC, "НАДБАВКА", getDevice().getParams().totalDiscountFont);
            }
            getDevice().printText(SMFP_STATION_REC, line, getDevice().getParams().totalDiscountFont);
        }
    }

    @Override
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
        TemplatePriceItem tItem=(TemplatePriceItem) lastItem;
        tItem.appendDiscountItem(item);
    }

    @Override
    public void printRecItemAsText(PriceItem item, long quantity) throws Exception {
        TemplatePriceItem tItem=(TemplatePriceItem) item;
        int tax = item.getTax1();
        if (tax == 0) {
            tax = 4;
        }
        if (quantity < 0) {
            getDevice().printText("СТОРНО");
        }
        for (TemplateLine templateLine:m_templateLines){
            String line=getPrinterLine(templateLine,tItem);
            getDevice().printText(line);
        }

        Vector<AmountItem> discounts=tItem.getDiscountItems();
        for (AmountItem discount:discounts){
            doPrintDiscount(discount);
        }
    }

    protected void printLastItem(PriceItem item) throws Exception {
        TemplatePriceItem tItem=(TemplatePriceItem) item;
        item.setText("//"+item.getText());
//        Vector<AmountItem> discounts=tItem.getDiscountItems();
//        for (AmountItem discount:discounts){
//            tItem.setTotalPrice(tItem.getTotalPrice()-discount.getAmount());
//        }
        if (tItem.getTotalQuantity() > 0) {
            if (isSaleReceipt()) {
                getDevice().printSale(item);
            } else {
                getDevice().printVoidSale(item);
            }
        } else {
            getDevice().printVoidItem(item);
        }
        item.setText(item.getText().substring(2));
        if (getParams().FSReceiptItemDiscountEnabled) {
            double d = Math.abs(item.getPrice()) * Math.abs(tItem.getTotalQuantity());
            long amount = MathUtils.round((d / 1000));
            if ((amount - tItem.getTotalPrice()) > 0) {
                printDiscount(amount - tItem.getTotalPrice(), 0, "");
            }
        }
        if (!getParams().FSCombineItemAdjustments) {
            printRecItemAsText(item, tItem.getTotalQuantity());
        }
        lastItemFooterPrinted = true;
        if (getParams().FSCombineItemAdjustments) return;
    }

    @Override
    protected void printTotalAndTax(PriceItem item) throws Exception {
        //просто печатаем последнюю позицию сдесь
        printLastItem(item);
    }

    @Override
    public void doPrintSale(long price, long quantity, long unitPrice,
                            int department, int vatInfo, String description) throws Exception {
        logger.debug(
                "price: " + price
                        + ", quantity: " + quantity
                        + ", unitPrice: " + unitPrice);
        m_position_count++;
        TemplatePriceItem item = new TemplatePriceItem();
        item.setPrice(unitPrice);
        item.setQuantity(Math.abs(quantity));
        item.setDepartment(department);
        item.setTax1(vatInfo);
        item.setTax2(0);
        item.setTax3(0);
        item.setTax4(0);
        item.setText(description);
        item.setTotalPrice(price);
        item.setTotalQuantity(quantity);
        item.setPosition(m_position_count);

        /**
         * Печатаем заголовок таблицы, если это первая позиция в чеке
         */
        if (lastItem == null){
            for (String line:m_tableHeaderLines){
                getDevice().printText(line);
            }
        }
        /**
         * На самом деле ничего не делаем тут, только запоминаем позицию, либо печатаем, пред
         */
        if (lastItem != null && !lastItemFooterPrinted) {
            printTotalAndTax(getLastItem());
        }
        lastItem = item;
        lastItemDiscountSum = 0;
        lastItemFooterPrinted = false;
    }
}
