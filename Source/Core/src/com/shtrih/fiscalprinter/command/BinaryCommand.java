/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
public class BinaryCommand extends PrinterCommand {

    private final int code;
    private final String text;
    private final byte[] txData;

    public BinaryCommand(int code, String text, byte[] txData) {
        this.code = code;
        this.text = text;
        this.txData = txData;
    }

    public final int getCode() {
        return code;
    }

    public final String getText() {
        return text;
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeBytes(txData);
    }

    public final void decode(CommandInputStream in) throws Exception {
    }
}
