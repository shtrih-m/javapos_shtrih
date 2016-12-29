package com.shtrih.fiscalprinter.command;

import java.io.IOException;

import com.shtrih.fiscalprinter.SmFiscalPrinterException;
import com.shtrih.util.Localizer;
import com.shtrih.fiscalprinter.AnswerCodeException;

public abstract class PrinterCommand {

    public final static int DefaultTimeout = 10000;
    private int timeout = DefaultTimeout;
    private int resultCode = 0;
    private final String charsetName = "Cp1251";
    private boolean repeatEnabled = false;
    private boolean repeatNeeded;
    private boolean errorReportEnabled = true;
    private byte[] rxData;

    /**
     * Creates a new instance of PrinterCommand
     */
    public PrinterCommand() {
    }

    public boolean getErrorReportEnabled() {
        return errorReportEnabled;
    }

    public void setErrorReportEnabled(boolean value) {
        errorReportEnabled = value;
    }

    public boolean getRepeatNeeded() {
        return repeatNeeded;
    }

    public void setRepeatNeeded(boolean value) {
        repeatNeeded = value;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int value) {
        this.timeout = value;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int value) {
        resultCode = value;
    }

    public static void checkMinLength(int length, int minLength)
            throws Exception {
        if (length < minLength) {
            throw new Exception(
                    Localizer.getString(Localizer.InvalidAnswerLength));
        }
    }

    public byte[] getRxData() {
        return rxData;
    }

    public void decodeData(byte[] data) throws Exception {
        rxData = data;
        CommandInputStream in = new CommandInputStream(charsetName);
        in.setData(data);
        checkMinLength(in.getSize(), 2);
        int code = in.readByte();
        if (code == 0xFF) {
            code = 0xFF00 + in.readByte();
            if (code != getCode()) {
                resultCode = code & 0xFF;
            } else {
                resultCode = in.readByte();
            }
        } else {
            if (code != getCode()) 
            {
                throw new AnswerCodeException("Invalid answer code");
            }
            resultCode = in.readByte();
        }
        if (resultCode == 0) {
            decode(in);
        }
    }

    public byte[] encodeData() throws Exception {
        CommandOutputStream out = new CommandOutputStream(charsetName);
        out.reset();
        int code = getCode();
        if (code > 0xFF) {
            out.writeByte(0xFF);
            out.writeByte(code & 0xFF);
        } else {
            out.writeByte(code);
        }
        encode(out);
        return out.getData();
    }

    public abstract int getCode();

    public abstract String getText();

    public abstract void encode(CommandOutputStream out) throws Exception;

    public abstract void decode(CommandInputStream in) throws Exception;

    public static int getDefaultTimeout(int code) {
        switch (code) {
            case 0x16:
                return 60000;
            case 0xB2:
                return 20000;
            case 0xB4:
                return 40000;
            case 0xB5:
                return 40000;
            case 0xB6:
                return 150000;
            case 0xB7:
                return 150000;
            case 0xB8:
                return 100000;
            case 0xB9:
                return 100000;
            case 0xBA:
                return 40000;
            case 0x40:
                return 30000;
            case 0x41:
                return 30000;
            case 0x61:
                return 20000;
            case 0x62:
                return 30000;
            default:
                return DefaultTimeout;
        }
    }

    public boolean getIsRepeatable() {
        return false;
    }

    public boolean isFailed() {
        return resultCode != 0;
    }

    public boolean isSucceeded() {
        return resultCode == 0;
    }

    public boolean getRepeatEnabled() {
        return repeatEnabled;
    }

    public void setRepeatEnabled(boolean value) {
        repeatEnabled = value;
    }
}
