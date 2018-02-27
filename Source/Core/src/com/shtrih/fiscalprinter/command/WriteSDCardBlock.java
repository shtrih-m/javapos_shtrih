/*
 * CloseEJArhive.java
 *
 * Created on 16 January 2009, 13:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
/****************************************************************************
Запись блока данных прошивки  ФР на SD карту FF4EH
Код команды FF4Eh . Длина сообщения: 137 байт.
Пароль системного администратора: 4 байта
Файл прошивки: 1 байт ( 0- загрузчик, 1 – прошивка)
Номер блока: 2 байта
Блок данных: 128 байт.

Ответ:	 FF4E  Длина сообщения: 1 байт.
Код ошибки: 1 байт
* 
 ****************************************************************************/
public final class WriteSDCardBlock extends PrinterCommand {
    // in
    private int password;
    private int fileType;
    private int blockNumber;
    private byte[] block;

    /**
     * Creates a new instance of CloseEJArhive
     */
    public WriteSDCardBlock() {
        super();
    }

    public final int getCode() {
        return 0xFF4E;
    }

    public final String getText() {
        return "Write SD card block";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getFileType());
        out.writeShort(getBlockNumber());
        out.writeBytes(getBlock());
        }

    public final void decode(CommandInputStream in) throws Exception {
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    /**
     * @return the fileType
     */
    public int getFileType() {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the blockNumber
     */
    public int getBlockNumber() {
        return blockNumber;
    }

    /**
     * @param blockNumber the blockNumber to set
     */
    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    /**
     * @return the block
     */
    public byte[] getBlock() {
        return block;
    }

    /**
     * @param block the block to set
     */
    public void setBlock(byte[] block) {
        this.block = block;
    }
}
