/*
 * SlipSetTextLine.java
 *
 * Created on January 16 2009, 11:38
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
 * Fill Slip Line Buffer With Non-fiscal Data Command: 7AH. Length: (6 + X)
 * bytes. · Operator password (4 bytes) · Slip line number (1 byte) 1…200 · Text
 * for printing (X bytes) * Answer: 7AH. Length: 3 bytes. · Result Code (1 byte)
 * · Operator index number (1 byte) 1…30 – only WIN1251 code page characters can
 * be printed. Characters with code 27 and the very next character are not
 * placed in the buffer, but set the font type of the following characters.
 * Parameter length is up to 260 bytes.
 ****************************************************************************/

public final class SlipSetTextLine extends PrinterCommand {
    // in params
    private final int password;
    private final int line;
    private final String text;
    // out params
    private int operator;

    /** Creates a new instance of SlipSetTextLine */
    public SlipSetTextLine(int password, int line, String text) {
        this.password = password;
        this.line = line;
        this.text = text;
    }

    public final int getCode() {
        return 0x7A;
    }

    public final String getText() {
        return "Set slip line text";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(line);
        out.writeString(text, PrinterConst.MIN_TEXT_LENGTH);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }
}
