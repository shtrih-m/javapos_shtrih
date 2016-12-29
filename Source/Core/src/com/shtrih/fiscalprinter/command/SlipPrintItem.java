/*
 * SlipPrintItem.java
 *
 * Created on January 15 2009, 15:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */

/****************************************************************************
 * Transaction On Slip Command: 72H. Length: 82 bytes. · Operator password (4
 * bytes) · Quantity format (1 byte) «0» – no digits after decimal dot, «1» –
 * digits after decimal dot · Number of lines in transaction block (1 byte) 1…3
 * · Line number of Text element in transaction block (1 byte) 0…3, «0» – do not
 * print · Line number of Quantity Times Unit Price element in transaction block
 * (1 byte) 0…3, «0» – do not print · Line number of Transaction Sum element in
 * transaction block (1 byte) 1…3 · Line number of Department element in
 * transaction block (1 byte) 1…3 · Font type of Text element (1 byte) · Font
 * type of Quantity element (1 byte) · Font type of Multiplication sign element
 * (1 byte) · Font type of Unit Price element (1 byte) · Font type of
 * Transaction Sum element (1 byte) · Font type of Department element (1 byte) ·
 * Length of Text element in characters (1 byte) · Length of Quantity element in
 * characters (1 byte) · Length of Unit Price element in characters (1 byte) ·
 * Length of Transaction Sum element in characters (1 byte) · Length of
 * Department element in characters (1 byte) · Position in line of Text element
 * (1 byte) · Position in line of Quantity Times Unit Price element (1 byte) ·
 * Position in line of Transaction Sum element (1 byte) · Position in line of
 * Department element (1 byte) · Slip line number with the first line of
 * transaction block (1 byte) · Quantity (5 bytes) · Unit Price (5 bytes) ·
 * Department (1 byte) 0…16 · Tax 1 (1 byte) «0» – no tax, «1»…«4» – tax ID ·
 * Tax 2 (1 byte) «0» – no tax, «1»…«4» – tax ID · Tax 3 (1 byte) «0» – no tax,
 * «1»…«4» – tax ID · Tax 4 (1 byte) «0» – no tax, «1»…«4» – tax ID · Text (40
 * bytes) Answer: 72H. Length: 3 bytes. · Result Code (1 byte) · Operator index
 * number (1 byte) 1…30
 ****************************************************************************/

public final class SlipPrintItem extends PrinterCommand {
    // in params
    private final int password;
    private final SlipItemParams params;
    private final int line;
    private final PriceItem item;
    // out params
    private int operator;

    /** Creates a new instance of SlipPrintItem */
    public SlipPrintItem(int password, SlipItemParams params, int line,
            PriceItem item) {
        this.password = password;
        this.params = params;
        this.line = line;
        this.item = item;
    }

    public final int getCode() {
        return 0x72;
    }

    public final String getText() {
        return "Transaction on slip";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        // params
        out.writeByte(params.quantityFormat);
        out.writeByte(params.lineNumber);
        out.writeByte(params.textLine);
        out.writeByte(params.quantityLine);
        out.writeByte(params.amountLine);
        out.writeByte(params.departmentLine);
        out.writeByte(params.textFont);
        out.writeByte(params.quantityFont);
        out.writeByte(params.multSignFont);
        out.writeByte(params.priceFont);
        out.writeByte(params.amountFont);
        out.writeByte(params.departmentFont);
        out.writeByte(params.textLength);
        out.writeByte(params.quantityLength);
        out.writeByte(params.priceLength);
        out.writeByte(params.amountLength);
        out.writeByte(params.departmentLength);
        out.writeByte(params.textOffset);
        out.writeByte(params.quantityOffset);
        out.writeByte(params.amountOffset);
        out.writeByte(params.departmentOffset);
        // line
        out.writeByte(line);
        // item
        out.writeLong(item.getQuantity(), 5);
        out.writeLong(item.getPrice(), 5);
        out.writeByte(item.getDepartment());
        out.writeByte(item.getTax1());
        out.writeByte(item.getTax2());
        out.writeByte(item.getTax3());
        out.writeByte(item.getTax4());
        out.writeString(item.getText(), PrinterConst.MIN_TEXT_LENGTH);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }
}
