package com.shtrih.fiscalprinter.command;

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
 */
public class FSReadBufferStatus extends PrinterCommand {

    // in
    private int sysPassword; // System sdministrator password (4 bytes)
    
    // out
    private int dataSize; // 
    private int blockSize; // 

    public FSReadBufferStatus(int sysPassword) {
        this.sysPassword = sysPassword;
    }

    public final int getCode() {
        return 0xFF30;
    }

    public final String getText() {
        return "Fiscal storage: read buffer status";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(sysPassword);
    }

    public void decode(CommandInputStream in) throws Exception {
        dataSize = in.readShort();
        blockSize = in.readByte();
    }

    /**
     * @return the dataSize
     */
    public int getDataSize() {
        return dataSize;
    }

    /**
     * @return the blockSize
     */
    public int getBlockSize() {
        return blockSize;
    }

}
