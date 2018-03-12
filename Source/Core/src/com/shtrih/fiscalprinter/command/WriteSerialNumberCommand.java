package com.shtrih.fiscalprinter.command;

public class WriteSerialNumberCommand extends PrinterCommand {

    public static final int SerialNumberLength = 16;

    // in
    private final int functionCode = ServiceCommand.FUNCTION_CODE_SERIALNUMBER_PROG; // Function code
    private final String serialNumber;   // Serial number (16 bytes)

    public WriteSerialNumberCommand(String serialNumber) {
        super();

        if(serialNumber == null)
            throw new IllegalArgumentException("Serial number should not be null");

        if(serialNumber.length() != SerialNumberLength)
            throw new IllegalArgumentException("Incorrect serial number length, expected " + SerialNumberLength + ", but was " + serialNumber.length());

        this.serialNumber = serialNumber;
    }

    public final int getCode() {
        return 0xFE;
    }

    public final String getText() {
        return "Write serial command";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeByte(functionCode);
        out.writeString(serialNumber, SerialNumberLength);
    }

    public void decode(CommandInputStream in) throws Exception {
    }
}
