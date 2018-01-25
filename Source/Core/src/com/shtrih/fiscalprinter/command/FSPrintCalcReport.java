package com.shtrih.fiscalprinter.command;

/**
Сформировать отчёт о состоянии расчётов FF38H
Код команды FF38h . Длина сообщения: 6 байт.
    Пароль системного администратора: 4 байта
Ответ:	    FF38h Длина сообщения: 16 байт.
    Код ошибки: 1 байт
    Номер ФД: 4 байта
    Фискальный признак: 4 байта
    Количество неподтверждённых документов: 4 байта
    Дата первого неподтверждённого документа: 3 байта ГГ,ММ,ДД
*/

public class FSPrintCalcReport extends PrinterCommand {

    // in
    private int sysPassword = 0; // System sdministrator password (4 bytes)
    // out
    private long documentNumber;
    private long documentDigest;
    private long documentCount;
    private PrinterDate documentDate;

    public FSPrintCalcReport(int sysPassword) {
        this.sysPassword = sysPassword;
    }

    public final int getCode() {
        return 0xFF38;
    }

    public final String getText() {
        return "Fiscal storage: print calculation report";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
    }

    public void decode(CommandInputStream in) throws Exception {
        documentNumber = in.readLong(4);
        documentDigest = in.readLong(4);
        documentCount = in.readLong(4);
        documentDate = in.readDateYMD();
    }

    /**
     * @return the sysPassword
     */
    public int getSysPassword() {
        return sysPassword;
    }

    /**
     * @return the documentNumber
     */
    public long getDocumentNumber() {
        return documentNumber;
    }

    /**
     * @return the documentDigest
     */
    public long getDocumentDigest() {
        return documentDigest;
    }

    /**
     * @return the documentCount
     */
    public long getDocumentCount() {
        return documentCount;
    }
    /**
     * @return the documentDate
     */
    public PrinterDate getDocumentDate() {
        return documentDate;
    }
}
