/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.port;

import com.shtrih.LibManager;
import com.shtrih.NativeResource;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Localizer;
import com.shtrih.util.StaticContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import ru.shtrih_m.kktnetd.PPPConfig;

import java.util.Calendar;
import java.util.UUID;

/**
 *
 * @author Виталий
 */
public class PPPPort implements PrinterPort {

    private Socket socket = null;
    private Thread dispatchThread = null;
    private PPPThread pppThread = null;
    private int openTimeout = 3000;
    private int connectTimeout = 3000;
    private int readTimeout = 3000;
    private boolean opened = false;
    private boolean firstCommand = true;
    private String localSocketName = null;
    private static CompositeLogger logger = CompositeLogger.getLogger(PPPPort.class);

    private final FptrParameters params;
    private final PrinterPort2 printerPort;

    public PPPPort(FptrParameters params, PrinterPort2 printerPort) {
        this.params = params;
        this.printerPort = printerPort;
        readTimeout = params.getByteTimeout();
    }

    public boolean isOpened() {
        return opened;
    }

    public void open(int timeout) throws Exception {
        if (isOpened()) {
            return;
        }

        firstCommand = true;
        openTimeout = timeout;
        localSocketName = UUID.randomUUID().toString();

        printerPort.setPortName(params.portName);
        printerPort.setTimeout(params.byteTimeout);
        printerPort.open(timeout);
        startPPPThread();
        openSocket();
        opened = true;
    }

    public void openSocket() throws Exception
    {
        if (socket != null){
            return;
        }
        socket = new Socket();
        socket.setTcpNoDelay(true);
        socket.setSoTimeout(connectTimeout);
        socket.connect(new InetSocketAddress("127.0.0.1", 7778));
        socket.setSoTimeout(readTimeout);
    }

    public void startPPPThread() throws Exception
    {
        if (pppThread != null) {
            return;
        }

        String libName = "libkktnetd";
        String fileName = NativeResource.getFileName(libName);
        InputStream stream = StaticContext.openResource(fileName);
        LibManager.getInstance(libName, stream);

        PPPConfig config = new PPPConfig();
        config.transport.path = params.portName;
        config.transport.type = PPPConfig.TRANSPORT_TYPE_SERIAL;
        if (!params.pppConfigFile.isEmpty()) {
            config.load(params.pppConfigFile);
        }
        pppThread = new PPPThread(config);
        pppThread.start();
        pppThread.waitForStatus("\"status\":\"RUNNING\"", 5000);
    }

    public void close()
    {
        if (!isOpened()) {
            return;
        }

        logger.debug("close");
        stopDispatchThread();
        stopPPPThread();
        closeSocket();
        printerPort.close();
        opened = false;
    }

    public void stopPPPThread()
    {
        if (pppThread == null)  return;

        pppThread.stop();
        pppThread = null;
    }


    public void stopDispatchThread() {
        if (dispatchThread == null) return;

        dispatchThread.interrupt();
        try {
            dispatchThread.join();
        } catch (InterruptedException e) {
        }
        dispatchThread = null;
    }

    public void closeSocket()
    {
        if (socket == null) {
            return;
        }

        try {
            socket.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void open() throws Exception {
        open(connectTimeout);
    }

    private void noConnectionError() throws Exception {
        throw new IOException(Localizer.getString(Localizer.NoConnection));
    }

    public int byteToInt(int B) {
        if (B < 0) {
            B = (int) (256 + B);
        }
        return B;
    }

    public int readByte() throws Exception
    {
        return byteToInt(readBytes(1)[0]);
    }

    public byte[] readBytes(int len) throws Exception
    {
        openSocket();

        firstCommand = false;
        byte[] data = new byte[len];
        int offset = 0;
        while (len > 0) {
            int count = socket.getInputStream().read(data, offset, len);
            if (count < 0) {
                closeSocket();
                noConnectionError();
            }
            len -= count;
            offset += count;
        }
        return data;
    }

    public void write(byte[] b) throws Exception {
        for (int i = 0; i < 2; i++) {
            try {
                openSocket();
                socket.getOutputStream().write(b);
                socket.getOutputStream().flush();
                return;
            } catch (Exception e) {
                closeSocket();
                if (i == 1) {
                    throw e;
                }
            }
        }
    }

    public void write(int b) throws Exception {
        open();
        byte[] data = new byte[1];
        data[0] = (byte) b;
        write(data);
    }

    public void setBaudRate(int baudRate) throws Exception {
    }


    public void setTimeout(int timeout) throws Exception {
        readTimeout = timeout;
        if (firstCommand){
            readTimeout = timeout + 100000; // !!!
        }

        if (socket != null)
        {
            logger.debug("socket.setSoTimeout(" + readTimeout + ")");
            socket.setSoTimeout(readTimeout);
        }
    }

    public String getPortName() {
        return printerPort.getPortName();
    }

    public void setPortName(String portName) throws Exception {
        printerPort.setPortName(portName);
    }

    public Object getSyncObject() throws Exception {
        return this;
    }

    public boolean isSearchByBaudRateEnabled() {
        return false;
    }

    public String[] getPortNames()
    {
        return printerPort.getPortNames();
    }

    public void setPortEvents(IPortEvents events) {
        printerPort.setPortEvents(events);
    }

    public String readParameter(int parameterID){
        switch (parameterID){
            case PrinterPort.PARAMID_IS_RELIABLE: return "1";
            default: return null;
        }
    }
}

