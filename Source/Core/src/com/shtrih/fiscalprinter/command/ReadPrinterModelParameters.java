package com.shtrih.fiscalprinter.command;

public class ReadPrinterModelParameters extends PrinterCommand {
    // in
    private final int requestType;

    // out params
    private PrinterModelParameters status = null;

    public ReadPrinterModelParameters() {
        // Тип запроса 1 – ПАРАМЕТРЫ МОДЕЛИ
        this.requestType = 1;
    }

    public final int getCode() {
        return 0xF7;
    }

    public final String getText() {
        return "Get extended status";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeByte(requestType);
    }

    public final void decode(CommandInputStream in) throws Exception {
        status = new PrinterModelParameters(in);
    }

    public PrinterModelParameters getStatus() {
        return status;
    }

    public boolean getIsRepeatable() {
        return true;
    }
}

