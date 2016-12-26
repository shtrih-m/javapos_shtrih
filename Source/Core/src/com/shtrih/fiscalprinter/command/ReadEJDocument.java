/*
 * ReadEJDocument.java
 *
 * Created on 16 January 2009, 14:18
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
 * Get Data of Document Duplicate By KPK From electronic journal Command: B5H.
 * Length: 9 bytes. · System Administrator password (4 bytes) 30 · KPK number (4
 * bytes) 00000000…99999999 Answer: B5H. Length: 18 bytes. · Result Code (1
 * byte) · ECR model (16 bytes) string of WIN1251 code page characters NOTE:
 * Command execution may take up to 40 seconds.
 ****************************************************************************/

public final class ReadEJDocument extends PrinterCommand {
    // in
    private final int password;
    private final int docCRC;
    // out
    private String ecrModel = "";

    /**
     * Creates a new instance of ReadEJDocument
     */
    public ReadEJDocument(int password, int docCRC) {
        this.password = password;
        this.docCRC = docCRC;
    }

    public final int getCode() {
        return 0xB5;
    }

    public final String getText() {
        return "Read electronic journal document";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeInt(docCRC);
    }

    public final void decode(CommandInputStream in) throws Exception {
        ecrModel = in.readString(in.getSize());
    }

    public String getEcrModel() {
        return ecrModel;
    }
}
