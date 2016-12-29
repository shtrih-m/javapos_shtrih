/*
 * WriteTime.java
 *
 * Created on April 2 2008, 20:59
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
 * Set Clock Time Command: 21H. Length: 8 bytes. · System Administrator password
 * (4 bytes) 30 · Time (3 bytes) HH-MM-SS Answer: 21H. Length: 2 bytes. · Result
 * Code (1 byte)
 *****************************************************************************/

public final class WriteTime extends PrinterCommand {
    // in
    private int password;
    private PrinterTime time;

    /**
     * Creates a new instance of WriteTime
     */
    public WriteTime() {
    }

    public final int getCode() {
        return 0x21;
    }

    public final String getText() {
        return "Set clock time";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeTime(time);
    }

    public final void decode(CommandInputStream in) throws Exception {
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
     * @return the time
     */
    public PrinterTime getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(PrinterTime time) {
        this.time = time;
    }
}
