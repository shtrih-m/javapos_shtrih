/*
 * PrintDiscount.java
 *
 * Created on 2 April 2008, 20:48
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
 * Discount Command: 86H. Length: 54 bytes. · Operator password (4 bytes) ·
 * Discount value (5 bytes) 0000000000…9999999999 · Tax 1 (1 byte) «0» – no tax,
 * «1»…«4» – tax ID · Tax 2 (1 byte) «0» – no tax, «1»…«4» – tax ID · Tax 3 (1
 * byte) «0» – no tax, «1»…«4» – tax ID · Tax 4 (1 byte) «0» – no tax, «1»…«4» –
 * tax ID · Text (40 bytes) Answer: 86H. Length: 3 bytes. · Result Code (1 byte)
 * · Operator index number (1 byte) 1…30
 ****************************************************************************/
public class PrintDiscount extends PrinterCommand {
    // in

    private int password;
    private AmountItem item;
    // out
    private int operator = 0;

    /**
     * Creates a new instance of PrintDiscount
     */
    public PrintDiscount() {
        super();
    }

    public final int getCode() {
        return 0x86;
    }

    public final String getText() {
        return "Discount";
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

    public void setOperator(int operator) {
        this.operator = operator;
    }
}
