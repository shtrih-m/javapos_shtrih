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
 * Начать фискализацию ФН
 * Код команды FF05h. Длина сообщения: 6 байт.
 * Пароль системного администратора: 4 байт
 * Ответ:	FF05h Длина сообщения: 1 байт.
 * Код ошибки (1 байт)
 *
 */
public class FSStartFiscalization extends PrinterCommand {

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

    // in
    private int sysPassword = 0; // System sdministrator password (4 bytes)

    public FSStartFiscalization() {
    }

    public final int getCode() {
        return 0xFF05;
    }

    public final String getText() {
        return "Fiscal storage: start fiscalization";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
    }

    public void decode(CommandInputStream in) throws Exception {
    }

}
