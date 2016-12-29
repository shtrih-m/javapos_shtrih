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
 * Запросить наличие данных в буфере
 * Код команды FF30h . Длина сообщения: 6 байт.
 * Пароль системного администратора: 4 байта
 * Ответ:	FF30h Длина сообщения: 4 байта.
 * Код ошибки (1 байт)
 * Количество байт в буфере ( 2 байта ) 0 – нет данных
 * Максимальный размер блока данных ( 1 байт)
 *
 *
 */
public class FSReadBufferStatus extends PrinterCommand {

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
    private int dataSize; // 
    private int blockSize; // 

    public FSReadBufferStatus() {
    }

    public final int getCode() {
        return 0xFF30;
    }

    public final String getText() {
        return "Fiscal storage: read buffer status";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
    }

    public void decode(CommandInputStream in) throws Exception {
        setDataSize(in.readShort());
        setBlockSize(in.readByte());
    }

    /**
     * @return the dataSize
     */
    public int getDataSize() {
        return dataSize;
    }

    /**
     * @param dataSize the dataSize to set
     */
    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    /**
     * @return the blockSize
     */
    public int getBlockSize() {
        return blockSize;
    }

    /**
     * @param blockSize the blockSize to set
     */
    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

}
