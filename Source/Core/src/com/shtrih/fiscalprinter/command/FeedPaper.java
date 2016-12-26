/*
 * FeedPaper.java
 *
 * Created on 2 April 2008, 19:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
/*****************************************************************************
 * Feed paper Command: 29H. Length: 7 bytes. · Operator password (4 bytes) ·
 * Flags (1 byte) Bit 0 – journal station, Bit 1 – receipt station, Bit 2 – slip
 * station · Number of lines to feed (1 byte) 1…255 – the maximum number of
 * lines to feed is limited by the size of print buffer, but does not exceed 255
 * Answer: 29H. Length: 3 bytes. · Result Code (1 byte) · Operator index number
 * (1 byte) 1…30
 *****************************************************************************/
public final class FeedPaper extends PrinterCommand {
    // in params
    private int password;
    private int stations;
    private int lineNumber;
    // out
    private int operator = 0;

    /**
     * Creates a new instance of FeedPaper
     */
    public FeedPaper() {
        super();
    }

    public final int getCode() {
        return 0x29;
    }

    public final String getText() {
        return "Feed paper";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getStations());
        out.writeByte(getLineNumber());
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

    public int getStations() {
        return stations;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setStations(int stations) {
        this.stations = stations;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }
}
