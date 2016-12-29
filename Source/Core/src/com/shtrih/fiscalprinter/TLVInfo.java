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
public class TLVInfo {

    private final int tagId;
    private final TLVType type;

    public enum TLVType {
        itByte, itArray, itInt32, itInt16, itSTLV, itUnixTime, itVLN,
        itFVLN, itASCII, itFSign, itVLN2, itTaxSystem, itCalcSign
    }

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

    public static String getTagPrintName(int tag) {
        switch (tag) {
            case 1002:
                return "АВТОНОМНЫЙ РЕЖИМ";
            case 1003:
                return "АДРЕС БАНК. АГЕНТА";
            case 1004:
                return "АДРЕС БАНК. СУБАГЕНТА";
            case 1005:
                return "АДРЕС ОПЕРАТОРА ПЕРЕВОДА";
            case 1006:
                return "АДРЕС ПЛАТЕЖНОГО АГЕНТА";
            case 1007:
                return "АДРЕС ПЛАТЕЖНОГО СУБАГЕНТА";
            case 1008:
                return "АДРЕС ПОКУПАТЕЛЯ";
            case 1009:
                return "АДРЕС РАСЧЕТОВ";
            case 1010:
                return "РАЗМЕР ВОЗНАГР.БАНК.АГЕНТА";
            case 1011:
                return "РАЗМЕР ВОЗНАГР.ПЛАТЕЖН.АГЕНТА";
            case 1012:
                return "ДАТА И ВРЕМЯ";
            case 1013:
                return "ЗАВОДСКОЙ НОМЕР ККТ";
            case 1014:
                return "ЗНАЧ. ТИПА СТРОКА";
            case 1015:
                return "ЗНАЧ. ТИПА ЦЕЛОЕ";
            case 1016:
                return "ИНН ОП.ПЕРЕВОДА";
            case 1017:
                return "ИНН ОФД";
            case 1018:
                return "ИНН";
            case 1019:
                return "ИНФ. CООБЩЕНИЕ";
            case 1020:
                return "ИТОГ";
            case 1021:
                return "КАССИР";
            case 1022:
                return "КОД ОТВЕТА ОФД";
            case 1023:
                return "КОЛИЧЕСТВО";
            case 1024:
                return "НАИМ. БАНК. АГЕНТА";
            case 1025:
                return "НАИМ. БАНК. СУБАГЕНТА";
            case 1026:
                return "ОПЕРАТОР ПЕРЕВОДА";
            case 1027:
                return "НАИМ. ПЛАТЕЖНОГО АГЕНТА";
            case 1028:
                return "НАИМ. ПЛАТЕЖНОГО СУБАГЕНТА";
            case 1029:
                return "НАИМ. РЕКВИЗИТА";
            case 1030:
                return "НАИМ. ТОВАРА";
            case 1031:
                return "НАЛИЧНЫМИ";
            case 1032:
                return "НАЛОГ";
            case 1033:
                return "НАЛОГИ";
            case 1034:
                return "НАЦЕНКА(СТАВКА)";
            case 1035:
                return "НАЦЕНКА(СУММА)";
            case 1036:
                return "АВТОМАТ";
            case 1037:
                return "РНККТ";
            case 1038:
                return "СМЕНА";
            case 1039:
                return "ЗАРЕЗЕРВИРОВАН";
            case 1040:
                return "ФД";
            case 1041:
                return "ФН";
            case 1042:
                return "ЧЕК";
            case 1043:
                return "ОБЩ.СТОИМ.ПОЗИЦ.(УЧ.СК.И НАЦЕН)";
            case 1044:
                return "ОПЕРАЦИЯ БАНК.АГЕНТА";
            case 1045:
                return "ОПЕРАЦИЯ БАНК.СУБАГЕНТА";
            case 1046:
                return "ОФД";
            case 1047:
                return "ПАРАМЕТР НАСТРОЙКИ";
            case 1048:
                return "НАИМ. ПОЛЬЗОВАТЕЛЯ";
            case 1049:
                return "ПОЧТОВЫЙ ИНДЕКС";
            case 1050:
                return "ПРИЗНАК ИСЧЕРП.РЕСУРСА ФН";
            case 1051:
                return "ПРИЗНАК НЕОБХ.СРОЧН.ЗАМЕНЫ ФН";
            case 1052:
                return "ПРИЗН. ПЕРЕПОЛНЕНИЯ ПАМЯТИ ФН";
            case 1053:
                return "ПРИЗН. ПРЕВЫШ. ВРЕМ.ОЖИД.ОТВ.ОФД";
            case 1054:
                return "ПРИЗНАК РАСЧЕТА";
            case 1055:
                return "ПРИМЕН.НАЛОГ.СИСТ.";
            case 1056:
                return "ПРИЗНАК ШИФРОВАНИЯ";
            case 1057:
                return "ПРИМЕНЕНИЕ ПЛАТ. АГЕНТАМИ";
            case 1058:
                return "ПРИМЕНЕНИЕ БАНК. АГЕНТАМИ";
            case 1059:
                return "НАИМ. ТОВАРА(РЕКВИЗИТЫ)";
            case 1060:
                return "САЙТ НАЛОГ. ОРГАНА";
            case 1061:
                return "САЙТ ОФД";
            case 1062:
                return "СИСТЕМЫ НАЛОГООБЛОЖЕНИЯ";
            case 1063:
                return "СКИДКА(СТАВКА)";
            case 1064:
                return "СКИДКА(СУММА)";
            case 1065:
                return "СОКРАЩ. НАИМ. НАЛОГА";
            case 1066:
                return "СООБЩ.";
            case 1067:
                return "СООБЩ. ОПЕРАТОРА ДЛЯ ККТ";
            case 1068:
                return "СООБЩ. ОПЕРАТОРА ДЛЯ ФН";
            case 1069:
                return "СООБЩ. ОПЕРАТОРУ";
            case 1070:
                return "СТАВКА НАЛОГА";
            case 1071:
                return "СТОРНО ТОВАРА (РЕКВИЗИТЫ)";
            case 1072:
                return "СУММА НАЛОГА";
            case 1073:
                return "ТЕЛ.БАНК. АГЕНТА";
            case 1074:
                return "ТЕЛ.ПЛАТ. АГЕНТА";
            case 1075:
                return "ТЕЛ.ОПЕР. ПО ПЕР. ДЕН.СРЕДСТВ";
            case 1076:
                return "ТИП СООБЩЕН.";
            case 1077:
                return "ФП ДОКУМЕНТА";
            case 1078:
                return "ФП ОПЕРАТОРА";
            case 1079:
                return "ЦЕНА ЗА ЕДИНИЦУ";
            case 1080:
                return "ШТРИХКОД EAN13";
            case 1081:
                return "ФОРМА РАСЧЕТА–ЭЛЕКТР.";
            case 1082:
                return "ТЕЛ.БАНК. СУБАГЕНТА";
            case 1083:
                return "ТЕЛ.ПЛАТ. СУБАГЕНТА";
            case 1084:
                return "ДОП.РЕКВ.";
            case 1085:
                return "НАИМ.ДОП.РЕКВ.";
            case 1086:
                return "ЗНАЧ.ДОП.РЕКВ.";
            case 1087:
                return "ИТОГ СМЕНЫ";
            case 1088:
                return "ПРИХОД НАЛ.";
            case 1089:
                return "ПРИХОД ЭЛЕКТР.";
            case 1090:
                return "ВОЗВР.ПРИХОДА НАЛ.";
            case 1091:
                return "ВОЗВР.ПРИХОДА ЭЛЕКТР.";
            case 1092:
                return "РАСХОД НАЛИЧНЫМИ";
            case 1093:
                return "РАСХОД ЭЛЕКТРОННЫМИ";
            case 1094:
                return "ВОЗВР.РАСХОДА НАЛИЧНЫМИ";
            case 1095:
                return "ВОЗВР.РАСХОДА ЭЛЕКТРОННЫМИ";
            case 1096:
                return "НОМЕР КОРРЕКТИРУЕМОГО ФД";
            case 1097:
                return "КОЛ-ВО НЕПЕРЕДАННЫХ ДОКУМЕНТОВ ФД";
            case 1098:
                return "ДАТА И ВРЕМЯ 1-ГО ИЗ НЕПЕРЕДАННЫХ ФД";
            case 1099:
                return "СВОДНЫЙ ИТОГ";
            case 1100:
                return "НОМЕР ПРЕДПИСАНИЯ";
            case 1101:
                return "КОД ПРИЧИНЫ ПЕРЕРЕГИСТРАЦИИ";
            case 1102:
                return "НДС ИТОГА ЧЕКА СТАВК.18%";
            case 1103:
                return "НДС ИТОГА ЧЕКА СТАВК.10%";
            case 1104:
                return "НДС ИТОГА ЧЕКА СТАВК.0%";
            case 1105:
                return "НДС НЕ ОБЛАГАЕТСЯ";
            case 1106:
                return "НДС ИТОГА ЧЕКА С РАССЧ. СТАВК.18%";
            case 1107:
                return "НДС ИТОГА ЧЕКА С РАССЧ. СТАВК.10%";
            case 1108:
                return "ПРИЗНАК РАСЧ. В ИНТЕРНЕТ";
            case 1109:
                return "ПРИЗНАК РАБОТЫ В СФЕРЕ УСЛУГ";
            case 1110:
                return "ПРИМ.ДЛЯ ФОРМИРОВАНИЯ БСО";
            case 1111:
                return "КОЛ-ВО ФИСК.ДОК-ТОВ ЗА СМЕНУ";
            case 1112:
                return "СКИДКА/НАЦЕНКА";
            case 1113:
                return "НАИМЕНОВАНИЕ СКИДКИ";
            case 1114:
                return "НАИМЕНОВАНИЕ НАЦЕНКИ";
            case 1115:
                return "АДР. САЙТА ДЛЯ ПРОВЕРКИ ФП";
            case 1116:
                return "НОМЕР 1-ГО НЕПЕРЕДАННОГО ДОК-ТА";
            case 1117:
                return "АДРЕС ОТПРАВИТЕЛЯ";
            case 1118:
                return "КОЛ-ВО КАСС.ЧЕКОВ ЗА СМЕНУ";
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
