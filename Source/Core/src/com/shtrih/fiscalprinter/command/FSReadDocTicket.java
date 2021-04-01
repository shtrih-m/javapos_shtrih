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
Запрос квитанции о получении данных в ОФД по номеру  документа FF3CH
Код команды FF3Сh . Длина сообщения: 11 байт.
    Пароль системного администратора: 4 байта
    Номер фискального документа: 4 байта
Ответ:	    FF3Сh Длина сообщения: 1+N байт.
    Код ошибки: 1 байт
    Квитанция: N байт
 */
public class FSReadDocTicket extends PrinterCommand {

    // in
    private int sysPassword; // System sdministrator password (4 bytes)
    private int docNumber;
    // out
    private byte[] ticket = null;


    public FSReadDocTicket() {
    }

    public final int getCode() {
        return 0xFF3C;
    }

    public final String getText() {
        return "Fiscal storage: read document ticket";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
        out.writeInt(getDocNumber());
    }

    public void decode(CommandInputStream in) throws Exception {
        setTicket(in.readBytes(in.size()));
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
     * @return the docNumber
     */
    public int getDocNumber() {
        return docNumber;
    }

    /**
     * @param docNumber the docNumber to set
     */
    public void setDocNumber(int docNumber) {
        this.docNumber = docNumber;
    }

    /**
     * @return the ticket
     */
    public byte[] getTicket() {
        return ticket;
    }

    /**
     * @param ticket the ticket to set
     */
    public void setTicket(byte[] ticket) {
        this.ticket = ticket;
    }

}
