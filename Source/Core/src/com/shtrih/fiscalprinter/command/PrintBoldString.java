/*
 * CommandPrintBoldString.java
 *
 * Created on April 2 2008, 19:16
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
 * Print String In Bold Type Command: 12H. Length: 26 bytes. · Operator password
 * (4 bytes) · Flags (1 byte) Bit 0 – print on journal station, Bit 1 – print on
 * receipt station. · String of characters (20 bytes) Answer: 12H. Length: 3
 * bytes. · Result Code (1 byte) · Operator index number (1 byte) 1…30 NOTE:
 * Only WIN1251 code page characters can be printed. Characters with codes 0 to
 * 31 are ignored.
 ****************************************************************************/
public class PrintBoldString extends PrinterCommand {
    // in

    private int password; // Operator password (4 bytes)
    private int station; // Flags (1 byte)
    private String text; // String of characters (20 bytes)
    // out
    private int operator = 0; // Operator index number

    /**
     * Creates a new instance of CommandPrintBoldString
     */
    public PrintBoldString() {
        super();
    }

    public final int getCode() {
        return 0x12;
    }

    public final String getText() {
        return "Print String In Bold Type";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getStation());
        out.writeString(text, 20);
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

    public int getStation() {
        return station;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setStation(int station) {
        this.station = station;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }


    public String getLine() {
        return text;
    }
}
