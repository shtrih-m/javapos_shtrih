/*
 * PrintRecHeader.java
 *
 * Created on January 15 2009, 12:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/****************************************************************************
 * Print Document Header Command: 18H. Length: 37 bytes. · Operator password (4
 * bytes) · Receipt title (30 bytes) · Receipt number (2 bytes) Answer: 18H.
 * Length: 5 bytes. · Result Code (1 byte) · Operator index number (1 byte) 1…30
 * · Current receipt number (2 bytes)
 ****************************************************************************/
public final class PrintDocHeader extends PrinterCommand {

    // in
    private int password;
    private String title;
    private int number;
    // out
    private int operator;
    private int recNumber;

    /**
     * Creates a new instance of PrintRecHeader
     */
    public PrintDocHeader() {
        super();
    }

    public final int getCode() {
        return 0x18;
    }

    public final String getText() {
        return "Print receipt header";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeString(getTitle(), 30);
        out.writeShort(getNumber());
    }

    public void decode(CommandInputStream in) throws Exception {
        setOperator(in.readByte());
        setRecNumber(in.readShort());
    }

    public int getPassword() {
        return password;
    }

    public String getTitle() {
        return title;
    }

    public int getNumber() {
        return number;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getOperator() {
        return operator;
    }

    public int getRecNumber() {
        return recNumber;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public void setRecNumber(int recNumber) {
        this.recNumber = recNumber;
    }
}
