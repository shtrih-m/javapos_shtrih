package com.shtrih.fiscalprinter.command;

import com.shtrih.util.MethodParameter;

/**
 * @author P.Zhirkov
 */
/**
 * **************************************************************************
    Получить случайную последовательность
        Код команды 	FF65h. Длина сообщения: 6 байт.
        Пароль оператора (4 байта)
    Ответ: 		FF65h. Длина сообщения: 19 байт.
        Код ошибки (1 байт)
        Данные (16 байт)
 ***************************************************************************
 */
public class FSReadRandomData extends PrinterCommand 
{
    // in 
    public int password;
    // out
    public byte[] randomData;

    public FSReadRandomData() {
    }

    public final int getCode() {
        return 0xFF65;
    }

    public final String getText() {
        return "FS Read random data";
    }

    public final void encode(CommandOutputStream out) throws Exception 
    {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception 
    {
        randomData = in.readBytes(16);
    }

}
