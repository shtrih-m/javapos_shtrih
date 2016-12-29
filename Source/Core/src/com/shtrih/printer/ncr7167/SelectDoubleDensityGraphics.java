/*
 * SelectDoubleDensityGraphics.java
 *
 * Created on 28 March 2008, 11:44
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
 * Select Double-Density Graphics ASCII: ESC Y n1 n2 d1…dn or ESC L n1 n2 d1 ...
 * dn Hexadecimal: 1B 59 n1 n2 d1 ... dn or 1B 4C n1 n2 d1… dn
 * ------------------------------------------------------------- Mode | 8-Dot |
 * 24-Dot ------------------------------------------------------------- Value of
 * n: | n1 + (256 x n2) | 3 x [n1 + (256 x n2)]
 **********************************************************************/

public final class SelectDoubleDensityGraphics extends NCR7167Command {
    private final int n1;
    private final int n2;
    private final byte[] data;

    /**
     * Creates a new instance of SelectDoubleDensityGraphics
     */
    public SelectDoubleDensityGraphics(int n1, int n2, byte[] data) {
        this.n1 = n1;
        this.n2 = n2;
        this.data = data;
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
        return "Select double density graphics";
    }

    public final NCR7167Command newInstance(NCR7167Command command) {
        SelectDoubleDensityGraphics src = (SelectDoubleDensityGraphics) command;
        return new SelectDoubleDensityGraphics(src.getN1(), src.getN2(),
                src.getData());
    }

    public void execute(NCR7167Printer printer) throws Exception {
    }
}
