/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.port;

import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Localizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * @author V.Kravtsov
 */
public class SocketPort implements PrinterPort {

    private Socket socket = null;
    private String portName = "";
    private int readTimeout = 1000;
    private int openTimeout = 1000;
    static CompositeLogger logger = CompositeLogger.getLogger(SocketPort.class);

    public SocketPort() throws Exception {
    }

    public void open() throws Exception {
        open(openTimeout);
    }

    public boolean isConnected() {
        return socket != null;
    }

    private InputStream inputStream;
    private OutputStream outputStream;

    public void open(int timeout) throws Exception {
        if (isConnected()) {
            return;
        }

        this.openTimeout = timeout;
        socket = (Socket) SharedObjects.getInstance().findObject(portName);
        if (socket == null) {
            socket = new Socket();
            SharedObjects.getInstance().add(socket, portName);

            socket.setReuseAddress(true);
            socket.setSoTimeout(openTimeout);
            socket.setTcpNoDelay(true);

            StringTokenizer tokenizer = new StringTokenizer(portName, ":");
            String host = tokenizer.nextToken();
            int port = Integer.parseInt(tokenizer.nextToken());
            socket.connect(new InetSocketAddress(host, port), timeout);
        }

        SharedObjects.getInstance().addref(portName);

        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    public void close() {
        if (!isConnected()) {
            return;
        }

        SharedObjects.getInstance().release(portName);

        try {

            socket.close();
            
            socket = null;
            inputStream = null;
            outputStream = null;

            Thread.sleep(100);
        } catch (Exception e) {
            socket = null;
            inputStream = null;
            outputStream = null;
        }
    }

    public int readByte() throws Exception {
        checkLock();
        open();

        int b = doReadByte();
        return b;
    }

    public int doReadByte() throws Exception {
        checkLock();
        open();

        InputStream in = inputStream;
        int result;
        long startTime = System.currentTimeMillis();
        for (; ; ) {
            long currentTime = System.currentTimeMillis();
            if (in.available() > 0) {
                result = in.read();
                if (result >= 0) {
                    return result;
                }
            }
            if ((currentTime - startTime) > readTimeout) {
                noConnectionError();
            }
        }
    }

    private void noConnectionError() throws Exception {
        throw new IOException(Localizer.getString(Localizer.NoConnection));
    }

    public byte[] readBytes(int len) throws Exception {
        checkLock();
        open();

        InputStream in = inputStream;
        byte[] data = new byte[len];
        int offset = 0;
        while (len > 0) {
            int count = in.read(data, offset, len);
            if (count == -1) {
                break;
            }
            len -= count;
            offset += count;
        }
        return data;
    }

    public void write(byte[] b) throws Exception {

        checkLock();

        OutputStream out = outputStream;
        for (int i = 0; i < 2; i++) {
            try {
                open();
                out.write(b);
                out.flush();
                return;
            } catch (Exception e) {
                close();
                if (i == 1) {
                    throw e;
                }
                logger.error(e);
            }
        }
    }

    public void write(int b) throws Exception {
        checkLock();
        open();
        byte[] data = new byte[1];
        data[0] = (byte) b;
        write(data);
    }

    public void setBaudRate(int baudRate) throws Exception {
    }

    public void setTimeout(int timeout) throws Exception {
        this.readTimeout = timeout;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) throws Exception {
        if (portName.equalsIgnoreCase(this.portName)) {
            return;
        }
        close();
        this.portName = portName;
    }

    public Object getSyncObject() throws Exception {
        return socket;
    }

    public boolean isSearchByBaudRateEnabled() {
        return false;
    }

    public void checkLock() throws Exception {
        if (!Thread.holdsLock(socket)) {
            throw new Exception("Not locked");
        }
    }
}
