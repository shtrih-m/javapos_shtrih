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

*/

public class FSRegistrationReport extends PrinterCommand {

    // in
    private int sysPassword = 0; // System sdministrator password (4 bytes)
    private int code = 0; // Code: 1 byte
    // out
    private int docNumber = 0; // Document number: 4 bytes
    private int docDigest = 0; // Document digest: 4 bytes

    public FSRegistrationReport() {
    }

    public final int getCode() {
        return 0xFF34;
    }

    public final String getText() {
        return "Fiscal storage: reset state";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(sysPassword);
        out.writeByte(code);
    }

    public void decode(CommandInputStream in) throws Exception {
        docNumber = in.readInt();
        docDigest = in.readInt();
    }

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

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the docNumber
     */
    public int getDocNumber() {
        return docNumber;
    }

    /**
     * @param docNumber the docNumber to set
     */
    public void setDocNumber(int docNumber) {
        this.docNumber = docNumber;
    }

    /**
     * @return the docDigest
     */
    public int getDocDigest() {
        return docDigest;
    }

    /**
     * @param docDigest the docDigest to set
     */
    public void setDocDigest(int docDigest) {
        this.docDigest = docDigest;
    }
}
