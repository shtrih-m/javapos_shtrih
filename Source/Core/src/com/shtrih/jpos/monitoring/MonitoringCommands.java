/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.monitoring;

import com.shtrih.ej.EJActivation;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.FSReadCommStatus;
import com.shtrih.fiscalprinter.command.FSReadFiscalization;
import com.shtrih.fiscalprinter.command.FSReadStatus;
import com.shtrih.fiscalprinter.command.LongPrinterStatus;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.util.StringUtils;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 *
 * @author Roman
 */
public class MonitoringCommands {

    public static void Help(PrintWriter out) {
        out.println("=============================");
        out.println("  Welcome to Shtrih JavaPOS  ");
        out.println("=============================");
        out.println("Commands:");
        out.println(" STATUS");
        out.println(" INFO");
        out.println(" FN");
        out.println(" FN_UNIXTIME");
        out.println(" CNT_QUEUE");
        out.println(" DATE_LAST");
        out.println(" CASH_REG regNumber");
        out.println(" OPER_REG regNumber");
        out.println("=============================");
        out.println("");
    }

    public static String runCommand(String command, FiscalPrinterImpl service) {
        if (command == null || command.equals("")) {
            return "";
        }
        try {
            if (command.equals("STATUS")) {
                return getStatusText(service);
            }
            if (command.equals("INFO")) {
                return getInfoText(service);
            }
            if (command.equals("ECTP")) {
                return getECTPText(service);
            }
            if (command.equals("FN")) {
                return getFNText(service);
            }
            if (command.equals("FN_UNIXTIME")) {
                return getFNUnixTimeText(service);
            }
            if (command.equals("CNT_QUEUE")) {
                return getCntQueueText(service);
            }
            if (command.equals("DATE_LAST")) {
                return getDateLastText(service);
            }
            if (command.startsWith("CASH_REG")) {
                return getCashRegText(command, service);
            }
            if (command.startsWith("OPER_REG")) {
                return getOperRegText(command, service);
            }
            if (command.startsWith("OFD_SETTING")) {
                return getOFDSettingsText(service);
            }
        } catch (Exception e) {
        }
        return "";
    }

    private static String getStatusText(FiscalPrinterImpl service) throws Exception {
        SMFiscalPrinter printer = service.getPrinter();
        if (printer.getResultCode() == 0) {
            return "OK";
        } else {
            return "Error " + printer.getResultText();
        }
    }

    // INFO Модель ККМ, Зав. № ККМ, Номер ЭКЛЗ/Номер ФН
    private static String getInfoText(FiscalPrinterImpl service) throws Exception {
        LongPrinterStatus status = service.getPrinter().getLongStatus();
        String text = service.getDeviceMetrics().getDeviceName() + ","
                + status.getSerial() + ",";
        if (service.getPrinter().getCapFiscalStorage()) {
            text += service.getPrinter().fsReadStatus().getFsSerial();
        } else {
            text += service.getEJStatus().getSerialNumber();
        }
        return text;
    }

    // ECTP Номер ЭКЛЗ, Дата активизации ЭКЛЗ, Время Активизации
    private static String getECTPText(FiscalPrinterImpl service) throws Exception {
        EJActivation activation = service.getEJActivation();
        String text = activation.getEJSerial() + "," + activation.getDate()
                + "," + activation.getTime();
        return text;
    }

    // FN Номер ФН, Дата активизации ФН, Время Активизации ФН 
    private static String getFNText(FiscalPrinterImpl service) throws Exception {
        FSReadStatus status = service.getPrinter().fsReadStatus();
        FSReadFiscalization param = service.getPrinter().fsReadFiscalization();
        String text = status.getFsSerial().trim() + "," + param.getDate() + ","
                + param.getTime();
        return text;
    }

    // FN_UNIXTIME Дата активизации ФН в UNIXTIME
    private static String getFNUnixTimeText(FiscalPrinterImpl service) throws Exception {
        FSReadFiscalization param = service.getPrinter().fsReadFiscalization();
        Calendar calendar = Calendar.getInstance();
        calendar.set(param.getDate().getYear() + 2000, param.getDate().getMonth() - 1,
                param.getDate().getDay(), param.getTime().getHour(),
                param.getTime().getMin(), param.getTime().getSec());
        calendar.set(Calendar.MILLISECOND, 0);
        String text = String.valueOf(calendar.getTimeInMillis() / 1000L);
        return text;
    }

    // CNT_QUEUE Кол-во документов, ожидающих отправки в ОФД (очередь)
    private static String getCntQueueText(FiscalPrinterImpl service) throws Exception {
        FSReadCommStatus status = service.getPrinter().fsReadCommStatus();
        String text = String.valueOf(status.getQueueSize());
        return text;
    }

    // DATE_LAST Дата последнего отправленного документа в ОФД
    private static String getDateLastText(FiscalPrinterImpl service) throws Exception {
        FSReadCommStatus status = service.getPrinter().fsReadCommStatus();
        String text = status.getDocumentDate() + "," + status.getDocumentTime();
        return text;
    }

    // CASH_REG Номер регистра
    // Содержимое денежного регистра
    private static String getCashRegText(String command, FiscalPrinterImpl service) throws Exception {
        String[] params = StringUtils.split(command, ' ');
        if (params.length < 2) {
            return "WRONG_PARAM";
        }
        long reg = service.getPrinter().readCashRegister(Integer.parseInt(params[1]));
        String text = String.valueOf(reg);
        return text;
    }

    // OPER_REG Номер регистра
    // Содержимое операционного регистра
    private static String getOperRegText(String command, FiscalPrinterImpl service) throws Exception {
        String[] params = StringUtils.split(command, ' ');
        if (params.length < 2) {
            return "WRONG_PARAM";
        }
        int reg = service.getPrinter().readOperationRegister(Integer.parseInt(params[1]));
        String text = String.valueOf(reg);
        return text;
    }

    // Таблица 19, Параметры офд
    // Номер таблицы,Ряд,Поле,Размер поля,Тип поля,Мин. значение, Макс.значение, Название,Значение
    //19,1,1,64,1,0,255,'Сервер','connect.ofd-ya.ru'
    //19,1,2,2,0,0,65535,'Порт','7779'
    //19,1,3,2,0,0,65535,'Таймаут чтения ответа','1000'
    //Выводит настройки ОФД из таблицы 19, "Параметры ОФД".
    //Пример: connect.ofd-ya.ru:7779 (Формат: сервер:порт)
    
    private static String getOFDSettingsText(FiscalPrinterImpl service) 
            throws Exception 
    {
        String result = "";
        SMFiscalPrinter printer = service.getPrinter();
        if (printer.getCapFiscalStorage()) {
            String server = printer.readTable(19, 1, 1);
            String port = printer.readTable(19, 1, 2);
            result = server + ":" + port;
        }
        return result;
    }

}
