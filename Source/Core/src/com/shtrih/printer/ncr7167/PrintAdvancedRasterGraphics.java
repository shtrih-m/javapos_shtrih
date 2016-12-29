/*
 * PrintAdvancedRasterGraphics.java
 *
 * Created on 28 March 2008, 11:42
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
 * Print Advanced Raster Graphics ASCII: ESC .mn rl rh d1 … dn Hexadecimal: 1B
 * 2E m n rl rh d1…dn
 **********************************************************************/

public final class PrintAdvancedRasterGraphics extends NCR7167Command {
    private final int m;
    private final int n;
    private final int rl;
    private final int rh;
    private final byte[] data = {};

    /**
     * Creates a new instance of PrintAdvancedRasterGraphics
     */
    public PrintAdvancedRasterGraphics(int m, int n, int rl, int rh, byte[] data) {
        this.m = m;
        this.n = n;
        this.rl = rl;
        this.rh = rh;
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public int getRl() {
        return rl;
    }

    public int getRh() {
        return rh;
    }

    public byte[] getData() {
        return data;
    }

    public final String getText() {
        return "Print advanced raster graphics";
    }

    public final NCR7167Command newInstance(NCR7167Command command) {
        PrintAdvancedRasterGraphics src = (PrintAdvancedRasterGraphics) command;
        return new PrintAdvancedRasterGraphics(src.getM(), src.getN(),
                src.getRl(), src.getRh(), src.getData());
    }

    public void execute(NCR7167Printer printer) throws Exception {
    }
}
