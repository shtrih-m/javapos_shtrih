/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.util;

import gnu.io.*;
import java.io.*;
import java.nio.*;
import org.usb4java.*;
import java.nio.channels.*;
import ru.sir.ymodem.XModem;
import com.github.kairyu.flop.programmer.dfu.*;

/**
 *
 * @author V.Kravtsov
 */
public class FirmwareUpdater {

    public FirmwareUpdater() {
    }

    public static boolean capXModemUpdate() {
        return true;
    }

    public static boolean capDFUUpdate() {
        return true;
    }

    public static void updateFirmwareDFU(String firmwareFileName) throws Exception {
        CompositeLogger logger = CompositeLogger.getLogger(FirmwareUpdater.class);
        logger.debug("updateFirmwareDFU");
        
        RandomAccessFile aFile = new RandomAccessFile(firmwareFileName, "r");
        FileChannel inChannel = aFile.getChannel();
        long fileSize = inChannel.size();
        ByteBuffer buffer = ByteBuffer.allocateDirect((int) fileSize);
        inChannel.read(buffer);
        buffer.flip();

        DfuDevice dfuDevice = new DfuDevice();
        dfuDevice.init();

        int bus_number = 0;
        int device_address = 0;
        int vendorId = 0x1fc9;
        int productId = 0x0089;

        Device device = null;
        int maxTryCount = 10;
        for (int i = 0; i < maxTryCount; i++) {
            try {
                device = dfuDevice.initDevice(vendorId, productId,
                        bus_number, device_address, false, false);
                if (device != null) {
                    break;
                }
            } catch (Exception e) {
                logger.error("initDevice", e);
            }
            Thread.sleep(500);
        }
        if (device == null) {
            throw new Exception("Device not found");
        }

        try {
            dfuDevice.download(buffer);
            dfuDevice.reset();
        } finally {
            aFile.close();
            dfuDevice.uninitDevice();
            dfuDevice.uninit();
        }
        logger.debug("updateFirmwareDFU: OK");
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
            xModem.send(new File(firmwareFileName));
            serialport.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            serialport.close();
            throw e;
        }
    }

}
