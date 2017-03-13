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
 *
 * Записать блок данных в буфер
 * Код команды FF33h . Длина сообщения: 9+N байт.
 * Пароль системного администратора: (4 байта)
 * Начальное смещение: (2 байта)
 * Размер данных (1 байт)
 * Данные для записи ( N байт)
 * Ответ:	FF33h Длина сообщения: 1 байт.
 * Код ошибки (1 байт)
 *
 */
public class FSWriteBlock extends PrinterCommand {

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
    private int offset;
    private byte[] data;

    public FSWriteBlock() {
    }

    public final int getCode() {
        return 0xFF33;
    }

    public final String getText() {
        return "Fiscal storage: write block";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
        out.writeShort(getOffset());
        out.writeByte(getData().length);
        out.writeBytes(getData());
    }

    public void decode(CommandInputStream in) throws Exception {
    }

    /**
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

}
