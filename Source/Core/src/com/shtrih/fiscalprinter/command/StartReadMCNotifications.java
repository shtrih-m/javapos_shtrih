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
 * Начать выгрузку уведомлений о реализации маркированных товаров
 * 
 * Код команды FF71h. Длина сообщения: 5 байт.
 *  - Пароль оператора: 4 байта
 * 
 * Ответ: FF71h	    Длина сообщения: 4 байт.
 *  - Код ошибки: 1 байт
 *  - Общее число уведомлений: 2 байта
 *  - Номер первого уведомления: 4 байта
 *  - Размер первого уведомления: 2 байта

 ***************************************************************************/

public final class StartReadMCNotifications extends PrinterCommand {
    // in
    public int password;
    // out
    public int notificationCount;
    public long notificationNumber;
    public int notificationSize;

    /**
     * Creates a new instance of ConfirmDate
     */
    public StartReadMCNotifications() {
        super();
    }

    public final int getCode() {
        return 0xFF71;
    }

    public final String getText() {
        return "FS: Start read marking notifications";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
        notificationCount = in.readShort();
        notificationNumber = in.readLong(4);
        notificationSize = in.readShort();
   }
   
    
}
