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
    Привязка  маркированного товара к позиции FF67H
    Код команды FF67h. Длина сообщения: 5+N байт.
    Пароль оператора: 4 байта
    Длина кода маркировки: 1 байт
    Данные маркировки N байт
    Данная команда должна вызываться после привязки всех тегов к предмету расчета.

    Ответ: FF67h	    Длина сообщения: 4 байт.
    Код ошибки: 1 байт
    первые 2 байта значения реквизита "код товара”: 2 байта,
    Тип Data Matrix:1 байт.
    0 – КМ 88,
    1-КМ симметричный,
    2-КМ Табачный,
    3-КМ 44.
****************************************************************************/

public final class FSBindMC extends PrinterCommand {
    // in
    public int password;
    public byte[] data; 
    // out
    public int itemCode;
    public int codeType;
    public int localCheckStatus;
    public int localErrorCode;
    public int symbolicType;
    public int serverErrorCode;
    public int serverCheckStatus;
    public byte[] serverTLVData;

    /*
     * Creates a new instance of ConfirmDate
     */
    public FSBindMC() {
        super();
    }

    public final int getCode() {
        return 0xFF67;
    }

    public final String getText() {
        return "FS: Bind marking code";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(data.length);
        out.writeBytes(data);
    }

    public final void decode(CommandInputStream in) throws Exception {
        itemCode = in.readShort();
        codeType = in.readByte();
        if (in.size() >= 4){
            localCheckStatus = in.readByte();
            localErrorCode = in.readByte();
            symbolicType = in.readByte();
            int paramLen = in.readByte();
            if (paramLen > 0)
            {
                if ((paramLen > 0)&&(in.size() > 7)){
                    serverErrorCode = in.readByte();
                }
                if ((paramLen > 1)&&(in.size() > 8)){
                    serverCheckStatus = in.readByte();
                }
                if ((paramLen > 2)&&(in.size() > 9)){
                    serverTLVData =  in.readBytesToEnd();
                }
            }
        }
   }
   
    
}
