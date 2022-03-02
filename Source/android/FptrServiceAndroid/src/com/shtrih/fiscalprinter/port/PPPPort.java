/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.port;

import com.shtrih.fiscalprinter.scoc.commands.DeviceStatusCommand;
import com.shtrih.fiscalprinter.scoc.commands.ScocCommand;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Hex;
import com.shtrih.util.Localizer;
import com.shtrih.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import gnu.io.LibManager;
import ru.shtrih_m.kktnetd.Api;
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
            localSocket.setSoTimeout(readTimeout);
        }
    }

    public void close()
    {
        if (!isOpened()) {
            return;
        }

        logger.debug("close");
        // stop dispatch thread
        try {
            bluetoothThread.interrupt();
            bluetoothThread.join();
            bluetoothThread = null;
        }
        catch(InterruptedException e){

        }

        if (thread != null) {
            thread.stop();
            thread = null;
        }

        if (localSocket != null) {
            try {
                localSocket.close();
            } catch (Exception e) {
                logger.error(e);
            }
            localSocket = null;
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                logger.error(e);
            }
            socket = null;
        }

        if (bluetoothPort != null) {
            bluetoothPort.close();
            bluetoothPort = null;
        }
    }

    public void bluetoothProc()
    {
        while (true)
        {
            try
            {
                int count;
                byte[] data;
                BluetoothSocket bluetoothSocket = bluetoothPort.getPort();

                Thread.sleep(0);
                //openLocalSocket(openTimeout);
                //openBluetoothPort(openTimeout);

                // read bluetoothSocket
                count = bluetoothSocket.getInputStream().available();
                if (count > 0)
                {
                    data = new byte[count];
                    count = bluetoothSocket.getInputStream().read(data);
                    if (count > 0){
                        //logger.debug("BT <- " + Hex.toHex(data));
                        localSocket.getOutputStream().write(data);
                    }
                    /*
                    if (count == -1) {
                        logger.debug("bluetoothPort.close");
                        bluetoothPort.close();
                        bluetoothPort = null;
                        continue;
                    }
                     */
                }
                // read localSocket
                count = localSocket.getInputStream().available();
                if (count > 0) {
                    data = new byte[count];
                    count = localSocket.getInputStream().read(data);
                    if (count > 0){
                        //logger.debug("BT -> " + Hex.toHex(data));
                        bluetoothSocket.getOutputStream().write(data);
                    }
                    /*
                    if (count == -1) {
                        logger.debug("localSocket.close");
                        localSocket.close();
                        localSocket = null;
                        continue;
                    }
                     */
                }
            }
            catch(Exception e){
                logger.error(e);
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

        int b = socket.getInputStream().read();
        if (b == -1) {
            noConnectionError();
        }

        return b;
    }

    public byte[] readBytes(int len) throws Exception {
        open();

        byte[] data = new byte[len];
        int offset = 0;
        while (len > 0) {
            int count = socket.getInputStream().read(data, offset, len);
            if (count == -1) {
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
        this.readTimeout = timeout;

        if (isOpened()) {
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
