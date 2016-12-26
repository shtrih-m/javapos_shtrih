/*
 * PrintExtendedGraphics.java
 *
 * Created on April 2 2008, 21:27
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
 * Print extended graphics Command: C3H. Length: 9 bytes. · Operator password (4
 * bytes) · Number of first line of preloaded graphics to be printed (1 byte)
 * 1…1200 · Number of last line of preloaded graphics to be printed (1 byte)
 * 1…1200 Answer: C3H. Length: 3 bytes. · Result Code (1 byte) · Operator index
 * number (1 byte) 1…30
 ****************************************************************************/

public final class PrintGraphics2 extends PrinterCommand {
    // in
    private final int password; // Operator password
    private final int line1; // Number of first line
    private final int line2; // Number of last line
    // out
    private int operator = 0;

    /**
     * Creates a new instance of PrintExtendedGraphics
     */
    public PrintGraphics2(int password, int line1, int line2) throws Exception {
        MethodParameter.checkRange(line1, 1,
                PrinterConst.MAX_LINES_GRAPHICS2, "line1");

        MethodParameter.checkRange(line2, 1,
                PrinterConst.MAX_LINES_GRAPHICS2, "line2");

        this.password = password;
        this.line1 = line1;
        this.line2 = line2;
    }

    public static final boolean validLine(int line) {
        return (line >= 1) && (line <= PrinterConst.MAX_LINES_GRAPHICS2);
    }

    public static final boolean validLines(int line1, int line2) {
        return validLine(line1) && validLine(line2) && (line1 <= line2);
    }

    
    public final int getCode() {
        return 0xC3;
    }

    
    public final String getText() {
        return "Print extended graphics";
    }

    
    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeShort(line1);
        out.writeShort(line2);
    }

    
    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }
}
