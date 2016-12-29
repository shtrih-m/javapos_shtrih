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
Операция со скидками, надбавками и налогом FF44H
Код команды FF44h . Длина сообщения:  254 байт.
Пароль системного администратора: 4 байта
Тип операции: 1 байт 
1 – Приход, 
2 – Возврат прихода,
3 – Расход,
4 – Возврат расхода 
Количество: 5 байт 0000000000…9999999999
Цена:             5 байт 0000000000…9999999999
Скидка:         5 байт 0000000000…9999999999
Надбавка:    5 байт 0000000000…9999999999
Налог:           5 байт 0000000000…9999999999
Номер отдела: 1 байт
0…16 – режим свободной продажи, 255 – режим продажи по коду товара
Налог:  1 байт
Бит 1 «0» – нет, «1» – 1 налоговая группа
Бит 2 «0» – нет, «1» – 2 налоговая группа
Бит 3 «0» – нет, «1» – 3 налоговая группа
Бит 4 «0» – нет, «1» – 4 налоговая группа
Штрих-код: 5 байт  000000000000…999999999999
Текст: 215 байта строка - название товара и скидки 
Примечание: если строка начинается символами \\ то она передаётся на сервер ОФД но не печатается на кассе. Названия товара и скидки должны заканчиваться нулём (Нуль терминированные строки).
Примечание: налог является справочной информацией и передаётся извне в случае, когда касса не может его рассчитать сама.


Ответ:    FF43h Длина сообщения: 1 байт.
Код ошибки: 1 байт

 */
public class FSSale2 extends PrinterCommand {

    // in
    private int sysPassword; // System sdministrator password (4 bytes)
    private int operation;
    private long quantity;
    private long price;
    private long discount;
    private long charge;
    private long taxAmount;
    private int department;
    private int tax;
    private long barcode;
    private String text;

    public FSSale2() {
    }

    public final int getCode() {
        return 0xFF44;
    }

    public final String getText() {
        return "Fiscal storage: sale with tax";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(sysPassword);
        out.writeByte(operation);
        out.writeLong(quantity, 5);
        out.writeLong(price, 5);
        out.writeLong(discount, 5);
        out.writeLong(charge, 5);
        out.writeLong(taxAmount, 5);
        out.writeByte(department);
        out.writeByte(tax);
        out.writeLong(barcode, 5);
        out.writeString(text, 0);
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

    /**
     * @return the operation
     */
    public int getOperation() {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(int operation) {
        this.operation = operation;
    }

    /**
     * @return the quantity
     */
    public long getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the price
     */
    public long getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(long price) {
        this.price = price;
    }

    /**
     * @return the discount
     */
    public long getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(long discount) {
        this.discount = discount;
    }

    /**
     * @return the charge
     */
    public long getCharge() {
        return charge;
    }

    /**
     * @param charge the charge to set
     */
    public void setCharge(long charge) {
        this.charge = charge;
    }

    /**
     * @return the taxAmount
     */
    public long getTaxAmount() {
        return taxAmount;
    }

    /**
     * @param taxAmount the taxAmount to set
     */
    public void setTaxAmount(long taxAmount) {
        this.taxAmount = taxAmount;
    }

    /**
     * @return the department
     */
    public int getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(int department) {
        this.department = department;
    }

    /**
     * @return the tax
     */
    public int getTax() {
        return tax;
    }

    /**
     * @param tax the tax to set
     */
    public void setTax(int tax) {
        this.tax = tax;
    }

    /**
     * @return the barcode
     */
    public long getBarcode() {
        return barcode;
    }

    /**
     * @param barcode the barcode to set
     */
    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

}
