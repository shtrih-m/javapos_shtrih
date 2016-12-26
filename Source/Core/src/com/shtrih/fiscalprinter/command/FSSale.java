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
 * Операция со скидками и надбавками
 * Код команды FF0Dh . Длина сообщения: 98 байт.
 * Пароль системного администратора: 4 байта
 * Тип операции ( 1 байт)
 * 1 – Приход,
 * 2 – Возврат прихода,
 * 3 – Расход,
 * 4 – Возврат расхода
 * Количество (5 байт) 0000000000…9999999999
 * Цена (5 байт) 0000000000…9999999999
 * Скидка (5 байт) 0000000000…9999999999
 * Надбавка (5 байт) 0000000000…9999999999
 * Номер отдела (1 байт)
 * 0…16 – режим свободной продажи, 255 – режим продажи по коду товара
 * Налог (1 байт)
 * Бит 1 «0» – нет, «1» – 1 налоговая группа
 * Бит 2 «0» – нет, «1» – 2 налоговая группа
 * Бит 3 «0» – нет, «1» – 3 налоговая группа
 * Бит 4 «0» – нет, «1» – 4 налоговая группа
 * Штрих-код (5 байт) 000000000000…999999999999
 * Текст (64 байта) строка названия товара
 * Ответ: FF0Dh Длина сообщения 1 байт.
 * Код ошибки (1 байт)
 *
 *
 */
public class FSSale extends PrinterCommand {

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

    // in
    private int sysPassword; // System sdministrator password (4 bytes)
    private int operation;
    private long quantity;
    private long price;
    private long discount;
    private long charge;
    private int department;
    private int tax;
    private long barcode;
    private String text;

    public FSSale() {
    }

    public final int getCode() {
        return 0xFF0D;
    }

    public final String getText() {
        return "Fiscal storage: sale";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
        out.writeByte(operation);
        out.writeLong(quantity, 5);
        out.writeLong(price, 5);
        out.writeLong(discount, 5);
        out.writeLong(charge, 5);
        out.writeByte(department);
        out.writeByte(tax);
        out.writeLong(barcode, 5);
        out.writeString(text, 0);
    }

    public void decode(CommandInputStream in) throws Exception {
    }

}
