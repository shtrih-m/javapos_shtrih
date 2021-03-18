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
 * Прочитать блок уведомления (в автономном режиме)
 * 
 * Код команды FF72h. Длина сообщения: 5 байт.
 *  - Пароль оператора: 4 байта
 * 
 * Ответ: FF72h	    Длина сообщения: 4 байт.
 *  - Код ошибки: 1 байт
 *  - Номер текущего уведомления: 4 байта
 *  - Полный размер текущего уведомления: 2 байта
 *  - Смещение от начала текущего уведомления: 2 байта
 *  - Размер прочитанного блока данных: 2 байта
 *  - Блок данных: X байт
 * 
 * ККТ выполняет поблочное всех доступных уведомлений 
 * (максимально ККТ может прочитать блок 128 байт). 
 * Следует вызывать команду до получения ошибки «нет данных» или 
 * на основании общего числа уведомлений, полученного из команды FF 71. 
 * Допускается прочитать лишь часть уведомлений и подтвердить их. 
 * В любой момент до подтверждения чтения можно вызвать команду FF71 и 
 * начать чтение неподтвержденных уведомлений заново.
 * 
 ***************************************************************************/

public final class ReadMCNotification extends PrinterCommand {
    // in
    public int password;
    // out
    public long notificationNumber;
    public int notificationSize;
    public int notificationOffset;
    public int blockSize;
    public byte[] data;

    /**
     * Creates a new instance of ConfirmDate
     */
    public ReadMCNotification() {
        super();
    }

    public final int getCode() {
        return 0xFF72;
    }

    public final String getText() {
        return "FS: Read marking notifications";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
        notificationNumber = in.readLong(4);
        notificationSize = in.readShort();
        notificationOffset = in.readShort();
        blockSize = in.readShort();
        data = in.readBytesToEnd();
   }
   
    
}
