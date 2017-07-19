/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Date;
import java.util.Vector;
import java.io.ByteArrayInputStream;

import com.shtrih.util.Hex;
import com.shtrih.util.BitUtils;
import com.shtrih.fiscalprinter.command.CommandInputStream;

public class TLVParser {

    private int level = 0;
    private final TLVTags tags = new TLVTags();
    private final Vector<TLVItem> items = new Vector<TLVItem>();

    public TLVParser() {
    }

    public Vector<TLVItem> getItems() {
        return items;
    }

    public Vector<String> getPrintText() throws Exception {
        Vector<String> lines = new Vector<String>();
        for (int i = 0; i < items.size(); i++) {
            TLVItem item = items.get(i);

            String tagName = item.getTag().getPrintName();
            String itemText = item.getText();

            // При выводе e-mail в приказе ФНС прописано, что строка должна начинаться с "ЭЛ. АДР. ПОКУПАТЕЛЯ".
            // При выводе телефона - строка начинается с "ТЕЛ. ПОКУПАТЕЛЯ".
            if(item.getTag().getId() == 1008) {
                tagName = itemText.contains("@") ? "ЭЛ. АДР. ПОКУПАТЕЛЯ" : "ТЕЛ. ПОКУПАТЕЛЯ";
            }

            String line = tagName + ": " + itemText;
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
