/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import com.shtrih.util.CompositeLogger;
import com.shtrih.ej.EJActivation;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.FSReadCommStatus;
import com.shtrih.fiscalprinter.command.FSReadFiscalization;
import com.shtrih.fiscalprinter.command.FSReadStatus;
import com.shtrih.fiscalprinter.command.LongPrinterStatus;
import com.shtrih.util.StringUtils;
import java.util.Calendar;
import java.util.Date;

public class MonitoringServer implements Runnable {

    private int port = 0;
    private boolean isStarted = false;
    private final FiscalPrinterImpl service;
    private static CompositeLogger logger = CompositeLogger.getLogger(MonitoringServer.class);

    private static final int ReadTimeout = 3000;
    private static final int AcceptTimeout = 3000;

    public MonitoringServer(FiscalPrinterImpl service) {
        this.service = service;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void start(int port) {
        try {
            this.port = port;
            Thread thread = new Thread(this);
            thread.start();
            isStarted = true;
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void stop() {
        try {
            isStarted = false;
        } catch (Exception e) {
            logger.error(e);
        }
    }

    
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (isStarted) {
                try {
                    serverSocket.setSoTimeout(AcceptTimeout);
                    Socket socket = serverSocket.accept();
                    InputStream sin = socket.getInputStream();
                    try {
                        socket.setSoTimeout(ReadTimeout);
                        OutputStream sout = socket.getOutputStream();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(sin));
                        BufferedWriter writer = new BufferedWriter(
                                new OutputStreamWriter(sout));
                        String command = reader.readLine();
                        if (command != null) {
                            String answer = runCommand(command);
                            if (answer.length() > 0) {
                                writer.write(answer);
                                writer.newLine();
                                writer.flush();
                            }
                        }
                    } catch (IOException e) {
                    }
                    socket.shutdownOutput();
                    while (sin.read() >= 0) {
                    }
                    socket.close();
                } catch (IOException e) {
                }
            }
            serverSocket.close();
        } catch (IOException e) {
            logger.error("run, ", e);
        }
    }

    private String runCommand(String command) {
        if (command == null || command.equals(""))
            return "";
        try {
            if (command.equals("STATUS")) {
                return getStatusText();
            }
            if (command.equals("INFO")) {
                return getInfoText();
            }
            if (command.equals("ECTP")) {
                return getECTPText();
            }
            if (command.equals("FN")) {
                return getFNText();
            }
            if (command.equals("FN_UNIXTIME")) {
                return getFNUnixTimeText();
            }   
            if (command.equals("CNT_QUEUE")) {
                return getCntQueueText();
            }
            if (command.equals("DATE_LAST")) {
                return getDateLastText();
            }
            if (command.startsWith("CASH_REG")) {
                return getCashRegText(command);
            }      
            if (command.startsWith("OPER_REG")) {
                return getOperRegText(command);
            }                     
        } catch (Exception e) {
        }
        return "";
    }

    public String getStatusText() throws Exception {
        SMFiscalPrinter printer = service.getPrinter();
        if (printer.getResultCode() == 0) {
            return "OK";
        } else {
            return "Error " + printer.getResultText();
        }
    }

    // INFO Модель ККМ, Зав. № ККМ, Номер ЭКЛЗ
    public String getInfoText() throws Exception {
        LongPrinterStatus status = service.getLongStatus();
        String text = service.getDeviceMetrics().getDeviceName() + ","
                + status.getSerial() + ",";
        if (status.getPrinterFlags().isEJPresent())
            text += service.getEJStatus().getSerialNumber();
        else
            text += service.getPrinter().fsReadStatus().getFsSerial();
        return text;
    }

    // ECTP Номер ЭКЛЗ, Дата активизации ЭКЛЗ, Время Активизации
    public String getECTPText() throws Exception {
        EJActivation activation = service.getEJActivation();
        String text = activation.getEJSerial() + "," + activation.getDate()
                + "," + activation.getTime();
        return text;
    }

    private String getFNText() throws Exception {
        FSReadStatus status = service.getPrinter().fsReadStatus();
        FSReadFiscalization param = service.getPrinter().fsReadFiscalization();
        String text = status.getFsSerial() + ";" + param.getDate() + ";"
                + param.getTime();
        return text;
    }

    private String getFNUnixTimeText() throws Exception {
        FSReadFiscalization param = service.getPrinter().fsReadFiscalization();
        Calendar calendar = Calendar.getInstance();
        calendar.set(param.getDate().getYear(), param.getDate().getMonth(), 
                param.getDate().getDay(), param.getTime().getHour(), 
                param.getTime().getMin(), param.getTime().getSec());
        String text = String.valueOf(calendar.getTimeInMillis() / 1000);
        return text;
    }

    private String getCntQueueText() throws Exception {
        FSReadCommStatus status = service.getPrinter().fsReadCommStatus();
        String text = String.valueOf(status.getQueueSize());
        return text;
    }

    private String getDateLastText() throws Exception {
        FSReadCommStatus status = service.getPrinter().fsReadCommStatus();
        String text = status.getDocumentDate() + ";" + status.getDocumentTime();
        return text;
    }

    private String getCashRegText(String command) throws Exception {
        String[] params = StringUtils.split(command, ' ');
        if (params.length < 2)
            return "WRONG_PARAM";
        long reg = service.getPrinter().readCashRegister(Integer.parseInt(params[1]));
        String text = String.valueOf(reg);
        return text;
    }

    private String getOperRegText(String command) throws Exception {
        String[] params = StringUtils.split(command, ' ');
        if (params.length < 2)
            return "WRONG_PARAM";
        int reg = service.getPrinter().readOperationRegister(Integer.parseInt(params[1]));
        String text = String.valueOf(reg);
        return text;    }

}
