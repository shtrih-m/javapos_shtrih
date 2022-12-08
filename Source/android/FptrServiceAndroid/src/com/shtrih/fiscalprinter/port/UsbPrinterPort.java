package com.shtrih.fiscalprinter.port;

import java.io.IOException;

public class UsbPrinterPort implements PrinterPort {

    public static android.content.Context Context;

    private DeviceUSB usb;

    private int timeout;
    private String portName;
    private int baudRate;
    private volatile boolean isConnected;

    public UsbPrinterPort() {
        this.usb = new DeviceUSB(Context);
    }

    @Override
    public void open(int timeout) throws Exception {
        if (isConnected)
            return;

        isConnected = usb.connect(portName, baudRate);
        if (!isConnected)
            throw new IOException("Connection failed");
    }

    @Override
    public void close() {
        try {
            usb.disconnect();
        } finally {
            isConnected = false;
        }
    }

    @Override
    public int readByte() throws Exception {
        return readBytes(1)[0] & 0xFF;
    }

    @Override
    public byte[] readBytes(int len) throws Exception {
        open(timeout);

        try {


            byte[] buffer = new byte[len];
            if (!usb.Read(buffer, 0, len, timeout)) {
                throw new IOException("Read failed");
            }
            return buffer;
        } catch (Exception e) {
            isConnected = false;
            throw new IOException("Read failed", e);
        }
    }

    @Override
    public void write(byte[] b) throws Exception {
        open(timeout);

        try {
            if (!usb.Write(b)) {
                throw new IOException("Write failed");
            }
        } catch (Exception e) {
            isConnected = false;
            throw new IOException("Read failed", e);
        }
    }

    @Override
    public void write(int b) throws Exception {
        byte[] buffer = new byte[]{(byte) b};
        write(buffer);
    }

    @Override
    public void setBaudRate(int baudRate) throws Exception {
        this.baudRate = baudRate;
    }

    @Override
    public void setTimeout(int timeout) throws Exception {
        this.timeout = timeout;
    }

    @Override
    public void setPortName(String portName) throws Exception {
        this.portName = portName;
    }

    @Override
    public String getPortName() {
        return portName;
    }

    @Override
    public Object getSyncObject() throws Exception {
        return usb;
    }

    @Override
    public boolean isSearchByBaudRateEnabled() {
        return false;
    }

    public String[] getPortNames() throws Exception{
        return new String[]{portName};
    }

    public void setPortEvents(IPortEvents events){

    }

    public int directIO(int command, int[] data, Object object)
    {
        switch (command){
            case PrinterPort.DIO_READ_IS_RELIABLE: data[0] = 0;
            default: data[0] = 0;
        }
        return 0;
    }
}
