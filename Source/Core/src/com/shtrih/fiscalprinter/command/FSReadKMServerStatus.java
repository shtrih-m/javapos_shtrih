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
 * - Код ошибки: 1 байт
 * - Состояние по передаче уведомлений: 1 байт
        0 - нет активного обмена;
        1 - начато чтение уведомления;
        2 - ожидание квитанции на уведомление;
    - Количество уведомлений в очереди: 2 байта
        0, если на все уведомления была получена квитанция
    - Номер текущего уведомления: 4 байта
        Номер уведомления для передачи, или уведомления на 
        которое ожидается квитанция
    - Дата и время текущего уведомления: 5 байт
        0, если на все уведомления получена квитанция
    - Процент заполнения области хранения уведомлений: 1 байт
 *
 ***************************************************************************
 */
public final class FSReadKMServerStatus extends PrinterCommand {

    // in
    public int password;
    // out
    // Статус чтения сообщения для ИСМ : 1 байт
    public int messageStatus;
    // Количество сообщений «Отчет об изменении статуса» в очереди : 2 байта
    public int messageQuantity;
    // Номер сообщения для ИСМ : 4 байта
    public int messageNumber;
    // Дата-время документа для ИСМ : 5 байт
    public PrinterDate messageDate;
    public PrinterTime messageTime;
    //Размер свободной области для хранения «Отчет об изменении статуса» в килобайтах : 4 байта
    public int freeMemoryPercents;

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
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
        messageStatus = in.readByte();
        messageQuantity = in.readShort();
        messageNumber = in.readInt();
        messageDate = in.readDate();
        messageTime = in.readTimeHM();
        freeMemoryPercents = in.readByte();
    }

}
