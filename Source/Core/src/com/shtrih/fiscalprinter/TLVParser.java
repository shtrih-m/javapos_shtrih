/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

/**
 * @author V.Kravtsov
 */

import java.util.*;
import java.io.ByteArrayInputStream;

import com.shtrih.util.Hex;
import com.shtrih.util.BitUtils;
import com.shtrih.fiscalprinter.command.CommandInputStream;
import com.sun.deploy.util.OrderedHashSet;

public class TLVParser {

    private int level = 0;
    private final TLVTags tags = new TLVTags();
    private final TLVItems items = new TLVItems();

    public TLVParser() {
    }

    public TLVItems getItems() {
        return items;
    }

    public Vector<String> getPrintText() throws Exception {
        Vector<String> lines = new Vector<String>();
        for (int i = 0; i < items.size(); i++) {
            TLVItem item = items.get(i);

            // 1084	дополнительный реквизит пользователя
            if (item.getTag().getId() == 1084) {
                continue;
            }

            // 1085	наименование дополнительного реквизита пользователя
            if (item.getTag().getId() == 1085) {
                continue;
            }

            // 1086	значение дополнительного реквизита пользователя
            if (item.getTag().getId() == 1086) {
                continue;
            }

            // 1203	ИНН кассира
            if (item.getTag().getId() == 1203) {
                continue;
            }

            String tagName = item.getTag().getPrintName();
            String itemText = item.getText();

            // При выводе e-mail в приказе ФНС прописано, что строка должна начинаться с "ЭЛ. АДР. ПОКУПАТЕЛЯ".
            // При выводе телефона - строка начинается с "ТЕЛ. ПОКУПАТЕЛЯ".
            if (item.getTag().getId() == 1008) {
                tagName = itemText.contains("@") ? "ЭЛ. АДР. ПОКУПАТЕЛЯ" : "ТЕЛ. ПОКУПАТЕЛЯ";
            }

            String line = tagName + ": " + itemText;

            if (item.getTag().getId() == 1057) {
                line = "";
                if (BitUtils.testBit(item.toInt(), 0))
                    line += "БАНК. ПЛ. АГЕНТ";
                if (BitUtils.testBit(item.toInt(), 1))
                    line += " БАНК. ПЛ. СУБАГЕНТ";
                if (BitUtils.testBit(item.toInt(), 2))
                    line += " ПЛ. АГЕНТ";
                if (BitUtils.testBit(item.toInt(), 3))
                    line += " ПЛ. СУБАГЕНТ";
                if (BitUtils.testBit(item.toInt(), 4))
                    line += " ПОВЕРЕННЫЙ";
                if (BitUtils.testBit(item.toInt(), 5))
                    line += " КОМИССИОНЕР";
                if (BitUtils.testBit(item.toInt(), 6))
                    line += " АГЕНТ";

                line = line.trim();
            }

            if (line.equals(""))
                continue;

            lines.add(line);
        }
        return lines;
    }

    public void read(byte[] data) throws Exception {
        level = 0;
        parse(data);
    }

    public void parse(byte[] data) throws Exception {
        CommandInputStream stream = new CommandInputStream("");
        stream.setData(data);

        while (stream.getSize() >= 4) {
            int tagId = stream.readShort();
            int len = stream.readShort();
            byte[] adata = stream.readBytes(len);

            TLVTag tag = tags.find(tagId);
            if (tag != null) {
                if (tag.getType() == TLVTag.TLVType.itSTLV) {
                    level++;
                    items.add(new TLVItem(tag, adata, level));
                    level--;
                } else {
                    items.add(new TLVItem(tag, adata, level));
                }
            }
        }
    }

    public String TLVDocTypeToStr(int tag) {
        switch (tag) {
            case 1:
                return "Отчёт о регистрации";
            case 11:
                return "Отчёт об изменении параметров регистрации";
            case 2:
                return "Отчёт об открытии смены";
            case 21:
                return "Отчёт о текущем состоянии расчетов";
            case 3:
                return "Кассовый чек";
            case 31:
                return "Кассовый чек коррекции";
            case 4:
                return "Бланк строгой отчетности";
            case 41:
                return "Бланк строгой отчетности коррекции";
            case 5:
                return "Отчёт о закрытии смены";
            case 6:
                return "Отчёт о закрытии фискального накопителя";
            case 7:
                return "Подтверждение оператора";
            default:
                return "Неизвестный тип документа: " + String.valueOf(tag);
        }
    }

}
