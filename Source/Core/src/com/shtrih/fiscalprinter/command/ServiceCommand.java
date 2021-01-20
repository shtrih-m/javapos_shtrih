/*
 * Beep.java
 *
 * Created on 2 April 2008, 19:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
/**
 * **************************************************************************
 * Service command: FE Length: 6 bytes.
 * - Function code (1 byte)
 - Operator parameter (4 bytes)
 Answer: FEH. Length: 2 bytes.
 Result Code (1 byte)
 ***************************************************************************
 */
public class ServiceCommand extends PrinterCommand {

    private final String charsetName = "Cp1251";
    public static final int CODE_FM_WRITE = 0x00;
    public static final int CODE_FM_READ = 0x01;
    public static final int CODE_PB_READ = 0x02;
    public static final int CODE_CF_WRITE = 0x03;
    public static final int CODE_GET_PRN_VER = 0xeb;
    public static final int CODE_GET_BL_VER = 0xec;
    public static final int CODE_DFU_REBOOT = 0xed;
    public static final int CODE_PROG_LIC = 0xee;
    public static final int CODE_READ_LIC = 0xef;
    public static final int CODE_READ_MCU_UID = 0xf0;
    public static final int CODE_SERIALNUMBER_REPLACE = 0xf1;
    public static final int CODE_PING = 0xf2;
    public static final int CODE_REBOOT = 0xf3;
    public static final int CODE_GLOBALSUMM_GET = 0xf4;
    public static final int CODE_SERIALNUMBER_PROG = 0xf5;
    public static final int CODE_FLASHKEY_PROG = 0xf6;
    public static final int CODE_SET_MODEL = 0xf7;
    public static final int CODE_MASS_ERASE = 0xf8;
    public static final int CODE_SESSIONKEY_PROG = 0xf9;
    public static final int CODE_MASTERKEY_PROG = 0xfa;
    public static final int CODE_WIFI_PROG = 0xfb;
    public static final int CODE_PRINTER_INIT = 0xfc;
    public static final int CODE_FM_PROG = 0xfd;
    public static final int CODE_GRAPH_OFF = 0xfe;
    public static final int CODE_GRAPH_ON = 0xff;

    // in
    private int functionCode = 0; // Function code
    private byte[] data; // Parameter (X bytes)
    // out
    private byte[] answer;

    /**
     * Creates a new instance of Beep
     */
    public ServiceCommand() {
        super();
    }

    public static final int SerialNumberLength = 16;
    public ServiceCommand writeSerialCommand(String serialNumber) throws Exception
    {
        if(serialNumber == null)
            throw new IllegalArgumentException("Serial number should not be null");

        if(serialNumber.length() != SerialNumberLength)
            throw new IllegalArgumentException("Incorrect serial number length");

        CommandOutputStream stream = new CommandOutputStream(charsetName);
        stream.writeString(serialNumber, SerialNumberLength);
        ServiceCommand command = new ServiceCommand();
        command.setFunctionCode(CODE_SERIALNUMBER_PROG);
        command.setData(stream.getData());
        return command;
    }
            
    public final int getCode() {
        return 0xFE;
    }

    public final String getText() {
        return "Service command";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeByte(functionCode);
        out.writeBytes(data);
   }

    public void decode(CommandInputStream in) throws Exception {
        answer = in.readBytesToEnd();
    }

    public byte[] getAnswer() {
        return answer;
    }

    public byte[] getData() {
        return data;
    }

    public void setIntData(int value) throws Exception
    {
        CommandOutputStream stream = new CommandOutputStream("");
        stream.writeInt(value);
        data = stream.getData();
    }
    
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * @return the functionCode
     */
    public int getFunctionCode() {
        return functionCode;
    }

    /**
     * @param functionCode the functionCode to set
     */
    public void setFunctionCode(int functionCode) {
        this.functionCode = functionCode;
    }
}
