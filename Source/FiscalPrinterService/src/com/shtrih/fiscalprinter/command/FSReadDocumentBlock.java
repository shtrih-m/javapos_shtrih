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
Чтение TLV фискального документа FF3BH
Код команды FF3Bh . Длина сообщения: 6 байт.
Пароль системного администратора: 4 байта
Ответ:	    FF3Bh Длина сообщения: 1+N байт.
Код ошибки:1 байт
TLV структура: N байт 

 */
public class FSReadDocumentBlock extends PrinterCommand {

    private int sysPassword; // System sdministrator password (4 bytes)
    // out
    private byte[] data;

    public FSReadDocumentBlock() {
    }

    public final int getCode() {
        return 0xFF3B;
    }

    public final String getText() {
        return "Fiscal storage: read document block";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
    }

    public void decode(CommandInputStream in) throws Exception {
        setData(in.readBytes(in.getSize()));
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
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

}
