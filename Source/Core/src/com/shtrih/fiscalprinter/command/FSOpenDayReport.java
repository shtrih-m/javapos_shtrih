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
// Отчёт об открытии смены
///////////////////////////////////////////////////////////////////////////////
// Дата и время         DATE_TIME	5
// Номер ФД             Uint32, LE	4
// Фискальный признак	Uint32, LE	4
// Номер смены          Uint16, LE	2
///////////////////////////////////////////////////////////////////////////////

public class FSOpenDayReport extends FSDocument
{
    private final int dayNumber;

    public FSOpenDayReport(CommandInputStream in, int docType) throws Exception
    {
        super(in, docType);
        dayNumber = (int)in.readLong(2);
    }

    /**
     * @return the dayNumber
     */
    public int getDayNumber() {
        return dayNumber;
    }
}
