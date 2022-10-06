package com.shtrih.fiscalprinter.port;

import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.util.CompositeLogger;

import java.io.IOException;

public class BluetoothPortU implements PrinterPort
{
    int timeout;
    int openTimeout;
    private String portName;
    private boolean useBLE = false;
    private PrinterPort port = null;
    private PrinterPort.IPortEvents events;
    private final FptrParameters params;
    private static CompositeLogger logger = CompositeLogger.getLogger(BluetoothPortU.class);

    public BluetoothPortU(FptrParameters params){
        this.params = params;
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

    private boolean connectBluetoothLE() throws Exception {
        return connect(new BluetoothLEPort(params));
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
            logger.error(e);
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

    public String[] getPortNames() throws Exception {
       return null;
    }

    public void setPortEvents(PrinterPort.IPortEvents events){
        this.events = events;
    }

    public String readParameter(int parameterID){
        switch (parameterID){
            case PrinterPort.PARAMID_IS_RELIABLE: return "1";
            default: return null;
        }
    }
}
