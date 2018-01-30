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
        socket = new Socket();
    }

    public void open() throws Exception {
        open(openTimeout);
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public void checkLocked() throws Exception {
        if (!Thread.holdsLock(getSyncObject())) {
            throw new Exception("Not locked");
        }
    }

    public void open(int timeout) throws Exception {
        if (isConnected()) {
            return;
        }

        this.openTimeout = timeout;
        socket = (Socket) SharedObjects.getInstance().findObject(portName);
        if (socket == null) {
            try {
                socket = new Socket();
                socket.setReuseAddress(true);
                socket.setSoTimeout(openTimeout);
                socket.setTcpNoDelay(true);

                StringTokenizer tokenizer = new StringTokenizer(portName, ":");
                String host = tokenizer.nextToken();
                int port = Integer.parseInt(tokenizer.nextToken());
                socket.connect(new InetSocketAddress(host, port), timeout);
                SharedObjects.getInstance().add(socket, portName);
            } catch (Exception e) {
                SharedObjects.getInstance().release(portName);
                throw e;
            }
        }
        SharedObjects.getInstance().addref(portName);
    }

    public void close() {
        SharedObjects.getInstance().release(portName);
        try {
            socket.close();
            Thread.sleep(100);
        } catch (Exception e) {
        }
        socket = new Socket();
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
            InputStream in = socket.getInputStream();
            if (in == null){
                throw new Exception("InputStream = null");
            }
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
        open();

        byte[] data = new byte[len];
        int offset = 0;
        while (len > 0) {
            InputStream in = socket.getInputStream();
            if (in == null){
                throw new Exception("InputStream = null");
            }
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
        open();
        for (int i = 0; i < 2; i++) {
            try {
                open();
                OutputStream out = socket.getOutputStream();
                if (out == null){
                    throw new Exception("OutputStream = null");
                }
                out.write(b);
                out.flush();
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
}
