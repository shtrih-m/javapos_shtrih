/*
 * SlipDiscountStandard.java
 *
 * Created on January 15 2009, 17:18
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
 * Standard Discount/Surcharge On Slip Command: 75H. Length: 56 bytes. ·
 * Operator password (4 bytes) · Transaction type (1 byte) «0» – Discount, «1» –
 * Surcharge · Slip line number with the first line of Discount/Surcharge block
 * (1 byte) · Transaction Sum (5 bytes) · Tax 1 (1 byte) «0» – no tax, «1»…«4» –
 * tax ID · Tax 2 (1 byte) «0» – no tax, «1»…«4» – tax ID · Tax 3 (1 byte) «0» –
 * no tax, «1»…«4» – tax ID · Tax 4 (1 byte) «0» – no tax, «1»…«4» – tax ID ·
 * Text (40 bytes) Answer: 75H. Length: 3 bytes. · Result Code (1 byte) ·
 * Operator index number (1 byte) 1…30
 ****************************************************************************/

public final class SlipDiscountStandard extends PrinterCommand {

    // in params
    private final int password;
    private final int operationType;
    private final int line;
    private final AmountItem item;
    // out params
    private int operator;

    /** Creates a new instance of SlipDiscountStandard */
    public SlipDiscountStandard(int password, int operationType, int line,
            AmountItem item) {
        this.password = password;
        this.operationType = operationType;
        this.line = line;
        this.item = item;
    }

    public final int getCode() {
        return 0x75;
    }

    public final String getText() {
        return "Standard discount/surcharge on slip";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(operationType);
        out.writeByte(line);
        // item
        out.writeLong(item.getAmount(), 5);
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
