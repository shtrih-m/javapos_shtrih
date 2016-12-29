/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
public class FMTotals {

    private final long salesAmount;
    private final long buyAmount;
    private final long retSaleAmount;
    private final long retBuyAmount;

    public FMTotals() {
        this.salesAmount = 0;
        this.buyAmount = 0;
        this.retSaleAmount = 0;
        this.retBuyAmount = 0;
    }

    public FMTotals(long salesAmount, long buyAmount, long retSaleAmount, long retBuyAmount) {
        this.salesAmount = salesAmount;
        this.buyAmount = buyAmount;
        this.retSaleAmount = retSaleAmount;
        this.retBuyAmount = retBuyAmount;
    }

    public long getSalesAmount() {
        return salesAmount;
    }

    public long getBuyAmount() {
        return buyAmount;
    }

    public long getRetSaleAmount() {
        return retSaleAmount;
    }

    public long getRetBuyAmount() {
        return retBuyAmount;
    }

}
