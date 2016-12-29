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
 * Передать произвольную TLV структуру
 * Код команды FF0Ch . Длина сообщения: 6+N байт.
 * Пароль системного администратора: 4 байта
 * TLV Структура N байт (мах 250 байт)
 * Ответ: FF0Сh Длина сообщения 1 байт.
 * Код ошибки (1 байт)
 *
 */
public class FSWriteTLV extends PrinterCommand {

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
    private byte[] tlv;

    public FSWriteTLV() {
    }

    public final int getCode() {
        return 0xFF0C;
    }

    public final String getText() {
        return "Fiscal storage: write TLV data";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
        out.writeBytes(getTlv());
    }

    public void decode(CommandInputStream in) throws Exception {
    }

    /**
     * @return the tlv
     */
    public byte[] getTlv() {
        return tlv;
    }

    /**
     * @param tlv the tlv to set
     */
    public void setTlv(byte[] tlv) {
        this.tlv = tlv;
    }


}
