/*
 * SlipPrintItemStandard.java
 *
 * Created on January 15 2009, 15:12
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
 * Standard Transaction On Slip Command: 73H. Length: 61 bytes. · Operator
 * password (4 bytes) · Slip line number with the first line of transaction
 * block (1 byte) · Quantity (5 bytes) · Unit Price (5 bytes) · Department (1
 * byte) 0…16 · Tax 1 (1 byte) «0» – no tax, «1»…«4» – tax ID · Tax 2 (1 byte)
 * «0» – no tax, «1»…«4» – tax ID · Tax 3 (1 byte) «0» – no tax, «1»…«4» – tax
 * ID · Tax 4 (1 byte) «0» – no tax, «1»…«4» – tax ID · Text (40 bytes) Answer:
 * 73H. Length: 3 bytes. · Result Code (1 byte) · Operator index number (1 byte)
 * 1…30
 ****************************************************************************/

public final class SlipPrintItemStandard extends PrinterCommand {
    // in params
    private final int password;
    private final int line;
    private final PriceItem item;
    // out params
    private int operator;

    /** Creates a new instance of SlipPrintItemStandard */
    public SlipPrintItemStandard(int password, int line, PriceItem item) {
        this.password = password;
        this.line = line;
        this.item = item;
    }

    public final int getCode() {
        return 0x73;
    }

    public final String getText() {
        return "Standard transaction on slip";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
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
