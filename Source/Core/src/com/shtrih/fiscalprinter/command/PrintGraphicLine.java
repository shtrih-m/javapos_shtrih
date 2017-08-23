/*
 * PrintGraphicLine.java
 *
 * Created on April 2 2008, 21:29
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
 * Print graphic line Command: C5H. Length: X + 7 bytes. · 
 * Operator password (4 bytes) 
 * Number of repetitions (2 bytes) 
 * Station (1 byte) 
 * Graphical data (X bytes) 
 * 
 * Answer: C5H. Length: 3 bytes. 
 * · Result Code (1 byte) 
 * · Operator index number (1 byte) 1…30
 * 
 ****************************************************************************/

public final class PrintGraphicLine extends PrinterCommand {
    // in
    private int password; // Operator password
    private int height;   // Number of repetitions
    private int station;  // Station
    private byte[] data;  // Graphical data
    // out
    private int operator = 0;

    /**
     * Creates a new instance of PrintGraphicLine
     */
    public PrintGraphicLine() {
    }

    public final int getCode() {
        return 0xC5;
    }

    public final String getText() {
        return "Print graphic line";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeShort(getHeight());
        out.writeByte(getStation());
        out.writeBytes(getData());
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
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the station
     */
    public int getStation() {
        return station;
    }

    /**
     * @param station the station to set
     */
    public void setStation(int station) {
        this.station = station;
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(int operator) {
        this.operator = operator;
    }
}
