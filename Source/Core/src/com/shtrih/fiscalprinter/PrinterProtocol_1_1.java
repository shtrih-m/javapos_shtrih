/*
 * SmPrinterDevice.java
 *
 * Created on July 31 2007, 16:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 * @author V.Kravtsov
 */
package com.shtrih.fiscalprinter;

import java.io.IOException;
import java.io.ByteArrayOutputStream;

import com.shtrih.util.CompositeLogger;

import com.shtrih.util.Time;
import com.shtrih.util.Logger2;
import com.shtrih.util.SysUtils;
import com.shtrih.fiscalprinter.port.PrinterPort;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.command.PrinterCommand;

public class PrinterProtocol_1_1 implements PrinterProtocol 
{
    // constants
    private final static byte STX = 0x02;

    private PrinterPort port = null;
    private int byteTimeout = 100;
    private final Frame frame = new Frame();
    private static CompositeLogger logger = CompositeLogger.getLogger(PrinterProtocol_1_1.class);

    public PrinterProtocol_1_1(PrinterPort port) {
        this.port = port;
    }

    private void portWrite(byte[] data) throws Exception {
        Logger2.logTx(logger, data);
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
        port.write(data);
    }

    private byte[] portReadBytes(int len) throws Exception {
        byte[] data = port.readBytes(len);
        Logger2.logRx(logger, data);
        return data;
    }

    private int portReadByte() throws Exception {
        return portReadBytes(1)[0];
    }

    private byte[] readAnswer(int timeout) throws Exception {
        port.setTimeout(timeout + byteTimeout);
        // STX
        while (portReadByte() != STX) {
        }
        // set byte timeout
        port.setTimeout(byteTimeout);
        // data length
        int dataLength = portReadByte();
        // command data
        byte[] data = portReadBytes(dataLength);
        return data;
    }

    private byte[] sendCommand(byte[] data, int timeout)
            throws Exception 
    {
        port.setTimeout(byteTimeout);
        data = frame.encode(data);
        portWrite(data);
        byte[] rx = readAnswer(timeout);
        return frame.encode(rx);
    }

    public void connect() throws Exception 
    {
    }

    public int getByteTimeout() {
        return byteTimeout;
    }

    public void setByteTimeout(int value) {
        this.byteTimeout = value;
    }
    
    public class Frame {

        public byte[] encode(byte[] data) throws Exception {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (data.length > 0xFF) {
                throw new Exception("Data length exeeds 256 bytes");
            }

            baos.write(STX);
            baos.write(data.length);
            baos.write(data, 0, data.length);
            return baos.toByteArray();
        }
    }

    public void send(PrinterCommand command) throws Exception {
        synchronized (port.getSyncObject()) 
        {
            logger.debug("sendCommand: " + command.getText() + ", " + command.getIsRepeatable());
            byte[] tx = command.encodeData();
            int timeout = command.getTimeout();
            byte[] rx = sendCommand(tx, timeout);
            command.decodeData(rx);
        }
    }

}
