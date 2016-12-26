/*
 * ReadEJActivationReport.java
 *
 * Created on 16 January 2009, 14:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
/**
 * **************************************************************************
 * Get Data Of electronic journal activation receipt Command: BBH. Length: 5
 * bytes. · System Administrator password (4 bytes) 30 Answer: BBH. Length: 18
 * bytes. · Result Code (1 byte) · ECR model (16 bytes) string of WIN1251 code
 * page characters
 */
public final class ReadEJActivationReport extends PrinterCommand {

    // in
    private int password;
    // out
    private String ecrModel = "";

    /**
     * Creates a new instance of ReadEJActivationReport
     */
    public ReadEJActivationReport() {
    }

    public final int getCode() {
        return 0xBB;
    }

    public final String getText() {
        return "Read electronic journal activation report";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
    }

    public final void decode(CommandInputStream in) throws Exception {
        setEcrModel(in.readString(in.getSize()));
    }

    public String getEcrModel() {
        return ecrModel;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setEcrModel(String ecrModel) {
        this.ecrModel = ecrModel;
    }
}
