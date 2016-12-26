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
Начать открытие смены FF41H
Код команды FF41h . Длина сообщения: 6 байт.
    Пароль системного администратора: 4 байта
Ответ:	    FF41h Длина сообщения: 1 байт.
    Код ошибки: 1 байт
 */
public class FSStartDayOpen extends PrinterCommand {

    // in
    private int sysPassword; // System sdministrator password (4 bytes)

    public FSStartDayOpen() {
    }

    public final int getCode() {
        return 0xFF41;
    }

    public final String getText() {
        return "Fiscal storage: start day open";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(sysPassword);
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
