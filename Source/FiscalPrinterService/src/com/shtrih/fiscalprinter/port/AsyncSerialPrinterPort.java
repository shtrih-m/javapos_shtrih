/*
 * PrinterPort.java
 *
 * Created on August 30 2007, 12:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 *
 * @author V.Kravtsov
 */
package com.shtrih.fiscalprinter.port;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPortEventListener;

import java.util.Vector;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Enumeration;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.shtrih.util.Localizer;
import com.shtrih.util.CompositeLogger;


public class AsyncSerialPrinterPort implements Runnable, PrinterPort
{

    private int timeout = 1000;
    private int baudRate = 9600;
    private String portName = "";
    private SerialPort port = null;
    
    private int readPos = 0;
    private int writePos = 0;
    private byte[] buffer = new byte[1024];
    private static CompositeLogger logger = CompositeLogger.getLogger(AsyncSerialPrinterPort.class);

    /**
     * Creates a new instance of PrinterPort
     */
    public AsyncSerialPrinterPort() {
    }

    public void checkOpened() throws Exception {
        if (port == null) {
            throw new Exception("Port is not opened");
        }
    }

    public String getPortName() {
        return portName;
    }

    public SerialPort getPort() throws Exception {
        checkOpened();
        return port;
    }

    public void setPortName(String portName) throws Exception {
        if (!this.portName.equalsIgnoreCase(portName)) {
            close();
            this.portName = portName;
        }
    }

    public void setBaudRate(int baudRate) throws Exception {
        if (this.baudRate != baudRate) {
            this.baudRate = baudRate;
            updateBaudRate();
        }
    }

    public synchronized void open(int timeout) throws Exception {
        if (isClosed()) {
            port = SharedSerialPorts.getInstance().openPort(portName, timeout,
                    this);
            port.setInputBufferSize(1024);
            port.setOutputBufferSize(1024);
            port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            port.enableReceiveTimeout(100000);
            updateBaudRate();
            (new Thread(this)).start();        
        }
    }

    public void run ()
    {
        int len = -1;
        try
        {
            InputStream in = getPort().getInputStream();
            while ( ( len = in.read(buffer, writePos, 1)) > -1 )
            {
                writePos += len;
                //logger.debug("readPos: " + readPos);
                //logger.debug("writePos: " + writePos);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            close();
        }            
    }
    
    public synchronized void close() 
    {
        logger.debug("close");
        
        if (isOpened()) {
            SharedSerialPorts.getInstance().closePort(port);
            port = null;
        }
    }

    public synchronized boolean isOpened() {
        return port != null;
    }

    public synchronized boolean isClosed() {
        return port == null;
    }

    public synchronized void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public synchronized void updateBaudRate() throws Exception {
        if (isOpened()) {
            getPort().setSerialPortParams(baudRate, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        }
    }

    private void noConnectionError() throws Exception {
        throw new IOException(Localizer.getString(Localizer.NoConnection));
    }

    public void write(byte[] b) throws Exception 
    {
        for (int i = 0; i < 2; i++) {
            try {
                open(timeout);
                readPos = 0;
                writePos = 0;
                OutputStream out = getPort().getOutputStream();
                out.write(b);
                out.flush();
                return;
            } catch (IOException e) {
                close();
                if (i == 1) {
                    throw e;
                }
                logger.error(e);
            }
        }
    }

    public void write(int b) throws Exception {
        byte[] data = new byte[1];
        data[0] = (byte) b;
        write(data);
    }

    public byte[] readBytes(int len) throws Exception {
        byte[] data = new byte[len];
        for (int i = 0; i < len; i++) {
            data[i] = (byte) doReadByte();
        }
        return data;
    }

    public int readByte() throws Exception {
        int b = doReadByte();
        return b;
    }

    public int doReadByte() throws Exception 
    {
        int result = -1;
        long startTime = System.currentTimeMillis();
        for (;;) {
            long currentTime = System.currentTimeMillis();
            if (readPos < writePos) 
            {
                result = buffer[readPos];
                readPos++;
                return result;
            }
            if ((currentTime - startTime) > timeout) {
                noConnectionError();
            }
            //Thread.sleep(1);
        }
    }

    public boolean find(Properties properties) throws Exception {
        return false;
    }

    public void initialize(Properties properties) throws Exception {
    }

    public static String[] getPortNames() throws Exception {
        Vector result = new Vector();
        Enumeration e = CommPortIdentifier.getPortIdentifiers();
        while (e.hasMoreElements()) {
            CommPortIdentifier port = (CommPortIdentifier) e.nextElement();
            if (port.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                result.add(port.getName());
            }
        }
        return (String[]) result.toArray(new String[0]);
    }

    public Object getSyncObject() throws Exception {
        return port;
    }

    public boolean isSearchByBaudRateEnabled() {
        return true;
    }

}
