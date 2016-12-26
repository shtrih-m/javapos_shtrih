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
 * Открыть смену в ФН
 * Код команды FF0Bh . Длина сообщения: 10 байт.
 * Пароль системного администратора: 4 байт
 * Ответ: FF0Bh Длина сообщения 11 байт.
 * Код ошибки (1 байт)
 * Номер новой открытой смены ( 2 байт)
 * Номер ФД (4 байта)
 * Фискальный признак ( 4 байт)
 *
 *
 */
public class FSOpenDay extends PrinterCommand {

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
    private int sysPassword; // System sdministrator password (4 bytes)
    // out
    private int dayNumber;
    private long docNumber;
    private long macNumber;

    public FSOpenDay() {
    }

    public final int getCode() {
        return 0xFF0B;
    }

    public final String getText() {
        return "Fiscal storage: open fiscal day";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
    }

    public void decode(CommandInputStream in) throws Exception {
        setDayNumber(in.readShort());
        setDocNumber(in.readLong(4));
        setMacNumber(in.readLong(4));
    }

    /**
     * @return the docNumber
     */
    public long getDocNumber() {
        return docNumber;
    }

    /**
     * @param docNumber the docNumber to set
     */
    public void setDocNumber(long docNumber) {
        this.docNumber = docNumber;
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
     * @return the macNumber
     */
    public long getMacNumber() {
        return macNumber;
    }

    /**
     * @param macNumber the macNumber to set
     */
    public void setMacNumber(long macNumber) {
        this.macNumber = macNumber;
    }

}
