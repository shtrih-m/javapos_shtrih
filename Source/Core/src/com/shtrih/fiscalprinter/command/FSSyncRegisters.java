package com.shtrih.fiscalprinter.command;

import com.shtrih.util.MethodParameter;

/**
 * @author P.Zhirkov
 */
/**
 * **************************************************************************
    Синхронизировать регистры со счётчиком ФН
        Код команды 	FF62h. Длина сообщения: 6 байт.
        Пароль системного администратора (4 байта)
    Ответ: 		FF62h. Длина сообщения: 3 байта.
        Код ошибки (1 байт)
 *
 ***************************************************************************
 */
public class FSSyncRegisters extends PrinterCommand 
{
    // in 
    public int password;

    public FSSyncRegisters() {
    }

    public final int getCode() {
        return 0xFF62;
    }

    public final String getText() {
        return "FS Synchronize registers";
    }

    public final void encode(CommandOutputStream out) throws Exception 
    {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
    }

}
