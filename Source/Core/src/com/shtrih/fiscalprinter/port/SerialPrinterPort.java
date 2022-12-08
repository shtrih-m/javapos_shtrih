package com.shtrih.fiscalprinter.port;

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

    public String[] getPortNames() throws Exception{
        return port.getPortNames();
    }

    public void setPortEvents(IPortEvents events){

    }

    public int available() throws Exception{
        return port.getInputStream().available();
    }

    public byte[] read(int len) throws Exception{
        byte[] buffer = new byte[len];
        port.getInputStream().read(buffer, 0, len);
        return buffer;
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
