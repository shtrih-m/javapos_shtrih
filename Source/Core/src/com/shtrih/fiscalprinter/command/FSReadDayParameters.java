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
Запрос параметров текущей смены FF40H
Код команды FF40h . Длина сообщения: 6 байт.
    Пароль системного администратора: 4 байта
Ответ:	    FF40h Длина сообщения: 6 байт.
    Код ошибки: 1 байт
    Состояние смены: 1 байт
    Номер смены : 2 байта
    Номер чека: 2 байта
 */

public class FSReadDayParameters extends PrinterCommand {

    // in
    private int sysPassword; // System sdministrator password (4 bytes)
    // out
    private int dayStatus;
    private int dayNumber;
    private int receiptNumber;
    
    public FSReadDayParameters() {
    }

    public final int getCode() {
        return 0xFF40;
    }

    public final String getText() {
        return "Fiscal storage: read day parameters";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
    }

    public void decode(CommandInputStream in) throws Exception {
        setDayStatus(in.readByte());
        setDayNumber(in.readShort());
        setReceiptNumber(in.readShort());
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
     * @return the dayStatus
     */
    public int getDayStatus() {
        return dayStatus;
    }

    /**
     * @param dayStatus the dayStatus to set
     */
    public void setDayStatus(int dayStatus) {
        this.dayStatus = dayStatus;
    }

    /**
     * @return the dayNumber
     */
    public int getDayNumber() {
        return dayNumber;
    }

    /**
     * @param dayNumber the dayNumber to set
     */
    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    /**
     * @return the receiptNumber
     */
    public int getReceiptNumber() {
        return receiptNumber;
    }

    /**
     * @param receiptNumber the receiptNumber to set
     */
    public void setReceiptNumber(int receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

}
