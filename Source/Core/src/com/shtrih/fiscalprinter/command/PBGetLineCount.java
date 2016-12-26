/*
 * PBGetLineCount.java
 *
 * Created on January 16 2009, 14:57
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
 * Read print buffer line number Command: C8H. Message length: 5 bytes. ·
 * Operator password (4 bytes) Answer: C8H. Message length: 6 bytes. · Result
 * code (1 byte) · Total lines number (2 bytes) · Printed lines number (2 bytes)
 ****************************************************************************/
public final class PBGetLineCount extends PrinterCommand {
    // in

    private int password;
    // out
    private int bufferCount;
    private int printedCount;

    /** Creates a new instance of PBGetLineCount */
    public PBGetLineCount() {
        super();
    }

    public final int getCode() {
        return 0xC8;
    }

    public final String getText() {
        return "Read print buffer lines count";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
    }

    public final void decode(CommandInputStream in) throws Exception {
        setBufferCount(in.readShort());
        setPrintedCount(in.readShort());
    }

    public int getPassword() {
        return password;
    }

    public int getBufferCount() {
        return bufferCount;
    }

    public int getPrintedCount() {
        return printedCount;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setBufferCount(int bufferCount) {
        this.bufferCount = bufferCount;
    }

    public void setPrintedCount(int printedCount) {
        this.printedCount = printedCount;
    }
}
