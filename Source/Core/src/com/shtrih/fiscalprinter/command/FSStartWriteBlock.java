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
 * Начать запись данных в буфер
 * Код команды FF32h . Длина сообщения: 8 байт.
 * Пароль системного администратора: (4 байта)
 * Размер данных ( 2 байта)
 * Ответ:	FF32h Длина сообщения: 2 байта.
 * Код ошибки (1 байт)
 * Максимальный размер блок данных (1 байт)
 *
 *
 */
public class FSStartWriteBlock extends PrinterCommand {

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
    private int size;
    // out
    private int blockSize;

    public FSStartWriteBlock() {
    }

    public final int getCode() {
        return 0xFF32;
    }

    public final String getText() {
        return "Fiscal storage: start write block";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
        out.writeShort(getSize());
    }

    public void decode(CommandInputStream in) throws Exception {
        setBlockSize(in.readByte());
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
