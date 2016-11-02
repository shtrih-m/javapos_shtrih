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
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

public class MonitoringServer implements Runnable {

    private int port = 0;
    private boolean isStarted = false;
    private final FiscalPrinterImpl service;
    private static CompositeLogger logger = CompositeLogger.getLogger(MonitoringServer.class);

    ServerSocket serverSocket = null;
    Thread thread = null;
    
    public MonitoringServer(FiscalPrinterImpl service) {
        this.service = service;
    }

    public boolean isStarted() {
        return serverSocket != null;
    }

    public void start(int port) {
        stop();
        try {
            this.port = port;
            serverSocket = new ServerSocket(port);            
            thread = new Thread(this);
            thread.start();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void stop() {
        if (serverSocket == null)
            return;
        try {
            serverSocket.close();
            thread.join();
            serverSocket = null;
        } catch (Exception e) {
            logger.error(e);
        }
    }

    class ClientSession implements Runnable {

        public ClientSession(Socket s) {
            this.socket = s;
        }
        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Hello(out);
                while(true) {
                    String cmd = reader.readLine();
                    if (cmd == null)
                        continue;
                    String answer = runCommand(cmd);
                    out.println(answer);                
                }
            } catch (IOException e) {
            }
        }
        
        private void Hello(PrintWriter out) {
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
        }
        
        Socket socket = null;
    }
    
    public void run() {
        try {
            while (true){
                Socket socket = serverSocket.accept();
                Thread session = new Thread(new ClientSession(socket));
                session.start();
            }
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
        return text;
    }

}
