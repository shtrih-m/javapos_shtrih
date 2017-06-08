package com.shtrih.fiscalprinter.command;

/**
 * Найти фискальный документ по номеру
 * Код команды FF0Ah . Длина сообщения: 10 байт.
 * Пароль системного администратора: 4 байт
 * Номер фискального документа: 4 байт
 * Ответ: FF0Аh Длина сообщения 3+N байт.
 * Код ошибки (1 байт)
 * Тип фискального документа ( 1 байт)
 * Получена ли квитанция из ОФД. ( 1 байт)
 * 1- да
 * 0 -нет
 * Данные фискального документа (N байт) в зависимости от типа документа
 */
public class FSFindDocument extends PrinterCommand {

    // in
    private final int sysPassword; // System sdministrator password (4 bytes)
    private final long docNumber;
    // out
    private FSDocumentInfo document;

    public FSFindDocument(int sysPassword, long docNumber) {
        this.sysPassword = sysPassword;
        this.docNumber = docNumber;
    }

    public final int getCode() {
        return 0xFF0A;
    }

    public final String getText() {
        return "Fiscal storage: find document";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(sysPassword);
        out.writeLong(docNumber, 4);
    }

    public void decode(CommandInputStream in) throws Exception {
        document = new FSDocumentInfo(in);
    }

    public FSDocumentInfo getDocument() {
        return document;
    }
}