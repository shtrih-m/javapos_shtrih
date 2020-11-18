/*
 * ConfirmDate.java
 *
 * Created on 2 April 2008, 21:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
/**
 * **************************************************************************
 * Получить статус информационного обмена с АС «Серверы СКЗКМ» FF68H
 * Код команды FF68h. Длина сообщения: 6 байт.
 * Пароль оператора: 4 байта
 *
 * Ответ: FF68h	Длина сообщения: 18 байт.
 * Код ошибки: 1 байт
 * Состояние транспортного соединения : 1 байт
 * Статус чтения сообщения для ИСМ : 1 байт
 * Количество сообщений «Отчет об изменении статуса» в очереди : 2 байта
 * Номер сообщения для ИСМ : 4 байта
 * Дата-время документа для ИСМ : 5 байт
 * Размер свободной области для хранения «Отчет об изменении статуса» в
 * килобайтах : 4 байта
 *
 *
 ***************************************************************************
 */
public final class FSReadKMServerStatus extends PrinterCommand {

    // in
    private int password;
    // out
    // Состояние транспортного соединения : 1 байт
    private int connectionStatus;
    // Статус чтения сообщения для ИСМ : 1 байт
    private int messageStatus;
    // Количество сообщений «Отчет об изменении статуса» в очереди : 2 байта
    private int messageQuantity;
    // Номер сообщения для ИСМ : 4 байта
    private int messageNumber;
    // Дата-время документа для ИСМ : 5 байт
    private PrinterDate messageDate;
    private PrinterTime messageTime;
    //Размер свободной области для хранения «Отчет об изменении статуса» в килобайтах : 4 байта
    private long freeMemorySizeInKB;

    /**
     * Creates a new instance of ConfirmDate
     */
    public FSReadKMServerStatus() {
        super();
    }

    public final int getCode() {
        return 0xFF68;
    }

    public final String getText() {
        return "FS: Read KM server status";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
    }

    public final void decode(CommandInputStream in) throws Exception {
        connectionStatus = in.readByte();
        messageStatus = in.readByte();
        messageQuantity = (int) in.readLong(2);
        messageNumber = (int) in.readLong(4);
        messageDate = in.readDate();
        messageTime = in.readFSTime();
        freeMemorySizeInKB = (int) in.readLong(4);
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    /**
     * @return the connectionStatus
     */
    public int getConnectionStatus() {
        return connectionStatus;
    }

    /**
     * @param connectionStatus the connectionStatus to set
     */
    public void setConnectionStatus(int connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    /**
     * @return the messageStatus
     */
    public int getMessageStatus() {
        return messageStatus;
    }

    /**
     * @param messageStatus the messageStatus to set
     */
    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    /**
     * @return the messageQuantity
     */
    public int getMessageQuantity() {
        return messageQuantity;
    }

    /**
     * @param messageQuantity the messageQuantity to set
     */
    public void setMessageQuantity(int messageQuantity) {
        this.messageQuantity = messageQuantity;
    }

    /**
     * @return the messageNumber
     */
    public int getMessageNumber() {
        return messageNumber;
    }

    /**
     * @param messageNumber the messageNumber to set
     */
    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }

    /**
     * @return the messageDate
     */
    public PrinterDate getMessageDate() {
        return messageDate;
    }

    /**
     * @param messageDate the messageDate to set
     */
    public void setMessageDate(PrinterDate messageDate) {
        this.messageDate = messageDate;
    }

    /**
     * @return the freeMemorySizeInKB
     */
    public long getFreeMemorySizeInKB() {
        return freeMemorySizeInKB;
    }

    /**
     * @param freeMemorySizeInKB the freeMemorySizeInKB to set
     */
    public void setFreeMemorySizeInKB(long freeMemorySizeInKB) {
        this.freeMemorySizeInKB = freeMemorySizeInKB;
    }

    /**
     * @return the messageTime
     */
    public PrinterTime getMessageTime() {
        return messageTime;
    }

    /**
     * @param messageTime the messageTime to set
     */
    public void setMessageTime(PrinterTime messageTime) {
        this.messageTime = messageTime;
    }

}
