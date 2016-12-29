/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 * @author V.Kravtsov
 */
public class RawCommand extends PrinterCommand {

    private int code = 0;
    private byte[] txData;

    public RawCommand() {
    }

    public void setTxData(byte[] txData) {
        if (txData.length > 0) {
            code = txData[0];
        }
        byte[] buffer = new byte[txData.length - 1];
        System.arraycopy(txData, 1, buffer, 0, buffer.length);
        this.txData = buffer;
    }


    public int getCode() {
        return code;
    }

    public final String getText() {
        return "";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeBytes(txData);
    }

    public final void decode(CommandInputStream in) throws Exception {
    }

}
