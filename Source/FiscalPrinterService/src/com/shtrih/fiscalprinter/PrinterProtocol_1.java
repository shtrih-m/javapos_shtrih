/*
 * SmPrinterDevice.java
 *
 * Created on July 31 2007, 16:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 *
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
    private int maxRepeatCount = 1;
    private byte[] txData = new byte[0];
    private byte[] rxData = new byte[0];
    private final Frame frame = new Frame();
    private static CompositeLogger logger = CompositeLogger.getLogger(PrinterProtocol_1.class);

    public PrinterProtocol_1(PrinterPort port) {
        this.port = port;
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
            result = port.readByte();
            if (result != 0xFF) {
                break;
            }
        }
        return result;
    }

    private byte[] readAnswer(int timeout) throws Exception {
        int enqNumber = 0;
        for (;;) {
            port.setTimeout(timeout + byteTimeout);
            // STX
            while (port.readByte() != STX) {
            }
            // set byte timeout
            port.setTimeout(byteTimeout);
            // data length
            int dataLength = port.readByte() + 1;
            // command data
            byte[] commandData = port.readBytes(dataLength);
            // check CRC
            byte crc = commandData[commandData.length - 1];
            commandData = copyOf(commandData, commandData.length - 1);
            if (frame.getCrc(commandData) == crc) {
                port.write(ACK);
                return commandData;
            } else {
                port.write(NAK);
                port.write(ENQ);
                int B = readControlByte();
                switch (B) {
                    case ACK:
                        break;
                    case NAK:
                        return commandData;
                    default:
                        enqNumber++;
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
            port.write(data);
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
            port.write(ENQ);
            enqNumber++;

            int B = readControlByte();
            switch (B) {
                case ACK:
                    readAnswer(timeout);
                    ackNumber++;
                    break;

                case NAK:
                    writeCommand(data);
                    return readAnswer(timeout);

                default:
                    SysUtils.sleep(100);
                    enqNumber++;
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

    public byte[] sendCommand(byte[] data, int timeout)
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
                port.write(ENQ);

                int B = readControlByte();
                switch (B) {
                    case ACK:
                        readAnswer(0);
                        ackNumber++;
                        break;

                    case NAK:
                        return;

                    default:
                        SysUtils.sleep(100);
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

    public void send(PrinterCommand command) throws Exception {
        int repeatCount = 0;
        while (true) {
            try {
                repeatCount++;
                byte[] tx = command.encodeData();
                int timeout = command.getTimeout();
                byte[] rx = sendCommand(tx, timeout);
                command.decodeData(rx);
                break;
            } catch (IOException e) {
                logger.error("IOException: ", e);
                if (!command.getIsRepeatable()) {
                    throw e;
                }
                if (repeatCount >= maxRepeatCount) {
                    throw e;
                }
            } catch (DeviceException e) {
                logger.error("DeviceException: ", e);

                port.close();
                port.open(100);

                if (!command.getIsRepeatable()) {
                    throw e;
                }
                if (repeatCount >= maxRepeatCount) {
                    throw e;
                }
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

        public byte[] encode(byte[] data) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            baos.write(STX);
            baos.write(data.length);
            baos.write(data, 0, data.length);
            baos.write(getCrc(data));
            return baos.toByteArray();
        }
    }

}
