/*
 * VoidSurcharge.java
 *
 * Created on January 16 2009, 12:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
/**
 * **************************************************************************
 * Void Surcharge Command: 8BH. Length: 54 bytes. · Operator password (4 bytes)
 * · Void Surcharge value (5 bytes) 0000000000…9999999999 · Tax 1 (1 byte) «0» –
 * no tax, «1»…«4» – tax ID · Tax 2 (1 byte) «0» – no tax, «1»…«4» – tax ID ·
 * Tax 3 (1 byte) «0» – no tax, «1»…«4» – tax ID · Tax 4 (1 byte) «0» – no tax,
 * «1»…«4» – tax ID · Text (40 bytes) Answer: 8BH. Length: 3 bytes. · Result
 * Code (1 byte) · Operator index number (1 byte) 1…30
 ***************************************************************************
 */
public class PrintVoidCharge extends PrinterCommand {
    // in

    private int password;
    private AmountItem item;
    // out
    private int operator = 0;

    /**
     * Creates a new instance of VoidSurcharge
     */
    public PrintVoidCharge() {
    }

    public final int getCode() {
        return 0x8B;
    }

    public final String getText() {
        return "Void surcharge";
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

    public int getOperator() {
        return operator;
    }

    /**
     * @return the password
     */
    public int getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(int password) {
        this.password = password;
    }

    /**
     * @return the item
     */
    public AmountItem getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(AmountItem item) {
        this.item = item;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(int operator) {
        this.operator = operator;
    }
}
