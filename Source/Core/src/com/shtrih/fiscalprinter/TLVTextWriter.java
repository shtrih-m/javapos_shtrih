package com.shtrih.fiscalprinter;

import com.shtrih.util.BitUtils;
import com.shtrih.util.Hex;
import com.shtrih.util.HexUtils;
import com.shtrih.util.encoding.IBM866;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author V.Kravtsov
 */
public class TLVTextWriter {

    private final List<TLVItem> items;

    public TLVTextWriter(List<TLVItem> items) {
        this.items = items;
    }

    public String calcTypeToStr(int type) {
        switch (type) {
            case 1:
                return "Приход";
            case 2:
                return "Возврат прихода";
            case 3:
                return "Расход";
            case 4:
                return "Возврат расхода";
            default:
                return "Неизв. тип: " + String.valueOf(type);
        }
    }

    //
    /*
     0 Общая
     1 Упрощенная Доход
     2 Упрощенная Доход минус Расход
     3 Единый налог на вмененный доход
     4 Единый сельскохозяйственный налог
     5 Патентная система налогообложения
     */
    public String taxSystemToStr(int type) {
        if (type == 0) {
            return "Нет";
        }

        String result = "";
        if (BitUtils.testBit(type, 0)) {
            result += "Общ.";
        }
        if (BitUtils.testBit(type, 1)) {
            result += "+УД";
        }
        if (BitUtils.testBit(type, 2)) {
            result += "+УДМР";
        }
        if (BitUtils.testBit(type, 3)) {
            result += "+ЕНВД";
        }
        if (BitUtils.testBit(type, 4)) {
            result += "+ЕСН";
        }
        if (BitUtils.testBit(type, 5)) {
            result += "+ПСН";
        }
        return result;
    }

    public void getPrintText(List<String> lines) throws Exception {
        for (int i = 0; i < items.size(); i++) {
            addPrintLines(items.get(i), lines);
        }
    }

    private void addPrintLines(TLVItem item, List<String> lines) throws Exception {
        switch (item.getId()) {
            case 1084: // 1084, дополнительный реквизит пользователя
            case 1085: // 1085, наименование дополнительного реквизита пользователя
            case 1086: // 1086, значение дополнительного реквизита пользователя
            case 1203: // 1203, ИНН кассира
                return;
        }

        TLVTag tag = item.getTag();
        if (tag != null) 
        {
            if (tag.getType() == TLVTag.TLVType.itBitMask)
            {
                lines.add(item.getText());
            } else
            {
                String itemText = item.getText();
                String tagName = tag.getPrintName(item.getText());

                // Если печатное название тэга пустое, то не печатаем его
                String line = tagName.isEmpty() ? itemText : tagName + ": " + itemText;

                if (!line.equals("")) {
                    lines.add(line);
                }
            }
            for (int i = 0; i < item.getItems().size(); i++) {
                addPrintLines(item.getItems().get(i), lines);
            }
        }
    }

    public void getDocumentText(List<String> lines) throws Exception {
        for (int i = 0; i < items.size(); i++) {
            addDocumentLines("", items.get(i), lines);
        }
    }

    private void addDocumentLines(String prefix, TLVItem item,
            List<String> lines) throws Exception {
        String line = "";
        TLVTag tag = item.getTag();
        if (tag != null) {
            String itemText = item.getText();
            String tagName = tag.getPrintName(item.getText());

            line = item.getId() + ", " + tagName + ": " + itemText;
        } else {
            line = item.getId() + ", " + item.getText();
        }
        lines.add(prefix + line);
        for (int i = 0; i < item.getItems().size(); i++) {
            addDocumentLines(prefix + "  ", item.getItems().get(i), lines);
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
