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
/****************************************************************************
 * Подтвердить выгрузку уведомления (в автономном режиме).
 * 
 * Код команды FF73h. Длина сообщения: 5 байт.
 *  - Пароль оператора: 4 байта
 *  - Номер уведомления: 4 байта, получается из ответа на команду  FF72
 *  - CRC16, Контрольная сумма уведомления: 2 байта
 * 
 * Ответ: FF73h	    Длина сообщения: 4 байт.
 *  - Код ошибки: 1 байт
 * 
 * Драйвер должен выгрузить из ККТ уведомления (все или часть), 
 * сохранить их в файл, посчитать контрольные суммы и подтвердить 
 * выгрузку в ККТ. Если была выгружена часть уведомлений, 
 * необходимо повторить процедуру. 
 * Формат и алгоритм выгрузки описан в ФФД, пункты 174-178.
 * 
 ***************************************************************************/

public final class ConfirmMCNotification extends PrinterCommand {
    // in
    public int password;
    public int number;
    public int crc;

    /**
     * Creates a new instance of ConfirmDate
     */
    public ConfirmMCNotification() {
        super();
    }

    public final int getCode() {
        return 0xFF73;
    }

    public final String getText() {
        return "FS: Confirm notification";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeInt(number);
        out.writeShort(crc);
    }

    public final void decode(CommandInputStream in) throws Exception {
   }
   
    
}
