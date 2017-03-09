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
 * Прочитать блок данных данных из буфера
 * Код команды FF31h . Длина сообщения: 6 байт.
 * Пароль системного администратора: (4 байта)
 * Начальное смещение: 2 байта
 * Количество запрашиваемых данных (1 байт)
 * Ответ:	FF31h Длина сообщения: 1+N байт.
 * Код ошибки (1 байт)
 * Данные (N байт)
 *
 *
 */
public class FSReadBlock extends PrinterCommand {

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
    private int size;
    // out
    private byte[] data;

    public FSReadBlock() {
    }

    public final int getCode() {
        return 0xFF31;
    }

    public final String getText() {
        return "Fiscal storage: read buffer block";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
        out.writeShort(getOffset());
        out.writeByte(getSize());
    }

    public void decode(CommandInputStream in) throws Exception {
        setData(in.readBytes(in.getSize()));
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
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
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
