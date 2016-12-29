/*
 * DefineCharacters.java
 *
 * Created on 28 March 2008, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.printer.ncr7167;

/**
 *
 * @author V.Kravtsov
 */

/****************************************************************************
 * Define User-Defined Characters Receipt Slip ASCII: ESC & 3 c1 c2 n1 d1 ... nn
 * dn ESC & 0 c1 c2 d1 ... dn Hexadecimal: 1B 26 3 c1 c2 n1 d1 ... nn dn 1B 26 0
 * c1 c2 d1 ... dn Decimal: 27 38 3 c1 c2 n1 d1 ... nn dn 27 38 0 c1 c2 d1 ...
 * dn
 ****************************************************************************/

public final class DefineCharacters extends NCR7167Command {
    private final int mode; // 0 - slip, 3 - receipt
    private final int c1;
    private final int c2;
    private final int n1;
    private final byte[] data;

    public DefineCharacters(int mode, int c1, int c2, int n1, byte[] data) {
        this.mode = mode;
        this.c1 = c1;
        this.c2 = c2;
        this.n1 = n1;
        this.data = data;
    }

    public final NCR7167Command newInstance(NCR7167Command command) {
        DefineCharacters cmd = (DefineCharacters) command;
        return new DefineCharacters(cmd.getMode(), cmd.getC1(), cmd.getC2(),
                cmd.getN1(), cmd.getData());
    }

    public int getMode() {
        return mode;
    }

    public int getC1() {
        return c1;
    }

    public int getC2() {
        return c2;
    }

    public int getN1() {
        return n1;
    }

    public byte[] getData() {
        return data;
    }

    public final String getText() {
        return "Define characters";
    }
}
