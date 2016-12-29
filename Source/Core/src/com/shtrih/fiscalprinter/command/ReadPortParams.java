/*
 * ReadPortParams.java
 *
 * Created on 15 January 2009, 11:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */

/****************************************************************************
 * Get Communication Parameters Command: 15H. Length: 6 bytes. · System
 * Administrator password (4 bytes) 30 · Port number (1 byte) 0…255 Answer: 15H.
 * Length: 4 bytes. · Result Code (1 byte) · Baud rate (1 byte) 0…6 ·
 * Inter-character time out (1 byte) 0…255
 ****************************************************************************/

public final class ReadPortParams extends PrinterCommand {
    // in
    private final int password;
    private final int portNumber;
    private int baudRate;
    private int timeout;

    /** Creates a new instance of SetPortParamsCommand */
    public ReadPortParams(int password, byte portNumber) {
        this.password = password;
        this.portNumber = portNumber;
    }

    public final int getCode() {
        return 0x15;
    }

    public final String getText() {
        return "Get communication parameters";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(portNumber);
    }

    public final void decode(CommandInputStream in) throws Exception {
        baudRate = in.readByte();
        timeout = in.readByte();
    }

    public int getBaudRate() {
        return baudRate;
    }

    public int getTimeout() {
        return timeout;
    }

    public boolean getIsRepeatable() {
        return true;
    }
}
