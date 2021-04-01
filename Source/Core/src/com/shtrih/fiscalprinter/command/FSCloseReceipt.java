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
/**
 * Закрытие чека расширенное вариант V2 FF45H Код команды FF45H. Длина
 * сообщения: 182 байт. Пароль системного администратора: 4 байта Сумма наличных
 * (5 байт) Сумма типа оплаты 2 (5 байт) Сумма типа оплаты 3 (5 байт) Сумма типа
 * оплаты 4 (5 байт) Сумма типа оплаты 5 (5 байт) Сумма типа оплаты 6 (5 байт)
 * Сумма типа оплаты 7 (5 байт) Сумма типа оплаты 8 (5 байт) Сумма типа оплаты 9
 * (5 байт) Сумма типа оплаты 10 (5 байт) Сумма типа оплаты 11 (5 байт) Сумма
 * типа оплаты 12 (5 байт) Сумма типа оплаты 13 (5 байт) Сумма типа оплаты 14 (5
 * байт) (предоплата) Сумма типа оплаты 15 (5 байт) (постоплата) Сумма типа
 * оплаты 16 (5 байт) (встречное представление) Округление до рубля в копейках
 * (1 байт) Налог 1 (5 байт) (НДС 20%) Налог 2 (5 байт) (НДС 10%) Оборот по
 * налогу 3 (5 байт) (НДС 0%) Оборот по налогу 4 (5 байт) (Без НДС) Налог 5 (5
 * байт) (НДС расч. 18/118) Налог 6 (5 байт) (НДС расч. 10/110) Система
 * налогообложения(1 байт) Текст (0-64 байт)
 * _______________________________________________________ Примечания: Типы
 * оплаты 2-13 при передаче в ОФД суммируются и передаются как оплата
 * «ЭЛЕКТРОННЫМИ». В режиме начисления налогов 0 ( 1 Таблица) касса рассчитывает
 * налоги самостоятельно исходя из проведенных в документе операций и налоги
 * переданные в команде игнорируются. В режиме начисления налогов 1 налоги
 * должны быть обязательно переданы из верхнего ПО.
 *
 * Ответ: FF45h Длина сообщения: 14 байт. Код ошибки: 1 байт Сдача ( 5 байт)
 * Номер ФД :4 байта Фискальный признак: 4 байта Дата и время: 5 байт DATE_TIME3
 */
public class FSCloseReceipt extends PrinterCommand {

    // in
    private int sysPassword; // System sdministrator password (4 bytes)
    private long[] payments = new long[16];
    private int discount = 0;
    private long[] taxValues = new long[6];
    private int taxSystem = 0;
    private String text = "";
    // out
    private long change;
    private long docNum;
    private long docMAC;
    private PrinterDate docDate;
    private PrinterTime docTime;

    public FSCloseReceipt() {
        for (int i = 0; i < payments.length; i++) {
            payments[i] = 0;
        }
    }

    public final int getCode() {
        return 0xFF45;
    }

    public final String getText() {
        return "Fiscal storage: close receipt V2";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(sysPassword);
        for (int i = 0; i < payments.length; i++) {
            out.writeLong(payments[i], 5);
        }
        out.writeByte(discount);
        for (int i = 0; i < taxValues.length; i++) {
            out.writeLong(taxValues[i], 5);
        }
        out.writeByte(taxSystem);
        out.writeString(text, 0);
    }

    public void decode(CommandInputStream in) throws Exception {
        docDate = null;
        docTime = null;
        change = in.readLong(5);
        docNum = in.readLong(4);
        docMAC = in.readLong(4);
        if (in.size() >= 5) {
            docDate = in.readDateYMD();
            docTime = in.readTimeHM();
        }
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
     * @return the payments
     */
    public long[] getPayments() {
        return payments;
    }

    /**
     * @param payments the payments to set
     */
    public void setPayment(int index, long value) {
        this.payments[index] = value;
    }

    /**
     * @return the discount
     */
    public int getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(int discount) {
        this.discount = discount;
    }

    /**
     * @return the taxValues
     */
    public long[] getTaxValues() {
        return taxValues;
    }

    /**
     * @param taxValues the taxValues to set
     */
    public void setTaxValue(int index, long value) {
        this.taxValues[index] = value;
    }

    /**
     * @return the taxSystem
     */
    public int getTaxSystem() {
        return taxSystem;
    }

    /**
     * @param taxSystem the taxSystem to set
     */
    public void setTaxSystem(int taxSystem) {
        this.taxSystem = taxSystem;
    }

    public String getPrintableText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the change
     */
    public long getChange() {
        return change;
    }

    /**
     * @param change the change to set
     */
    public void setChange(long change) {
        this.change = change;
    }

    /**
     * @return the docNumber
     */
    public long getDocNum() {
        return docNum;
    }

    /**
     * @return the document MAC, Message Authentication Code
     */
    public long getDocMAC() {
        return docMAC;
    }

    public PrinterDate getDocDate() {
        return docDate;
    }

    public PrinterTime getDocTime() {
        return docTime;
    }
}
