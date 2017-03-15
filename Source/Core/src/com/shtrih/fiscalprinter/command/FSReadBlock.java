/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

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
 */
public class FSReadBlock extends PrinterCommand {
        
    // in
    private int sysPassword; // System sdministrator password (4 bytes)
    private int offset;
    private int size;
    // out
    private byte[] data;

    public FSReadBlock(int sysPassword, int offset, int size) {
        this.sysPassword = sysPassword;
        this.offset = offset;
        this.size = size;
    }

    public final int getCode() {
        return 0xFF31;
    }

    public final String getText() {
        return "Fiscal storage: read buffer block";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(sysPassword);
        out.writeShort(offset);
        out.writeByte(size);
    }

    public void decode(CommandInputStream in) throws Exception {
        data = in.readBytes(in.getSize());
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }
}
