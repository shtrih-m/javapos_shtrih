package com.shtrih.fiscalprinter.command;

public class RawCommand extends PrinterCommand {

    private int code = 0;
    private final byte[] txData;

    public RawCommand(byte[] txData) {
        
        if (txData == null)
            throw new IllegalArgumentException("txData is null");

        if (txData.length == 0)
            throw new IllegalArgumentException("txData length should be greater then 1");

        int offset = 1;
        code = txData[0] & 0xFF;
        if (code == 0xFF && txData.length >= 2) {
            offset = 2;
            code = (code << 8) + (txData[1] & 0xFF);
        }

        byte[] buffer = new byte[txData.length - offset];
        System.arraycopy(txData, offset, buffer, 0, buffer.length);
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
