/*
 * WriteUserDataStorage.java
 *
 * Created on 28 March 2008, 12:45
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
 * Write to User Data Storage ASCII: ESC ‘ m a0 a1 a2 d1 ... dm Hexadecimal: 1B
 * 27 m a0 a1 a2 d1 ... dm
 **********************************************************************/

public final class WriteUserDataStorage extends NCR7167Command {
    private final int m;
    private final int a0;
    private final int a1;
    private final int a2;
    private final byte[] data;

    /**
     * Creates a new instance of WriteUserDataStorage
     */
    public WriteUserDataStorage(int m, int a0, int a1, int a2, byte[] data) {
        this.m = m;
        this.a0 = a0;
        this.a1 = a1;
        this.a2 = a2;
        this.data = data;
    }

    public int getM() {
        return m;
    }

    public int getA0() {
        return a0;
    }

    public int getA1() {
        return a1;
    }

    public int getA2() {
        return a2;
    }

    public byte[] getData() {
        return data;
    }

    
    public final String getText() {
        return "Write user data storage";
    }

    
    public final NCR7167Command newInstance(NCR7167Command command) {
        WriteUserDataStorage src = (WriteUserDataStorage) command;
        return new WriteUserDataStorage(src.getM(), src.getA0(), src.getA1(),
                src.getA2(), src.getData());
    }

    public void execute(NCR7167Printer printer) throws Exception {
    }
}
