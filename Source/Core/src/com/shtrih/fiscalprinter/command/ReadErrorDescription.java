package com.shtrih.fiscalprinter.command;

public class ReadErrorDescription extends PrinterCommand {
    // in
    private final int errorCode;

    // out params
    private String errorDescription;

    public ReadErrorDescription(int errorCode) {
        this.errorCode = errorCode;
    }

    public final int getCode() {
        return 0x6B;
    }

    public final String getText() {
        return "Get error description";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeByte(errorCode);
    }

    public final void decode(CommandInputStream in) throws Exception {
        errorDescription = in.readString();
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
