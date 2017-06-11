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

/*
Операция V2 FF46H
Код команды FF46h . Длина сообщения:  160 байта.
Пароль: 4 байта
Тип операции: 1 байт 
1 – Приход, 
2 – Возврат прихода,
3 – Расход,
4 – Возврат расхода 
Количество: 6 байт ( 6 знаков после запятой )
Цена:             5 байт 
Сумма операции 5 байт *
Налог:           5 байт **
Налоговая ставка:  1 байт 
Номер отдела: 1 байт
0…16 – режим свободной продажи, 255 – режим продажи по коду товара
Признак способа расчёта : 1 байт
Признак предмета расчёта: 1 байт
Наименование товара: 0-128 байт ASCII  

_____________________________________________________________
Примечание: если строка начинается символами // то она передаётся 
на сервер ОФД но не печатается на кассе.
 * если сумма операции 0xffffffffff то сумма операции  рассчитывается кассой 
как цена х количество,  в противном случае сумма операции берётся из команды 
и не должна отличаться более чем на +-1 коп от рассчитанной кассой.
**. В режиме начисления налогов 1 ( 1 Таблица) налоги на позицию и на чек 
должны передаваться из верхнего ПО. Если в сумме налога на позицию передать 
0xFFFFFFFFFF то считается что сумма налога на позицию не указана, в противном 
случае сумма налога учитывается ФР и передаётся в ОФД. Для налогов 3 и 4 сумма 
налога всегда считается равной нулю и в ОФД не передаётся. 

Ответ:    FF46h Длина сообщения: 1 байт.
Код ошибки: 1 байт

*/

public class FSPrintRecItem extends PrinterCommand
{
    private int password;
    private FSReceiptItem item;
    

    public final int getCode() {
        return 0xFF46;
    }

    public final String getText() {
        return "FS: Print receipt item";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(item.getOperation());
        out.writeLong(item.getQuantity(), 6);
        out.writeLong(item.getPrice(), 5);
        out.writeLong(item.getAmount(), 5);
        out.writeLong(item.getTaxAmount(), 5);
        out.writeByte(item.getTax());
        out.writeByte(item.getDepartment());
        out.writeByte(item.getPaymentType());
        out.writeByte(item.getPaymentItem());
        String text = item.getText();
        int len = Math.min(text.length(), 128);
        out.writeString(text.substring(0, len), PrinterConst.MIN_TEXT_LENGTH);
    }

    public final void decode(CommandInputStream in) throws Exception {
    }

    /**
     * @return the password
     */
    public int getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(int password) {
        this.password = password;
    }

    /**
     * @return the item
     */
    public FSReceiptItem getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(FSReceiptItem item) {
        this.item = item;
    }


}
