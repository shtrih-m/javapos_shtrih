/*
 * PrintGraphics.java
 *
 * Created on April 15 2008, 13:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.util.MethodParameter;

/****************************************************************************
 * Print Graphics Command: C1H. Length: 7 bytes. · Operator password (4 bytes) ·
 * Number of first line of preloaded graphics to be printed (1 byte) 1…200 ·
 * Number of last line of preloaded graphics to be printed (1 byte) 1…200
 * Answer: C1H. Length: 3 bytes. · Result Code (1 byte) · Operator index number
 * (1 byte) 1…30
 ****************************************************************************/

public class PrintGraphics extends PrinterCommand {
    // in
    private final int password; // Operator password
    private final int line1; // Number of first line
    private final int line2; // Number of last line
    // out
    private int operator = 0;

    /**
     * Creates a new instance of PrintGraphics
     */
    public PrintGraphics(int password, int line1, int line2) throws Exception {
        MethodParameter.checkRange(line1, 1, PrinterConst.MAX_LINES_GRAPHICS,
                "line1");

        MethodParameter.checkRange(line2, 1, PrinterConst.MAX_LINES_GRAPHICS,
                "line2");

        this.password = password;
        this.line1 = line1;
        this.line2 = line2;
    }

    
    public final int getCode() {
        return 0xC1;
    }

    
    public final String getText() {
        return "Print graphics";
    }

    public static final boolean validLine(int line) {
        return (line >= 1) && (line <= PrinterConst.MAX_LINES_GRAPHICS);
    }

    public static final boolean validLines(int line1, int line2) {
        return validLine(line1) && validLine(line2) && (line1 <= line2);
    }

    
    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(line1);
        out.writeByte(line2);
    }

    
    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }
}
