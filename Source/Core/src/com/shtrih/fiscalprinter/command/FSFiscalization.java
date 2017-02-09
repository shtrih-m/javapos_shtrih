package com.shtrih.fiscalprinter.command;

/**
 * Сформировать отчёт о регистрации ККТ
 * Код команды FF06h. Длина сообщения: 40 байт.
 * Пароль системного администратора: 4 байта
 * ИНН : 12 байт ASCII
 * Регистрационный номер ККТ: 20 байт ASCII
 * Код налогообложения: 1 байт
 * Режим работы: 1 байт
 * Ответ: FF06h. Длина сообщения: 9 байт.
 * Код ошибки: 1 байт
 * Номер ФД: 4 байта
 * Фискальный признак: 4 байта
 */
public class FSFiscalization extends PrinterCommand {

    // in
    private final int sysPassword; // System sdministrator password (4 bytes)

    private final String taxID;
    private final String regID;
    private final int taxSystemCode;
    private final int operationMode;
    // out
    private long docNumber;
    private long macNumber;

    public FSFiscalization(int sysPassword, String taxID, String regID, int taxSystemCode, int operationMode) {
        this.sysPassword = sysPassword;
        this.taxID = taxID;
        this.regID = regID;
        this.taxSystemCode = taxSystemCode;
        this.operationMode = operationMode;
    }

    public final int getCode() {
        return 0xFF06;
    }

    public final String getText() {
        return "Fiscal storage: start fiscalization";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(sysPassword);
        out.writeString(taxID, 12);
        out.writeString(regID, 20);
        out.writeByte(taxSystemCode);
        out.writeByte(operationMode);
    }

    public void decode(CommandInputStream in) throws Exception {
        docNumber = in.readLong(4);
        macNumber = in.readLong(4);
    }

    public long getDocNumber() {
        return docNumber;
    }

    public long getMacNumber() {
        return macNumber;
    }
}
