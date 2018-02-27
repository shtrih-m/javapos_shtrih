/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.util;

import gnu.io.*;
import java.nio.file.*;
import ru.sir.ymodem.XModem;

/**
 *
 * @author V.Kravtsov
 */
public class FirmwareUpdater 
{
    
    public FirmwareUpdater(){
    }
    
    public static boolean capXModemUpdate(){
        return true;
    }
    
    public static boolean capDFUUpdate(){
        return false;
    }
    
    public static void updateFirmwareDFU(String firmwareFileName) throws Exception
    {
        
    }
    
    public static void updateFirmwareXModem(String portName, String firmwareFileName) throws Exception
    {
        CompositeLogger logger = CompositeLogger.getLogger(FirmwareUpdater.class);
        CommPortIdentifier portId = CommPortIdentifier
                .getPortIdentifier(portName);
        if (portId == null) {
            throw new Exception("Port does not exist, " + portName);
        }
        SerialPort serialport = (SerialPort) portId.open("FirmwareUpdater", 0);
        if (serialport == null) {
            throw new Exception("Failed to open port " + portName);
        }
        try {
            serialport.setInputBufferSize(1024);
            serialport.setOutputBufferSize(1024);
            serialport.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            serialport.enableReceiveTimeout(100000);
            serialport.setSerialPortParams(115200, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            XModem xModem = new XModem(serialport.getInputStream(), serialport.getOutputStream());
            Path path = FileSystems.getDefault().getPath(".", firmwareFileName);
            xModem.send(path);
            serialport.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            serialport.close();
            throw e;
        }
    }
    
}
