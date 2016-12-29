/*
 * SelectBitImageMode.java
 *
 * Created on 28 March 2008, 11:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.printer.ncr7167;

/**
 *
 * @author V.Kravtsov
 */

/*********************************************************************
 * Select Bit Image Mode ASCII: ESC *m n1 n2 d1 ... dn Hexadecimal: 1B 2A m n1
 * n2 d1 ... dn
 **********************************************************************/

public final class SelectBitImageMode extends NCR7167Command {
    private final int m;
    private final int n1;
    private final int n2;
    private final byte[] data;

    public SelectBitImageMode(int m, int n1, int n2, byte[] data) {
        this.m = m;
        this.n1 = n1;
        this.n2 = n2;
        this.data = data;
    }

    public int getM() {
        return m;
    }

    public int getN1() {
        return n1;
    }

    public int getN2() {
        return n2;
    }

    public byte[] getData() {
        return data;
    }

    public final String getText() {
        return "Select bit image mode";
    }

    public final NCR7167Command newInstance(NCR7167Command command) {
        SelectBitImageMode src = (SelectBitImageMode) command;
        return new SelectBitImageMode(src.getM(), src.getN1(), src.getN2(),
                src.getData());
    }

    public void execute(NCR7167Printer printer) throws Exception {
    }
}
