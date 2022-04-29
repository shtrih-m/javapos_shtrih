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
import com.shtrih.util.Hex;
import com.shtrih.util.StaticContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import ru.shtrih_m.kktnetd.PPPConfig;

import java.util.Calendar;
import java.util.UUID;
import android.net.LocalSocket;
import android.bluetooth.BluetoothSocket;
import android.net.LocalSocketAddress;

/**
 *
 * @author Виталий
 */
public class PPPPort implements PrinterPort {

    private Socket socket = null;
    private LocalSocket localSocket = null;
    private Thread dispatchThread = null;
    private PPPThread pppThread = null;
    private int openTimeout = 3000;
    private int connectTimeout = 3000;
    private int readTimeout = 3000;
    private String portName = "";
    private boolean opened = false;
    private boolean firstCommand = true;
    private String localSocketName = null;
    private static CompositeLogger logger = CompositeLogger.getLogger(PPPPort.class);

    private final FptrParameters params;
    private final PrinterPort2 printerPort;

    public PPPPort(FptrParameters params, PrinterPort2 printerPort) {
        this.params = params;
        this.printerPort = printerPort;

        portName = params.portName;
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

        printerPort.open(timeout);
        startPPPThread();
        openLocalSocket(timeout);
        dispatchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                bluetoothProc();
            }
        });
        dispatchThread.start();
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
        InputStream stream = StaticContext.getContext().getAssets().open(fileName);
        LibManager.getInstance(libName, stream);

        PPPConfig config = new PPPConfig();
        config.transport.path = localSocketName;
        config.transport.type = PPPConfig.TRANSPORT_TYPE_FORWARDER;
        if (!params.pppConfigFile.isEmpty()) {
            config.load(params.pppConfigFile);
        }
        pppThread = new PPPThread(config);
        pppThread.start();
        pppThread.waitForStatus("\"status\":\"RUNNING\"", 5000);
    }

    public void openLocalSocket(int timeout) throws Exception
    {
        if (localSocket != null) {
            return;
        }
        logger.debug("localSocket.connect");
        localSocket = new LocalSocket();

        long time = Calendar.getInstance().getTimeInMillis() + timeout;
        for (;;) {
            try {
                localSocket.connect(new LocalSocketAddress(localSocketName));
                break;
            } catch (IOException e) {
                logger.error(e.getMessage());
                if (Calendar.getInstance().getTimeInMillis() > time){
                    throw e;
                }
                Thread.sleep(100);
            }
        }
        logger.debug("LocalSocket connected!");
    }

    public void close()
    {
        if (!isOpened()) {
            return;
        }

        logger.debug("close");
        stopDispatchThread();
        stopPPPThread();
        closeLocalSocket();
        closeSocket();
        printerPort.close();
        opened = false;
    }

    public void closeLocalSocket(){
        if (localSocket == null) return;

        try {
            localSocket.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        localSocket = null;
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

    public void bluetoothProc()
    {
        try {
            while (true)
            {
                Thread.sleep(0);
                try {
                    dispatchPackets();
                } catch (Exception e) {
                    logger.error("bluetoothProc: " + e.getMessage());
                }
            }
        }
        catch(InterruptedException e)
        {
            logger.error("bluetoothProc interrupted");
        }
    }

    public void dispatchPackets() throws Exception
        {
            int count;
            byte[] data;

            //openLocalSocket(openTimeout);
            //openPrinterPort(openTimeout);

            // read bluetoothSocket
            count = printerPort.getInputStream().available();
            if (count > 0) {
                data = new byte[count];
                count = printerPort.getInputStream().read(data);
                if (count > 0) {
                    //logger.debug("BT <- " + Hex.toHex(data));
                    localSocket.getOutputStream().write(data, 0, count);
                    localSocket.getOutputStream().flush();
                }
                if (count == -1) {
                    logger.debug("bluetoothSocket.getInputStream().read(data) = -1");
                    //printerPort.close();
                    //printerPort = null;
                    return;
                }
            }
            // read localSocket
            count = localSocket.getInputStream().available();
            if (count > 0) {
                data = new byte[count];
                count = localSocket.getInputStream().read(data);
                if (count > 0) {
                    //logger.debug("BT -> " + Hex.toHex(data));
                    printerPort.getOutputStream().write(data, 0, count);
                    printerPort.getOutputStream().flush();
                }
                if (count == -1) {
                    logger.debug("localSocket.getInputStream().read(data) = -1");
                    //localSocket.close();
                    //localSocket = null;
                    return;
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

    public int readByte() throws Exception
    {
        return byteToInt(readBytes(1)[0]);
    }

    public byte[] readBytes(int len) throws Exception
    {
        openSocket();
        try {
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
        catch(Exception e){
            logger.error("readBytes: " + e.getMessage());
            closeSocket();
            throw e;
        }
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
        return portName;
    }

    public void setPortName(String portName) throws Exception {
        this.portName = portName;
    }

    public Object getSyncObject() throws Exception {
        return this;
    }

    public boolean isSearchByBaudRateEnabled() {
        return false;
    }

    public String[] getPortNames() {
        return new String[]{portName};
    }

    public void setPortEvents(IPortEvents events) {

    }
}

    /*
    public byte[] readBytes2(int len) throws Exception {
        open();

        long time = Calendar.getInstance().getTimeInMillis() + readTimeout;

        firstCommand = false;
        byte[] data = new byte[len];
        int offset = 0;
        while (len > 0)
        {
            int count = Math.min(len, socket.getInputStream().available());
            if (count > 0) {
                count = socket.getInputStream().read(data, offset, count);
                if (count == -1) {
                    noConnectionError();
                }
                len -= count;
                offset += count;
            }
            if (Calendar.getInstance().getTimeInMillis() > time){
                noConnectionError();
            }
            Thread.sleep(0);
        }
        return data;
    }

    public void openPrinterPort(int timeout) throws Exception
    {
        if (printerPort == null) {
            printerPort = new PrinterPort();
            printerPort.setPortName(portName);
            printerPort.setTimeout(params.byteTimeout);
            printerPort.setOpenTimeout(timeout);
            printerPort.open(timeout);
        }
    }



     */

