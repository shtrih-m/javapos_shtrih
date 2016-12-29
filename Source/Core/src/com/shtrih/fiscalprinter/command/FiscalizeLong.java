/*
 * FiscalizeLong.java
 *
 * Created on 2 April 2008, 18:05
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
 * Fiscalize with long ECRRN Command: 0DH. Length: 22 bytes. · Old Tax Officer
 * password (4 bytes) · New Tax Officer password (4 bytes) · Long ECRRN (7
 * bytes) 00000000000000…99999999999999 · Taxpayer ID (6 bytes)
 * 000000000000…999999999999 Answer: 0DH. Length: 9 bytes. · Result Code (1
 * byte) · Fiscalization/Refiscalization number(1 byte) 1…16 · Quantity of
 * refiscalizations left in FM (1 byte) 0…15 · Last daily totals record number
 * in FM (2 bytes) 0000…2100 · Fiscalization/Refiscalization date (3 bytes)
 * DD-MM-YY NOTE: The command is introduced into this protocol to conform to the
 * Byelorussian legislation that requires Electronic Cash Registers to have
 * registration numbers (ECRRN) 14 digits long, where as Russian ECRRN is 10
 * digits long.
 ****************************************************************************/
public final class FiscalizeLong extends PrinterCommand {
    // in params
    private int password;
    private int newPassword;
    private long regID;
    private long taxID;
    // out params
    private int fiscNumber = 0;
    private int fiscNumberLeft = 0;
    private int sessionNumber = 0;
    private PrinterDate date = new PrinterDate();

    /**
     * Creates a new instance of FiscalizeLong
     */
    public FiscalizeLong() {
        super();
    }

    public final int getCode() {
        return 0x0D;
    }

    public final String getText() {
        return "Fiscalize with long ECRRN";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeInt(getNewPassword());
        out.writeLong(getRegID(), 7);
        out.writeLong(getTaxID(), 6);
    }

    public final void decode(CommandInputStream in) throws Exception {
        setFiscNumber(in.readByte());
        setFiscNumberLeft(in.readByte());
        setSessionNumber(in.readShort());
        setDate(in.readDate());
    }

    public int getPassword() {
        return password;
    }

    public int getNewPassword() {
        return newPassword;
    }

    public long getRegID() {
        return regID;
    }

    public long getTaxID() {
        return taxID;
    }

    public int getFiscNumber() {
        return fiscNumber;
    }

    public int getFiscNumberLeft() {
        return fiscNumberLeft;
    }

    public int getSessionNumber() {
        return sessionNumber;
    }

    public PrinterDate getDate() {
        return date;
    }

    public void setPassword(int password) {
        this.password = password;
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

    public void setFiscNumberLeft(int fiscNumberLeft) {
        this.fiscNumberLeft = fiscNumberLeft;
    }

    public void setSessionNumber(int sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    public void setDate(PrinterDate date) {
        this.date = date;
    }
}
