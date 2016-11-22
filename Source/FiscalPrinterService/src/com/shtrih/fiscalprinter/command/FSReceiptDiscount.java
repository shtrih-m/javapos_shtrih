/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author Admin
 */


/**
Скидка, надбавка  на чек для Роснефти FF4BH
  Код команды FF4Bh . Длина сообщения:  145 байт.
  Пароль системного администратора: 4 байта
  Скидка:         5 байт
  Надбавка:    5 байт
  Налог:  1 байт
  Описание скидки или надбавки: 128 байт ASCII
Ответ:    FF4Bh Длина сообщения: 1 байт.
  Код ошибки: 1 байт

**/


public class FSReceiptDiscount extends PrinterCommand {

        // in
    private int sysPassword; // System sdministrator password (4 bytes)
    private long discount = 0;
    private long charge = 0;
    private int tax1 = 0;
    private String name = "";

    public FSReceiptDiscount(){
    }
    
    public final int getCode() {
        return 0xFF4B;
    }

    public final String getText() {
        return "Receipt discount";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
        out.writeLong(getDiscount(), 5);
        out.writeLong(getCharge(), 5);
        out.writeByte(getTax1());
        out.writeString(getName(), 40);
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
     * @param text the text to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
}
