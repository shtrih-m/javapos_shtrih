/*
 * SlipCloseStandard.java
 *
 * Created on January 15 2009, 18:03
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
 * Close Standard Fiscal Slip Command: 77H. Length: 72 bytes. · Operator
 * password (4 bytes) · Slip line number with the first line of Close Fiscal
 * Slip block (1 byte) · Cash Payment value (5 bytes) · Payment Type 2 value (5
 * bytes) · Payment Type 3 value (5 bytes) · Payment Type 4 value (5 bytes) ·
 * Receipt Discount Value 0 to 99,99 % (2 bytes) 0000…9999 · Tax 1 (1 byte) «0»
 * – no tax, «1»…«4» – tax ID · Tax 2 (1 byte) «0» – no tax, «1»…«4» – tax ID ·
 * Tax 3 (1 byte) «0» – no tax, «1»…«4» – tax ID · Tax 4 (1 byte) «0» – no tax,
 * «1»…«4» – tax ID · Text (40 bytes) Answer: 77H. Length: 8 bytes. · Result
 * Code (1 byte) · Operator index number (1 byte) 1…30 · Change value (5 bytes)
 * 0000000000…9999999999
 ****************************************************************************/

public final class SlipCloseStandard extends PrinterCommand {

    // in
    private final int password;
    private final int line;
    private final CloseRecParams params;
    // out
    private int operator = 0;
    private long change = 0;

    /** Creates a new instance of SlipCloseStandard */
    public SlipCloseStandard(int password, int line, CloseRecParams params) {
        this.password = password;
        this.line = line;
        this.params = params;
    }

    public final int getCode() {
        return 0x77;
    }

    public final String getText() {
        return "Close standard fiscal slip";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(line);
        out.writeLong(params.getSum1(), 5);
        out.writeLong(params.getSum2(), 5);
        out.writeLong(params.getSum3(), 5);
        out.writeLong(params.getSum4(), 5);
        out.writeShort(params.getDiscount());
        out.writeByte(params.getTax1());
        out.writeByte(params.getTax2());
        out.writeByte(params.getTax3());
        out.writeByte(params.getTax4());
        out.writeString(params.getText(), PrinterConst.MIN_TEXT_LENGTH);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
        change = in.readLong(5);
    }

    public int getOperator() {
        return operator;
    }

    public long getChange() {
        return change;
    }
}
