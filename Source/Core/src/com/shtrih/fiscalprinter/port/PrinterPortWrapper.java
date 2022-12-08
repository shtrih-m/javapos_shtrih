/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.port;

/**
 *
 * @author Виталий
 */
public class PrinterPortWrapper implements PrinterPort {

    private final PrinterPort port;
    private boolean isOpened = false;

    public PrinterPortWrapper(PrinterPort port) {
        this.port = port;
    }

    public void open(int timeout) throws Exception {
        if (!isOpened) {
            port.open(timeout);
            isOpened = true;
        }
    }

    public void close() {
        if (isOpened) {
            port.close();
            isOpened = false;
        }
    }

    public int readByte() throws Exception {
        try {
            open(0);
            return port.readByte();
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    public byte[] readBytes(int len) throws Exception {
        try {
            open(0);
            return port.readBytes(len);
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    public void write(byte[] b) throws Exception {
        try {
            open(0);
            port.write(b);
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    public void write(int b) throws Exception {
        try {
            open(0);
            port.write(b);
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    public void setBaudRate(int baudRate) throws Exception {
        try {
            port.setBaudRate(baudRate);
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    public void setTimeout(int timeout) throws Exception {
        try {
            port.setTimeout(timeout);
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    public void setPortName(String portName) throws Exception {
        try {
            port.setPortName(portName);
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    public String getPortName() {
        return port.getPortName();
    }

    public Object getSyncObject() throws Exception {
        return port.getSyncObject();
    }

    public boolean isSearchByBaudRateEnabled() {
        return port.isSearchByBaudRateEnabled();
    }

    public String[] getPortNames() throws Exception {
        return port.getPortNames();
    }

    public void setPortEvents(IPortEvents events){
        port.setPortEvents(events);
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
