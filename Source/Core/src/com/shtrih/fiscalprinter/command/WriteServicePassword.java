/*
 * WriteServicePassword.java
 *
 * Created on January 16 2009, 16:43
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
 * Set Service Support Center Specialist Password Command: F3H. Length: 9 bytes.
 * · Old password of Service Support Center specialist (4 bytes) · New password
 * of Service Support Center specialist (4 bytes) Answer: F3H. Length: 2 bytes.
 * · Result Code (1 byte)
 ****************************************************************************/

public final class WriteServicePassword extends PrinterCommand {
    // in
    private final int oldPassword;
    private final int newPassword;

    /**
     * Creates a new instance of WriteServicePassword
     */
    public WriteServicePassword(int oldPassword, int newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public final int getCode() {
        return 0xF3;
    }

    public final String getText() {
        return "Set service password";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(oldPassword);
        out.writeInt(newPassword);
    }

    public final void decode(CommandInputStream in) throws Exception {
    }
}
