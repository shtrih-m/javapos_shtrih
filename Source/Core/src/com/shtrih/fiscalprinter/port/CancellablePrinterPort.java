/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.port;

import com.shtrih.jpos.fiscalprinter.FptrParameters;

/**
 *
 * @author V.Kravtsov
 */
public class CancellablePrinterPort implements PrinterPort 
{
    private final PrinterPort port;
    private final FptrParameters params;

    public CancellablePrinterPort(PrinterPort port, FptrParameters params) {
        this.port = port;
        this.params = params;
    }

    public String getPortName(){
        return port.getPortName();
    }
    
    void checkCancelled() throws Exception {
        if (params.cancelIO) {
            throw new Exception("Cancelled");
        }
    }

    public void open(int timeout) throws Exception {
        checkCancelled();
        port.open(timeout);
    }

    public void close() throws Exception {
        port.close();
    }

    public int readByte() throws Exception {
        checkCancelled();
        return port.readByte();
    }

    public byte[] readBytes(int len) throws Exception {
        checkCancelled();
        return port.readBytes(len);
    }

    public void write(byte[] b) throws Exception {
        checkCancelled();
        port.write(b);
    }

    public void write(int b) throws Exception {
        checkCancelled();
        port.write(b);
    }

    public void setBaudRate(int baudRate) throws Exception {
        port.setBaudRate(baudRate);
    }

    public void setTimeout(int timeout) throws Exception {
        port.setTimeout(timeout);
    }

    public void setPortName(String portName) throws Exception {
        port.setPortName(portName);
    }

    public Object getSyncObject() throws Exception {
        return port.getSyncObject();
    }

    public boolean isSearchByBaudRateEnabled(){
        return port.isSearchByBaudRateEnabled();
    }
}
