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

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import com.shtrih.util.Localizer;
import com.shtrih.util.CompositeLogger;

public class SerialPrinterPort implements PrinterPort {

    private int timeout = 1000;
    private int baudRate = 9600;
    private String portName = "";
    private SerialPort port = null;
    private static CompositeLogger logger = CompositeLogger.getLogger(SerialPrinterPort.class);

    /**
     * Creates a new instance of PrinterPort
     */
    public SerialPrinterPort() {
    }

    public void checkOpened() throws Exception {
        if (port == null) {
            throw new Exception("Port is not opened");
        }
    }

    public String getPortName(){
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
        }
    }

    public synchronized void close() {
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
            port.setSerialPortParams(baudRate, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        }
    }

    public void readAll() throws Exception {
        SerialPort serialPort = getPort();
        InputStream is = serialPort.getInputStream();
        int len = is.available();
        if (len > 0) {
            int offset = 0;
            byte[] data = new byte[len];
            is.read(data, offset, len);
        }
    }

    private void noConnectionError() throws Exception {
        throw new IOException(Localizer.getString(Localizer.NoConnection));
    }

    public void write(byte[] b) throws Exception {
        OutputStream stream = getPort().getOutputStream();
        stream.write(b);
        stream.flush();
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

    public int doReadByte() throws Exception {
        int result;
        SerialPort serialPort = getPort();
        InputStream is = serialPort.getInputStream();
        long startTime = System.currentTimeMillis();
        for (;;) {
            long currentTime = System.currentTimeMillis();
            if (is.available() > 0) {
                result = is.read();
                if (result >= 0) {
                    return result;
                }
            }
            if ((currentTime - startTime) > timeout) {
                noConnectionError();
            }
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

    public InputStream getInputStream() throws Exception {
        return port.getInputStream();
    }

    public OutputStream getOutputStream() throws Exception {
        return port.getOutputStream();
    }

    public Object getSyncObject() throws Exception {
        return port;
    }

    public boolean isSearchByBaudRateEnabled(){
        return true;
    }
}
