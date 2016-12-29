/*
 * PrintSale.java
 *
 * Created on April 2 2008, 20:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */

/*****************************************************************************
 * Sale Command: 80H. Length: 60 bytes. · Operator password (4 bytes) · Quantity
 * (5 bytes) 0000000000…9999999999 · Unit Price (5 bytes) 0000000000…9999999999
 * · Department (1 byte) 0…16 · Tax 1 (1 byte) «0» – no tax, «1»…«4» – tax ID ·
 * Tax 2 (1 byte) «0» – no tax, «1»…«4» – tax ID · Tax 3 (1 byte) «0» – no tax,
 * «1»…«4» – tax ID · Tax 4 (1 byte) «0» – no tax, «1»…«4» – tax ID · Text (40
 * bytes) Answer: 80H. Length: 3 bytes. · Result Code (1 byte) · Operator index
 * number (1 byte) 1…30
 *****************************************************************************/

public final class PrintRefund extends PrinterCommand {
    // in
    private final int password;
    private final PriceItem item;
    // out
    private int operator;

    /**
     * Creates a new instance of PrintSale
     */
    public PrintRefund(int password, PriceItem item) {
        this.password = password;
        this.item = item;
    }

    public final int getCode() {
        return 0x81;
    }

    public final int getOperator() {
        return operator;
    }

    public int getPassword() {
        return password;
    }

    public PriceItem getItem() {
        return item;
    }

    public final String getText() {
        return "Refund";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
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
}
