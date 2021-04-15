package com.shtrih.fiscalprinter.command;

import com.shtrih.util.MethodParameter;

/**
 * @author P.Zhirkov
 */
/**
 * **************************************************************************
    Запрос ресурса свободной памяти в ФН
        Код команды 	FF63h. Длина сообщения: 6 байт.
        Пароль системного администратора (4 байта)
    Ответ: 		FF63h. Длина сообщения: 11 байт.
        Код ошибки (1 байт)
        Ресурс данных 5 летнего хранения 1 (4 байта)
        Ресурс данных 30 дневного хранения 2 (4 байта)
        
Примечание:
1 - Ориентировочное количество документов, которые можно создать в ФН.
2 - Размер свободной области (в килобайтах) для записи документов 30 дней хранения. После 30 дней работы значение может колебаться на постоянном уровне.
 *
 ***************************************************************************
 */
public class FSReadMemorySize extends PrinterCommand 
{
    // in 
    public int password;
    // out
    public int maxDocCount;
    public int memSizeInKb;

    public FSReadMemorySize() {
    }

    public final int getCode() {
        return 0xFF63;
    }

    public final String getText() {
        return "FS Read memory size";
    }

    public final void encode(CommandOutputStream out) throws Exception 
    {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception 
    {
        maxDocCount = in.readInt();
        memSizeInKb = in.readInt();
    }

}
