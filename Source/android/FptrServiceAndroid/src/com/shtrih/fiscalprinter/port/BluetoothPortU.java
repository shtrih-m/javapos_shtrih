package com.shtrih.fiscalprinter.port;

import com.shtrih.jpos.fiscalprinter.XmlPropWriter;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.SysUtils;
import com.shtrih.util.XmlUtils;

import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class BluetoothPort2 implements PrinterPort
{
    int timeout;
    int openTimeout;
    private String portName;
    private boolean useBLE = false;
    private PrinterPort port = null;
    private PrinterPort.IPortEvents events;
    private static CompositeLogger logger = CompositeLogger.getLogger(BluetoothPort2.class);

    public BluetoothPort2(){

    }

    private PrinterPort getPort() throws Exception
    {
        if (port == null){
            open(0);
        }
        return port;
    }

    public void open(int timeout) throws Exception
    {
        openTimeout = timeout;
        if (useBLE) {
            if (connectBluetoothLE()) return;
            if (connectBluetooth()) {
                useBLE = false;
                return;
            }
        } else{
            if (connectBluetooth()) return;
            if (connectBluetoothLE()) {
                useBLE = true;
                return;
            }
        }
        throw new IOException("Failed to connect");
    }

    private boolean connectBluetooth() {
        return connect(new BluetoothPort());
    }

    private boolean connectBluetoothLE() {
        return connect(new BluetoothLEPort());
    }

    private boolean connect(PrinterPort port) {
        try {
            port.setPortEvents(events);
            port.setPortName(portName);
            port.setTimeout(timeout);
            port.open(openTimeout);
            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
    }

    public void close(){
        if (port != null){
            port.close();
        }
    }

    public int readByte() throws Exception
    {
        return getPort().readByte();
    }

    public byte[] readBytes(int len) throws Exception{
        return getPort().readBytes(len);
    }

    public void write(byte[] b) throws Exception{
        getPort().write(b);
    }

    public void write(int b) throws Exception{
        getPort().write(b);
    }

    public void setBaudRate(int baudRate) throws Exception{
    }

    public void setTimeout(int timeout) throws Exception
    {
        this.timeout = timeout;
        if (port != null){
            port.setTimeout(timeout);
        }
    }

    public void setPortName(String portName) throws Exception{
        this.portName = portName;
    }

    public String getPortName(){
        return portName;
    }

    public Object getSyncObject() throws Exception{
        return this;
    }

    public boolean isSearchByBaudRateEnabled(){
        return false;
    }

    public String[] getPortNames(){
       return null;
    }

    public void setPortEvents(PrinterPort.IPortEvents events){
        this.events = events;
    }

}
