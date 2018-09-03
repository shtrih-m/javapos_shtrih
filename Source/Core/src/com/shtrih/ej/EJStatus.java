/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.ej;

import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterTime;

/**
 * @author V.Kravtsov
 */
public class EJStatus {

    private long docMAC;
    private PrinterDate docDate;
    private PrinterTime docTime;
    private long docMACNumber;
    private long serialNumber;
    private EJFlags flags;

    public EJStatus() {
    }

    public long getDocMAC() {
        return docMAC;
    }

    public PrinterDate getDocDate() {
        return docDate;
    }

    public PrinterTime getDocTime() {
        return docTime;
    }

    public long getDocMACNumber() {
        return docMACNumber;
    }

    public long getSerialNumber() {
        return serialNumber;
    }

    public EJFlags getFlags() {
        return flags;
    }

    public void setDocMAC(long docMAC) {
        this.docMAC = docMAC;
    }

    public void setDocDate(PrinterDate docDate) {
        this.docDate = docDate;
    }

    public void setDocTime(PrinterTime docTime) {
        this.docTime = docTime;
    }

    public void setDocMACNumber(long docMACNumber) {
        this.docMACNumber = docMACNumber;
    }

    public void setSerialNumber(long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setFlags(EJFlags flags) {
        this.flags = flags;
    }

}
