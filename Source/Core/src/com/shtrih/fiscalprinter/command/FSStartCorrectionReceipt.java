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
Начать формирование чека коррекции FF35H
Код команды FF35h . Длина сообщения: 6 байт.
    Пароль системного администратора: 4 байта
Ответ:	    FF35h Длина сообщения: 1 байт.
    Код ошибки: 1 байт
*/

public class FSStartCorrectionReceipt extends PrinterCommand {

    // in
    private int sysPassword = 0; // System sdministrator password (4 bytes)

    public FSStartCorrectionReceipt() {
    }

    public final int getCode() {
        return 0xFF35;
    }

    public final String getText() {
        return "Fiscal storage: start correction receipt";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
    }

    public void decode(CommandInputStream in) throws Exception {
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
}
