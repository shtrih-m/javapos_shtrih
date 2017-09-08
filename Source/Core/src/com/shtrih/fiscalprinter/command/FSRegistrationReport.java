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
//  Отчёт о регистрации
///////////////////////////////////////////////////////////////////////////////
//
//  Дата и время                DATE_TIME	5
//  Номер ФД                    Uint32, LE	4
//  Фискальный признак          Uint32, LE	4
//  ИНН                         ASCII           12
//  Регистрационный номер ККТ	ASCII           20
//  Код налогообложения         Byte            1
//  Режим работы                Byte            1
//
///////////////////////////////////////////////////////////////////////////////

public class FSRegistrationReport extends FSDocument
{
    private final String taxId;
    private final String regId;
    private final int taxType;
    private final int workMode;

    public FSRegistrationReport(CommandInputStream in, int docType) throws Exception
    {
        super(in, docType);
        taxId = in.readString(12);
        regId = in.readString(20);
        taxType = in.readByte();
        workMode = in.readByte();
    }

    /**
     * @return the taxId
     */
    public String getTaxId() {
        return taxId;
    }

    /**
     * @return the regId
     */
    public String getRegId() {
        return regId;
    }

    /**
     * @return the taxType
     */
    public int getTaxType() {
        return taxType;
    }

    /**
     * @return the workMode
     */
    public int getWorkMode() {
        return workMode;
    }
}
