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
 * - Статус локальной проверки: 1 байт
 * - Причина, по которой не была проведена локальная проверка: 1 байт
 * - Распознанный тип КМ: 1 байт
 * - Длина дополнительных параметров: 1 байт
 * - Код ответа ФН на команду онлайн-проверки: 1 байт
 * - Результат проверки КМ: 1 байт
 * - Список реквизитов ответа сервера: X байт
 *
 ***************************************************************************
 */
public class FSCheckMC extends PrinterCommand {

    ////////////////////////////////////////////////////////////////////////////
    // Local error code
    // 0 – КМ проверен в ФН
    public static final int FS_LEC_OK = 0;
    // 1 – КМ данного типа не подлежит проверки в ФН 
    public static final int FS_LEC_NOT_CHECK_ABLE = 1;
    // 2 – ФН не содержит ключ проверки кода проверки этого КМ 
    public static final int FS_LEC_FS_HAS_NO_KEY = 2;
    // 3 – Проверка невозможна, так как отсутствуют идентификаторы применения GS1 91 и / или 92 или их формат неверный. 
    public static final int FS_LEC_MC_FORMAT_ERROR = 3;
    // 4 – Проверка КМ в ФН невозможна по иной причине 
    public static final int FS_LEC_CHECK_FAILED = 4;

    ////////////////////////////////////////////////////////////////////////////
    // ItemStatus codes, 2003 tag
    // Штучный товар, реализован
    public static final int FS_ITEM_STATUS_PIECE_SELL = 1;
    // Мерный товар, реализован
    public static final int FS_ITEM_STATUS_WEIGHT_SELL = 2;
    // Штучный товар, возвращен
    public static final int FS_ITEM_STATUS_PIECE_RETURN = 3;
    // Мерный товар, возвращен
    public static final int FS_ITEM_STATUS_WEIGHT_RETURN = 4;
    // Статус товара не изменился
    public static final int FS_ITEM_STATUS_NOCHANGE = 255;

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

    public FSCheckMC() {
    }

    public final int getCode() {
        return 0xFF61;
    }

    public final String getText() {
        return "Check item barcode";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        MethodParameter.checkRange(itemStatus, 0, 0xFF, "itemStatus");
        MethodParameter.checkRange(checkMode, 0, 0xFF, "checkMode");
        MethodParameter.checkNotNull(mcData, "mcData");
        MethodParameter.checkNotNull(tlv, "tlv");

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
        if (paramLen > 0) {
            serverCheckStatus = in.readByte();
            serverErrorCode = in.readByte();
            serverTLVData = in.readBytesToEnd();
        }
    }

    public String getServerErrorCodeText() {
        switch (serverErrorCode) {
            case 1:
                return "Неверный фискальный признак ответа";
            case 2:
                return "Неверный формат реквизиов ответа";
            case 3:
                return "Неверный номер запроса в ответе";
            case 4:
                return "Неверный номер ФН";
            case 5:
                return "Неверный CRC блока данных";
            case 7:
                return "Неверная длина ответа";
            default:
                return "Неизвестный код ошибки";
        }
    }
}
