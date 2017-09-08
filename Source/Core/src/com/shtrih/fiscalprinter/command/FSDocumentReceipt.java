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

///////////////////////////////////////////////////////////////////////////////
// Кассовый чек
///////////////////////////////////////////////////////////////////////////////
// Дата и время         DATE_TIME	5
// Номер ФД             Uint32, LE	4
// Фискальный признак	Uint32, LE	4
// Тип операции         Byte            1
// Сумма операции	Uint40, LE	5
///////////////////////////////////////////////////////////////////////////////

public class FSDocumentReceipt extends FSDocument
{
    private final int type;
    private final long amount;

    public FSDocumentReceipt(CommandInputStream in, int docType) throws Exception
    {
        super(in, docType);
        type = in.readByte();
        amount = in.readLong(5);
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @return the amount
     */
    public long getAmount() {
        return amount;
    }

}
