package com.shtrih.fiscalprinter.port;

import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Localizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class SerialPrinterPort implements PrinterPort {

    private int timeout = 1000;
    private int baudRate = 9600;
    private final String portName;
    private SerialPort port = null;
    private static CompositeLogger logger = CompositeLogger.getLogger(SerialPrinterPort.class);

    private static Map<String, SerialPrinterPort> items = new HashMap<String, SerialPrinterPort>();

    public static synchronized SerialPrinterPort getInstance(String portName) throws Exception {
        SerialPrinterPort item = items.get(portName);
        if (item == null) {
            item = new SerialPrinterPort(portName);
            items.put(portName, item);
        }
        return item;
    }

    /**
     * Creates a new instance of PrinterPort
     */
    private SerialPrinterPort(String portName) {
        this.portName = portName;
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
    }

    public void setBaudRate(int baudRate) throws Exception {
        if (this.baudRate != baudRate) {
            this.baudRate = baudRate;
            updateBaudRate();
        }
    }

    public synchronized void open(int timeout) throws Exception {
        if (isClosed()) {
            logger.debug("open(" + portName + "," + baudRate + ")"); // !!!

            CommPortIdentifier portId = CommPortIdentifier
                    .getPortIdentifier(portName);
            if (portId == null) {
                throw new Exception("Port does not exist, " + portName);
            }
            port = (SerialPort) portId.open(getClass().getName(), timeout);
            if (port == null) {
                throw new Exception("Failed to open port " + portName);
            }
            port.setInputBufferSize(1024);
            port.setOutputBufferSize(1024);
            port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            port.enableReceiveTimeout(100000);
            updateBaudRate();
        }
    }

    public synchronized void close() {
        if (isOpened()) {
            logger.debug("close"); // !!!
            port.close();
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

    private void noConnectionError() throws Exception {
        throw new IOException(Localizer.getString(Localizer.NoConnection));
    }

    public void write(byte[] b) throws Exception {
        open(0);
        OutputStream out = getPort().getOutputStream();
        if (out == null) {
            throw new Exception("Port open failed");
        }
        out.write(b);
        out.flush();
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
        open(0);
        int result;
        SerialPort serialPort = getPort();
        InputStream is = serialPort.getInputStream();
        if (is == null) {
            throw new Exception("Port open failed");
        }

        long startTime = System.currentTimeMillis();
        for (; ; ) {
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

    public String[] getPortNames() {
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
        return this;
    }

    public boolean isSearchByBaudRateEnabled() {
        return true;
    }
}
