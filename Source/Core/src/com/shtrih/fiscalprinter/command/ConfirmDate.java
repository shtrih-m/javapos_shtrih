/*
 * ConfirmDate.java
 *
 * Created on 2 April 2008, 21:14
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
 * Confirm date Command: 23H. Length: 8 bytes. · System Administrator password
 * (4 bytes) 30 · Date (3 bytes) DD-MM-YY Answer: 23H. Length: 2 bytes. · Result
 * Code (1 byte)
 ****************************************************************************/
public final class ConfirmDate extends PrinterCommand {
    // in
    private int password;
    private PrinterDate date;

    /**
     * Creates a new instance of ConfirmDate
     */
    public ConfirmDate() {
        super();
    }

    public final int getCode() {
        return 0x23;
    }

    public final String getText() {
        return "Confirm date";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeDate(getDate());
    }

    public final void decode(CommandInputStream in) throws Exception {
    }

    public int getPassword() {
        return password;
    }

    public PrinterDate getDate() {
        return date;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setDate(PrinterDate date) {
        this.date = date;
    }
}
