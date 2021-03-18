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
 * Запрос статуса по работе с кодами маркировки FF70H
 * 
 * Код команды FF70h. Длина сообщения: 6 байт.
 * Пароль оператора: 4 байта
 *
 * Ответ: FF70h	Длина сообщения: 8 байт.
 * - Код ошибки: 1 байт
 * - Состояние по проверке КМ	[Byte	1]  MCCheckStatus
    0 - работа с КМ временно заблокирована
    1 - нет КМ на проверке
    2 - передан КМ в команде B1h
    3 - сформирован запрос о статусе КМ в команде B5h
    4 -  получен ответ на запрос о статусе КМ в команде B6h
- Состояние по формированию уведомления	[Byte	1] MCNotificationStatus
    0 - уведомление о реализации не формируется
    1 - начато формирование уведомления о реализации
- Флаги разрешения команд работы с КМ	[Byte	1]  MCCommandFlags
    См. таблицу "Флаги разрешения команд работы с КМ"
    Биты	Код разрешенной команды
    0	0	0	0	0	0	0	1	B1h
    0	0	0	0	0	0	1	0	B2h
    0	0	0	0	0	1	0	0	B3h
    0	0	0	0	1	0	0	0	B5h
    0	0	0	1	0	0	0	0	B6h
    0	0	1	0	0	0	0	0	B7h с доп. кодом 1
    0	1	0	0	0	0	0	0	B7h с доп. кодом 2
    1	0	0	0	0	0	0	0	B7h с доп. кодом 3
- Количество сохранённых результатов проверки КМ	[Byte	1] MCCheckResultSavedCount
  Количество КМ, результаты проверки которых, сохранены в ФН командой B2h c кодом 1
- Количество КМ, включенных в уведомление о реализации	[Byte	1] MCRealizationCount
- Предупреждение о заполнении области хранения уведомлений о реализации маркированного товара	[Byte	1] MCStorageSize
  В этом параметре ФН информирует ККТ о заполнении области хранения маркированного товара. Возможные следующие значения
    0 - область заполнена менее чем на 50%
    1 - область от 50 до 80%
    2 - область от 80 до 90%
    3 - область заполнена более чем на 90%
- Количество уведомлений в очереди	[Uint16,LE	2]	
* Количество неподтверждённых или невыгруженных уведомлений о реализации маркированного товара
* 
* 
 ***************************************************************************
 */

public final class FSReadMCStatus extends PrinterCommand {

    // in
    private int password;
    // out
    // Состояние по проверке КМ
    private int status; 
    // Состояние по формированию уведомления
    private int notificationStatus; 
    // Флаги разрешения команд работы с КМ
    private int commandFlags;
    // Количество сохранённых результатов проверки КМ
    private int resultSavedCount;
    // Количество КМ, результаты проверки которых, сохранены в ФН
    private int realizationCount;
    // Предупреждение о заполнении области хранения уведомлений о реализации маркированного товара
    private int storageSize;
    // Количество уведомлений в очереди
    private int messageCount;
  

    /**
     * Creates a new instance of ConfirmDate
     */
    public FSReadMCStatus() {
        super();
    }

    public final int getCode() {
        return 0xFF70;
    }

    public final String getText() {
        return "FS: Read MC status";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
    }

    public final void decode(CommandInputStream in) throws Exception {
        
        status = in.readByte(); 
        notificationStatus = in.readByte();
        commandFlags = in.readByte();
        resultSavedCount = in.readByte();
        realizationCount = in.readByte();
        storageSize = in.readByte();
        messageCount = in.readShort();
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return the notificationStatus
     */
    public int getNotificationStatus() {
        return notificationStatus;
    }

    /**
     * @return the commandFlags
     */
    public int getCommandFlags() {
        return commandFlags;
    }

    /**
     * @return the resultSavedCount
     */
    public int getResultSavedCount() {
        return resultSavedCount;
    }

    /**
     * @return the realizationCount
     */
    public int getRealizationCount() {
        return realizationCount;
    }

    /**
     * @return the storageSize
     */
    public int getStorageSize() {
        return storageSize;
    }

    /**
     * @return the messageCount
     */
    public int getMessageCount() {
        return messageCount;
    }

}
