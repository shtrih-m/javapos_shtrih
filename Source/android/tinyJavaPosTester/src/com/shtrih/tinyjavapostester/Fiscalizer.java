package com.shtrih.tinyjavapostester;

import android.util.Log;

import com.shtrih.fiscalprinter.ShtrihFiscalPrinter;
import com.shtrih.fiscalprinter.command.DeviceMetrics;
import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterModelParameters;
import com.shtrih.fiscalprinter.command.PrinterTime;
import com.shtrih.fiscalprinter.command.ReadPrinterModelParameters;
import com.shtrih.util.BitUtils;

import java.util.Calendar;

import jpos.JposException;

/**
 * Пример регистрации/перерегистрации и закрытия ФН.
 * Поддерживаемое оборудование:
 *  - ШТРИХ-МОБАЙЛ-Ф прошивка 20024+
 *  - Кассовое ядро
 *  - Десктопные ФРы
 */
public class Fiscalizer {
    /**
     * Регистрация ФН
     */
    public void fiscalizeFS(ShtrihFiscalPrinter printer, ParametersFiscal params) throws Exception {

        syncDateTime(printer);

        fillDeviceTables(printer, params);

        fiscalizeDevice(printer, params);
    }

    private void fiscalizeDevice(ShtrihFiscalPrinter printer, ParametersFiscal params) throws Exception {

        String inn = formatInn(params.VATIN);
        String rnm = formatRnm(params.KKTNumber);
        int taxSystemCode = getTaxSystem(params.TaxVariant);
        int operationMode = getOperationMode(params);
        int agentType = getAgentType(params.SignOfAgent);

        printer.fsStartFiscalization(0);

        if (agentType != 0)
            printer.fsWriteTag(1057, agentType, 1);

        writeVATINTagIfNotNullAndNotEmpty(printer, 1203, params.CashierVATIN);

        if ((isDesktop(printer) && printer.readLongPrinterStatus().getFirmwareBuild() > 46150) || isCashCore(printer)) {

            int extendedModes = getExtOperationMode(params);

            int fsTableNumber = readPrinterModelParameters(printer).getFsTableNumber();

            printer.writeTable(fsTableNumber, 1, 21, "" + extendedModes);

        } else {

            printer.fsWriteTag(1207, params.SaleExcisableGoods ? 1 : 0, 1);
            printer.fsWriteTag(1193, params.SignOfGambling ? 1 : 0, 1);
            printer.fsWriteTag(1126, params.SignOfLottery ? 1 : 0, 1);

            // Принтер уже знает в корпуесе он принтера или автомата
            // printer.fsWriteTag(1221, params.PrinterAutomatic ? 1 : 0, 1);
        }

        if (isMobile(printer)) {
            if (printer.readLongPrinterStatus().getFirmwareBuild() < 20025) {
                printer.fsWriteTag(1117, params.SenderEmail);
            }
        }

        printer.fsFiscalization(inn, rnm, taxSystemCode, operationMode);
    }

    /**
     * Перерегистрация ФН
     */
    public void refiscalizeFS(ShtrihFiscalPrinter printer, ParametersFiscal params) throws Exception {

        syncDateTime(printer);

        fillDeviceTables(printer, params);

        reаiscalizeDevice(printer, params);
    }

    private void reаiscalizeDevice(ShtrihFiscalPrinter printer, ParametersFiscal params) throws Exception {
        String inn = formatInn(params.VATIN);
        String rnm = formatRnm(params.KKTNumber);
        int taxSystemCode = getTaxSystem(params.TaxVariant);
        int operationMode = getOperationMode(params);
        int agentType = getAgentType(params.SignOfAgent);
        long infoReasonCodes = getInfoReasonCodes(params.InfoChangesReasonsCodes);

        Log.d("ShtrihJpos1CDriver", String.format("'%s' '%s' '%d' '%d'", inn, rnm, taxSystemCode, operationMode));

        printer.fsStartFiscalization(2);

        if (agentType != 0)
            printer.fsWriteTag(1057, agentType, 1);

        writeVATINTagIfNotNullAndNotEmpty(printer, 1203, params.CashierVATIN);

        if ((isDesktop(printer) && printer.readLongPrinterStatus().getFirmwareBuild() > 46150) || isCashCore(printer)) {

            int extendedModes = getExtOperationMode(params);

            int fsTableNumber = readPrinterModelParameters(printer).getFsTableNumber();

            printer.writeTable(fsTableNumber, 1, 21, "" + extendedModes);

            /*
               1205 актуален только для ФН1.1 при работе по ФФД 1.1 и задается через таблицу при фискализации
               по 1.05 и 1.0 вместо него 1101
               они взаимоисключающие
            */
            printer.writeTable(fsTableNumber, 1, 22, "" + infoReasonCodes);

        } else {

            printer.fsWriteTag(1207, params.SaleExcisableGoods ? 1 : 0, 1);
            printer.fsWriteTag(1193, params.SignOfGambling ? 1 : 0, 1);
            printer.fsWriteTag(1126, params.SignOfLottery ? 1 : 0, 1);

            // Принтер уже знает в корпуесе он принтера или автомата
            // printer.fsWriteTag(1221, params.PrinterAutomatic ? 1 : 0, 1);
        }

        if (isMobile(printer)) {
            if (printer.readLongPrinterStatus().getFirmwareBuild() < 20025) {
                printer.fsWriteTag(1117, params.SenderEmail);
            }
        }

        printer.fsReFiscalization(inn, rnm, taxSystemCode, operationMode, params.ReasonCode);
    }

    private void syncDateTime(ShtrihFiscalPrinter printer) throws Exception {
        Calendar c = Calendar.getInstance();

        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR) - 2000;

        PrinterDate date = new PrinterDate(day, month, year);

        Log.d("ShtrihJpos1CDriverTests", date.toString());

        printer.writeDate(date);
        printer.confirmDate(date);

        int seconds = c.get(Calendar.SECOND);
        int minutes = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR);

        PrinterTime time = new PrinterTime(hour, minutes, seconds);

        Log.d("ShtrihJpos1CDriverTest", time.toString());

        printer.writeTime(time);
    }

    private void fillDeviceTables(ShtrihFiscalPrinter printer, ParametersFiscal params) throws Exception {

        PrinterModelParameters model = readPrinterModelParameters(printer);
        int fsTable = model.getFsTableNumber();

        final String targetFFDVersion = "2"; // 1.05

        if (model.capFFDTableAndColumnNumber()) {
            printer.writeTable(model.getFFDTableNumber(), 1, model.getFFDColumnNumber(), targetFFDVersion);
        } else {
            if (isDesktop(printer)) {
                printer.writeTable(17, 1, 17, targetFFDVersion);
            } else {
                printer.writeTable(10, 1, 4, targetFFDVersion);
            }
        }

        if (isDesktop(printer)) {
            printer.writeTable(fsTable, 1, 7, firstPart(params.OrganizationName));
            printer.writeTable(fsTable, 1, 17, secondPart(params.OrganizationName));
            printer.writeTable(fsTable, 1, 9, firstPart(params.AddressSettle));
            printer.writeTable(fsTable, 1, 18, secondPart(params.AddressSettle));
            printer.writeTable(fsTable, 1, 10, firstPart(params.OFDOrganizationName));
            printer.writeTable(fsTable, 1, 19, secondPart(params.OFDOrganizationName));
        } else {
            printer.writeTable(fsTable, 1, 7, params.OrganizationName);
            printer.writeTable(fsTable, 1, 9, params.AddressSettle);
            printer.writeTable(fsTable, 1, 10, params.OFDOrganizationName);
        }

        printer.writeTable(fsTable, 1, 8, params.CashierName);

        printer.writeTable(fsTable, 1, 14, params.PlaceSettle);

        printer.writeTable(fsTable, 1, 12, formatInn(params.OFDVATIN));
        printer.writeTable(fsTable, 1, 13, params.FNSWebSite);

        if (isMobile(printer)) {
            if (printer.readLongPrinterStatus().getFirmwareBuild() >= 20025) {
                printer.writeTable(fsTable, 1, 15, params.SenderEmail);
            }
        } else {
            printer.writeTable(fsTable, 1, 15, params.SenderEmail);
        }

        if (params.AutomaticMode) {
            if (model.getEmbeddableAndInternetDeviceTableNumber() > 0) {
                printer.writeTable(model.getEmbeddableAndInternetDeviceTableNumber(), 1, 1, params.AutomaticNumber);
            } else {
                printer.writeTable(24, 1, 1, params.AutomaticNumber);
            }
        }
    }

    private static final int firstPartLength = 128;

    private String firstPart(String value) {
        if (value.length() > firstPartLength)
            return value.substring(0, firstPartLength);

        return value;
    }

    private String secondPart(String value) {
        if (value.length() > firstPartLength)
            return value.substring(firstPartLength);

        return "";
    }

    private String formatRnm(String rnm) {
        return ExtendWithSpacesToFixedLength(rnm, 20);
    }

    private String formatInn(String inn) {
        return ExtendWithSpacesToFixedLength(inn, 12);
    }

    private String ExtendWithSpacesToFixedLength(String value, int length) {
        if (value.length() >= length)
            return value;

        return value + new String(new char[length - value.length()]).replace('\0', ' ');
    }

    private int getOperationMode(ParametersFiscal params) {
        long result = 0;

        if (params.DataEncryption)
            result = result | BitUtils.setBit(0);

        if (params.OfflineMode)
            result = result | BitUtils.setBit(1);

        if (params.AutomaticMode)
            result = result | BitUtils.setBit(2);

        if (params.ServiceSign)
            result = result | BitUtils.setBit(3);

        if (params.BSOSing)
            result = result | BitUtils.setBit(4);

        if (params.CalcOnlineSign)
            result = result | BitUtils.setBit(5);

        return (int) result;
    }

    private int getExtOperationMode(ParametersFiscal params) {
        long result = 0;

        if (params.SaleExcisableGoods)
            result = result | BitUtils.setBit(0);

        if (params.SignOfGambling)
            result = result | BitUtils.setBit(1);

        if (params.SignOfLottery)
            result = result | BitUtils.setBit(2);

        if (params.PrinterAutomatic)
            result = result | BitUtils.setBit(3);

        return (int) result;
    }

    private int getTaxSystem(String taxVariant) {
        long result = 0;

        for (String part : taxVariant.split(",")) {
            switch (part.trim()) {
                case "0":
                    result = result | BitUtils.setBit(0);
                case "1":
                    result = result | BitUtils.setBit(1);
                case "2":
                    result = result | BitUtils.setBit(2);
                case "3":
                    result = result | BitUtils.setBit(3);
                case "4":
                    result = result | BitUtils.setBit(4);
                case "5":
                    result = result | BitUtils.setBit(5);
            }
        }

        return (int) result;
    }

    private int getAgentType(String agentType) {
        long result = 0;

        for (String part : agentType.split(",")) {
            switch (part.trim()) {
                case "0":
                    result = result | BitUtils.setBit(0);
                case "1":
                    result = result | BitUtils.setBit(1);
                case "2":
                    result = result | BitUtils.setBit(2);
                case "3":
                    result = result | BitUtils.setBit(3);
                case "4":
                    result = result | BitUtils.setBit(4);
                case "5":
                    result = result | BitUtils.setBit(5);
                case "6":
                    result = result | BitUtils.setBit(6);
            }
        }

        return (int) result;
    }

    private int getInfoReasonCodes(String agentType) {
        long result = 0;

        for (String part : agentType.split(",")) {
            switch (part.trim()) {
                case "0":
                    result = result | BitUtils.setBit(0);
                case "1":
                    result = result | BitUtils.setBit(1);
                case "2":
                    result = result | BitUtils.setBit(2);
                case "3":
                    result = result | BitUtils.setBit(3);
                case "4":
                    result = result | BitUtils.setBit(4);
                case "5":
                    result = result | BitUtils.setBit(5);
                case "6":
                    result = result | BitUtils.setBit(6);
                case "7":
                    result = result | BitUtils.setBit(7);
                case "8":
                    result = result | BitUtils.setBit(8);
                case "9":
                    result = result | BitUtils.setBit(9);
                case "10":
                    result = result | BitUtils.setBit(10);
                case "11":
                    result = result | BitUtils.setBit(11);
                case "12":
                    result = result | BitUtils.setBit(12);
                case "13":
                    result = result | BitUtils.setBit(13);
                case "14":
                    result = result | BitUtils.setBit(14);
                case "15":
                    result = result | BitUtils.setBit(15);
                case "16":
                    result = result | BitUtils.setBit(16);
                case "17":
                    result = result | BitUtils.setBit(17);
                case "18":
                    result = result | BitUtils.setBit(18);
                case "19":
                    result = result | BitUtils.setBit(19);
                case "20":
                    result = result | BitUtils.setBit(20);
                case "21":
                    result = result | BitUtils.setBit(21);
                case "31":
                    result = result | BitUtils.setBit(31);
            }
        }

        return (int) result;
    }

    /**
     * Закрытие ФН
     */
    public void closeFS(ShtrihFiscalPrinter printer, ParametersFiscal params) throws Exception {

        printer.fsStartFiscalClose();

        writeVATINTagIfNotNullAndNotEmpty(printer, 1203, params.CashierVATIN);

        printer.fsPrintFiscalClose();
    }

    private boolean isDesktop(ShtrihFiscalPrinter printer) throws JposException {
        DeviceMetrics metrics = printer.readDeviceMetrics();
        return metrics.getModel() != 19 && // Штрих-МОБАЙЛ
                metrics.getModel() != 45;  // КЯ
    }

    private boolean isMobile(ShtrihFiscalPrinter printer) throws JposException {
        DeviceMetrics metrics = printer.readDeviceMetrics();
        return metrics.getModel() == 19; // Штрих-МОБАЙЛ
    }

    private boolean isCashCore(ShtrihFiscalPrinter printer) throws JposException {
        DeviceMetrics metrics = printer.readDeviceMetrics();
        return metrics.getModel() == 45; // КЯ
    }

    private void writeVATINTagIfNotNullAndNotEmpty(ShtrihFiscalPrinter printer, int tagId, String value) throws Exception {
        if (value != null && !value.isEmpty())
            printer.fsWriteTag(tagId, formatInn(value));
    }

    private PrinterModelParameters readPrinterModelParameters(ShtrihFiscalPrinter printer) throws JposException {
        ReadPrinterModelParameters cmd = new ReadPrinterModelParameters();
        printer.executeCommand(cmd);

        return cmd.getParameters();
    }
}

class ParametersFiscal {

    /**
     * ФИО и должность уполномоченного лица для проведения операции
     */
    public String CashierName;

    /**
     * ИНН уполномоченного лица для проведения операции
     */
    public String CashierVATIN;

    /**
     * Регистрационный номер ККТ
     */
    public String KKTNumber;

    /**
     * Название организации
     */
    public String OrganizationName;

    /**
     * ИНН организации
     */
    public String VATIN;

    /**
     * Адрес установки ККТ для проведения расчетов
     */
    public String AddressSettle;

    /**
     * Место проведения расчетов
     */
    public String PlaceSettle;

    /**
     * Коды системы налогообложения через разделитель ",".
     * Коды системы налогообложения приведены в таблице "Системы налогообложения".
     */
    public String TaxVariant;

    /**
     * Признак автономного режима
     */
    public boolean OfflineMode;

    /**
     * Признак шифрование данных
     */
    public boolean DataEncryption;

    /**
     * Признак расчетов за услуги
     */
    public boolean ServiceSign;

    /**
     * Продажа подакцизного товара
     */
    public boolean SaleExcisableGoods;

    /**
     * Признак проведения азартных игр
     */
    public boolean SignOfGambling;

    /**
     * Признак проведения лотереи
     */
    public boolean SignOfLottery;

    /**
     * Коды признаков агента через разделитель ",".
     * (Коды приведены в таблице 10 форматов фискальных данных)
     */
    public String SignOfAgent;

    /**
     * Признак формирования только БСО
     */
    public boolean BSOSing;

    /**
     * Признак ККТ для расчетов только в Интернет
     */
    public boolean CalcOnlineSign;

    /**
     * Признак установки принтера в автомате
     */
    public boolean PrinterAutomatic;

    /**
     * Признак автоматического режима
     */
    public boolean AutomaticMode;

    /**
     * Номер автомата для автоматического режима
     */
    public String AutomaticNumber;

    /**
     * Код причины перерегистрации
     * * указывается только для операции "Изменение параметров регистрации"
     * (Коды приведены в таблице 15 форматов фискальных данных)
     */
    public int ReasonCode;

    /**
     * Коды причин изменения сведений о ККТ через разделитель ".
     * (Коды приведены в таблице 16 форматов фискальных данных)
     */
    public String InfoChangesReasonsCodes;

    /**
     * Название организации ОФД
     */
    public String OFDOrganizationName;

    /**
     * ИНН организации ОФД
     */
    public String OFDVATIN;

    /**
     * Адрес сайта уполномоченного органа (ФНС) в сети «Интернет»
     */
    public String FNSWebSite;

    /**
     * Адрес электронной почты отправителя чека
     */
    public String SenderEmail;
}
