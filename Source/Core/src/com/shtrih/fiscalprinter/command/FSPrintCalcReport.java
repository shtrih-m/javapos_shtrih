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
Сформировать отчёт о состоянии расчётов FF38H
Код команды FF38h . Длина сообщения: 6 байт.
    Пароль системного администратора: 4 байта
Ответ:	    FF38h Длина сообщения: 16 байт.
    Код ошибки: 1 байт
    Номер ФД: 4 байта
    Фискальный признак: 4 байта
    Количество неподтверждённых документов: 4 байта
    Дата первого неподтверждённого документа: 3 байта ГГ,ММ,ДД
*/

public class FSPrintCalcReport extends PrinterCommand {

    // in
    private int sysPassword = 0; // System sdministrator password (4 bytes)
    // out
    private int documentNumber;
    private int documentDigest;
    private int documentCount;
    private PrinterDate documentDate;
    
    public FSPrintCalcReport() {
    }

    public final int getCode() {
        return 0xFF38;
    }

    public final String getText() {
        return "Fiscal storage: print calculation report";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
    }

    public void decode(CommandInputStream in) throws Exception {
        documentNumber = in.readInt();
        documentDigest = in.readInt();
        documentCount = in.readInt();
        documentDate = in.readDateYMD();
    }

    /**
     * @return the sysPassword
     */
    public int getSysPassword() {
        return sysPassword;
    }

    /**
     * @param sysPassword the sysPassword to set
     */
    public void setSysPassword(int sysPassword) {
        this.sysPassword = sysPassword;
    }

    /**
     * @return the documentNumber
     */
    public int getDocumentNumber() {
        return documentNumber;
    }

    /**
     * @param documentNumber the documentNumber to set
     */
    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }

    /**
     * @return the documentDigest
     */
    public int getDocumentDigest() {
        return documentDigest;
    }

    /**
     * @param documentDigest the documentDigest to set
     */
    public void setDocumentDigest(int documentDigest) {
        this.documentDigest = documentDigest;
    }

    /**
     * @return the documentCount
     */
    public int getDocumentCount() {
        return documentCount;
    }

    /**
     * @param documentCount the documentCount to set
     */
    public void setDocumentCount(int documentCount) {
        this.documentCount = documentCount;
    }

    /**
     * @return the documentDate
     */
    public PrinterDate getDocumentDate() {
        return documentDate;
    }

    /**
     * @param documentDate the documentDate to set
     */
    public void setDocumentDate(PrinterDate documentDate) {
        this.documentDate = documentDate;
    }
}
