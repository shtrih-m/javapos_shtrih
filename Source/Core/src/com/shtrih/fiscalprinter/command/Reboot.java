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
/****************************************************************************
 * Service command: FE Length: 6 bytes. 
 * - 0xF3 - function code
 * - Operator password (4 bytes) 
 * Answer: FEH. Length: 2 bytes. 
 * Result Code (1 byte)
 ****************************************************************************/
public class Reboot extends PrinterCommand {
    // in
    private int code = 0;       // Function code
    private int password = 0;   // Admin password (4 bytes)

    /**
     * Creates a new instance of Beep
     */
    public Reboot() {
        super();
    }

    public final int getCode() {
        return 0xFE;
    }

    public final String getText() {
        return "Service command";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeByte(0xF3);
        out.writeInt(password);
    }

    public void decode(CommandInputStream in) throws Exception {
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }
}
