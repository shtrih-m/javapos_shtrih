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
/**
 * Запрос номера ФН
 * Код команды FF02h . Длина сообщения: 6 байт.
 * Пароль системного администратора: 4 байт
 * Ответ:	FF02 Длина сообщения: 17 байт.
 * Код ошибки (1 байт)
 * Номер ФН (16 байт) ASCII
 */
public class FSReadSerial extends PrinterCommand {

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
    private String serial; // Serial number

    public FSReadSerial() {
    }

    public final int getCode() {
        return 0xFF02;
    }

    public final String getText() {
        return "Fiscal storage: read serial";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
    }

    public void decode(CommandInputStream in) throws Exception {
        setSerial(in.readString(16));
    }

    /**
     * @return the serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

}
