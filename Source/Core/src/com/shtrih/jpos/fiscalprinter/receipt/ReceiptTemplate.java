/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.TLVItem;
import com.shtrih.fiscalprinter.TLVItems;
import com.shtrih.fiscalprinter.TLVReader;
import com.shtrih.fiscalprinter.TLVTags;
import com.shtrih.fiscalprinter.TLVTag;
import com.shtrih.fiscalprinter.Tag2115;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.jpos.fiscalprinter.receipt.FSTLVItem;
import com.shtrih.jpos.fiscalprinter.receipt.template.Field;
import com.shtrih.jpos.fiscalprinter.receipt.template.FormatLineParser;
import com.shtrih.jpos.fiscalprinter.receipt.template.ParsingException;
import com.shtrih.jpos.fiscalprinter.receipt.template.TemplateLine;
import com.shtrih.util.StringUtils;
import com.shtrih.fiscalprinter.command.AmountItem;

import java.util.Vector;

/**
 *
 * @author Виталий
 */
public class ReceiptTemplate {

    private final ReceiptContext context;
    private String[] headerLines = new String[0];
    private String[] trailerLines = new String[0];
    private TemplateLine[] itemTemplate = null;
    private TemplateLine[] discountTemplate = null;
    private TemplateLine[] chargeTemplate = null;
    private long m_position_count = 0;

    public ReceiptTemplate(ReceiptContext context) throws Exception {
        this.context = context;
        parseFormatLines();
    }

    public FptrParameters getParams() {
        return context.getParams();
    }

    public boolean hasPreLine() {
        return getParams().ItemRowFormat.contains("%PRELINE%");
    }

    public boolean hasPostLine() {
        return getParams().ItemRowFormat.contains("%POSTLINE%");
    }

        /**
     * Parsing format lines, create format string and determine fields
     *
     * @throws Exception
     */
    private void parseFormatLines() throws Exception 
    {
        if (getParams().ItemTableHeader == null) return;
        String fieldSeparator = getParams().fieldSeparator;
        if (!getParams().ItemTableHeader.isEmpty()) {
            headerLines = getParams().ItemTableHeader.split(fieldSeparator);
        }
        if (!getParams().ItemTableTrailer.isEmpty()) {
            trailerLines = getParams().ItemTableTrailer.split(fieldSeparator);
        }
        // item
        String[] lines = getParams().ItemRowFormat.split(fieldSeparator);
        itemTemplate = new TemplateLine[lines.length];
        for (int i = 0; i < lines.length; i++) {
            TemplateLine templateLine = FormatLineParser.getLineFromString(lines[i]);
            templateLine.parseLine();
            itemTemplate[i] = templateLine;
        }
        // discount
        lines = getParams().discountFormat.split(fieldSeparator);
        discountTemplate = new TemplateLine[lines.length];
        for (int i = 0; i < lines.length; i++) {
            TemplateLine templateLine = FormatLineParser.getLineFromString(lines[i]);
            templateLine.parseLine();
            discountTemplate[i] = templateLine;
        }
        // charge
        lines = getParams().chargeFormat.split(fieldSeparator);
        chargeTemplate = new TemplateLine[lines.length];
        for (int i = 0; i < lines.length; i++) {
            TemplateLine templateLine = FormatLineParser.getLineFromString(lines[i]);
            templateLine.parseLine();
            chargeTemplate[i] = templateLine;
        }
    }

    public String[] getHeaderLines() {
        return headerLines;
    }

    public String[] getTrailerLines() {
        return trailerLines;
    }

    public String[] getReceiptItemLines(FSSaleReceiptItem item) throws Exception {
        Vector<String> lines = new Vector<String>();

        if (item.getIsStorno()) {
            lines.add("СТОРНО");
        }
        for (TemplateLine templateLine : itemTemplate) {
            String line = getReceiptItemLine(templateLine, item);
            if (!line.trim().isEmpty()) {
                lines.add(line);
            }
        }
        return lines.toArray(new String[0]);
    }

    public String[] getDiscountLines(AmountItem item) throws Exception {
        Vector<String> lines = new Vector<String>();
        for (TemplateLine templateLine : discountTemplate) {
            String line = getDiscountItemLine(templateLine, item);
            lines.add(line);
        }
        return lines.toArray(new String[0]);
    }

    public String[] getChargeLines(AmountItem item) throws Exception {
        Vector<String> lines = new Vector<String>();
        for (TemplateLine templateLine : chargeTemplate) {
            String line = getDiscountItemLine(templateLine, item);
            lines.add(line);
        }
        return lines.toArray(new String[0]);
    }

    /**
     *
     * @param line template line to prepare
     * @param item item to use as format context
     * @return line to print
     * @throws Exception format/parsing exception may appear
     */
    private String getReceiptItemLine(TemplateLine line, FSSaleReceiptItem item) throws Exception {
        String[] fmts = new String[line.getTags().size()];
        Vector<Field> tags = line.getTags();
        for (int i = 0; i < tags.size(); i++) {
            Field f = tags.elementAt(i);

            if (f.width >= 0) {
                switch (f.justify) {
                    case CENTER:
                        fmts[i] = StringUtils.center(f.prefix + getTagContents(f, item), f.width);
                        break;
                    case LEFT:
                        fmts[i] = StringUtils.left(f.prefix + getTagContents(f, item), f.width);
                        break;
                    case RIGHT:
                        fmts[i] = StringUtils.right(f.prefix + getTagContents(f, item), f.width);
                        break;
                }
            } else {
                fmts[i] = f.prefix + getTagContents(f, item);
            }
        }
        String s = String.format(line.getOutputFmt(), (String[]) fmts);
        return s;
    }

    private String getDiscountItemLine(TemplateLine line, AmountItem item) throws Exception {
        String[] fmts = new String[line.getTags().size()];
        Vector<Field> tags = line.getTags();
        for (int i = 0; i < tags.size(); i++) {
            Field f = tags.elementAt(i);

            if (f.width >= 0) {
                switch (f.justify) {
                    case CENTER:
                        fmts[i] = StringUtils.center(f.prefix + getDiscountTagContents(f, item), f.width);
                        break;
                    case LEFT:
                        fmts[i] = StringUtils.left(f.prefix + getDiscountTagContents(f, item), f.width);
                        break;
                    case RIGHT:
                        fmts[i] = StringUtils.right(f.prefix + getDiscountTagContents(f, item), f.width);
                        break;
                }
            } else {
                fmts[i] = f.prefix + getDiscountTagContents(f, item);
            }
        }
        String s = String.format(line.getOutputFmt(), (String[]) fmts);
        return s;
    }

    private String getDiscountTagContents(Field f, AmountItem item) throws Exception {
        if (f.tag.equals("TITLE")) {
            return item.getText();
        }

        if (f.tag.equals("TOTAL")) {
            return StringUtils.amountToString(item.getAmount());
        }
        throw new ParsingException("Unknown tag: " + f.tag);
    }

    public String getSign(FSSaleReceiptItem item) {
        if (item.getIsStorno()) {
            return "-";
        } else {
            return "";
        }
    }

    private String getTagContents(Field f, FSSaleReceiptItem item) throws Exception {
        if (f.tag.equals("POS")) {
            return String.valueOf(item.getPos());
        }

        if (f.tag.equals("TITLE")) {
            return item.getText();
        }

        if (f.tag.equals("UNITPRICE")) {
            return StringUtils.amountToString(item.getUnitPrice());
        }
        if (f.tag.equals("PRICE")) {
            return StringUtils.amountToString(item.getUnitPrice());
        }
        if (f.tag.equals("QUAN")) {
            return getParams().quantityToStr(item.getQuantity(), item.getUnitName());
        }
        if (f.tag.equals("SUM")) {
            return StringUtils.amountToString(item.getPrice());
        }

        if (f.tag.equals("DISCOUNT")) {
            long amount = item.getPriceDiscount();
            if (amount == 0) {
                return "";
            }
            return StringUtils.amountToString(amount);
        }
        if (f.tag.equals("TOTAL")) {
            return getSign(item) + StringUtils.amountToString(item.getTotal());
        }
        if (f.tag.equals("TOTAL_TAX")) {
            return getSign(item) + StringUtils.amountToString(item.getTotal()) + "_"
                    + getTaxLetter(item.getTax1());
        }
        if (f.tag.equals("TAX_LETTER")) {
            return getTaxLetter(item.getTax1());
        }
        if (f.tag.equals("TAX_NAME")) {
            return getTaxName(item.getTax1());
        }
        if (f.tag.equals("TAX_AMOUNT")) {
            return getTaxAmount(item);
        }
        if (f.tag.equals("PRELINE")) {
            String text = item.getPreLine();
            item.setPreLine("");
            return text;
        }
        if (f.tag.equals("POSTLINE")) {
            String text = item.getPostLine();
            item.setPostLine("");
            return text;
        }
        if (f.tag.equals("MULT_NE_ONE")) {
            return item.getQuantity() == 1000 ? " " : "*";
        }
        // TAG_NAME
        String tagName = getTagName(f, item);
        if (tagName != null) {
            return tagName;
        }
        // TAG_VALUE
        String tagText = getTagText(f, item);
        if (tagText != null) {
            return tagText;
        }

        String fieldValue = item.getReceiptField(f.tag);
        return fieldValue;
    }

    private String getTagName(Field f, FSSaleReceiptItem item) throws Exception {
        String tagText = "TAG_NAME";
        if (f.tag.startsWith(tagText)) {
            int tagId = Integer.parseInt(f.tag.substring(tagText.length()));
            if (tagId == 2115) {
                if (item.getItemCodes().isEmpty()) {
                    return null;
                }
                TLVTags tags = TLVTags.getInstance();
                TLVTag tag = tags.find(2115);
                if (tag != null) {
                    return tag.getPrintName();
                }
                return null;
            }

            TLVReader reader = new TLVReader();
            for (FSTLVItem fsTLVItem : item.getTags()) {
                if (fsTLVItem.getPrint()) {
                    TLVItems items = reader.read(fsTLVItem.getData());
                    TLVItem tlvItem = items.find(tagId);
                    if (tlvItem != null) {
                        return tlvItem.getTag().getPrintName(tlvItem.getText());
                    }
                }
            }
        }
        return null;
    }

    private String getTagText(Field f, FSSaleReceiptItem item) throws Exception {
        String tagText = "TAG_VALUE";
        if (f.tag.startsWith(tagText)) {
            int tagId = Integer.parseInt(f.tag.substring(tagText.length()));
            if (tagId == 2115) {
                if (item.getItemCodes().size() == 0) {
                    return null;
                }
                byte[] mc = item.getItemCodes().get(0).getData();
                return Tag2115.getValue(mc);
            }

            TLVReader reader = new TLVReader();
            for (FSTLVItem fsTLVItem : item.getTags()) {
                if (fsTLVItem.getPrint()) {
                    TLVItems items = reader.read(fsTLVItem.getData());
                    TLVItem tlvItem = items.find(tagId);
                    if (tlvItem != null) {
                        return tlvItem.getText();
                    }
                }
            }
        }
        return null;
    }

    static String taxLetters = "АБВГДЕ";

    private String getTaxName(int tax) throws Exception {
        return context.getPrinter().getPrinter().getTaxName(tax);
    }

    private String getTaxAmount(FSSaleReceiptItem item) throws Exception {
        long taxAmount = context.getPrinter().getPrinter().getTaxAmount(item.getTax1(), item.getTotal());
        return StringUtils.amountToString(taxAmount);
    }

    private String getTaxLetter(int tax) {

        if ((tax < 1) || (tax > 6)) {
            tax = 4;
        }
        return String.valueOf(taxLetters.charAt(tax - 1));
    }

    /**
     * Prints discount only if it was made explicitly, skips empty discount
     * messages
     *
     * @param item discount item to print
     * @throws Exception
     */
    private String[] getDiscountLines2(AmountItem item) throws Exception {
        Vector<String> lines = new Vector<String>();
        long amount = item.getAmount();
        item.setAmount(Math.abs(amount));

        //не печатаем автоматически посчитанную скидку. Оставляем это для шаблона
        if (!item.getText().equals("")) {
            if (amount > 0) {
                lines.add("СКИДКА");
            } else {
                lines.add("НАДБАВКА");
            }
            String line = formatStrings(getParams().discountFont, item.getText(), "=" + StringUtils.amountToString(amount));
            lines.add(line);
        }
        return lines.toArray(new String[0]);
    }

    /**
     *
     * @param font font to use
     * @param line1 left side string
     * @param line2 right side string
     * @return strings separated by spaces
     * @throws Exception
     */
    private String formatStrings(FontNumber font, String line1, String line2) throws Exception {
        int len;
        StringBuilder sb = new StringBuilder();
        len = context.getService().getPrinter().getMessageLength(font)
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

}
