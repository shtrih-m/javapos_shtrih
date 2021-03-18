package com.shtrih.fiscalprinter.command;

import com.shtrih.util.MethodParameter;

/**
 * @author P.Zhirkov
 */
/**
 * **************************************************************************
 * Проверка маркированного товара FF61H
 * Код команды FF61h. Длина сообщения: 9 байт.
 * - Пароль оператора: 4 байта
 * - Планируемый статус: 1 байт
 * - Режим обработки: 1 байт
 * - Длина КМ в байтах: 1 байт
 * - Длина списка TLV в байтах: 1 байт
 * - Сам КМ, как он был прочитан сканером: N байт
 * - Список TLV	
 * Если планируется частичное выбытие (согласно с тегом 2003), 
 * то необходимо сформировать буфер из тегов 2108 (мера) и 1023(количество) и 
 * передать его здесь
* 
 * Ответ: FF61h	Длина сообщения: 8 байт.
 * - Код ошибки: 1 байт
 * - Статус  локальной проверки: 1 байт
 * - Причина, по которой не была проведена локальная проверка: 1 байт
 * - Распознанный тип КМ: 1 байт
 * - Длина дополнительных параметров: 1 байт
 * - Код ответа ФН на команду онлайн-проверки: 1 байт
 * - Результат проверки КМ: 1 байт
 * - Список реквизитов ответа сервера: X байт
 *
 ***************************************************************************
 */
public class FSCheckBarcode extends PrinterCommand {
    ////////////////////////////////////////////////////////////////////////////
    // ItemStatus codes

    // 1 - «Сформирован».Не выдан регистратору.
    public static final int FS_ITEM_STATUS_FORMED = 1;
    // 2 - «Готов». Выдан регистратору, но не применен.
    public static final int FS_ITEM_STATUS_READY = 2;
    // 3 - «Выдан». КМ выдан ТС для нанесения. Применение не подтверждено.
    public static final int FS_ITEM_STATUS_SENT = 3;
    // 4 - «Выпущен». КМ нанесен на товар или упаковку, правильность нанесения кода подтверждена, маркированный товар произведен.
    public static final int FS_ITEM_STATUS_RELEASED = 4;
    // 5 - «Не использован». КМ не был выдан ТС к моменту закрытия заказа.
    public static final int FS_ITEM_STATUS_NOTUSED = 5;
    // 6 - «Упакован». Товар или упаковка с данным КМ находится в составе логистической единицы.
    public static final int FS_ITEM_STATUS_PACKED = 6;
    // 7 - «Распакован». Маркированный объект находится в обороте или в употреблении в виде товарной единицы.
    public static final int FS_ITEM_STATUS_UNPACKED = 7;
    // 8 - Выбыл по определенным, известным участникам обращения товара, причинам на этапе производства (например, отобран, как опытный образец для испытаний), оптового или розничного оборота (уничтожен безвозвратно в составе логистической единицы, похищенной, испорченной в совокупности со всем содержимым и т.п.).
    public static final int FS_ITEM_STATUS_DROPPED = 8;
    //9 - «Выбыл через розничную сеть».
    public static final int FS_ITEM_STATUS_RETAIL = 9;
    // 10 - «В состоянии выбытия» (мерный товар).
    public static final int FS_ITEM_STATUS_DROPPING = 10;
    //11 - «Утерян».
    public static final int FS_ITEM_STATUS_LOST = 11;
    // 12 - «Оборот приостановлен».
    public static final int FS_ITEM_STATUS_DELAYED = 12;
    // 13 - «Оборот запрещен».
    public static final int FS_ITEM_STATUS_DISABLED = 13;
    // 14 - «Потреблен».
    public static final int FS_ITEM_STATUS_SOLD = 14;
    // 15 - «Дублирован».
    public static final int FS_ITEM_STATUS_DUPLICATED = 15;

    ////////////////////////////////////////////////////////////////////////////
    // checkMode constants
    // 0 – полная проверка.
    public static final int FS_CHECK_MODE_FULL = 0;
    // 1 – только онлайн проверка.
    public static final int FS_CHECK_MODE_ONLINE = 1;
    // 2 – только локальная проверка.
    public static final int FS_CHECK_MODE_LOCAL = 2;

    ////////////////////////////////////////////////////////////////////////////
    // localResultCode constants
    // Результат локальной проверки кода маркировки: 1 байт
    // 0 – проверка не проводилась, (для симметричной криптографической системы). 
    public static final int FS_LOCAL_RESULT_CODE_SYMMETRIC = 0;
    // 1 – код маркировки проверен, достоверный. 
    public static final int FS_LOCAL_RESULT_CODE_VALID = 1;
    // 2 – код маркировки проверен, недостоверный.
    public static final int FS_LOCAL_RESULT_CODE_INVALID = 2;
    // 3 – проверка не проводилась, (криптографическая система асимметричная, но в ФН-М нет ключа с идентификатором КПКИЗ.ид)
    public static final int FS_LOCAL_RESULT_CODE_ASYMMETRIC = 0;

    ////////////////////////////////////////////////////////////////////////////
    // salePermission constants
    // Разрешение на продажу товара от ИСМ: 1 байт.
    // 0 – товар разрешен к продаже
    public static final int FS_SALE_PERMISSION_OK = 0;
    // 1 – товар запрещен к продаже
    public static final int FS_SALE_PERMISSION_DENIED = 1;

    ////////////////////////////////////////////////////////////////////////////
    // serverResultCode constants
    // Код ошибки от сервера КМ: 1 байт
    // 0 - Статус успешно изменен
    // Контрольные идентификационные знаки (КиЗ)
    // Control Identification Mark (CIM)
    // 0 - Статус успешно изменен
    public static final int FS_SERVER_OK = 0;
    // 1 - КИЗ отсутствует в базе Серверы СКЗКМ или КИЗ отсутствует в базе ИСМ
    public static final int FS_SERVER_CIM_NOTFOUND = 1;
    // 2 - Не корректен формат КИЗ
    public static final int FS_SERVER_CIM_INCORRECT_FORMAT = 2;
    // 3 - Криптографическая проверка КПКИЗ дала отрицательный результат
    public static final int FS_SERVER_CIM_INCORRECT_CRC = 3;
    // 4 - КИЗ имеет в базе Серверы СКЗКМ статус не совместимый с запрашиваемым
    public static final int FS_SERVER_CIM_INCORRECT_STATUS = 4;
    // 5 - В списке вложения обнаружены ошибки
    public static final int FS_SERVER_CIM_ATTACH_ERROR = 5;

    // in 
    public int password;
    public int itemStatus;
    public int checkMode;
    public byte[] mcData;
    public byte[] tlv;
    // out
    public int localCheckStatus;
    public int localErrorCode;
    public int symbolicType;
    public int serverErrorCode;
    public int serverCheckStatus;
    public byte[] serverTLVData;
   

    public FSCheckBarcode() {
    }

    public final int getCode() {
        return 0xFF61;
    }

    public final String getText() {
        return "Check item barcode";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        MethodParameter.checkRange(itemStatus, 1, 15, "itemStatus");
        MethodParameter.checkRange(checkMode, 0, 2, "checkMode");

        out.writeInt(password);
        out.writeByte(itemStatus);
        out.writeByte(checkMode);
        out.writeByte(mcData.length);
        out.writeByte(tlv.length);
        out.writeBytes(mcData);
        out.writeBytes(tlv);
    }

    public final void decode(CommandInputStream in) throws Exception {
        localCheckStatus = in.readByte();
        localErrorCode = in.readByte();
        symbolicType = in.readByte();
        int paramLen = in.readByte();
        if (paramLen > 0)
        {
            serverErrorCode = in.readByte();
            serverCheckStatus = in.readByte();
            serverTLVData =  in.readBytesToEnd();
        }
    }

    public String getServerResultCodeText(int serverResultCode) {
        switch (serverResultCode) {
            case FS_SERVER_CIM_NOTFOUND:
                return "КИЗ отсутствует в базе Серверы СКЗКМ или КИЗ отсутствует в базе ИСМ";
            case FS_SERVER_CIM_INCORRECT_FORMAT:
                return "Не корректен формат КИЗ";
            case FS_SERVER_CIM_INCORRECT_CRC:
                return "Криптографическая проверка КПКИЗ дала отрицательный результат";
            case FS_SERVER_CIM_INCORRECT_STATUS:
                return "КИЗ имеет в базе Серверы СКЗКМ статус не совместимый с запрашиваемым";
            case FS_SERVER_CIM_ATTACH_ERROR:
                return "В списке вложения обнаружены ошибки";
            default:
                return "Неизвестный код ошибки";
        }
    }

/*    
    public boolean isCorrect() throws Exception {
        if (localResultCode == FSCheckBarcode.FS_LOCAL_RESULT_CODE_INVALID) {
            return false;
        }
        if (processingCode == 0) {
            if (salePermission != FSCheckBarcode.FS_SALE_PERMISSION_OK) {
                return false;
            }
            if (serverResultCode != FSCheckBarcode.FS_SERVER_OK) {
                return false;
            }
        }
        return true;
    }

    public void checkCorrect() throws Exception {
        if (localResultCode == FSCheckBarcode.FS_LOCAL_RESULT_CODE_INVALID) {
            throw new Exception("Barcode is not valid");
        }
        if (processingCode == 0) {
            if (salePermission != FSCheckBarcode.FS_SALE_PERMISSION_OK) {
                throw new Exception("Item is forbidden to sold");
            }
            if (serverResultCode != FSCheckBarcode.FS_SERVER_OK) {
                throw new Exception(getServerResultCodeText());
            }
        }
    }
*/
}
