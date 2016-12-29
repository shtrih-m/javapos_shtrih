/*
 * ReadEJStatus2.java
 *
 * Created on 16 January 2009, 13:36
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

 Get EJ Status 2

 Command:  AEH. Length: 5 bytes.
 ·	System Administrator password (4 bytes) 30

 Answer:		AEH. Length: 28 bytes.
 ·	Result Code (1 byte)
 ·	Number of last daily totals (2 bytes) 0000…2100
 ·	Grand totals of sales (6 bytes) 000000000000…999999999999
 ·	Grand totals of buys (6 bytes) 000000000000…999999999999
 ·	Grand totals of sale refunds (6 bytes) 000000000000…999999999999
 ·	Grand totals of buy refunds (6 bytes) 000000000000…999999999999

 ****************************************************************************/
import com.shtrih.ej.EJStatus2;

public final class ReadEJStatus2 extends PrinterCommand {
    // in

    private int password;
    // out
    private EJStatus2 status;

    /**
     * Creates a new instance of ReadEJStatus2
     */
    public ReadEJStatus2() {
    }

    
    public final int getCode() {
        return 0xAE;
    }

    
    public final String getText() {
        return "Read electronic journal status by code 2";
    }

    
    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
    }

    
    public final void decode(CommandInputStream in) throws Exception {
        int dayNumber = in.readShort();
        long saleTotal = in.readLong(6);
        long buyTotal = in.readLong(6);
        long saleRefundTotal = in.readLong(6);
        long buyRefundTotal = in.readLong(6);

        status = new EJStatus2(dayNumber, saleTotal, buyTotal, saleRefundTotal,
                buyRefundTotal);
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public EJStatus2 getStatus() {
        return status;
    }
}
