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

public class SerialPrinterPort implements PrinterPort2 {

    public SerialPrinterPortSingle port = null;

    /**
     * Creates a new instance of PrinterPort
     */
    public SerialPrinterPort(String portName) throws Exception {
        port = SerialPrinterPortSingle.getInstance(portName);
    }

    public void open(int timeout) throws Exception{
        port.open(timeout);
    }

    public void close(){
        port.close();
    }

    public int readByte() throws Exception{
        return port.readByte();
    }

    public byte[] readBytes(int len) throws Exception{
        return port.readBytes(len);
    }

    public void write(byte[] b) throws Exception{
        port.write(b);
    }

    public void write(int b) throws Exception{
        port.write(b);
    }

    public void setBaudRate(int baudRate) throws Exception{
        port.setBaudRate(baudRate);
    }

    public void setTimeout(int timeout) throws Exception{
        port.setTimeout(timeout);
    }

    public void setPortName(String portName) throws Exception
    {
        if (!portName.equalsIgnoreCase(getPortName())){
            port = SerialPrinterPortSingle.getInstance(portName);
        }
    }
    
    public String getPortName(){
        return port.getPortName();
    }
    
    public Object getSyncObject() throws Exception{
        return port.getSyncObject();
    }

    public boolean isSearchByBaudRateEnabled(){
        return port.isSearchByBaudRateEnabled();
    }

    public String[] getPortNames(){
        return port.getPortNames();
    }

    public void setPortEvents(IPortEvents events){

    }

    public InputStream getInputStream() throws Exception{
        return port.getInputStream();
    }
}
