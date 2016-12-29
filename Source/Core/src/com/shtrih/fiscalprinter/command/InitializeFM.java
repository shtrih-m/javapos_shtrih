/*
 * InitializeFM.java
 *
 * Created on January 15 2009, 13:39
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
 * Initialize FM Command: 61H. Length: 1 byte. Answer: 61H. Length: 2 bytes. ·
 * Result Code (1 byte) NOTE: Command can be performed only when a special
 * technological processor is installed in the FM.
 ****************************************************************************/
public final class InitializeFM extends PrinterCommand {

    /**
     * Creates a new instance of InitializeFM
     */
    public InitializeFM() {
        super();
    }

    public final int getCode() {
        return 0x61;
    }

    public final String getText() {
        return "Initialize FM";
    }

    public final void encode(CommandOutputStream out) throws Exception {
    }

    public final void decode(CommandInputStream in) throws Exception {
    }
}
