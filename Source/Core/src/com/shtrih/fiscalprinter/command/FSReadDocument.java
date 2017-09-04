package com.shtrih.fiscalprinter.command;

/**

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

    public FSReadDocument(int sysPassword, long docNumber) {
        this.sysPassword = sysPassword;
        this.docNumber = docNumber;
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
        docType = in.readShort();
        docSize = in.readShort();
    }

    /**
     * @return the sysPassword
     */
    public int getSysPassword() {
        return sysPassword;
    }

    /**
     * @return the docNumber
     */
    public long getDocNumber() {
        return docNumber;
    }

    /**
     * @return the docType
     */
    public int getDocType() {
        return docType;
    }

    /**
     * @return the docSize
     */
    public int getDocSize() {
        return docSize;
    }
}
    

