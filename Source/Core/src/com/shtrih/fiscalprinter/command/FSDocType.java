/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import com.shtrih.util.BitUtils;

/**
 *
 * @author V.Kravtsov
 */
public class FSDocType {

    //////////////////////////////////////////////////////////////////////
    //
    // 00h – нет открытого документа
    public static final int FS_DOCTYPE_NONE = 0;
    // 01h – отчет о фискализации
    public static final int FS_DOCTYPE_REG_REPORT = 1;
    // 02h – отчет об открытии смены
    public static final int FS_DOCTYPE_DAYOPEN = 2;
    // 03h – кассовый чек
    public static final int FS_DOCTYPE_RECEIPT = 3;
    // 05h – отчет о закрытии смены
    public static final int FS_DOCTYPE_DAYCLOSE = 5;
    // 10h – отчет о закрытии фискального режима
    public static final int FS_DOCTYPE_FISCAL_CLOSE = 16;
    /*
      11h – Бланк строкой отчетности
      12h - Отчет об изменении параметров регистрации ККТ в связи с заменой ФН
      13h – Отчет об изменении параметров регистрации ККТ
      14h – Кассовый чек коррекции
      15h – БСО коррекции
      17h – Отчет о текущем состоянии расчетов
     */

    private final int docType;

    public FSDocType(int docType) {
        this.docType = docType;
    }

    public int getValue() {
        return docType;
    }

    public boolean isDayOpen() {
        return docType == FS_DOCTYPE_DAYOPEN;
    }

    public String getText() {
        switch (docType) {
            case FS_DOCTYPE_NONE:
                return "None";
            case FS_DOCTYPE_REG_REPORT:
                return "Open fiscal mode";
            case FS_DOCTYPE_DAYOPEN:
                return "Open fiscal day";
            case FS_DOCTYPE_RECEIPT:
                return "Fiscal receipt";
            case FS_DOCTYPE_DAYCLOSE:
                return "Close fiscal day";
            case FS_DOCTYPE_FISCAL_CLOSE:
                return "Close fiscal mode";

            default:
                return "Unknown document type";
        }

    }
}
