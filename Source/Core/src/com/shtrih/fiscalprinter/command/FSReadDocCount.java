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
Запрос количества ФД на которые нет квитанции FF3FH
Код команды FF3Fh . Длина сообщения: 6 байт.
    Пароль системного администратора: 4 байта
Ответ:	    FF3Fh Длина сообщения: 3 байт.
    Код ошибки: 1 байт
    Количество неподтверждённых ФД : 2 байта
 */

public class FSReadDocCount extends PrinterCommand {

    // in
    private int sysPassword; // System sdministrator password (4 bytes)
    // out
    private int documentCount;

    public FSReadDocCount() {
    }

    public final int getCode() {
        return 0xFF3F;
    }

    public final String getText() {
        return "Fiscal storage: read documents count";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(sysPassword);
    }

    public void decode(CommandInputStream in) throws Exception {
        documentCount = in.readInt();
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
}
