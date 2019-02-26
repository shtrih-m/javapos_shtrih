
package com.shtrih.fiscalprinter.command;

/**
Передача в ФН  TLV из буфера FF64H
Код команды FF64h. Длина сообщения: 6 байт.
Пароль системного администратора: 4 байта
Ответ: FF64h Длина сообщения: 1 байт.
Код ошибки: 1 байт
 */

public class FSWriteTLVBuffer extends PrinterCommand {

    // in
    private int sysPassword = 0; // System sdministrator password (4 bytes)
    
    public FSWriteTLVBuffer() {
    }

    public final int getCode() {
        return 0xFF64;
    }

    public final String getText() {
        return "Fiscal storage: write TLV data from buffer";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
    }

    public void decode(CommandInputStream in) throws Exception {
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
}

