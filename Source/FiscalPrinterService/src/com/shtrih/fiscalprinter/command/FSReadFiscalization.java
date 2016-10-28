/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import com.shtrih.ej.EJDate;
import com.shtrih.util.BitUtils;

/**
 *
 * @author V.Kravtsov
 */
/**
 *
 * ������ ������ ������������
 * ��� ������� FF09h . ����� ���������: 6 ����.
 * ������ ���������� ��������������: 4 ����
 *
 * �����: FF09h ����� ���������: 64 ����.
 * ��� ������ (1 ����)
 * ���� � ����� DATE_TIME (5 ����)
 * ��� ASCII (12 ����)
 * ��������������� ����� ��� ASCII (20 ����)
 * ����� �� ASCII (16 ����)
 * ��� ��������������� ( 1 ����)
 * ����� ������ ( 1 ����)
 * ����� �� (4 �����)
 * ���������� ������� ( 4 ����)
 *
 *
 *
 */
public class FSReadFiscalization extends PrinterCommand {

    /**
     * @return the sysPassword
     */
    public int getSysPassword() {
        return sysPassword;
    }

    /**
     * @param sysPassword the sysPassword to set
     */
    public void setSysPassword(int sysPassword) {
        this.sysPassword = sysPassword;
    }

    // in
    private int sysPassword; // System sdministrator password (4 bytes)
    // out
    private PrinterDate date;
    private PrinterTime time;
    private String taxID;
    private String regID;
    private String serial;
    private int taxSystemCode;
    private int mode;
    private long docNumber;
    private long macNumber;

    public FSReadFiscalization() {
    }

    public final int getCode() {
        return 0xFF09;
    }

    public final String getText() {
        return "Fiscal storage: read fiscalization";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
    }

    public void decode(CommandInputStream in) throws Exception {
        setDate(in.readDate());
        setTime(in.readTime());
        setTaxID(in.readString(12));
        setRegID(in.readString(20));
        setSerial(in.readString(16));
        setTaxSystemCode(in.readByte());
        setMode(in.readByte());
        setDocNumber(in.readLong(4));
        setMacNumber(in.readLong(4));
    }

    /**
     * @return the date
     */
    public PrinterDate getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(PrinterDate date) {
        this.date = date;
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

    /**
     * @return the taxID
     */
    public String getTaxID() {
        return taxID;
    }

    /**
     * @param taxID the taxID to set
     */
    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    /**
     * @return the regID
     */
    public String getRegID() {
        return regID;
    }

    /**
     * @param regID the regID to set
     */
    public void setRegID(String regID) {
        this.regID = regID;
    }

    /**
     * @return the serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * @return the taxSystemCode
     */
    public int getTaxSystemCode() {
        return taxSystemCode;
    }

    /**
     * @param taxSystemCode the taxSystemCode to set
     */
    public void setTaxSystemCode(int taxSystemCode) {
        this.taxSystemCode = taxSystemCode;
    }

    /**
     * @return the mode
     */
    public int getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * @return the docNumber
     */
    public long getDocNumber() {
        return docNumber;
    }

    /**
     * @param docNumber the docNumber to set
     */
    public void setDocNumber(long docNumber) {
        this.docNumber = docNumber;
    }

    /**
     * @return the macNumber
     */
    public long getMacNumber() {
        return macNumber;
    }

    /**
     * @param macNumber the macNumber to set
     */
    public void setMacNumber(long macNumber) {
        this.macNumber = macNumber;
    }

}
