/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.ej;

/**
 * @author V.Kravtsov
 */
public class EJStatus2 {

    private int dayNumber;
    private long saleTotal;
    private long buyTotal;
    private long saleRefundTotal;
    private long buyRefundTotal;

    public EJStatus2(int dayNumber, long saleTotal, long buyTotal,
            long saleRefundTotal, long buyRefundTotal) {
        this.dayNumber = dayNumber;
        this.saleTotal = saleTotal;
        this.buyTotal = buyTotal;
        this.saleRefundTotal = saleRefundTotal;
        this.buyRefundTotal = buyRefundTotal;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public long getSaleTotal() {
        return saleTotal;
    }

    public long getBuyTotal() {
        return buyTotal;
    }

    public long getSaleRefundTotal() {
        return saleRefundTotal;
    }

    public long getBuyRefundTotal() {
        return buyRefundTotal;
    }
}
