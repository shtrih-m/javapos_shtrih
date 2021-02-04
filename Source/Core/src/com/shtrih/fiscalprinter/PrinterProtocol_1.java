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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.shtrih.util.CompositeLogger;

import com.shtrih.fiscalprinter.command.PrinterCommand;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.port.PrinterPort;
import com.shtrih.util.Localizer;
import com.shtrih.util.Logger2;
import com.shtrih.util.SysUtils;
import com.shtrih.util.Time;

public class PrinterProtocol_1 implements PrinterProtocol {
    // serial port interface

    private PrinterPort port = null;
    // byte receive timeout
    private int byteTimeout = 100;
    // constants
    private final static byte STX = 0x02;
    private final static byte ENQ = 0x05;
    private final static byte ACK = 0x06;
    private final static byte NAK = 0x15;
    // maximum counters
    private int maxEnqNumber = 3;
    private int maxNakCommandNumber = 3;
    private int maxNakAnswerNumber = 3;
    private int maxAckNumber = 3;
    private int maxRepeatCount = 3;
    private byte[] txData = new byte[0];
    private byte[] rxData = new byte[0];
    private final Frame frame = new Frame();
    private static CompositeLogger logger = CompositeLogger.getLogger(PrinterProtocol_1.class);

    public PrinterProtocol_1(PrinterPort port) {
        this.port = port;
    }

    private void portWrite(int b) throws Exception {
        byte[] data = new byte[1];
        data[0] = (byte) b;
        portWrite(data);
    }

    private void portWrite(byte[] data) throws Exception {
        Logger2.logTx(logger, data);
        port.write(data);
    }

    int portReadByte() throws Exception {
        int b = port.readByte();
        Logger2.logRx(logger, (byte) b);
        return b;
    }

    byte[] portReadBytes(int len) throws Exception {
        byte[] data = port.readBytes(len);
        Logger2.logRx(logger, data);
        return data;
    }

    public PrinterPort getPrinterPort() {
        return port;
    }

    public void setPrinterPort(PrinterPort port) {
        this.port = port;
    }

    public int getByteTimeout() {
        return byteTimeout;
    }

    public void setByteTimeout(int value) {
        this.byteTimeout = value;
    }

    private static byte[] copyOf(byte[] original, int newLength) {
        byte[] copy = new byte[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;

    }

    private int readControlByte() throws Exception {
        int result = 0;
        while (true) {
            result = portReadByte();
            if (result != 0xFF) {
                break;
            }
        }
        return result;
    }

    private byte[] readAnswer(int timeout) throws Exception {
        int enqNumber = 0;
        int nakCount = 0;
        for (;;) {
            port.setTimeout(timeout + byteTimeout);
            // STX
            while (portReadByte() != STX) {
            }
            // set byte timeout
            port.setTimeout(byteTimeout);
            // data length
            int dataLength = portReadByte() + 1;
            // command data
            byte[] commandData = portReadBytes(dataLength);
            // check CRC
            byte crc = commandData[commandData.length - 1];
            commandData = copyOf(commandData, commandData.length - 1);
            if (frame.getCrc(commandData) == crc) {
                portWrite(ACK);
                return commandData;
            } else {
                if (nakCount >= maxNakAnswerNumber) {
                    throw DeviceException.readAnswerError();
                }
                nakCount++;
                portWrite(NAK);
                for (;;) {
                    portWrite(ENQ);
                    enqNumber++;
                    int B = readControlByte();
                    if (B == ACK) {
                        break;
                    }
                    if (B == NAK) {
                        throw DeviceException.readAnswerError();
                    }
                    if (enqNumber >= maxEnqNumber) {
                        throw DeviceException.readAnswerError();
                    }
                }

            }
        }
    }

    private void writeCommand(byte[] data) throws Exception {
        byte nakCommandNumber = 0;
        while (true) {
            portWrite(data);
            switch (readControlByte()) {
                case ACK:
                    return;
                case NAK:
                    nakCommandNumber++;
                    if (nakCommandNumber >= maxNakCommandNumber) {
                        throw new DeviceException(
                                PrinterConst.SMFPTR_E_NOCONNECTION,
                                "nakCommandNumber >= maxNakCommandNumber");
                    }
                    break;

                default:
                    return;
            }
        }
    }

    private byte[] send(byte[] data, int timeout) throws Exception {
        int ackNumber = 0;
        int enqNumber = 0;

        for (;;) {
            port.setTimeout(byteTimeout);
            portWrite(ENQ);
            enqNumber++;

            int B;
            try {
                B = readControlByte();
            } catch (IOException e) {

                logger.debug("<- timeout " + enqNumber + "/" + maxEnqNumber);

                if (enqNumber >= maxEnqNumber) {
                    throw new DeviceException(
                            PrinterConst.SMFPTR_E_NOCONNECTION,
                            Localizer.getString(Localizer.NoConnection));

                }

                continue;
            }

            switch (B) {
                case ACK:
                    readAnswer(timeout);
                    ackNumber++;
                    break;

                case NAK:
                    writeCommand(data);
                    return readAnswer(timeout);

                default:
                    Time.delay(100);
            }

            if (ackNumber >= maxAckNumber) {
                throw new DeviceException(PrinterConst.SMFPTR_E_NOCONNECTION,
                        Localizer.getString(Localizer.NoConnection));
            }

            if (enqNumber >= maxEnqNumber) {
                throw new DeviceException(
                        PrinterConst.SMFPTR_E_NOCONNECTION,
                        Localizer.getString(Localizer.NoConnection));

            }
        }
    }

    private byte[] sendCommand(byte[] data, int timeout)
            throws Exception {
        txData = frame.encode(data);
        byte[] rx = send(txData, timeout);
        rxData = frame.encode(rx);
        return rx;
    }

    public void connect() throws Exception {
        int ackNumber = 0;
        int enqNumber = 0;

        for (;;) {
            try {
                port.setTimeout(byteTimeout);
                portWrite(ENQ);

                int B = readControlByte();
                switch (B) {
                    case ACK:
                        readAnswer(0);
                        ackNumber++;
                        break;

                    case NAK:
                        return;

                    default:
                        Time.delay(100);
                        enqNumber++;
                }

            } catch (IOException e) {
                enqNumber++;
            }
            if (ackNumber >= maxAckNumber) {
                throw new DeviceException(PrinterConst.SMFPTR_E_NOCONNECTION,
                        Localizer.getString(Localizer.NoConnection));
            }

            if (enqNumber >= maxEnqNumber) {
                throw new DeviceException(PrinterConst.SMFPTR_E_NOCONNECTION,
                        Localizer.getString(Localizer.NoConnection));
            }
        }
    }

    private void sendCommand(PrinterCommand command) throws Exception {
        int repeatCount = 0;
        while (true) {
            try {
                logger.debug("sendCommand: " + command.getText() + ", " + command.getIsRepeatable());

                if (repeatCount > 0) {
                    logger.debug("retry " + repeatCount + "...");
                }
                repeatCount++;
                byte[] tx = command.encodeData();
                int timeout = command.getTimeout();
                byte[] rx = sendCommand(tx, timeout);
                command.decodeData(rx);
                break;

            } catch (AnswerCodeException e)
            {
                // !!! Thread.sleep(100) ???
                try {
                    port.readBytes(0xFF);
                } catch (Exception e1) {
                }
                throw e;
            }
        }
    }

    public void setMaxEnqNumber(int value) {
        maxEnqNumber = value;
    }

    public int getMaxEnqNumber() {
        return maxEnqNumber;
    }

    public int getMaxNakCommandNumber() {
        return maxNakCommandNumber;
    }

    public int getMaxNakAnswerNumber() {
        return maxNakAnswerNumber;
    }

    public int getMaxAckNumber() {
        return maxAckNumber;
    }

    public int getMaxRepeatCount() {
        return maxRepeatCount;
    }

    public void setMaxNakCommandNumber(int value) {
        maxNakCommandNumber = value;
    }

    public void setMaxNakAnswerNumber(int value) {
        maxNakAnswerNumber = value;
    }

    public void setMaxAckNumber(int value) {
        maxAckNumber = value;
    }

    public void setMaxRepeatCount(int value) {
        maxRepeatCount = value;
    }

    public byte[] getTxData() {
        return txData;
    }

    public byte[] getRxData() {
        return rxData;
    }

    public class Frame {

        private final byte STX = 0x02;

        public byte getCrc(byte[] data) {
            byte crc = (byte) data.length;
            for (int i = 0; i < data.length; i++) {
                crc ^= data[i];
            }
            return crc;
        }

        public byte[] encode(byte[] data) throws Exception
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (data.length > 0xFF) {
                throw new Exception("Data length exeeds 256 bytes");
            }

            baos.write(STX);
            baos.write(data.length);
            baos.write(data, 0, data.length);
            baos.write(getCrc(data));
            return baos.toByteArray();
        }
    }

    public void send(PrinterCommand command) throws Exception {
        synchronized (port.getSyncObject()) {
            sendCommand(command);
        }
    }

}
