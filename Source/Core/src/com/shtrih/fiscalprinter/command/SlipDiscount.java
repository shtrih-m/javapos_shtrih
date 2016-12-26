/*
 * SlipDiscount.java
 *
 * Created on January 15 2009, 16:26
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
 * Discount/Surcharge On Slip Command: 74H. Length: 68 bytes. · Operator
 * password (4 bytes) · Number of lines in transaction block (1 byte) 1…2 · Line
 * number of Text element in transaction block (1 byte) 0…2, «0» – do not print
 * · Line number of Transaction Name element in transaction block (1 byte) 1…2 ·
 * Line number of Transaction Sum element in transaction block (1 byte) 1…2 ·
 * Font type of Text element (1 byte) · Font type of Transaction Name element (1
 * byte) · Font type of Transaction Sum element (1 byte) · Length of Text
 * element in characters (1 byte) · Length of Transaction Sum element in
 * characters (1 byte) · Position in line of Text element (1 byte) · Position in
 * line of Transaction Name element (1 byte) · Position in line of Transaction
 * Sum element (1 byte) · Transaction type (1 byte) «0» – Discount, «1» –
 * Surcharge · Slip line number with the first line of Discount/Surcharge block
 * (1 byte) · Transaction Sum (5 bytes) · Tax 1 (1 byte) «0» – no tax, «1»…«4» –
 * tax ID · Tax 2 (1 byte) «0» – no tax, «1»…«4» – tax ID · Tax 3 (1 byte) «0» –
 * no tax, «1»…«4» – tax ID · Tax 4 (1 byte) «0» – no tax, «1»…«4» – tax ID ·
 * Text (40 bytes) Answer: 74H. Length: 3 bytes. · Result Code (1 byte) ·
 * Operator index number (1 byte) 1…30
 ****************************************************************************/

public final class SlipDiscount extends PrinterCommand {
    class SlipAmountParams {
        // Number of lines in transaction block (1 byte) 1…2
        public byte lineNumber = 0;
        // Line number of Text element in transaction block (1 byte) 0…2, «0» –
        // do not print
        public byte textLine = 0;
        // Line number of Transaction Name element in transaction block (1 byte)
        // 1…2
        public byte nameLine = 0;
        // Line number of Transaction Sum element in transaction block (1 byte)
        // 1…2
        public byte amountLine = 0;
        // Font type of Text element (1 byte)
        public byte textFont = 0;
        // Font type of Transaction Name element (1 byte)
        public byte nameFont = 0;
        // Font type of Transaction Sum element (1 byte)
        public byte amountFont = 0;
        // Length of Text element in characters (1 byte)
        public byte textLength = 0;
        // Length of Transaction Sum element in characters (1 byte)
        public byte amountLength = 0;
        // Position in line of Text element (1 byte)
        public byte textOffset = 0;
        // Position in line of Transaction Name element (1 byte)
        public byte nameOffset = 0;
        // Position in line of Transaction Sum element (1 byte)
        public byte amountOffset = 0;
        // Transaction type (1 byte) «0» – Discount, «1» – Surcharge
        public byte operationType = 0;
    }

    // in params
    private final int password;
    private final SlipAmountParams params;
    private final int line;
    private final AmountItem item;
    // out params
    private int operator;

    /** Creates a new instance of SlipDiscount */
    public SlipDiscount(int password, SlipAmountParams params, int line,
            AmountItem item) {
        this.password = password;
        this.params = params;
        this.line = line;
        this.item = item;
    }

    public final int getCode() {
        return 0x74;
    }

    public final String getText() {
        return "Discount/Surcharge on slip";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        // params
        out.writeByte(params.lineNumber);
        out.writeByte(params.textLine);
        out.writeByte(params.nameLine);
        out.writeByte(params.amountLine);
        out.writeByte(params.textFont);
        out.writeByte(params.nameFont);
        out.writeByte(params.amountFont);
        out.writeByte(params.textLength);
        out.writeByte(params.amountLength);
        out.writeByte(params.textOffset);
        out.writeByte(params.nameOffset);
        out.writeByte(params.amountOffset);
        out.writeByte(params.operationType);
        // line
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
