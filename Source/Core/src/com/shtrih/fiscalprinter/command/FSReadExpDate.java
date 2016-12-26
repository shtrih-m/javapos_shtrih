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
 * Запрос срока действия ФН
 * Код команды FF03h . Длина сообщения: 6 байт.
 * Пароль системного администратора: 4 байт
 * Ответ:	FF03h Длина сообщения: 4 байт.
 * Код ошибки (1 байт)
 * Срок действия (3 байт) BCD Год, Месяц, День
 */
public class FSReadExpDate extends PrinterCommand {

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
    // out
    private PrinterDate date; // Serial number

    public FSReadExpDate() {
    }

    public final int getCode() {
        return 0xFF03;
    }

    public final String getText() {
        return "Fiscal storage: read expiration date";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
    }

    public void decode(CommandInputStream in) throws Exception {
        setDate(in.readDateYMD());
    }

    /**
     * @return the date
     */
    public PrinterDate getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(PrinterDate date) {
        this.date = date;
    }

}
