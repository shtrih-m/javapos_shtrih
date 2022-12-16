package com.shtrih.fiscalprinter.port;

import java.io.InputStream;

/**
 * @author V.Kravtsov
 */

import java.io.InputStream;
import java.io.OutputStream;

public interface PrinterPort 
{
    public static final int DIO_READ_IS_RELIABLE = 1;
    public static final int DIO_REPORT_RSSI      = 2;
    public static final int DIO_REPORT_IS_PPP    = 3;

    void open(int timeout) throws Exception;

    void close();

    int readByte() throws Exception;

    byte[] readBytes(int len) throws Exception;

    void write(byte[] b) throws Exception;

    void write(int b) throws Exception;

    void setBaudRate(int baudRate) throws Exception;

    void setTimeout(int timeout) throws Exception;

    void setPortName(String portName) throws Exception;
    
    String getPortName();
    
    Object getSyncObject() throws Exception;

    boolean isSearchByBaudRateEnabled();

    String[] getPortNames() throws Exception;

    void setPortEvents(IPortEvents events);

    int directIO(int command, int[] data, Object object);

    public interface IPortEvents
    {
        void onConnect();
        void onDisconnect();
    }
}