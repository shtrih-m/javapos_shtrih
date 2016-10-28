/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.port;

/**
 * @author V.Kravtsov
 */

public interface PrinterPort 
{
    void open(int timeout) throws Exception;

    void close() throws Exception;

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
}
