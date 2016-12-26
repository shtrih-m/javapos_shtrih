/*
 * ReadFiscalizationParams.java
 *
 * Created on 15 January 2009, 14:31
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
 * Get Fiscalization/Refiscalization Parameters Command: 69H. Length: 6 bytes. ·
 * Tax Officer password (4 bytes) password of Tax Officer who fiscalized the
 * printer · Fiscalization/Refiscalization number (1 byte) 1…16 Answer: 69H.
 * Length: 22 bytes. · Result Code (1 byte) · Password (4 bytes) · ECRRN (5
 * bytes) 0000000000…9999999999 · Taxpayer ID (6 bytes)
 * 000000000000…999999999999 · Number of the last daily totals record in FM
 * before fiscalization/refiscalization (2 bytes) 0000…2100 ·
 * Fiscalization/Refiscalization date (3 bytes) DD-MM-YY
 ****************************************************************************/

public final class ReadFiscalizationParams extends PrinterCommand {

    // in params
    private final int password;
    private final int fiscNumber;
    // out params
    private int fiscPassword;
    private long regID;
    private long taxID;
    private int sessionNumber = 0;
    private PrinterDate date = new PrinterDate();

    /**
     * Creates a new instance of ReadFiscalizationParams
     */
    public ReadFiscalizationParams(int password, int fiscNumber) {
        this.password = password;
        this.fiscNumber = fiscNumber;
    }

    public final int getCode() {
        return 0x69;
    }

    public final String getText() {
        return "Get fiscalization parameters";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(fiscNumber);
    }

    public final void decode(CommandInputStream in) throws Exception {
        fiscPassword = in.readInt();
        regID = in.readLong(5);
        taxID = in.readLong(6);
        sessionNumber = in.readShort();
        date = in.readDate();
    }
}
