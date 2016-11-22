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
Закрытие чека расширенное вариант №2 FF45H
Код команды FF45h . Длина сообщения:  131 байт.
Пароль системного администратора: 4 байта
Сумма наличных (5 байт) 0000000000…9999999999 
Сумма типа оплаты 2 (5 байт) 0000000000…9999999999 
Сумма типа оплаты 3 (5 байт) 0000000000…9999999999 
Сумма типа оплаты 4 (5 байт) 0000000000…9999999999
Сумма типа оплаты 5 (5 байт) 0000000000…9999999999
Сумма типа оплаты 6 (5 байт) 0000000000…9999999999 
Сумма типа оплаты 7 (5 байт) 0000000000…9999999999 
Сумма типа оплаты 8 (5 байт) 0000000000…9999999999
Сумма типа оплаты 9 (5 байт) 0000000000…9999999999
Сумма типа оплаты 10 (5 байт) 0000000000…9999999999 
Сумма типа оплаты 11 (5 байт) 0000000000…9999999999 
Сумма типа оплаты 12 (5 байт) 0000000000…9999999999
Сумма типа оплаты 13 (5 байт) 0000000000…9999999999
Сумма типа оплаты 14 (5 байт) 0000000000…9999999999 
Сумма типа оплаты 15 (5 байт) 0000000000…9999999999 
Сумма типа оплаты 16 (5 байт) 0000000000…9999999999
Скидка/Надбавка(в случае отрицательного значения) в % на чек от 0 до 99,99 % (2 байта со знаком) -9999…9999 
Налог 1 (1 байт) «0» – нет, «1»…«4» – налоговая группа 
Налог 2 (1 байт) «0» – нет, «1»…«4» – налоговая группа 
Налог 3 (1 байт) «0» – нет, «1»…«4» – налоговая группа 
Налог 4 (1 байт) «0» – нет, «1»…«4» – налоговая группа 
Система налогообложения ( 1 байт) 
Текст (40 байт) 

Ответ:    FF45h Длина сообщения: 6 байт.
Код ошибки: 1 байт
Сдача ( 5 байт) 0000000000…9999999999
*/

public class FSCloseReceipt extends PrinterCommand {

    // in
    private int sysPassword; // System sdministrator password (4 bytes)
    private long[] payments = new long[16];
    private int discount = 0;
    private int tax1 = 0;
    private int tax2 = 0;
    private int tax3 = 0;
    private int tax4 = 0;
    private int taxSystem = 0;
    private String text = "";
    // out
    private long change;

    public FSCloseReceipt() {
    }

    public final int getCode() {
        return 0xFF45;
    }

    public final String getText() {
        return "Fiscal storage: close receipt";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(sysPassword);
        for (int i=0;i<payments.length;i++){
            out.writeLong(payments[i], i);
        }
        out.writeShort(discount);
        out.writeByte(tax1);
        out.writeByte(tax2);
        out.writeByte(tax3);
        out.writeByte(tax4);
        out.writeByte(taxSystem);
        out.writeString(text, 40);
    }

    public void decode(CommandInputStream in) throws Exception {
        change = in.readLong(5);
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
    public void setPayments(long[] payments) {
        this.payments = payments;
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
     * @return the tax1
     */
    public int getTax1() {
        return tax1;
    }

    /**
     * @param tax1 the tax1 to set
     */
    public void setTax1(int tax1) {
        this.tax1 = tax1;
    }

    /**
     * @return the tax2
     */
    public int getTax2() {
        return tax2;
    }

    /**
     * @param tax2 the tax2 to set
     */
    public void setTax2(int tax2) {
        this.tax2 = tax2;
    }

    /**
     * @return the tax3
     */
    public int getTax3() {
        return tax3;
    }

    /**
     * @param tax3 the tax3 to set
     */
    public void setTax3(int tax3) {
        this.tax3 = tax3;
    }

    /**
     * @return the tax4
     */
    public int getTax4() {
        return tax4;
    }

    /**
     * @param tax4 the tax4 to set
     */
    public void setTax4(int tax4) {
        this.tax4 = tax4;
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

} 
