package com.shtrih.fiscalprinter.command;

import com.shtrih.util.MethodParameter;

/**
 * @author P.Zhirkov
 */
/**
 * **************************************************************************
    Авторизоваться
        Код команды 	FF66h. Длина сообщения: 22 байта.
        Пароль оператора (4 байта)
        Данные для авторизации (16 байт)
    Ответ: 		FF66h. Длина сообщения: 3 байта.
        Код ошибки (1 байт)
 ***************************************************************************
 */

public class FSAuthorize extends PrinterCommand 
{
    // in 
    public int password;
    public byte[] data;

    public FSAuthorize() {
    }

    public final int getCode() {
        return 0xFF66;
    }

    public final String getText() {
        return "FS Read random data";
    }

    public final void encode(CommandOutputStream out) throws Exception 
    {
        out.writeInt(password);
        out.writeBytes(data);
    }

    public final void decode(CommandInputStream in) throws Exception 
    {
    }

}
