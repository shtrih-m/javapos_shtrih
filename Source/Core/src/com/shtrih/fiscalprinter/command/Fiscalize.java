/*
 * Fiscalize.java
 *
 * Created on 15 January 2009, 14:24
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
 * Fiscalization Command: 65H. Length: 20 bytes. · Old tax officer opassword (4
 * bytes) · New tax officer password (4 bytes) · Registration ID (5 bytes)
 * 0000000000…9999999999 · Taxpayer ID (6 bytes) 000000000000…999999999999
 * Answer: 65H. Length: 9 bytes. · Result Code (1 byte) · Fiscalization number
 * (1 byte) 1…16 · Fiscalizations left number (1 byte) 0…15 · Fiscal memory day
 * number (2 bytes) 0000…2100 · Fiscalization date (3 bytes) DD-MM-YY
 ****************************************************************************/
public final class Fiscalize extends PrinterCommand {
    // in params
    private int oldPassword;
    private int newPassword;
    private long regID;
    private long taxID;
    // out params
    private int fiscNumber = 0;
    private int freeNumber = 0;
    private int dayNumber = 0;
    private PrinterDate date = new PrinterDate();

    /** Creates a new instance of Fiscalize */
    public Fiscalize() {
        super();
    }

    public int getOldPassword() {
        return oldPassword;
    }

    public int getNewPassword() {
        return newPassword;
    }

    public long GetRegID() {
        return getRegID();
    }

    public long getTaxID() {
        return taxID;
    }

    public int getFiscNumber() {
        return fiscNumber;
    }

    public int getFreeNumber() {
        return freeNumber;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public PrinterDate getDate() {
        return date;
    }

    public final int getCode() {
        return 0x65;
    }

    public final String getText() {
        return "Fiscalize printer";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getOldPassword());
        out.writeInt(getNewPassword());
        out.writeLong(getRegID(), 5);
        out.writeLong(getTaxID(), 6);
    }

    public final void decode(CommandInputStream in) throws Exception {
        setFiscNumber(in.readByte());
        setFreeNumber(in.readByte());
        setDayNumber(in.readShort());
        setDate(in.readDate());
    }

    public long getRegID() {
        return regID;
    }

    public void setOldPassword(int oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setNewPassword(int newPassword) {
        this.newPassword = newPassword;
    }

    public void setRegID(long regID) {
        this.regID = regID;
    }

    public void setTaxID(long taxID) {
        this.taxID = taxID;
    }

    public void setFiscNumber(int fiscNumber) {
        this.fiscNumber = fiscNumber;
    }

    public void setFreeNumber(int freeNumber) {
        this.freeNumber = freeNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public void setDate(PrinterDate date) {
        this.date = date;
    }
}
