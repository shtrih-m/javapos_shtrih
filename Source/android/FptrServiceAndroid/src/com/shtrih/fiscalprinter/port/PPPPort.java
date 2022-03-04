/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.port;

import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Localizer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import gnu.io.LibManager;
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
    private BluetoothPort bluetoothPort = null;
    private Thread bluetoothThread = null;
    private PPPThread thread = null;
    private int openTimeout = 3000;
    private int connectTimeout = 3000;
    private int readTimeout = 3000;
    private String portName = "";
    private boolean firstCommand = true;
    private final FptrParameters params;
    private String localSocketName = null;
    private static CompositeLogger logger = CompositeLogger.getLogger(PPPPort.class);

    public PPPPort(FptrParameters params) {
        this.params = params;
        portName = params.portName;
        readTimeout = params.getByteTimeout();
    }

    public boolean isOpened() {
        return (socket != null) && (socket.isConnected());
    }

    public void open(int timeout) throws Exception {
        if (isOpened()) {
            return;
        }

        firstCommand = true;
        openTimeout = timeout;
        localSocketName = UUID.randomUUID().toString();

        openBluetoothPort(timeout);

        logger.debug("LibManager.getInstance.0");
        LibManager.getInstance();
        logger.debug("LibManager.getInstance.1");

        PPPConfig config = new PPPConfig();
        config.transport.path = localSocketName;
        config.transport.type = PPPConfig.TRANSPORT_TYPE_FORWARDER;
        if (!params.pppConfigFile.isEmpty()) {
            config.load(params.pppConfigFile);
        }
        thread = new PPPThread(config);
        thread.start();
        Thread.sleep(100);

        openLocalSocket(timeout);

        socket = new Socket();
        socket.setTcpNoDelay(true);
        socket.setSoTimeout(connectTimeout);
        socket.connect(new InetSocketAddress("127.0.0.1", 7778));
        socket.setSoTimeout(readTimeout);

        bluetoothThread = new Thread(new Runnable() {
            @Override
            public void run() {
                bluetoothProc();
            }
        });
        bluetoothThread.start();
    }

    public void openBluetoothPort(int timeout) throws Exception
    {
        if (bluetoothPort == null) {
            bluetoothPort = new BluetoothPort();
            bluetoothPort.setPortName(portName);
            bluetoothPort.setTimeout(params.byteTimeout);
            bluetoothPort.setOpenTimeout(timeout);
            bluetoothPort.open(timeout);
        }
    }

    public void openLocalSocket(int timeout) throws Exception
    {
        if (localSocket == null) {
            localSocket = new LocalSocket();

            long time = Calendar.getInstance().getTimeInMillis() + timeout;
            for (;;) {
                try {
                    localSocket.connect(new LocalSocketAddress(localSocketName));
                    break;
                } catch (IOException e) {
                    logger.error(e);
                    if (Calendar.getInstance().getTimeInMillis() > time){
                        throw e;
                    }
                    Thread.sleep(100);
                }
            }
        }
    }

    public void close()
    {
        if (!isOpened()) {
            return;
        }

        // stop dispatch thread
        bluetoothThread.interrupt();
        try {
            bluetoothThread.join();
        }catch(InterruptedException e){
        }
        bluetoothThread = null;

        if (thread != null) {
            thread.stop();
            thread = null;
        }

        logger.debug("close");
        if (localSocket != null) {
            try {
                localSocket.close();
            } catch (Exception e) {
                logger.error(e);
            }
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                logger.error(e);
            }
        }

        if (bluetoothPort != null) {
            bluetoothPort.close();
        }


        socket = null;
        localSocket = null;
        bluetoothPort = null;
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
            BluetoothSocket bluetoothSocket = bluetoothPort.getSocket();

            //openLocalSocket(openTimeout);
            //openBluetoothPort(openTimeout);

            // read bluetoothSocket
            count = bluetoothSocket.getInputStream().available();
            if (count > 0) {
                data = new byte[count];
                count = bluetoothSocket.getInputStream().read(data);
                if (count > 0) {
                    //logger.debug("BT <- " + Hex.toHex(data));
                    localSocket.getOutputStream().write(data);
                }
                if (count == -1) {
                    logger.debug("bluetoothSocket.getInputStream().read(data) = -1");
                    //bluetoothPort.close();
                    //bluetoothPort = null;
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
                    bluetoothSocket.getOutputStream().write(data);
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

    public int readByte() throws Exception {
        open();
        return readBytes(1)[0];
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

     */

    public byte[] readBytes(int len) throws Exception {
        open();

        firstCommand = false;
        byte[] data = new byte[len];
        int offset = 0;
        while (len > 0)
        {
            int count = socket.getInputStream().read(data, offset, len);
            if (count < 0) {
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
                open();
                socket.getOutputStream().write(b);
                socket.getOutputStream().flush();
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
        readTimeout = timeout;
        if (firstCommand){
            readTimeout = timeout + 100000;
        }

        if (isOpened())
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
