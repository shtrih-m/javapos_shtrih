/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import com.shtrih.ej.EJDate;
import com.shtrih.util.BitUtils;

/**
 *
 * @author V.Kravtsov
 */
/**
 * // Добавлен запрос необнуляемых сумм через сервисную команду 1
 * // (FE F4 00 00 00 00), возвращает 4 8-ми байтных числа.
 * -> FE F4 00 00 00 00
 * <- SaleTotal
 * <- RetSale
 * <- BuyTotal
 * <- RetBuy
 */
public class FSReadTotals extends PrinterCommand {

    // out
    private FMTotals totals = new FMTotals(); // Firmware version

    public FSReadTotals() {
    }

    public final int getCode() {
        return 0xFE;
    }

    public final String getText() {
        return "Fiscal storage: read totals";
    }

    public void encode(CommandOutputStream out) throws Exception 
    {
        out.writeByte(0xF4);
        out.writeInt(0);
    }

    public void decode(CommandInputStream in) throws Exception {
        long saleAmount = in.readLong(8);
        long retSaleAmount = in.readLong(8);
        long buyAmount = in.readLong(8);
        long retBuyAmount = in.readLong(8);
        totals = new FMTotals(saleAmount, buyAmount, retSaleAmount, retBuyAmount);
    }

    public FMTotals getTotals() {
        return totals;
    }
}
