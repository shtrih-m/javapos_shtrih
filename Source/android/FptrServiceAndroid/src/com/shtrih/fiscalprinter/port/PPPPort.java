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
import com.shtrih.util.Hex;
import com.shtrih.util.Localizer;
import com.shtrih.util.StaticContext;
import com.shtrih.util.Time;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

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
    private boolean firstCommand = true;
    private String localSocketName = null;
    private Thread rxThread = null;
    private CircularBuffer rxBuffer = new CircularBuffer(1024);
    private static CompositeLogger logger = CompositeLogger.getLogger(PPPPort.class);

    private final FptrParameters params;
    private final PrinterPort2 printerPort;

    public PPPPort(FptrParameters params, PrinterPort2 printerPort) {
        this.params = params;
        this.printerPort = printerPort;
        readTimeout = params.getByteTimeout();
        printerPort.setPortEvents(this);
    }

    public boolean isOpened() {
        return opened;
    }

    public synchronized void open(int timeout) throws Exception {
        if (isOpened()) {
            return;
        }
        logger.debug("open");
        firstCommand = true;
        openTimeout = timeout;
        localSocketName = UUID.randomUUID().toString();

        printerPort.setPortName(params.portName);
        printerPort.setTimeout(params.byteTimeout);
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
        logger.debug("open: OK");
    }

    public void openSocket() throws Exception
    {
        if (socket != null){
            return;
        }
        logger.debug("openSocket()");
        socket = new Socket();
        socket.setTcpNoDelay(true);
        socket.setSoTimeout(connectTimeout);
        socket.connect(new InetSocketAddress("127.0.0.1", 7778));
        socket.setSoTimeout(0);
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                rxProc();
            }
        });
        rxThread.start();
        logger.debug("openSocket: OK");
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
    }

    public synchronized void close()
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
        firstCommand = true;
        logger.debug("close: OK");
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
        logger.debug("closeSocket");

        if (socket == null) {
            return;
        }

        try {
            socket.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        if (rxThread != null) {
            rxThread.interrupt();
            try {
                rxThread.join();
            } catch (InterruptedException e) {
            }
            rxThread = null;
        }
    }

    public void rxProc()
    {
        try {
                while (true)
                {
                    Thread.sleep(0);
                    try {
                        int b = socket.getInputStream().read();
                        if (b < 0) break;
                        rxBuffer.write(b);
                    }
                    catch(Exception e){
                        logger.error("rxProc: " + e.getMessage());
                    }
                }

        }
        catch(InterruptedException e)
        {
            logger.error("rxProc InterruptedException: " + e.getMessage());
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

        // read bluetoothSocket
        count = printerPort.available();
        if (count > 0) {
            data = printerPort.read(count);
            localSocket.getOutputStream().write(data, 0, count);
            localSocket.getOutputStream().flush();
        }
        // read localSocket
        count = localSocket.getInputStream().available();
        if (count > 0) {
            data = new byte[count];
            count = localSocket.getInputStream().read(data);
            if (count > 0) {
                printerPort.write(data);
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

    public byte[] readBytes(int len) throws Exception{
        open();

        firstCommand = false;
        //logger.debug("readBytes: " + len);
        if (len <= 0)
        {
            throw new Exception("Data length <= 0");
        }

        // wait for data available with timaout
        long startTime = System.currentTimeMillis();
        for (;;)
        {
            if (rxBuffer.available() >= len) break;
            Time.delay(1);
            long currentTime = System.currentTimeMillis();
            if ((currentTime - startTime) > readTimeout)
            {
                throw new IOException("Read timed out");
            }
        }
        byte[] data = rxBuffer.read(len);
        //logger.debug("read(" + Hex.toHex(data) + ")");
        return data;
    }

    public void write(byte[] b) throws Exception
    {
        open();
        for (int i = 0; i < 2; i++) {
            try {
                //openSocket();
                rxBuffer.clear();
                socket.getOutputStream().write(b);
                socket.getOutputStream().flush();
                return;
            } catch (Exception e)
            {

                logger.error(e.getMessage());
                //closeSocket();
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
            readTimeout = timeout + 20000; // !!!
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

    public String[] getPortNames()
    {
        return printerPort.getPortNames();
    }

    public void setPortEvents(IPortEvents events)
    {
        this.events = events;
    }

    public String readParameter(int parameterID){
        switch (parameterID){
            case PrinterPort.PARAMID_IS_RELIABLE: return "1";
            default: return null;
        }
    }

    public void onConnect()
    {
        try {
            //open();
            if (events != null) {
                events.onConnect();
            }
        }
        catch(Exception e){
            logger.error("onConnect: " + e.getMessage());
        }
    }

    public void onDisconnect(){
        if (events != null) {
            events.onDisconnect();
        }
        close();
    }
}