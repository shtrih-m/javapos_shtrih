/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */

/*

Запросить фискальный документ в TLV формате FF3AH
    Код команды FF3Аh . Длина сообщения: 10 байт.
    Пароль системного администратора: 4 байта
    Номер фискального документа: 4 байта
Ответ:	    FF3Аh Длина сообщения: 5 байт.
    Код ошибки: 1 байт
    Тип фискального документа: 2 байта STLV
    Длина фискального документа: 2 байта

*/

public class FSReadDocument extends PrinterCommand {

    private int sysPassword; // System sdministrator password (4 bytes)
    private long docNumber;
    // out
    private int docType;
    private int docSize;

    public FSReadDocument() {
    }

    public final int getCode() {
        return 0xFF3A;
    }

    public final String getText() {
        return "Fiscal storage: read document";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
        out.writeLong(getDocNumber(), 4);
    }

    public void decode(CommandInputStream in) throws Exception {
        setDocType(in.readShort());
        setDocSize(in.readShort());
    }

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
    public int getDocType() {
        return docType;
    }

    /**
     * @param docType the docType to set
     */
    public void setDocType(int docType) {
        this.docType = docType;
    }

    /**
     * @return the docSize
     */
    public int getDocSize() {
        return docSize;
    }

    /**
     * @param docSize the docSize to set
     */
    public void setDocSize(int docSize) {
        this.docSize = docSize;
    }
    
}
    

