/*
 * EndFiscalReceipt.java
 *
 * Created on 2 April 2008, 20:34
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
 * Close receipt Command: 85H. Length: 71 bytes. · Operator password (4 bytes) ·
 * Cash Payment value (5 bytes) 0000000000…9999999999 · Payment Type 2 value (5
 * bytes) 0000000000…9999999999 · Payment Type 3 value (5 bytes)
 * 0000000000…9999999999 · Payment Type 4 value (5 bytes) 0000000000…9999999999
 * · Receipt Percentage Discount/Surcharge Value 0 to 99,99 % (2 bytes with
 * sign) –9999…9999, surcharge if value is negative · Tax 1 (1 byte) «0» – no
 * tax, «1»…«4» – tax ID · Tax 2 (1 byte) «0» – no tax, «1»…«4» – tax ID · Tax 3
 * (1 byte) «0» – no tax, «1»…«4» – tax ID · Tax 4 (1 byte) «0» – no tax,
 * «1»…«4» – tax ID · Text (40 bytes) Answer: 85H. Length: 8 bytes. · Result
 * Code (1 byte) · Operator index number (1 byte) 1…30 · Change value (5 bytes)
 * 0000000000…9999999999
 ****************************************************************************/
public final class EndFiscalReceipt extends PrinterCommand {
    // in
    private int password;
    private CloseRecParams params;
    // out
    private int operator = 0;
    private long change = 0;

    /**
     * Creates a new instance of EndFiscalReceipt
     */
    public EndFiscalReceipt() {
        super();
    }

    public final int getCode() {
        return 0x85;
    }

    public final String getText() {
        return "Close receipt";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeLong(getParams().getSum1(), 5);
        out.writeLong(getParams().getSum2(), 5);
        out.writeLong(getParams().getSum3(), 5);
        out.writeLong(getParams().getSum4(), 5);
        out.writeShort(getParams().getDiscount());
        out.writeByte(getParams().getTax1());
        out.writeByte(getParams().getTax2());
        out.writeByte(getParams().getTax3());
        out.writeByte(getParams().getTax4());
        out.writeString(getParams().getText(), PrinterConst.MIN_TEXT_LENGTH);
    }

    public final void decode(CommandInputStream in) throws Exception {
        setOperator(in.readByte());
        setChange(in.readLong(5));
    }

    public int getOperator() {
        return operator;
    }

    public long getChange() {
        return change;
    }

    public int getPassword() {
        return password;
    }

    public CloseRecParams getParams() {
        return params;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setParams(CloseRecParams params) {
        this.params = params;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public void setChange(long change) {
        this.change = change;
    }
}
