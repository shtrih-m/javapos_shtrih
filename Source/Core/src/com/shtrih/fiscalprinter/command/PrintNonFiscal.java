/*
 * PrintNonFiscal.java
 *
 * Created on January 16 2009, 16:59
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
 * Print document attribute Command: E4H. Message length: 7-206 bytes · Operator
 * password (4 bytes) · Attribute ID (1 byte) · Attribute value (1-200 bytes)
 * Answer: E4H. Message length: 3 bytes · Result code (1 byte) · Operator number
 * (1 byte) 1…30 This command prints document attribute in the open fiscal
 * document context. Attribute value is string with win1251 encoding. Lines are
 * separated with 0x0A character. There can be up to 4 lines.
 ****************************************************************************/

public final class PrintNonFiscal extends PrinterCommand {
    // in
    private final int password;
    private final int itemID;
    private final byte[] itemData;
    // out
    private int operator;

    /**
     * Creates a new instance of PrintNonFiscal
     */
    public PrintNonFiscal(int password, int itemID, byte[] itemData) {
        this.password = password;
        this.itemID = itemID;
        this.itemData = itemData;
    }

    public final int getCode() {
        return 0xE4;
    }

    public final String getText() {
        return "Print non fiscal item";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(itemID);
        out.writeBytes(itemData);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }
}
