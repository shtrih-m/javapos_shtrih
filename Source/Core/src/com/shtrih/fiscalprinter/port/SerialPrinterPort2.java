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

public class SerialPrinterPort2 implements PrinterPort {

    private int timeout = 1000;
    private int baudRate = 9600;
    private String portName = "";
    private SerialPort port = null;

    /**
     * Creates a new instance of PrinterPort
     */
    public SerialPrinterPort2() {
    }

    public Object getConnection() throws Exception {
        return port;
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

    public SerialPort getSerialPort() {
        return port;
    }

    public void setSerialPort(SerialPort port) {
        this.port = port;
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

    public void open(int timeout) throws Exception {
        if (isClosed()) {
            port = SharedSerialPorts.getInstance().openPort(portName, timeout,
                    this);
            port.setInputBufferSize(1024);
            port.setOutputBufferSize(1024);
            port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            port.enableReceiveTimeout(0);
            updateBaudRate();
        }
    }

    public void close() {
        if (isOpened()) {
            SharedSerialPorts.getInstance().closePort(port);
            port = null;
        }
    }

    public boolean isOpened() {
        return port != null;
    }

    public boolean isClosed() {
        return port == null;
    }

    public void updateBaudRate() throws Exception {
        if (isOpened()) {
            port.setSerialPortParams(baudRate, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        }
    }

    private void updateTimeout() throws Exception {
        if (isOpened()) {
            if (timeout == 0) {
                timeout = 1;
            }
            port.enableReceiveTimeout(timeout);
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

    public void setTimeout(int value) throws Exception {
        if (value != timeout) {
            timeout = value;
            updateTimeout();
        }
    }

    public byte[] readBytes(int len) throws Exception {
        int offset = 0;
        int readLen = 0;
        byte[] data = new byte[len];

        SerialPort serialPort = getPort();
        InputStream is = serialPort.getInputStream();
        do {
            readLen = is.read(data, offset, len);
            if (readLen <= 0) {
                noConnectionError();
            }
            offset += readLen;
            len -= readLen;
        } while (len > 0);
        return data;
    }

    private void noConnectionError() throws Exception {
        throw new IOException(Localizer.getString(Localizer.NoConnection));
    }

    private int byte2Int(byte value) {
        int B = value;
        if (B < 0) {
            B = 256 + B;
        }
        return B;
    }

    public int readByte() throws Exception {
        byte[] data = readBytes(1);
        return byte2Int(data[0]);
    }

    public void write(byte[] b) throws Exception {
        OutputStream stream = getPort().getOutputStream();
        stream.write(b);
        stream.flush();
    }

    public void write(int b) throws Exception {
        OutputStream stream = getPort().getOutputStream();
        stream.write(b);
        stream.flush();
    }

    public boolean find(Properties properties) throws Exception {
        return false;
    }

    public void initialize(Properties properties) throws Exception {
    }

    public String[] getPortNames() throws Exception {
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
