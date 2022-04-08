package com.shtrih.fiscalprinter.command;

public class ReadLastErrorText extends PrinterCommand {

    // out
    private String text;

    public ReadLastErrorText() {
    }

    public final int getCode() {
        return 0x6B;
    }

    public final String getText() {
        return "Get last error text";
    }

    public final void encode(CommandOutputStream out) throws Exception {
    }

    public final void decode(CommandInputStream in) throws Exception {
        text = in.readString();
    }

    public String getErrorText() {
        return text;
    }
}
