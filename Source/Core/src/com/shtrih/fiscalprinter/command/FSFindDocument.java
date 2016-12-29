/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import com.shtrih.ej.EJDate;
import com.shtrih.util.BitUtils;

/**
 *
 * @author V.Kravtsov
 */
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
 *
 *
 */
public class FSFindDocument extends PrinterCommand {

    /**
     * @return the sysPassword
     */
    public int getSysPassword() {
        return sysPassword;
    }

    /**
     * @param sysPassword the sysPassword to set
     */
    public void setSysPassword(int sysPassword) {
        this.sysPassword = sysPassword;
    }

    // in
    private int sysPassword; // System sdministrator password (4 bytes)
    private long docNumber;
    // out
    private FSDocType docType;
    private boolean isTicketReceived;
    private String document;

    public FSFindDocument() {
    }

    public final int getCode() {
        return 0xFF0A;
    }

    public final String getText() {
        return "Fiscal storage: find document";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
        out.writeLong(getDocNumber(), 4);
    }

    public void decode(CommandInputStream in) throws Exception {
        setDocType(new FSDocType(in.readByte()));
        setIsTicketReceived(in.readByte() != 0);
        setDocument(in.readString());
    }

    /**
     * @return the docNumber
     */
    public long getDocNumber() {
        return docNumber;
    }

    /**
     * @param docNumber the docNumber to set
     */
    public void setDocNumber(long docNumber) {
        this.docNumber = docNumber;
    }

    /**
     * @return the docType
     */
    public FSDocType getDocType() {
        return docType;
    }

    /**
     * @param docType the docType to set
     */
    public void setDocType(FSDocType docType) {
        this.docType = docType;
    }

    /**
     * @return the isTicketReceived
     */
    public boolean isIsTicketReceived() {
        return isTicketReceived;
    }

    /**
     * @param isTicketReceived the isTicketReceived to set
     */
    public void setIsTicketReceived(boolean isTicketReceived) {
        this.isTicketReceived = isTicketReceived;
    }

    /**
     * @return the document
     */
    public String getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    public void setDocument(String document) {
        this.document = document;
    }

}
