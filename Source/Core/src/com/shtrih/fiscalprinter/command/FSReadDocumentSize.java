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
 * Запрос общего размера данных документа в ФН
 *
 * Код команды FF75h. Длина сообщения: 14 байт. Пароль оператора (4 байта)
 * Ответ:	FF75h. Длина сообщения: 11 байт. Код ошибки (1 байт) Размер в байтах
 * текущего документа для ОФД ( 4 байта) Размер в байтах текущего уведомления о
 * реализации маркированных товаров для ОИСМ ( 4 байта)
 *
 **************************************************************************
 */
public final class FSReadDocumentSize extends PrinterCommand {

    // in
    public int password;
    // out
    public int documentNumber;
    public int noticeNumber;

    /**
     * Creates a new instance of ConfirmDate
     */
    public FSReadDocumentSize() {
        super();
    }

    public final int getCode() {
        return 0xFF74;
    }

    public final String getText() {
        return "FS: read documents size";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
        documentNumber = in.readInt();
        noticeNumber = in.readInt();
    }

}
