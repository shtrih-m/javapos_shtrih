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

public class TLVReader {

    private int level = 0;
    private final Vector<TLVInfo> infos = new Vector<TLVInfo>();
    private final Vector<TLVItem> items = new Vector<TLVItem>();

    public TLVReader() {
        initialize();
    }

    public Vector<TLVItem> getItems() {
        return items;
    }

    public Vector<String> getPrintText() throws Exception {
        Vector<String> lines = new Vector<String>();
        for (int i = 0; i < items.size(); i++) {
            TLVItem item = items.get(i);
            String line = item.getInfo().getPrintName() + ": " + item.getText();
            lines.add(line);
        }
        return lines;
    }

    public void read(byte[] data) throws Exception {
        level = 0;
        parse(data);
    }

    public TLVInfo findInfo(int tag) {
        for (int i = 0; i < infos.size(); i++) {
            TLVInfo item = infos.get(i);
            if (item.getTagId() == tag) {
                return item;
            }
        }
        return null;
    }

    public void parse(byte[] data) throws Exception {
        CommandInputStream stream = new CommandInputStream("");
        stream.setData(data);

        while (stream.getSize() >= 4) {
            int tag = stream.readShort();
            int len = stream.readShort();
            byte[] adata = stream.readBytes(len);

            TLVInfo info = findInfo(tag);
            if (info != null) {
                if (info.getType() == TLVInfo.TLVType.itSTLV) {
                    level++;
                    items.add(new TLVItem(info, adata, level));
                    level--;
                } else {
                    items.add(new TLVItem(info, adata, level));
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

    public void add(int tag, TLVInfo.TLVType type) {
        infos.add(new TLVInfo(tag, type));
    }

    public void initialize() {
        infos.clear();
        add(1001, TLVInfo.TLVType.itByte);
        add(1002, TLVInfo.TLVType.itByte);
        add(1003, TLVInfo.TLVType.itASCII);
        add(1004, TLVInfo.TLVType.itASCII);
        add(1005, TLVInfo.TLVType.itASCII);
        add(1006, TLVInfo.TLVType.itASCII);
        add(1007, TLVInfo.TLVType.itASCII);
        add(1008, TLVInfo.TLVType.itASCII);
        add(1009, TLVInfo.TLVType.itASCII);
        add(1010, TLVInfo.TLVType.itVLN);
        add(1011, TLVInfo.TLVType.itVLN);
        add(1012, TLVInfo.TLVType.itUnixTime);
        add(1013, TLVInfo.TLVType.itASCII);
        add(1014, TLVInfo.TLVType.itASCII);
        add(1015, TLVInfo.TLVType.itInt32);
        add(1016, TLVInfo.TLVType.itASCII);
        add(1017, TLVInfo.TLVType.itASCII);
        add(1018, TLVInfo.TLVType.itASCII);
        add(1019, TLVInfo.TLVType.itASCII);
        add(1020, TLVInfo.TLVType.itVLN);
        add(1021, TLVInfo.TLVType.itASCII);
        add(1022, TLVInfo.TLVType.itByte);
        add(1023, TLVInfo.TLVType.itFVLN);
        add(1024, TLVInfo.TLVType.itASCII);
        add(1025, TLVInfo.TLVType.itASCII);
        add(1026, TLVInfo.TLVType.itASCII);
        add(1027, TLVInfo.TLVType.itASCII);
        add(1028, TLVInfo.TLVType.itASCII);
        add(1029, TLVInfo.TLVType.itASCII);
        add(1030, TLVInfo.TLVType.itASCII);
        add(1031, TLVInfo.TLVType.itVLN);
        add(1032, TLVInfo.TLVType.itASCII);
        add(1033, TLVInfo.TLVType.itASCII);
        add(1034, TLVInfo.TLVType.itFVLN);
        add(1035, TLVInfo.TLVType.itVLN);
        add(1036, TLVInfo.TLVType.itASCII);
        add(1037, TLVInfo.TLVType.itASCII);
        add(1038, TLVInfo.TLVType.itInt32);
        add(1039, TLVInfo.TLVType.itASCII);
        add(1040, TLVInfo.TLVType.itInt32);
        add(1041, TLVInfo.TLVType.itASCII);
        add(1042, TLVInfo.TLVType.itInt32);
        add(1043, TLVInfo.TLVType.itVLN);
        add(1044, TLVInfo.TLVType.itASCII);
        add(1045, TLVInfo.TLVType.itASCII);
        add(1046, TLVInfo.TLVType.itASCII);
        add(1047, TLVInfo.TLVType.itSTLV);
        add(1048, TLVInfo.TLVType.itASCII);
        add(1049, TLVInfo.TLVType.itASCII);
        add(1050, TLVInfo.TLVType.itByte);
        add(1051, TLVInfo.TLVType.itByte);
        add(1052, TLVInfo.TLVType.itByte);
        add(1053, TLVInfo.TLVType.itByte);
        add(1054, TLVInfo.TLVType.itCalcSign);
        add(1055, TLVInfo.TLVType.itTaxSystem);
        add(1056, TLVInfo.TLVType.itByte);
        add(1057, TLVInfo.TLVType.itASCII);
        add(1058, TLVInfo.TLVType.itASCII);
        add(1059, TLVInfo.TLVType.itSTLV);
        add(1060, TLVInfo.TLVType.itASCII);
        add(1061, TLVInfo.TLVType.itASCII);
        add(1062, TLVInfo.TLVType.itTaxSystem); //???
        add(1063, TLVInfo.TLVType.itFVLN);
        add(1064, TLVInfo.TLVType.itVLN);
        add(1065, TLVInfo.TLVType.itASCII);
        add(1066, TLVInfo.TLVType.itASCII);
        add(1067, TLVInfo.TLVType.itSTLV);
        add(1068, TLVInfo.TLVType.itSTLV);
        add(1069, TLVInfo.TLVType.itSTLV);
        add(1070, TLVInfo.TLVType.itFVLN);
        add(1071, TLVInfo.TLVType.itSTLV);
        add(1072, TLVInfo.TLVType.itVLN);
        add(1073, TLVInfo.TLVType.itASCII);
        add(1074, TLVInfo.TLVType.itASCII);
        add(1075, TLVInfo.TLVType.itASCII);
        add(1076, TLVInfo.TLVType.itASCII);
        add(1077, TLVInfo.TLVType.itFSign);
        add(1078, TLVInfo.TLVType.itArray);
        add(1079, TLVInfo.TLVType.itVLN);
        add(1080, TLVInfo.TLVType.itASCII);
        add(1081, TLVInfo.TLVType.itVLN);
        add(1082, TLVInfo.TLVType.itASCII);
        add(1083, TLVInfo.TLVType.itASCII);
        add(1084, TLVInfo.TLVType.itSTLV);
        add(1085, TLVInfo.TLVType.itASCII);
        add(1086, TLVInfo.TLVType.itASCII);
        add(1087, TLVInfo.TLVType.itVLN);
        add(1088, TLVInfo.TLVType.itVLN);
        add(1089, TLVInfo.TLVType.itVLN);
        add(1090, TLVInfo.TLVType.itASCII);
        add(1091, TLVInfo.TLVType.itASCII);
        add(1092, TLVInfo.TLVType.itASCII);
        add(1093, TLVInfo.TLVType.itASCII);
        add(1094, TLVInfo.TLVType.itASCII);
        add(1095, TLVInfo.TLVType.itASCII);
        add(1096, TLVInfo.TLVType.itASCII);
        add(1097, TLVInfo.TLVType.itInt32);
        add(1098, TLVInfo.TLVType.itUnixTime);
        add(1099, TLVInfo.TLVType.itASCII);
        add(1100, TLVInfo.TLVType.itASCII);
        add(1101, TLVInfo.TLVType.itInt16);
        add(1102, TLVInfo.TLVType.itVLN);
        add(1103, TLVInfo.TLVType.itVLN);
        add(1104, TLVInfo.TLVType.itVLN);
        add(1105, TLVInfo.TLVType.itVLN);
        add(1106, TLVInfo.TLVType.itVLN);
        add(1107, TLVInfo.TLVType.itVLN);
        add(1108, TLVInfo.TLVType.itByte);
        add(1109, TLVInfo.TLVType.itArray);
        add(1110, TLVInfo.TLVType.itASCII);
        add(1111, TLVInfo.TLVType.itInt32);
        add(1112, TLVInfo.TLVType.itSTLV);
        add(1113, TLVInfo.TLVType.itASCII);
        add(1114, TLVInfo.TLVType.itASCII);
        add(1115, TLVInfo.TLVType.itASCII);
        add(1116, TLVInfo.TLVType.itInt32);
        add(1117, TLVInfo.TLVType.itASCII);
        add(1118, TLVInfo.TLVType.itInt32);
    }
}
