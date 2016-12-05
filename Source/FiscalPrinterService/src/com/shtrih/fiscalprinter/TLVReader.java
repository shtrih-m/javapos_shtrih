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
                if (info.getType() == TLVType.itSTLV) {
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

    public void add(int tag, TLVType type) {
        infos.add(new TLVInfo(tag, type));
    }

    public void initialize() {
        infos.clear();
        add(1001, TLVType.itByte);
        add(1002, TLVType.itByte);
        add(1003, TLVType.itASCII);
        add(1004, TLVType.itASCII);
        add(1005, TLVType.itASCII);
        add(1006, TLVType.itASCII);
        add(1007, TLVType.itASCII);
        add(1008, TLVType.itASCII);
        add(1009, TLVType.itASCII);
        add(1010, TLVType.itVLN);
        add(1011, TLVType.itVLN);
        add(1012, TLVType.itUnixTime);
        add(1013, TLVType.itASCII);
        add(1014, TLVType.itASCII);
        add(1015, TLVType.itInt32);
        add(1016, TLVType.itASCII);
        add(1017, TLVType.itASCII);
        add(1018, TLVType.itASCII);
        add(1019, TLVType.itASCII);
        add(1020, TLVType.itVLN);
        add(1021, TLVType.itASCII);
        add(1022, TLVType.itByte);
        add(1023, TLVType.itFVLN);
        add(1024, TLVType.itASCII);
        add(1025, TLVType.itASCII);
        add(1026, TLVType.itASCII);
        add(1027, TLVType.itASCII);
        add(1028, TLVType.itASCII);
        add(1029, TLVType.itASCII);
        add(1030, TLVType.itASCII);
        add(1031, TLVType.itVLN);
        add(1032, TLVType.itASCII);
        add(1033, TLVType.itASCII);
        add(1034, TLVType.itFVLN);
        add(1035, TLVType.itVLN);
        add(1036, TLVType.itASCII);
        add(1037, TLVType.itASCII);
        add(1038, TLVType.itInt32);
        add(1039, TLVType.itASCII);
        add(1040, TLVType.itInt32);
        add(1041, TLVType.itASCII);
        add(1042, TLVType.itInt32);
        add(1043, TLVType.itVLN);
        add(1044, TLVType.itASCII);
        add(1045, TLVType.itASCII);
        add(1046, TLVType.itASCII);
        add(1047, TLVType.itSTLV);
        add(1048, TLVType.itASCII);
        add(1049, TLVType.itASCII);
        add(1050, TLVType.itByte);
        add(1051, TLVType.itByte);
        add(1052, TLVType.itByte);
        add(1053, TLVType.itByte);
        add(1054, TLVType.itCalcSign);
        add(1055, TLVType.itTaxSystem);
        add(1056, TLVType.itByte);
        add(1057, TLVType.itASCII);
        add(1058, TLVType.itASCII);
        add(1059, TLVType.itSTLV);
        add(1060, TLVType.itASCII);
        add(1061, TLVType.itASCII);
        add(1062, TLVType.itTaxSystem); //???
        add(1063, TLVType.itFVLN);
        add(1064, TLVType.itVLN);
        add(1065, TLVType.itASCII);
        add(1066, TLVType.itASCII);
        add(1067, TLVType.itSTLV);
        add(1068, TLVType.itSTLV);
        add(1069, TLVType.itSTLV);
        add(1070, TLVType.itFVLN);
        add(1071, TLVType.itSTLV);
        add(1072, TLVType.itVLN);
        add(1073, TLVType.itASCII);
        add(1074, TLVType.itASCII);
        add(1075, TLVType.itASCII);
        add(1076, TLVType.itASCII);
        add(1077, TLVType.itFSign);
        add(1078, TLVType.itArray);
        add(1079, TLVType.itVLN);
        add(1080, TLVType.itASCII);
        add(1081, TLVType.itVLN);
        add(1082, TLVType.itASCII);
        add(1083, TLVType.itASCII);
        add(1084, TLVType.itSTLV);
        add(1085, TLVType.itASCII);
        add(1086, TLVType.itASCII);
        add(1087, TLVType.itVLN);
        add(1088, TLVType.itVLN);
        add(1089, TLVType.itVLN);
        add(1090, TLVType.itASCII);
        add(1091, TLVType.itASCII);
        add(1092, TLVType.itASCII);
        add(1093, TLVType.itASCII);
        add(1094, TLVType.itASCII);
        add(1095, TLVType.itASCII);
        add(1096, TLVType.itASCII);
        add(1097, TLVType.itInt32);
        add(1098, TLVType.itUnixTime);
        add(1099, TLVType.itASCII);
        add(1100, TLVType.itASCII);
        add(1101, TLVType.itInt16);
        add(1102, TLVType.itVLN);
        add(1103, TLVType.itVLN);
        add(1104, TLVType.itVLN);
        add(1105, TLVType.itVLN);
        add(1106, TLVType.itVLN);
        add(1107, TLVType.itVLN);
        add(1108, TLVType.itByte);
        add(1109, TLVType.itArray);
        add(1110, TLVType.itASCII);
        add(1111, TLVType.itInt32);
        add(1112, TLVType.itSTLV);
        add(1113, TLVType.itASCII);
        add(1114, TLVType.itASCII);
        add(1115, TLVType.itASCII);
        add(1116, TLVType.itInt32);
        add(1117, TLVType.itASCII);
        add(1118, TLVType.itInt32);
    }

    public enum TLVType {
        itByte, itArray, itInt32, itInt16, itSTLV, itUnixTime, itVLN,
        itFVLN, itASCII, itFSign, itVLN2, itTaxSystem, itCalcSign
    }

    public class TLVInfo {

        private final int tagId;
        private final TLVType type;

        public TLVInfo(int tagId, TLVType type) {
            this.tagId = tagId;
            this.type = type;
        }

        public int getTagId() {
            return tagId;
        }

        public String getDisplayName() {
            return getTagDisplayName(tagId);
        }

        public String getPrintName() {
            return getTagPrintName(tagId);
        }
        
        public TLVType getType() {
            return type;
        }

        public String getTagPrintName(int tag) {
            switch (tag) {
                case 1002:
                    return "Автономный режим";
                case 1003:
                    return "Адрес банк. агента";
                case 1004:
                    return "Адрес банк. субагента";
                case 1005:
                    return "Адрес оператора перевода";
                case 1006:
                    return "Адрес платежного агента";
                case 1007:
                    return "Адрес платежного субагента";
                case 1008:
                    return "Адрес покупателя";
                case 1009:
                    return "Адрес расчетов";
                case 1010:
                    return "Размер вознагр.банк.агента";
                case 1011:
                    return "размер вознагр.платежн.агента";
                case 1012:
                    return "Дата и время";
                case 1013:
                    return "Заводской номер ККТ";
                case 1014:
                    return "знач. типа строка";
                case 1015:
                    return "знач. типа целое";
                case 1016:
                    return "ИНН ОП.ПЕРЕВОДА";
                case 1017:
                    return "ИНН ОФД";
                case 1018:
                    return "ИНН";
                case 1019:
                    return "Инф. cообщение";
                case 1020:
                    return "ИТОГ";
                case 1021:
                    return "кассир";
                case 1022:
                    return "код ответа ОФД";
                case 1023:
                    return "количество";
                case 1024:
                    return "наим. банк. агента";
                case 1025:
                    return "наим. банк. субагента";
                case 1026:
                    return "ОПЕРАТОР ПЕРЕВОДА";
                case 1027:
                    return "наим. платежного агента";
                case 1028:
                    return "наим. платежного субагента";
                case 1029:
                    return "наим. реквизита";
                case 1030:
                    return "наим. товара";
                case 1031:
                    return "НАЛИЧНЫМИ";
                case 1032:
                    return "налог";
                case 1033:
                    return "налоги";
                case 1034:
                    return "наценка(ставка)";
                case 1035:
                    return "наценка(сумма)";
                case 1036:
                    return "АВТОМАТ";
                case 1037:
                    return "РНККТ";
                case 1038:
                    return "СМЕНА";
                case 1039:
                    return "зарезервирован";
                case 1040:
                    return "ФД";
                case 1041:
                    return "ФН";
                case 1042:
                    return "ЧЕК";
                case 1043:
                    return "общ.стоим.позиц.(уч.ск.и нацен)";
                case 1044:
                    return "операция банк.агента";
                case 1045:
                    return "операция банк.субагента";
                case 1046:
                    return "ОФД";
                case 1047:
                    return "параметр настройки";
                case 1048:
                    return "наим. пользователя";
                case 1049:
                    return "почтовый индекс";
                case 1050:
                    return "признак исчерп.ресурса ФН";
                case 1051:
                    return "признак необх.срочн.замены ФН";
                case 1052:
                    return "призн. переполнения памяти ФН";
                case 1053:
                    return "призн. превыш. врем.ожид.отв.ОФД";
                case 1054:
                    return "признак расчета";
                case 1055:
                    return "примен.налог.сист.";
                case 1056:
                    return "признак шифрования";
                case 1057:
                    return "применение плат. агентами";
                case 1058:
                    return "применение банк. агентами";
                case 1059:
                    return "наим. товара(реквизиты)";
                case 1060:
                    return "сайт налог. органа";
                case 1061:
                    return "сайт ОФД";
                case 1062:
                    return "системы налогообложения";
                case 1063:
                    return "скидка(ставка)";
                case 1064:
                    return "скидка(сумма)";
                case 1065:
                    return "сокращ. наим. налога";
                case 1066:
                    return "сообщ.";
                case 1067:
                    return "сообщ. оператора для ККТ";
                case 1068:
                    return "сообщ. оператора для ФН";
                case 1069:
                    return "сообщ. оператору";
                case 1070:
                    return "ставка налога";
                case 1071:
                    return "сторно товара (реквизиты)";
                case 1072:
                    return "сумма налога";
                case 1073:
                    return "тел.банк. агента";
                case 1074:
                    return "тел.плат. агента";
                case 1075:
                    return "тел.опер. по пер. ден.средств";
                case 1076:
                    return "тип сообщен.";
                case 1077:
                    return "ФП документа";
                case 1078:
                    return "ФП оператора";
                case 1079:
                    return "цена за единицу";
                case 1080:
                    return "штрихкод EAN13";
                case 1081:
                    return "форма расчета–электр.";
                case 1082:
                    return "тел.банк. субагента";
                case 1083:
                    return "тел.плат. субагента";
                case 1084:
                    return "доп.рекв.";
                case 1085:
                    return "наим.доп.рекв.";
                case 1086:
                    return "знач.доп.рекв.";
                case 1087:
                    return "Итог смены";
                case 1088:
                    return "приход нал.";
                case 1089:
                    return "приход электр.";
                case 1090:
                    return "возвр.прихода нал.";
                case 1091:
                    return "возвр.прихода электр.";
                case 1092:
                    return "расход наличными";
                case 1093:
                    return "расход электронными";
                case 1094:
                    return "возвр.расхода наличными";
                case 1095:
                    return "возвр.расхода электронными";
                case 1096:
                    return "номер корректируемого ФД";
                case 1097:
                    return "кол-во непереданных документов ФД";
                case 1098:
                    return "дата и время 1-го из непереданных ФД";
                case 1099:
                    return "сводный итог";
                case 1100:
                    return "номер предписания";
                case 1101:
                    return "код причины перерегистрации";
                case 1102:
                    return "НДС итога чека ставк.18%";
                case 1103:
                    return "НДС итога чека ставк.10%";
                case 1104:
                    return "НДС итога чека ставк.0%";
                case 1105:
                    return "НДС не облагается";
                case 1106:
                    return "НДС итога чека с рассч. ставк.18%";
                case 1107:
                    return "НДС итога чека с рассч. ставк.10%";
                case 1108:
                    return "признак расч. в Интернет";
                case 1109:
                    return "признак работы в сфере услуг";
                case 1110:
                    return "прим.для формирования БСО";
                case 1111:
                    return "кол-во фиск.док-тов за смену";
                case 1112:
                    return "скидка/наценка";
                case 1113:
                    return "наим. скидки";
                case 1114:
                    return "наим. наценки";
                case 1115:
                    return "адр. сайта для проверки ФП";
                case 1116:
                    return "номер 1-го непереданного док-та";
                case 1117:
                    return "адр.отправителя";
                case 1118:
                    return "кол-во касс.чеков за смену";
                default:
                    return "";
            }
        }

        public String getTagDisplayName(int tag) {
            switch (tag) {
                case 1001:
                    return "автомат.режим";
                case 1002:
                    return "автоном.режим";
                case 1003:
                    return "адр.банк.агента";
                case 1004:
                    return "адр.банк.субагента";
                case 1005:
                    return "адр.опеер.по перев.ден.ср-в";
                case 1006:
                    return "адр.плат.агента";
                case 1007:
                    return "адр.плат. субагента";
                case 1008:
                    return "адр.покупателя";
                case 1009:
                    return "адр.расчетов";
                case 1010:
                    return "размер вознагр.банк.агента";
                case 1011:
                    return "размер вознагр.платежн.агента";
                case 1012:
                    return "дата, время";
                case 1013:
                    return "зав. номер ККТ";
                case 1014:
                    return "знач. типа строка";
                case 1015:
                    return "знач. типа целое";
                case 1016:
                    return "ИНН опер.по переводу ден.ср-в";
                case 1017:
                    return "ИНН ОФД";
                case 1018:
                    return "ИНН пользователя";
                case 1019:
                    return "инф. cообщение";
                case 1020:
                    return "ИТОГ";
                case 1021:
                    return "кассир";
                case 1022:
                    return "код ответа ОФД";
                case 1023:
                    return "количество";
                case 1024:
                    return "наим. банк. агента";
                case 1025:
                    return "наим. банк. субагента";
                case 1026:
                    return "наим. опер. по переводу ден. ср-в";
                case 1027:
                    return "наим. платежного агента";
                case 1028:
                    return "наим. платежного субагента";
                case 1029:
                    return "наим. реквизита";
                case 1030:
                    return "наим. товара";
                case 1031:
                    return "форма расчета–нал.";
                case 1032:
                    return "налог";
                case 1033:
                    return "налоги";
                case 1034:
                    return "наценка(ставка)";
                case 1035:
                    return "наценка(сумма)";
                case 1036:
                    return "номер автомата";
                case 1037:
                    return "рег. номер ККТ";
                case 1038:
                    return "номер смены";
                case 1039:
                    return "зарезервирован";
                case 1040:
                    return "порядковый номер ФД";
                case 1041:
                    return "зав. номер ФН";
                case 1042:
                    return "номер чека";
                case 1043:
                    return "общ.стоим.позиц.(уч.ск.и нацен)";
                case 1044:
                    return "операция банк.агента";
                case 1045:
                    return "операция банк.субагента";
                case 1046:
                    return "ОФД";
                case 1047:
                    return "параметр настройки";
                case 1048:
                    return "наим. пользователя";
                case 1049:
                    return "почтовый индекс";
                case 1050:
                    return "признак исчерп.ресурса ФН";
                case 1051:
                    return "признак необх.срочн.замены ФН";
                case 1052:
                    return "призн. переполнения памяти ФН";
                case 1053:
                    return "призн. превыш. врем.ожид.отв.ОФД";
                case 1054:
                    return "признак расчета";
                case 1055:
                    return "примен.налог.сист.";
                case 1056:
                    return "признак шифрования";
                case 1057:
                    return "применение плат. агентами";
                case 1058:
                    return "применение банк. агентами";
                case 1059:
                    return "наим. товара(реквизиты)";
                case 1060:
                    return "сайт налог. органа";
                case 1061:
                    return "сайт ОФД";
                case 1062:
                    return "системы налогообложения";
                case 1063:
                    return "скидка(ставка)";
                case 1064:
                    return "скидка(сумма)";
                case 1065:
                    return "сокращ. наим. налога";
                case 1066:
                    return "сообщ.";
                case 1067:
                    return "сообщ. оператора для ККТ";
                case 1068:
                    return "сообщ. оператора для ФН";
                case 1069:
                    return "сообщ. оператору";
                case 1070:
                    return "ставка налога";
                case 1071:
                    return "сторно товара (реквизиты)";
                case 1072:
                    return "сумма налога";
                case 1073:
                    return "тел.банк. агента";
                case 1074:
                    return "тел.плат. агента";
                case 1075:
                    return "тел.опер. по пер. ден.средств";
                case 1076:
                    return "тип сообщен.";
                case 1077:
                    return "ФП документа";
                case 1078:
                    return "ФП оператора";
                case 1079:
                    return "цена за единицу";
                case 1080:
                    return "штрихкод EAN13";
                case 1081:
                    return "форма расчета–электр.";
                case 1082:
                    return "тел.банк. субагента";
                case 1083:
                    return "тел.плат. субагента";
                case 1084:
                    return "доп.рекв.";
                case 1085:
                    return "наим.доп.рекв.";
                case 1086:
                    return "знач.доп.рекв.";
                case 1087:
                    return "Итог смены";
                case 1088:
                    return "приход нал.";
                case 1089:
                    return "приход электр.";
                case 1090:
                    return "возвр.прихода нал.";
                case 1091:
                    return "возвр.прихода электр.";
                case 1092:
                    return "расход наличными";
                case 1093:
                    return "расход электронными";
                case 1094:
                    return "возвр.расхода наличными";
                case 1095:
                    return "возвр.расхода электронными";
                case 1096:
                    return "номер корректируемого ФД";
                case 1097:
                    return "кол-во непереданных документов ФД";
                case 1098:
                    return "дата и время 1-го из непереданных ФД";
                case 1099:
                    return "сводный итог";
                case 1100:
                    return "номер предписания";
                case 1101:
                    return "код причины перерегистрации";
                case 1102:
                    return "НДС итога чека ставк.18%";
                case 1103:
                    return "НДС итога чека ставк.10%";
                case 1104:
                    return "НДС итога чека ставк.0%";
                case 1105:
                    return "НДС не облагается";
                case 1106:
                    return "НДС итога чека с рассч. ставк.18%";
                case 1107:
                    return "НДС итога чека с рассч. ставк.10%";
                case 1108:
                    return "признак расч. в Интернет";
                case 1109:
                    return "признак работы в сфере услуг";
                case 1110:
                    return "прим.для формирования БСО";
                case 1111:
                    return "кол-во фиск.док-тов за смену";
                case 1112:
                    return "скидка/наценка";
                case 1113:
                    return "наим. скидки";
                case 1114:
                    return "наим. наценки";
                case 1115:
                    return "адр. сайта для проверки ФП";
                case 1116:
                    return "номер 1-го непереданного док-та";
                case 1117:
                    return "адр.отправителя";
                case 1118:
                    return "кол-во касс.чеков за смену";
            }
            return "";
        }

    }

    public class TLVItem {

        private final int level;
        private final byte[] data;
        private final TLVInfo info;

        public TLVItem(TLVInfo info, byte[] data, int level) {
            this.info = info;
            this.data = data;
            this.level = level;
        }

        public TLVInfo getInfo() {
            return info;
        }

        public byte[] getData() {
            return data;
        }

        public int getLevel() {
            return level;
        }

        public long toInt(byte[] d) {
            long result = 0;
            for (int i = d.length; i <= 0; i--) {
                result = result * 0x100 + d[i];
            }
            return result;
        }

        public Date toDate(byte[] d) {
            return new Date(toInt(d) / 86400);
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

        public String toASCII(byte[] data) {
            return new String(data);
        }

        public double toFVLN(byte[] data) throws Exception {
            if (data[0] > 4) {
                throw new Exception("Неверная длина FVLN");
            }
            if (data.length < 2) {
                throw new Exception("Неверная длина FVLN");
            }
            byte[] d = new byte[data.length - 1];
            System.arraycopy(data, 1, d, 0, d.length);
            double result = toInt(d);
            int power = data[0];
            for (int i = 0; i < power; i++) {
                result = result / 10;
            }
            return result;
        }

        public String toFVLNS(byte[] data) throws Exception {
            double result = toFVLN(data);
            int power = data[0];
            return String.format("%." + power + "f", result);
        }

        public String getText() throws Exception {
            switch (info.getType()) {
                case itByte:
                    return String.valueOf(toInt(data));
                case itArray:
                    return Hex.toHex(data);
                case itInt32:
                    return String.valueOf(toInt(data));
                case itInt16:
                    return String.valueOf(toInt(data));
                case itUnixTime:
                    return toDate(data).toString();
                case itVLN:
                    return String.format("%.2f", toInt(data) / 100.0);
                case itFVLN:
                    return toFVLNS(data);
                case itASCII:
                    return toASCII(data).trim();
                case itTaxSystem:
                    return taxSystemToStr((int) toInt(data));
                case itCalcSign:
                    return calcTypeToStr((int) toInt(data));
                default:
                    return "";
            }
        }
    }

}
