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
 * ќпераци€ со скидками и надбавками
 *  од команды FF0Dh . ƒлина сообщени€: 98 байт.
 * ѕароль системного администратора: 4 байта
 * “ип операции ( 1 байт)
 * 1 Ц ѕриход,
 * 2 Ц ¬озврат прихода,
 * 3 Ц –асход,
 * 4 Ц ¬озврат расхода
 *  оличество (5 байт) 0000000000Е9999999999
 * ÷ена (5 байт) 0000000000Е9999999999
 * —кидка (5 байт) 0000000000Е9999999999
 * Ќадбавка (5 байт) 0000000000Е9999999999
 * Ќомер отдела (1 байт)
 * 0Е16 Ц режим свободной продажи, 255 Ц режим продажи по коду товара
 * Ќалог (1 байт)
 * Ѕит 1 Ђ0ї Ц нет, Ђ1ї Ц 1 налогова€ группа
 * Ѕит 2 Ђ0ї Ц нет, Ђ1ї Ц 2 налогова€ группа
 * Ѕит 3 Ђ0ї Ц нет, Ђ1ї Ц 3 налогова€ группа
 * Ѕит 4 Ђ0ї Ц нет, Ђ1ї Ц 4 налогова€ группа
 * Ўтрих-код (5 байт) 000000000000Е999999999999
 * “екст (64 байта) строка названи€ товара
 * ќтвет: FF0Dh ƒлина сообщени€ 1 байт.
 *  од ошибки (1 байт)
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
