/*
 * MockPrinterDevice.java
 *
 * Created on 14 Октябрь 2010 г., 19:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Vector;
import gnu.io.SerialPort;
import com.shtrih.fiscalprinter.PrinterProtocol;
import com.shtrih.fiscalprinter.port.PrinterPort;

public class MockPrinterDevice //implements PrinterProtocol 
{

    int byteTimeout = 0;
    private SerialPort serialPort;
    private PrinterPort printerPort;
    private Vector commands = new Vector();
    private byte[] txData = new byte[100];
    private byte[] rxData = new byte[100];

    /** Creates a new instance of MockPrinterDevice */
    public MockPrinterDevice() 
    {
    }

    /*
    public PrinterCommand getCommand(int index) {
        return (PrinterCommand)commands.get(index);
    }

    public Vector getCommands() {
        return commands;
    }

    public void connect() throws Exception {
    }

    public void execute(PrinterCommand command) throws Exception {
        commands.add(command);
    }

    public byte[] sendCommand(byte[] data, int timeout) throws Exception {
        return null;
    }

    public void setByteTimeout(int value) {
        byteTimeout = value;
    }

    public PrinterPort getPort() {
        return null;
    }
    
    public void setPrinterPort(PrinterPort printerPort){
    }
    
    public void setSerialPort(SerialPort serialPort){
    }
    
    public byte[] getTxData(){
        return txData;
    }
    
    public byte[] getRxData(){
        return rxData;
    }
    
    public SerialPort getSerialPort(){
        return serialPort;
    }
    
    public PrinterPort getPrinterPort(){
        return printerPort;
    }
    
    */
}
