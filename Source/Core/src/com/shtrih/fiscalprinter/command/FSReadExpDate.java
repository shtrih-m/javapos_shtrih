package com.shtrih.fiscalprinter.command;

/**
 * Запрос срока действия ФН
 *  Код команды FF03h . Длина сообщения: 6 байт.
 *  Пароль системного администратора: 4 байт
 * Ответ:	FF03h Длина сообщения: 4 байт.
 *  Код ошибки (1 байт)
 *  Срок действия ФН (3 байт) BCD Год, Месяц, День
 *  Кол-во оставшихся отчетов о перерегистрации (1 байт)
 *  Выполнено отчетов о перерегистрации (1 байт)
 */
public class FSReadExpDate extends PrinterCommand {

    /**
     * Пароль системного администратора
     */
    public int getSysPassword() {
        return sysPassword;
    }

    /**
     * @param sysPassword Пароль системного администратора
     */
    public void setSysPassword(int sysPassword) {
        this.sysPassword = sysPassword;
    }

    // in
    private int sysPassword = 0; // System sdministrator password (4 bytes)
    // out
    private PrinterDate date; // Serial number
    private int remainingRegistrationsCount;
    private int registrationsCount;

    public FSReadExpDate() {
    }

    public final int getCode() {
        return 0xFF03;
    }

    public final String getText() {
        return "Fiscal storage: read expiration date";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
    }

    public void decode(CommandInputStream in) throws Exception {
        date = in.readDateYMD();
        remainingRegistrationsCount  = in.readByte();
        registrationsCount  = in.readByte();
    }

    /**
     * Срок действия ФН
     */
    public PrinterDate getDate() {
        return date;
    }

    /**
     * Кол-во оставшихся отчетов о перерегистрации
     */
    public int getRemainingRegistrationsCount(){
        return remainingRegistrationsCount;
    }

    /**
     * Выполнено отчетов о перерегистрации
     */
    public int getRegistrationsCount(){
        return registrationsCount;
    }
}
