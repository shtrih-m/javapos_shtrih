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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import com.shtrih.util.CompositeLogger;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import java.io.PrintWriter;

public class MonitoringServerTelnet implements Runnable {

    private int port = 0;
    private boolean isStarted = false;
    private final FiscalPrinterImpl service;
    private static CompositeLogger logger = CompositeLogger.getLogger(MonitoringServerTelnet.class);

    ServerSocket serverSocket = null;
    Thread thread = null;
    
    public MonitoringServerTelnet(FiscalPrinterImpl service) {
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
                MonitoringCommands.Help(out);
                while(true) {
                    String cmd = reader.readLine();
                    if (cmd == null)
                        continue;
                    String answer = MonitoringCommands.runCommand(cmd, service);
                    out.println(answer);                
                }
            } catch (IOException e) {
            }
        }
        
        Socket socket = null;
    }
    
    @Override
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
}
