package com.shtrih.fiscalprinter.port;

import java.io.*;
import java.net.*;
import java.util.*;
import com.shtrih.util.*;

/**
 * @author V.Kravtsov
 */
public class SocketPort implements PrinterPort 
{
    private Socket socket = null;
    private final String portName;
    private int readTimeout = 1000;
    private int openTimeout = 1000;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    static CompositeLogger logger = CompositeLogger.getLogger(SocketPort.class);
    private static Map<String, SocketPort> items = new HashMap<String, SocketPort>();

    public static synchronized SocketPort getInstance(String portName, int openTimeout) throws Exception {
        SocketPort item = items.get(portName);
        if (item == null) {
            item = new SocketPort(portName, openTimeout);
            items.put(portName, item);
        }
        return item;
    }

    private SocketPort(String portName, int openTimeout) throws Exception {
        this.portName = portName;
        this.openTimeout = openTimeout;
        logger.debug("SocketPort(" + portName + ")");
    }

    public void open() throws Exception {
        open(openTimeout);
    }

    public boolean isConnected() {
        return socket != null;
    }

    public void checkLocked() throws Exception {
        if (!Thread.holdsLock(getSyncObject())) {
            throw new Exception("Not locked");
        }
    }

    public void open(int timeout) throws Exception {
        checkLocked();
        if (isConnected()) {
            return;
        }

        long startTime = System.currentTimeMillis();
        for (;;) {
            try {
                doOpen(timeout);
                return;
            } catch (Exception e) {
                long currentTime = System.currentTimeMillis();
                if ((currentTime - startTime) > timeout) {
                    throw e;
                }
            }
        }
    }

    private void doOpen(int timeout) throws Exception {
        socket = new Socket();
        socket.setReuseAddress(true);
        socket.setSoTimeout(openTimeout);
        socket.setTcpNoDelay(true);

        StringTokenizer tokenizer = new StringTokenizer(portName, ":");
        String host = tokenizer.nextToken();
        int port = Integer.parseInt(tokenizer.nextToken());
        socket.connect(new InetSocketAddress(host, port), timeout);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    public void close() {
        if (!isConnected()) {
            return;
        }
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
            Thread.sleep(100);
        } catch (Exception e) {
        }
        socket = null;
        inputStream = null;
        outputStream = null;
    }

    public int readByte() throws Exception {
        open();
        int b = doReadByte();
        return b;
    }

    public int doReadByte() throws Exception {
        open();

        int result;
        long startTime = System.currentTimeMillis();
        for (;;) {
            long currentTime = System.currentTimeMillis();
            if (inputStream.available() > 0) {
                result = inputStream.read();
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
        open();

        byte[] data = new byte[len];
        int offset = 0;
        while (len > 0) {
            int count = inputStream.read(data, offset, len);
            if (count == -1) {
                break;
            }
            len -= count;
            offset += count;
        }
        return data;
    }

    public void write(byte[] b) throws Exception {
        open();
        for (int i = 0; i < 2; i++) {
            try {
                open();
                outputStream.write(b);
                outputStream.flush();
                return;
            } catch (Exception e) {
                close();
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
        this.readTimeout = timeout;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) throws Exception {

    }

    public Object getSyncObject() throws Exception {
        return this;
    }

    public boolean isSearchByBaudRateEnabled() {
        return false;
    }
}
