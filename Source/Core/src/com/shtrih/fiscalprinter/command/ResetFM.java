/*
 * ResetFM.java
 *
 * Created on July 28 2008, 14:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 *
 * @author D.Tkachenko
 */

/****************************************************************************
 * Reset FM Command: 16H. Length: 1 byte. Answer: 16H. Length: 2 bytes. • Result
 * Code (1 byte)
 ****************************************************************************/

public class ResetFM extends PrinterCommand {

    /**
     * Creates a new instance of ResetFM
     */
    public ResetFM() {
    }

    public final int getCode() {
        return 0x16;
    }

    public final String getText() {
        return "Reset fiscal memory";
    }

    public void encode(CommandOutputStream out) throws Exception {
    }

    public void decode(CommandInputStream in) throws Exception {
    }

    public boolean getIsRepeatable() {
        return true;
    }
}
