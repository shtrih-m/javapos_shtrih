/*
 * LoadExtendedGraphics.java
 *
 * Created on April 2 2008, 21:24
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
 * Load extended graphics Command: C4H. Length: 47 bytes. · Operator password (4
 * bytes) · Graphics line number (2 bytes) 0…1199 · Graphical data (40 bytes)
 * Answer: C4H. Length: 3 bytes. · Result Code (1 byte) · Operator index number
 * (1 byte) 1…30
 ****************************************************************************/
public final class LoadGraphics2 extends PrinterCommand {
    // in

    private int password; // Operator password (4 bytes)
    private int lineNumber; // Graphics line number (2 bytes) 0…1199
    private byte[] data; // Graphical data (40 bytes)
    // out
    private int operator;

    /**
     * Creates a new instance of LoadExtendedGraphics
     */
    public LoadGraphics2() {
        super();
    }

    
    public final int getCode() {
        return 0xC4;
    }

    
    public final String getText() {
        return "Load extended graphics";
    }

    
    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeShort(getLineNumber());
        out.writeBytes(getData(), 40);
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

    public int getLineNumber() {
        return lineNumber;
    }

    public byte[] getData() {
        return data;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setLineNumber(int lineNumber) throws Exception {
        MethodParameter.checkRange(lineNumber, 1,
                PrinterConst.MAX_LINES_GRAPHICS2, "line number");
        this.lineNumber = lineNumber;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }
}
