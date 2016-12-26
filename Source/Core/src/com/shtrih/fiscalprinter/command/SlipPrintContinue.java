/*
 * SlipPrintContinue.java
 *
 * Created on January 16 2009, 15:38
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
 * Continue Printing Slip Command: E1H. Length: 5 bytes. · Operator password (4
 * bytes) Answer: E1H. Length: 2 bytes. · Operator index number (1 byte) 1…30
 * NOTE: Command allows finishing printing slip document after paper out, power
 * failure, etc. situations. Printing resumes from the very line of slip
 * document on which FP stopped printing because of technical failure.
 ****************************************************************************/

public final class SlipPrintContinue extends PrinterCommand {
    // in
    private final int password;
    // out
    private int operator;

    /**
     * Creates a new instance of SlipPrintContinue
     */
    public SlipPrintContinue(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0xE1;
    }

    public final String getText() {
        return "Continue slip printing";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }
}
