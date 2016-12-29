/*
 * PrintCharge.java
 *
 * Created on April 2 2008, 20:53
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
 * Surcharge Command: 87H. Length: 54 bytes. · Operator password (4 bytes) ·
 * Surcharge value (5 bytes) 0000000000…9999999999 · Tax 1 (1 byte) «0» – no
 * tax, «1»…«4» – tax ID · Tax 2 (1 byte) «0» – no tax, «1»…«4» – tax ID · Tax 3
 * (1 byte) «0» – no tax, «1»…«4» – tax ID · Tax 4 (1 byte) «0» – no tax,
 * «1»…«4» – tax ID · Text (40 bytes) Answer: 87H. Length: 3 bytes. · Result
 * Code (1 byte) · Operator index number (1 byte) 1…30
 ****************************************************************************/
public final class PrintCharge extends PrinterCommand {
    // in

    private int password;
    private AmountItem item;
    // out
    private int operator;

    /**
     * Creates a new instance of PrintCharge
     */
    public PrintCharge() {
        super();
    }

    public final int getCode() {
        return 0x87;
    }

    public final String getText() {
        return "Surcharge";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeLong(getItem().getAmount(), 5);
        out.writeByte(getItem().getTax1());
        out.writeByte(getItem().getTax2());
        out.writeByte(getItem().getTax3());
        out.writeByte(getItem().getTax4());
        out.writeString(getItem().getText(), PrinterConst.MIN_TEXT_LENGTH);
    }

    public final void decode(CommandInputStream in) throws Exception {
        setOperator(in.readByte());
    }

    public int getPassword() {
        return password;
    }

    public AmountItem getItem() {
        return item;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setItem(AmountItem item) {
        this.item = item;
    }

    /**
     * @return the operator
     */
    public int getOperator() {
        return operator;
    }

    /**
     * @param operator
     *            the operator to set
     */
    public void setOperator(int operator) {
        this.operator = operator;
    }
}
