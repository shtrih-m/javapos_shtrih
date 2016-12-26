/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.monitoring;

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
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.util.CompositeLogger;

public class MonitoringServerX5 implements Runnable {

    private int port = 0;
    private boolean isStarted = false;
    private final FiscalPrinterImpl service;
    private static CompositeLogger logger = CompositeLogger.getLogger(MonitoringServerX5.class);

    private static final int ReadTimeout = 3000;
    private static final int AcceptTimeout = 3000;

    public MonitoringServerX5(FiscalPrinterImpl service) {
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
                        if (command == null)
                            continue;
                        String answer = MonitoringCommands.runCommand(command, service);
                        if (answer.length() > 0) {
                            writer.write(answer);
                            writer.newLine();
                            writer.flush();
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
}
