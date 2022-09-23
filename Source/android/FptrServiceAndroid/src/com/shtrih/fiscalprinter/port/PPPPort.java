/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.port;

import com.shtrih.LibManager;
import com.shtrih.NativeResource;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.util.CircularBuffer;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Localizer;
import com.shtrih.util.StaticContext;
import com.shtrih.util.Time;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import ru.shtrih_m.kktnetd.PPPConfig;

import java.util.Calendar;
import java.util.UUID;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;

/**
 *
 * @author Виталий
 */
public class PPPPort implements PrinterPort, PrinterPort.IPortEvents {

    private IPortEvents events;
    private Socket socket = null;
    private LocalSocket localSocket = null;
    private Thread dispatchThread = null;
    private PPPThread pppThread = null;
    private int openTimeout = 3000;
    private int connectTimeout = 3000;
    private int readTimeout = 3000;
    private boolean opened = false;
    private boolean portOpened = false;
    private String localSocketName = null;
    private long lastTimeInMillis = 0;
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

    public synchronized void open(int timeout) throws Exception {
        if (isOpened()) return;
        logger.debug("open");
        connect(timeout);
        portOpened = true;
        logger.debug("open: OK");
    }

    public synchronized void connect(int timeout) throws Exception {
        if (isOpened()) return;
        openTimeout = timeout;
        localSocketName = UUID.randomUUID().toString();

        printerPort.setPortName(params.portName);
        printerPort.setTimeout(params.byteTimeout);
        printerPort.open(timeout);
        printerPort.setPortEvents(this);
        startPPPThread();
        openLocalSocket(timeout);
        startDispatchThread();
        pppThread.waitForPhase("PPP_PHASE_RUNNING", 60000);
        openSocket();
        opened = true;
        if (events != null) {
            events.onConnect();
        }
    }

    public void openSocket() throws Exception
    {
        /*
        if ((lastTimeInMillis != 0) && ((Calendar.getInstance().getTimeInMillis() - lastTimeInMillis) > 60000)) {
            logger.debug("Reopen connection after 60 seconds of inactivity");
            closeSocket();
        }
         */

        if (socket == null) {
            socket = new Socket();
            socket.setTcpNoDelay(true);
        }
        if (!socket.isConnected()) {
            logger.debug("socket.connect");
            socket.setSoTimeout(connectTimeout);
            socket.connect(new InetSocketAddress("127.0.0.1", 7778));
            socket.setSoTimeout(readTimeout);
            lastTimeInMillis = Calendar.getInstance().getTimeInMillis();
            logger.debug("socket.connect: OK");
        }
    }

    public void startPPPThread() throws Exception {
        if (pppThread != null) {
            return;
        }

        String libName = "libkktnetd";
        String fileName = NativeResource.getFileName(libName);
        InputStream stream = StaticContext.openResource(fileName);
        LibManager.getInstance(libName, stream);

        PPPConfig config = new PPPConfig();
        config.transport.path = localSocketName;
        config.transport.type = PPPConfig.TRANSPORT_TYPE_FORWARDER;
        if (!params.pppConfigFile.isEmpty()) {
            config.load(params.pppConfigFile);
        }
        pppThread = new PPPThread(config);
        pppThread.start();
        pppThread.waitForStatus("RUNNING", 5000);
    }

    public void openLocalSocket(int timeout) throws Exception {
        if (localSocket != null) {
            return;
        }
        logger.debug("openLocalSocket");
        localSocket = new LocalSocket();
        long time = Calendar.getInstance().getTimeInMillis() + timeout;
        for (; ; ) {
            try {
                localSocket.connect(new LocalSocketAddress(localSocketName));
                break;
            } catch (IOException e) {
                logger.error(e.getMessage());
                if (Calendar.getInstance().getTimeInMillis() > time) {
                    throw e;
                }
                Thread.sleep(100);
            }
        }
        logger.debug("openLocalSocket: OK");
    }

    public synchronized void close() {
        if (!isOpened()) return;
        logger.debug("close");
        disconnect();
        printerPort.setPortEvents(null);
        printerPort.close();
        portOpened = false;
        logger.debug("close: OK");
    }

    public synchronized void disconnect() {
        if (!isOpened()) return;

        opened = false;
        stopDispatchThread();
        stopPPPThread();
        closeLocalSocket();
        closeSocket();
        if (events != null) {
            events.onDisconnect();
        }
    }

    public void closeLocalSocket() {
        if (localSocket == null) return;

        try {
            localSocket.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        localSocket = null;
    }

    public void stopPPPThread() {
        if (pppThread == null) return;

        pppThread.stop();
        pppThread = null;
    }

    public void startDispatchThread() {
        if (dispatchThread != null) return;

        logger.debug("startDispatchThread");
        dispatchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                dispatchProc();
            }
        });
        dispatchThread.start();
        logger.debug("startDispatchThread: OK");
    }

    public void stopDispatchThread() {
        if (dispatchThread == null) return;

        logger.debug("stopDispatchThread");
        dispatchThread.interrupt();
        try {
            dispatchThread.join();
        } catch (InterruptedException e) {
        }
        dispatchThread = null;
        logger.debug("stopDispatchThread: OK");
    }

    public void closeSocket() {
        logger.debug("closeSocket");

        if (socket == null) {
            return;
        }

        try {
            socket.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        socket = null;
        logger.debug("closeSocket: OK");
    }

    public void dispatchProc() {
        logger.debug("dispatchProc.start");
        try {
            while (true) {
                Thread.sleep(0);
                dispatchPackets();
            }
        } catch (InterruptedException e) {
        } catch (Exception e) {
            logger.error("dispatchProc: ", e);
        }
        logger.debug("dispatchProc.end");
    }

    public void dispatchPackets() throws Exception {
        int count;
        byte[] data;

        // read bluetoothSocket
        count = printerPort.available();
        if (count > 0) {
            data = printerPort.read(count);
            //logger.debug("<- PPP " + Hex.toHex(data));
            OutputStream os = localSocket.getOutputStream();
            if (os != null) {
                os.write(data, 0, count);
                os.flush();
            }
        }
        // read localSocket
        InputStream is = localSocket.getInputStream();
        if (is != null) {
            count = is.available();
            if (count > 0) {
                data = new byte[count];
                count = is.read(data);
                if (count > 0) {
                    //logger.debug("-> PPP " + Hex.toHex(data));
                    printerPort.write(data);
                }
            }
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

    public int readByte() throws Exception {
        return byteToInt(readBytes(1)[0]);
    }

    public byte[] readBytes(int len) throws Exception {
        open();

        long time = Calendar.getInstance().getTimeInMillis() + readTimeout;

        byte[] data = new byte[len];
        int offset = 0;
        while (len > 0) {
            openSocket();

            int count = Math.min(len, socket.getInputStream().available());
            if (count > 0) {
                count = socket.getInputStream().read(data, offset, count);
                if (count == -1) {
                    closeSocket();
                    noConnectionError();
                }
                len -= count;
                offset += count;
            }
            lastTimeInMillis = Calendar.getInstance().getTimeInMillis();
            if (lastTimeInMillis > time) {
                noConnectionError();
            }
            Thread.sleep(0);
        }
        return data;
    }

    public void write(byte[] b) throws Exception {
        open();
        for (int i = 0; i < 2; i++) {
            try {
                openSocket();
                socket.getOutputStream().write(b);
                socket.getOutputStream().flush();
                return;
            } catch (SocketException e) {
                logger.error("write: " + e.getMessage(), e);
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
        if (isOpened()) {
            socket.setSoTimeout(readTimeout);
        }
        logger.debug("setTimeout(" + readTimeout + ")");
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

    public String[] getPortNames() throws Exception {
        return printerPort.getPortNames();
    }

    public void setPortEvents(IPortEvents events) {
        this.events = events;
    }

    public String readParameter(int parameterID) {
        switch (parameterID) {
            case PrinterPort.PARAMID_IS_RELIABLE:
                return "1";
            default:
                return null;
        }
    }

    public void onConnect() {
        if (portOpened) {
            try {
                connect(openTimeout);
            } catch (Exception e) {
                logger.error("Failed to reconnect, " + e.getMessage());
            }
        }
    }

    public void onDisconnect() {
        disconnect();
    }

}