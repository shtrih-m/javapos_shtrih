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
        if ((command != null) && (!command.equals(""))) {
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
            } catch (Exception e) {
            }
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
        String text = service.getDeviceMetrics().getDeviceName() + ","
                + service.getLongStatus().getSerial() + ","
                + service.getEJStatus().getSerialNumber();
        return text;
    }

    // ECTP Номер ЭКЛЗ, Дата активизации ЭКЛЗ, Время Активизации
    public String getECTPText() throws Exception {
        EJActivation activation = service.getEJActivation();
        String text = activation.getEJSerial() + "," + activation.getDate()
                + "," + activation.getTime();
        return text;
    }

}
