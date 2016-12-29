/*
 * SimplePrinterCommand.java
 *
 * Created on 28 March 2008, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.printer.ncr7167;

/**
 * @author V.Kravtsov
 */

public final class SimplePrinterCommand extends NCR7167Command {
    private final byte[] code;
    private final int paramLen;
    private final String text;

    public SimplePrinterCommand(byte[] code, int paramLen, String text) {
        this.code = code;
        this.paramLen = paramLen;
        this.text = text;
    }

    public byte[] getCode() {
        return code;
    }

    public int getParamLen() {
        return paramLen;
    }

    public final String getText() {
        return text;
    }

    public final NCR7167Command newInstance(NCR7167Command command) {
        SimplePrinterCommand src = (SimplePrinterCommand) command;
        return new SimplePrinterCommand(src.getCode(), src.getParamLen(),
                src.getText());
    }

    public void execute(NCR7167Printer printer) throws Exception {
    }
}
