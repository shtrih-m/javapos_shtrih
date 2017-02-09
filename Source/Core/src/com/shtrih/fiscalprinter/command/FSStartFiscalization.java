package com.shtrih.fiscalprinter.command;

/**
 * Начать отчет о регистрации ККТ
 * Код команды FF05h. Длина сообщения: 7 байт.
 * Пароль системного администратора: 4 байта
 * Тип отчета: 1 байт
 * 00 – Отчет о регистрации КТТ
 * 01 – Отчет об изменении параметров регистрации ККТ, в связи с заменой ФН
 * 02 – Отчет об изменении параметров регистрации ККТ без замены ФН
 * Ответ: FF05h. Длина сообщения: 1 байт.
 * Код ошибки: 1 байт
 */
public class FSStartFiscalization extends PrinterCommand {

    // in
    private final int sysPassword; // System sdministrator password (4 bytes)
    private final int reportType;

    public int getSysPassword() {
        return sysPassword;
    }

    public int getReportType() {
        return reportType;
    }

    public FSStartFiscalization(int sysPassword, int reportType) {
        this.sysPassword = sysPassword;
        this.reportType = reportType;
    }

    public final int getCode() {
        return 0xFF05;
    }

    public final String getText() {
        return "Fiscal storage: start fiscalization";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(sysPassword);
        out.writeByte(reportType);
    }

    public void decode(CommandInputStream in) throws Exception {
    }

}
