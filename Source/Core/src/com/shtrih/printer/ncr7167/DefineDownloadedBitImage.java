/*
 * DefineDownloadedBitImage.java
 *
 * Created on 28 March 2008, 11:45
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
 * Define Downloaded Bit Image ASCII: GS * n1 n2 d1 ... dn] Hexadecimal: 1D 2A
 * n1 n2 d1 ... dn n = 8 x n1 x n2 (n1 x n2 must be less than or equal to 4608).
 **********************************************************************/

public final class DefineDownloadedBitImage extends NCR7167Command {
    private final int n1;
    private final int n2;
    private final byte[] data;

    /**
     * Creates a new instance of DefineDownloadedBitImage
     */
    public DefineDownloadedBitImage(int n1, int n2, byte[] data) {
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
        return "Define downloaded bit image";
    }

    public final NCR7167Command newInstance(NCR7167Command command) {
        DefineDownloadedBitImage src = (DefineDownloadedBitImage) command;
        return new DefineDownloadedBitImage(src.getN1(), src.getN2(),
                src.getData());
    }

    public void execute(NCR7167Printer printer) throws Exception {
    }
}
