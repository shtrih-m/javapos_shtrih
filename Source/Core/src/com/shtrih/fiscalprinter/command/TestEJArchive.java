/*
 * TestEJArchive.java
 *
 * Created on 16 January 2009, 13:41
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
 * Test EJ Archive Structure Command: AFH. Length: 5 bytes. · System
 * Administrator password (4 bytes) 30 Answer: AFH. Length: 2 bytes. · Result
 * Code (1 byte)
 ****************************************************************************/
public final class TestEJArchive extends PrinterCommand {
    // in
    private int password;

    /**
     * Creates a new instance of TestEJArchive
     */
    public TestEJArchive() {
        super();
    }

    public final int getCode() {
        return 0xAF;
    }

    public final String getText() {
        return "Test electronic journal archive";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
    }

    public final void decode(CommandInputStream in) throws Exception {
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }
}
