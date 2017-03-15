/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

public class FSReFiscalization extends PrinterCommand {
        
    // in
    private final int sysPassword; // System administrator password (4 bytes)

    private final String taxID;
    private final String regID;
    private final int taxSystemCode;
    private final int operationMode;
    private final int reasonCode;
    // out
    private long docNumber;
    private long macNumber;

    public FSReFiscalization(int sysPassword, String taxID, String regID, int taxSystemCode, int operationMode, int reasonCode) {
        this.sysPassword = sysPassword;
        this.taxID = taxID;
        this.regID = regID;
        this.taxSystemCode = taxSystemCode;
        this.operationMode = operationMode;
        this.reasonCode = reasonCode;
    }

    public final int getCode() {
        return 0xFF34;
    }

    public final String getText() {
        return "Fiscal storage: refiscalization";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(sysPassword);
        out.writeString(taxID, 12);
        out.writeString(regID, 20);
        out.writeByte(taxSystemCode);
        out.writeByte(operationMode);
        out.writeByte(reasonCode);
    }

    public void decode(CommandInputStream in) throws Exception {
        docNumber = in.readLong(4);
        macNumber = in.readLong(4);
    }

    public long getDocNumber() {
        return docNumber;
    }

    public long getMacNumber() {
        return macNumber;
    }
}
