/*
 * SlipClose.java
 *
 * Created on January 15 2009, 17:23
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
 * Close Fiscal Slip Command: 76H. Length: 182 bytes. · Operator password (4
 * bytes) · Number of lines in transaction block (1 byte) 1…17 · Line number of
 * Receipt Total element in transaction block (1 byte) 1…17 · Line number of
 * Text element in transaction block (1 byte) 0…17, «0» – do not print · Line
 * number of Cash Payment element in transaction block (1 byte) 0…17, «0» – do
 * not print · Line number of Payment Type 2 element in transaction block (1
 * byte) 0…17, «0» – do not print · Line number of Payment Type 3 element in
 * transaction block (1 byte) 0…17, «0» – do not print · Line number of Payment
 * Type 4 element in transaction block (1 byte) 0…17, «0» – do not print · Line
 * number of Change element in transaction block (1 byte) 0…17, «0» – do not
 * print · Line number of Tax 1 Turnover element in transaction block (1 byte)
 * 0…17, «0» – do not print · Line number of Tax 2 Turnover element in
 * transaction block (1 byte) 0…17, «0» – do not print · Line number of Tax 3
 * Turnover element in transaction block (1 byte) 0…17, «0» – do not print ·
 * Line number of Tax 4 Turnover element in transaction block (1 byte) 0…17, «0»
 * – do not print · Line number of Tax 1 Sum element in transaction block (1
 * byte) 0…17, «0» – do not print · Line number of Tax 2 Sum element in
 * transaction block (1 byte) 0…17, «0» – do not print · Line number of Tax 3
 * Sum element in transaction block (1 byte) 0…17, «0» – do not print · Line
 * number of Tax 4 Sum element in transaction block (1 byte) 0…17, «0» – do not
 * print · Line number of Receipt Subtotal Before Discount/Surcharge element in
 * transaction block (1 byte) 0…17, «0» – do not print · Line number of
 * Discount/Surcharge Value element in transaction block (1 byte) 0…17, «0» – do
 * not print · Font type of Text element (1 byte) · Font type of «TOTAL» element
 * (1 byte) · Font type of Receipt Total Value element (1 byte) · Font type of
 * «CASH» element (1 byte) · Font type of Cash Payment Value element (1 byte) ·
 * Font type of Payment Type 2 Name element (1 byte) · Font type of Payment Type
 * 2 Value element (1 byte) · Font type of Payment Type 3 Name element (1 byte)
 * · Font type of Payment Type 3 Value element (1 byte) · Font type of Payment
 * Type 4Name element (1 byte) · Font type of Payment Type 4Value element (1
 * byte) · Font type of «CHANGE» element (1 byte) · Font type of Change Value
 * element (1 byte) · Font type of Tax 1 Name element (1 byte) · Font type of
 * Tax 1 Turnover Value element (1 byte) · Font type of Tax 1 Rate element (1
 * byte) · Font type of Tax 1 Value element (1 byte) · Font type of Tax 2 Name
 * element (1 byte) · Font type of Tax 2 Turnover Value element (1 byte) · Font
 * type of Tax 2 Rate element (1 byte) · Font type of Tax 2 Value element (1
 * byte) · Font type of Tax 3 Name element (1 byte) · Font type of Tax 3
 * Turnover Value element (1 byte) · Font type of Tax 3 Rate element (1 byte) ·
 * Font type of Tax 3 Value element (1 byte) · Font type of Tax 4 Name element
 * (1 byte) · Font type of Tax 4 Turnover Value element (1 byte) · Font type of
 * Tax 4 Rate element (1 byte) · Font type of Tax 4 Value element (1 byte) ·
 * Font type of «SUBTOTAL» element (1 byte) · Font type of Receipt Subtotal
 * Before Discount/Surcharge Value element (1 byte) · Font type of «DISCOUNT
 * XX.XX%» element (1 byte) · Font type of Receipt Discount Value element (1
 * byte) · Length of Text element in characters (1 byte) · Length of Receipt
 * Total Value element in characters (1 byte) · Length of Cash Payment Value
 * element in characters (1 byte) · Length of Payment Type 2 Value element in
 * characters (1 byte) · Length of Payment Type 3 Value element in characters (1
 * byte) · Length of Payment Type 4Value element in characters (1 byte) · Length
 * of Change Value element in characters (1 byte) · Length of Tax 1 Name element
 * in characters (1 byte) · Length of Tax 1 Turnover element in characters (1
 * byte) · Length of Tax 1 Rate element in characters (1 byte) · Length of Tax 1
 * Value element in characters (1 byte) · Length of Tax 2 Name element in
 * characters (1 byte) · Length of Tax 2 Turnover element in characters (1 byte)
 * · Length of Tax 2 Rate element in characters (1 byte) · Length of Tax 2 Value
 * element in characters (1 byte) · Length of Tax 3 Name element in characters
 * (1 byte) · Length of Tax 3 Turnover element in characters (1 byte) · Length
 * of Tax 3 Rate element in characters (1 byte) · Length of Tax 3 Value element
 * in characters (1 byte) · Length of Tax 4 Name element in characters (1 byte)
 * · Length of Tax 4 Turnover element in characters (1 byte) · Length of Tax 4
 * Rate element in characters (1 byte) · Length of Tax 4 Value element in
 * characters (1 byte) · Length of Receipt Subtotal Before Discount/Surcharge
 * Value element in characters (1 byte) · Length of «DISCOUNT XX.XX%» element in
 * characters (1 byte) · Length of Receipt Discount Value element in characters
 * (1 byte) · Position in line of Text element (1 byte) · Position in line of
 * «TOTAL» element (1 byte) · Position in line of Receipt Total Value element (1
 * byte) · Position in line of «CASH» element (1 byte) · Position in line of
 * Cash Payment Value element (1 byte) · Position in line of Payment Type 2 Name
 * element (1 byte) · Position in line of Payment Type 2 Value element (1 byte)
 * · Position in line of Payment Type 3 Name element (1 byte) · Position in line
 * of Payment Type 3 Value element (1 byte) · Position in line of Payment Type 4
 * Name element (1 byte) · Position in line of Payment Type 4 Value element (1
 * byte) · Position in line of «CHANGE» element (1 byte) · Position in line of
 * Change Value element (1 byte) · Position in line of Tax 1 Name element (1
 * byte) · Position in line of Tax 1 Turnover Value element (1 byte) · Position
 * in line of Tax 1 Rate element (1 byte) · Position in line of Tax 1 Value
 * element (1 byte) · Position in line of Tax 2 Name element (1 byte) · Position
 * in line of Tax 2 Turnover Value element (1 byte) · Position in line of Tax 2
 * Rate element (1 byte) · Position in line of Tax 2 Value element (1 byte) ·
 * Position in line of Tax 3 Name element (1 byte) · Position in line of Tax 3
 * Turnover Value element (1 byte) · Position in line of Tax 3 Rate element (1
 * byte) · Position in line of Tax 3 Value element (1 byte) · Position in line
 * of Tax 4 Name element (1 byte) · Position in line of Tax 4 Turnover Value
 * element (1 byte) · Position in line of Tax 4 Rate element (1 byte) · Position
 * in line of Tax 4 Value element (1 byte) · Position in line of «SUBTOTAL»
 * element (1 byte) · Position in line of Receipt Subtotal Before
 * Discount/Surcharge Value element (1 byte) · Position in line of «DISCOUNT
 * XX.XX%» element (1 byte) · Position in line of Receipt Discount Value element
 * (1 byte) · Slip line number with the first line of Close Fiscal Slip block (1
 * byte) · Cash Payment value (5 bytes) · Payment Type 2 value (5 bytes) ·
 * Payment Type 3 value (5 bytes) · Payment Type 4 value (5 bytes) · Receipt
 * Discount Value 0 to 99,99 % (2 bytes) 0000…9999 · Tax 1 (1 byte) «0» – no
 * tax, «1»…«4» – tax ID · Tax 2 (1 byte) «0» – no tax, «1»…«4» – tax ID · Tax 3
 * (1 byte) «0» – no tax, «1»…«4» – tax ID · Tax 4 (1 byte) «0» – no tax,
 * «1»…«4» – tax ID · Text (40 bytes) Answer: 76H. Length: 8 bytes. · Result
 * Code (1 byte) · Operator index number (1 byte) 1…30 · Change value (5 bytes)
 * 0000000000…9999999999
 ****************************************************************************/

public final class SlipClose extends PrinterCommand {
    // in params
    private final int password;
    private final SlipCloseParams slipCloseParams;
    private final int line;
    private final CloseRecParams params;
    // out params
    private int operator;
    private long change;

    /** Creates a new instance of SlipClose */
    public SlipClose(int password, SlipCloseParams slipCloseParams, int line,
            CloseRecParams params) {
        this.password = password;
        this.slipCloseParams = slipCloseParams;
        this.line = line;
        this.params = params;
    }

    public final int getCode() {
        return 0x76;
    }

    public final String getText() {
        return "Close fiscal slip";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        // slipCloseParams
        out.writeByte(slipCloseParams.lineNumber);
        out.writeByte(slipCloseParams.totalLine);
        out.writeByte(slipCloseParams.textLine);
        out.writeByte(slipCloseParams.cashLine);
        out.writeByte(slipCloseParams.amount2Line);
        out.writeByte(slipCloseParams.amount3Line);
        out.writeByte(slipCloseParams.amount4Line);
        out.writeByte(slipCloseParams.changeLine);
        out.writeByte(slipCloseParams.tax1Line);
        out.writeByte(slipCloseParams.tax2Line);
        out.writeByte(slipCloseParams.tax3Line);
        out.writeByte(slipCloseParams.tax4Line);
        out.writeByte(slipCloseParams.tax1TotalLine);
        out.writeByte(slipCloseParams.tax2TotalLine);
        out.writeByte(slipCloseParams.tax3TotalLine);
        out.writeByte(slipCloseParams.tax4TotalLine);
        out.writeByte(slipCloseParams.subtotalLine);
        out.writeByte(slipCloseParams.discountLine);
        out.writeByte(slipCloseParams.textFont);
        out.writeByte(slipCloseParams.totalTextFont);
        out.writeByte(slipCloseParams.totalAmountFont);
        out.writeByte(slipCloseParams.cashTextFont);
        out.writeByte(slipCloseParams.cashAmountFont);
        out.writeByte(slipCloseParams.amount2TextFont);
        out.writeByte(slipCloseParams.amount2ValueFont);
        out.writeByte(slipCloseParams.amount3TextFont);
        out.writeByte(slipCloseParams.amount3ValueFont);
        out.writeByte(slipCloseParams.amount4TextFont);
        out.writeByte(slipCloseParams.amount4ValueFont);
        out.writeByte(slipCloseParams.chnageTextFont);
        out.writeByte(slipCloseParams.chnageValueFont);
        out.writeByte(slipCloseParams.tax1TextFont);
        out.writeByte(slipCloseParams.tax1AmountFont);
        out.writeByte(slipCloseParams.tax1RateFont);
        out.writeByte(slipCloseParams.tax1ValueFont);
        out.writeByte(slipCloseParams.tax2TextFont);
        out.writeByte(slipCloseParams.tax2AmountFont);
        out.writeByte(slipCloseParams.tax2RateFont);
        out.writeByte(slipCloseParams.tax2ValueFont);
        out.writeByte(slipCloseParams.tax3TextFont);
        out.writeByte(slipCloseParams.tax3AmountFont);
        out.writeByte(slipCloseParams.tax3RateFont);
        out.writeByte(slipCloseParams.tax3ValueFont);
        out.writeByte(slipCloseParams.tax4TextFont);
        out.writeByte(slipCloseParams.tax4AmountFont);
        out.writeByte(slipCloseParams.tax4RateFont);
        out.writeByte(slipCloseParams.tax4ValueFont);
        out.writeByte(slipCloseParams.subtotalTextFont);
        out.writeByte(slipCloseParams.subtotalValueFont);
        out.writeByte(slipCloseParams.discountTextFont);
        out.writeByte(slipCloseParams.discountValueFont);
        out.writeByte(slipCloseParams.textLength);
        out.writeByte(slipCloseParams.totalLength);
        out.writeByte(slipCloseParams.cashLength);
        out.writeByte(slipCloseParams.payment2Length);
        out.writeByte(slipCloseParams.payment3Length);
        out.writeByte(slipCloseParams.payment4Length);
        out.writeByte(slipCloseParams.changeLength);
        out.writeByte(slipCloseParams.tax1TextLength);
        out.writeByte(slipCloseParams.tax1AmountLength);
        out.writeByte(slipCloseParams.tax1RateLength);
        out.writeByte(slipCloseParams.tax1ValueLength);
        out.writeByte(slipCloseParams.tax2TextLength);
        out.writeByte(slipCloseParams.tax2AmountLength);
        out.writeByte(slipCloseParams.tax2RateLength);
        out.writeByte(slipCloseParams.tax2ValueLength);
        out.writeByte(slipCloseParams.tax3TextLength);
        out.writeByte(slipCloseParams.tax3AmountLength);
        out.writeByte(slipCloseParams.tax3RateLength);
        out.writeByte(slipCloseParams.tax3ValueLength);
        out.writeByte(slipCloseParams.tax4TextLength);
        out.writeByte(slipCloseParams.tax4AmountLength);
        out.writeByte(slipCloseParams.tax4RateLength);
        out.writeByte(slipCloseParams.tax4ValueLength);
        out.writeByte(slipCloseParams.subtotalLength);
        out.writeByte(slipCloseParams.discountTextLength);
        out.writeByte(slipCloseParams.discountValueLength);
        out.writeByte(slipCloseParams.textOffset);
        out.writeByte(slipCloseParams.totalTextOffset);
        out.writeByte(slipCloseParams.totalValueOffset);
        out.writeByte(slipCloseParams.payment1TextOffset);
        out.writeByte(slipCloseParams.payment1ValueOffset);
        out.writeByte(slipCloseParams.payment2TextOffset);
        out.writeByte(slipCloseParams.payment2ValueOffset);
        out.writeByte(slipCloseParams.payment3TextOffset);
        out.writeByte(slipCloseParams.payment3ValueOffset);
        out.writeByte(slipCloseParams.payment4TextOffset);
        out.writeByte(slipCloseParams.payment4ValueOffset);
        out.writeByte(slipCloseParams.changeTextOffset);
        out.writeByte(slipCloseParams.changeValueOffset);
        out.writeByte(slipCloseParams.tax1TextOffset);
        out.writeByte(slipCloseParams.tax1AmountOffset);
        out.writeByte(slipCloseParams.tax1RateOffset);
        out.writeByte(slipCloseParams.tax1ValueOffset);
        out.writeByte(slipCloseParams.tax2TextOffset);
        out.writeByte(slipCloseParams.tax2AmountOffset);
        out.writeByte(slipCloseParams.tax2RateOffset);
        out.writeByte(slipCloseParams.tax2ValueOffset);
        out.writeByte(slipCloseParams.tax3TextOffset);
        out.writeByte(slipCloseParams.tax3AmountOffset);
        out.writeByte(slipCloseParams.tax3RateOffset);
        out.writeByte(slipCloseParams.tax3ValueOffset);
        out.writeByte(slipCloseParams.tax4TextOffset);
        out.writeByte(slipCloseParams.tax4AmountOffset);
        out.writeByte(slipCloseParams.tax4RateOffset);
        out.writeByte(slipCloseParams.tax4ValueOffset);
        out.writeByte(slipCloseParams.subtotalTextOffset);
        out.writeByte(slipCloseParams.subtotalValueOffset);
        out.writeByte(slipCloseParams.discountTextOffset);
        out.writeByte(slipCloseParams.discountValueOffset);
        // line
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
