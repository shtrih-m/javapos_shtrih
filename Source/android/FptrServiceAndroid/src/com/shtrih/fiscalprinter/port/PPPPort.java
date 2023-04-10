/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.port;

import com.shtrih.LibManager;
import com.shtrih.NativeResource;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.util.ByteUtils;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Localizer;
import com.shtrih.util.StaticContext;

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

    private int repeatCount = 0;
    private IPortEvents events;
    private Socket socket = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private LocalSocket localSocket = null;
    private InputStream lsInputStream;
    private OutputStream lsOutputStream;
    private boolean stopFlag = false;
    private Thread dispatchThread = null;
    private PPPThread pppThread = null;
    private int openTimeout = 3000;
    private int connectTimeout = 3000;
    private int readTimeout = 3000;
    private boolean connected = false;
    private boolean opened = false;
    private String localSocketName = null;
    private static CompositeLogger logger = CompositeLogger.getLogger(PPPPort.class);

    private final FptrParameters params;
    private final PrinterPort2 printerPort;

    public PPPPort(FptrParameters params, PrinterPort2 printerPort) {
        this.params = params;
        this.printerPort = printerPort;
        readTimeout = params.getByteTimeout();
    }

    public boolean isConnected() {
        return connected;
    }

    public synchronized void open(int timeout) throws Exception {
        if (opened) return;
        logger.debug("open");
        connect(timeout);
        opened = true;
        logger.debug("open: OK");
    }

    public synchronized void connect(int timeout) throws Exception
    {
        logger.debug("connect...");
        if (isConnected()) return;
        openTimeout = timeout;
        localSocketName = UUID.randomUUID().toString();

        printerPort.setPortName(params.portName);
        printerPort.setTimeout(params.byteTimeout);
        printerPort.open(timeout);
        printerPort.setPortEvents(this);
        startPPPThread();
        openLocalSocket(timeout);
        startDispatchThread();
        if (!pppThread.waitForPhase("PPP_PHASE_RUNNING", 60000)){
            noConnectionError();
        }
        openSocket();
        if (events != null) {
            events.onConnect();
        }
        connected = true;
        logger.debug("connect: OK");
    }

    public void openSocket() throws Exception
    {
        if (socket == null)
        {
            socket = new Socket();
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(connectTimeout);
            socket.connect(new InetSocketAddress("127.0.0.1", 7778));
            socket.setSoTimeout(readTimeout);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        }
    }

    public void startPPPThread() throws Exception
    {
        logger.debug("startPPPThread()...");
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
        logger.debug("startPPPThread(): OK");
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
                lsInputStream = localSocket.getInputStream();
                lsOutputStream = localSocket.getOutputStream();

                break;
            } catch (IOException e) {
                logger.error("openLocalSocket", e);
                if (Calendar.getInstance().getTimeInMillis() > time) {
                    throw e;
                }
                Thread.sleep(100);
            }
        }
        logger.debug("openLocalSocket: OK");
    }

    public synchronized void close()
    {
        if (!opened) return;
        logger.debug("close");
        disconnect();
        printerPort.setPortEvents(null);
        printerPort.close();
        opened = false;
        logger.debug("close: OK");
    }

    public void disconnect() {
        if (!isConnected()) return;

        connected = false;
        closeSocket();
        stopDispatchThread();
        closeLocalSocket();
        stopPPPThread();
        if (events != null) {
            events.onDisconnect();
        }
    }

    public void checkConnected() throws Exception{
        if (!connected) {
            throw new IOException("Device disconnected");
        }
    }

    public void closeLocalSocket() {
        if (localSocket == null) return;

        try {
            localSocket.close();
        } catch (Exception e) {
            logger.error(e);
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
        stopFlag = false;
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
        logger.debug("stopDispatchThread");
        if (dispatchThread == null) return;
        stopFlag = true;
        dispatchThread.interrupt();
        try {
            dispatchThread.join();
        } catch (InterruptedException e)
        {
            logger.error("stopDispatchThread ", e);
            Thread.currentThread().interrupt();
        }
        dispatchThread = null;
        logger.debug("stopDispatchThread: OK");
    }

    public void closeSocket() {
        logger.debug("closeSocket");

        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                logger.error(e);
            }
            socket = null;
        }
        logger.debug("closeSocket: OK");
    }

    public void dispatchProc() {
        logger.debug("dispatchProc.start");
        try {
            while (!stopFlag)
            {
                Thread.sleep(0);
                dispatchPackets();
            }
        } catch (InterruptedException e)
        {
            logger.error("dispatchProc: ", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("dispatchProc: ", e);
        }
        logger.debug("dispatchProc.end");
    }

    public void dispatchPackets() throws Exception
    {
        int count;
        byte[] data;

        // read bluetoothSocket
        count = printerPort.available();
        if (count > 0) {
            data = printerPort.read(count);
            //logger.debug("<- PPP " + Hex.toHex(data));
            if (lsOutputStream != null) {
                lsOutputStream.write(data, 0, count);
            }
        }
        // read localSocket
        if (lsInputStream != null) {
            count = lsInputStream.available();
            if (count > 0) {
                data = new byte[count];
                count = lsInputStream.read(data);
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

    public int readByte() throws Exception {
        return ByteUtils.byteToInt(readBytes(1)[0]);
    }

    public byte[] readBytes(int len) throws Exception
    {
        checkConnected();
        long time = Calendar.getInstance().getTimeInMillis() + readTimeout + 10000; // !!!
        byte[] data = new byte[len];
        int offset = 0;
        while (len > 0) {
            checkConnected();

            int count = Math.min(len, inputStream.available());
            if (count > 0) {
                count = inputStream.read(data, offset, count);
                if (count == -1) {
                    disconnect();
                    throw new ClosedConnectionException("Connection closed");
                }
                len -= count;
                offset += count;
            }
            long lastTimeInMillis = Calendar.getInstance().getTimeInMillis();
            if (lastTimeInMillis > time) {
                noConnectionError();
            }
            Thread.sleep(0);
        }
        return data;
    }

    public void write(byte[] b) throws Exception
    {
        checkConnected();
        for (int i = 0; i < 2; i++) {
            try {
                open();
                //logger.debug("=>> write: " + Hex.toHex(b));
                outputStream.write(b);
                return;
            } catch (SocketException e) {
                logger.error("write", e);
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
        if (socket != null) {
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

    public int directIO(int command, int[] data, Object object)
    {
        if (command == DIO_REPORT_IS_PPP){
            data[0] = 1;
            return 0;
        }
        return printerPort.directIO(command, data, object);
    }

    public void onConnect() {
        if (opened) {
            try {
                connect(openTimeout);
            } catch (Exception e) {
                logger.error("Failed to reconnect", e);
            }
        }
    }

    public void onDisconnect() {
        disconnect();
    }

}