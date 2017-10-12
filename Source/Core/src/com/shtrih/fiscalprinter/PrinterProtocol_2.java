/*
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.shtrih.jpos.fiscalprinter.directIO.DIOExecuteCommand;
import com.shtrih.util.CompositeLogger;

import com.shtrih.fiscalprinter.command.CommandOutputStream;
import com.shtrih.fiscalprinter.command.PrinterCommand;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.port.PrinterPort;
import com.shtrih.util.Localizer;
import com.shtrih.util.Logger2;

/**
 * @author V.Kravtsov
 */
public class PrinterProtocol_2 implements PrinterProtocol {

    private int frameNumber = 0;
    private boolean isSynchronized = false;
    private final PrinterPort port;
    private final Frame frame = new Frame();
    byte[] rx = {};
    private int byteTimeout = 100;
    private int maxRepeatCount = 3;
    private static CompositeLogger logger = CompositeLogger.getLogger(PrinterProtocol_2.class);

    public PrinterProtocol_2(PrinterPort port) {
        this.port = port;
    }

    public PrinterProtocol_2() {
        this.port = null;
    }

    public int getByteTimeout() {
        return byteTimeout;
    }

    public int getMaxRepeatCount() {
        return maxRepeatCount;
    }

    public void setByteTimeout(int value) {
        this.byteTimeout = value;
    }

    public void setMaxRepeatCount(int value) {
        this.maxRepeatCount = value;
    }

    public synchronized void connect() throws Exception {
        synchronizeFrames(3000);
    }

    public synchronized void doSend(PrinterCommand command) throws Exception {
        for (int i = 0; i < maxRepeatCount; i++) {
            if (sendCmd(command, i + 1)) {
                return;
            }
        }
        throw new DeviceException(PrinterConst.SMFPTR_E_NOCONNECTION,
                Localizer.getString(Localizer.NoConnection));
    }

    public synchronized void send(PrinterCommand command) throws Exception {
        synchronized (port.getSyncObject()) {
            doSend(command);
        }
    }

    public synchronized boolean sendCmd(PrinterCommand command, int retryNum)
            throws Exception {
        port.open(0);
        int timeout = command.getTimeout();
        port.setTimeout(timeout + byteTimeout);
        try {
            sendCommand(command.encodeData());
            int frameNum = readAnswer();
            if (frameNum != frameNumber) {
                if ((retryNum != 1) && (frameNum == (frameNumber - 1))) {
                    frameNumber = frameNum;
                    stepFrameNumber();
                    command.decodeData(rx);
                    return true;
                } else {
                    frameNumber = frameNum;
                    stepFrameNumber();
                    return false;
                }
            }
            stepFrameNumber();
            command.decodeData(rx);
            return true;
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
    }

    public void synchronizeFrames(int timeout) throws Exception {
        if (isSynchronized) {
            return;
        }
        port.setTimeout(timeout);
        for (int i = 0; i < maxRepeatCount; i++) {
            try {
                sendCommand(null);
                frameNumber = readAnswer();
                isSynchronized = true;
                stepFrameNumber();
                return;
            } catch (Exception e) {
                logger.error(e);
            }
        }

        throw new IOException("Frames sync failed");
    }

    private void sendCommand(byte[] data) throws Exception {
        byte[] tx = frame.encode(data);
        Logger2.logTx(logger, tx);
        port.write(tx);
    }

    private void stepFrameNumber() {
        if (frameNumber == 0xFFFF) {
            frameNumber = 0;
        } else {
            frameNumber++;
        }
    }

    private int readAnswer() throws Exception {
        while (readByte() != Frame.STX) {
        }
        int len = readWord();
        int num = readWord();
        if (len > 0) {
            rx = readBytes(len - 2);
        }
        int crc = readWord();

        CommandOutputStream stream = new CommandOutputStream("");
        stream.writeByte(Frame.STX);
        stream.writeShort(len);
        stream.writeShort(num);
        stream.writeBytes(rx);
        stream.writeShort(crc);
        Logger2.logRx(logger, stream.getData());

        stream = new CommandOutputStream("");
        stream.writeShort(len);
        stream.writeShort(num);
        stream.writeBytes(rx);
        int frameCrc = frame.getCRC(stream.getData());
        if (crc != frameCrc) {
            String text = "Invalid CRC (" + String.valueOf(crc) + " <> "
                    + String.valueOf(frameCrc) + ")";
            throw new Exception(text);
        }
        return num;

    }

    public byte[] readBytes(int count) throws Exception {

        byte[] result = new byte[count];

        for (int i = 0;i<count;i++){
            result[i] = (byte)readByte();
        }

        return result;
    }

    private int readWord() throws Exception {
        int b1 = readByte();
        int b2 = readByte();
        return b1 + (b2 << 8);
    }

    public int readByte() throws Exception {
        int C = port.readByte();
        if (C == Frame.ESC) {
            C = port.readByte();
            if (C == Frame.TSTX) {
                C = Frame.STX;
            } else if (C == Frame.TESC) {
                C = Frame.ESC;
            }
        }
        return C;
    }

    public Frame getFrame() {
        return frame;
    }

    public class Frame {

        public static final int STX = 0x8F;
        public static final int ESC = 0x9F;
        public static final int TSTX = 0x81;
        public static final int TESC = 0x83;

        public int Short(int value) {
            return value & 0xFFFF;
        }

        public int byteToInt(int B) {
            if (B < 0) {
                B = 256 + B;
            }
            return B;
        }

        public int updateCRC(int CRC, int value) {
            int result = Short(((CRC >>> 8) | (Short(CRC << 8))));
            result = Short(result ^ (Short(value)));
            result = Short(result ^ Short((result & 0x00FF) >>> 4));
            result = Short(result ^ (Short(result << 12)));
            result = Short(result ^ (Short((result & 0x00FF) << 5)));
            return result;
        }

        public int getCRC(byte[] data) {
            int result = 0xFFFF;
            for (int i = 0; i < data.length; i++) {
                result = updateCRC(result, byteToInt(data[i]));
            }
            return result;
        }

        private byte[] encode(byte[] data) throws Exception {
            CommandOutputStream stream = new CommandOutputStream("");
            if (data == null) {
                stream.writeShort(0);
            } else if (data.length == 0) {
                stream.writeShort(0);
            } else {
                stream.writeShort(data.length + 2);
                stream.writeShort(frameNumber);
                stream.writeBytes(data);
            }
            stream.writeShort(getCRC(stream.getData()));

            CommandOutputStream result = new CommandOutputStream("");
            result.writeByte(STX);
            result.writeBytes(stuffing(stream.getData()));
            return result.getData();
        }

        public byte[] stuffing(byte[] data) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            for (int i = 0; i < data.length; i++) {
                int item = byteToInt(data[i]);
                if (item == STX) {
                    stream.write(ESC);
                    stream.write(TSTX);
                } else if (item == ESC) {
                    stream.write(ESC);
                    stream.write(TESC);
                } else {
                    stream.write(data[i]);
                }
            }
            return stream.toByteArray();
        }

        public byte[] destuffing(byte[] data) throws Exception {
            byte[] result = {};
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if ((data == null) || (data.length == 0)) {
                return result;
            }
            for (int i = 0; i < data.length; i++) {
                if (byteToInt(data[i]) == ESC) {
                    if (i == data.length - 1) {
                        break;
                    }
                    if (byteToInt(data[i + 1]) == TSTX) {
                        stream.write(STX);
                    }
                    if (byteToInt(data[i + 1]) == TESC) {
                        stream.write(ESC);
                    }
                } else {
                    stream.write(data[i]);
                }
            }
            return stream.toByteArray();
        }

    }

}
