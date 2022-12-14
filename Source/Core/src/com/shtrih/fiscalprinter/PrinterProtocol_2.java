package com.shtrih.fiscalprinter;

import com.shtrih.fiscalprinter.command.CommandOutputStream;
import com.shtrih.fiscalprinter.command.PrinterCommand;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.port.PrinterPort;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Localizer;
import com.shtrih.util.Logger2;

import java.io.ByteArrayOutputStream;

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
    private final boolean isReliable;
    private static CompositeLogger logger = CompositeLogger.getLogger(PrinterProtocol_2.class);

    public PrinterProtocol_2(PrinterPort port) {
        this.port = port;
        int[] data = new int[1];
        data[0] = 0;
        port.directIO(PrinterPort.DIO_READ_IS_RELIABLE, data, null);
        isReliable = (data[0] == 1);
    }

    public void setByteTimeout(int value) {
        this.byteTimeout = value;
    }

    public void connect() throws Exception
    {
        synchronizeFrames(byteTimeout);
    }

    public void send(PrinterCommand command) throws Exception {

        try {
            connect();

            int timeout = command.getTimeout();
            port.setTimeout(timeout + byteTimeout);

            writeCommand(command.encodeData());
            if (command.readAnswer) {
                int frameNum = readAnswer();
                if (frameNum != frameNumber) {
                    logger.error("Incorrect frame number");
                    for (; ; ) {
                        frameNum = readAnswer();
                        if (frameNum == frameNumber) {
                            break;
                        }
                    }
                    stepFrameNumber();
                    command.decodeData(rx);
                    return;
                }
                command.decodeData(rx);
            }
            stepFrameNumber();
            return;

        } catch (Exception e)
        {
            logger.error("sendCommand", e);
            isSynchronized = false;
            throw e;
        }
    }

    private void synchronizeFrames(int timeout) throws Exception {
        if (isSynchronized) {
            return;
        }

        port.setTimeout(timeout);
        writeCommand(null);
        for(;;) {
            frameNumber = readAnswer();
            if (rx.length == 0) break;
        }
        isSynchronized = true;
        stepFrameNumber();
    }

    private void writeCommand(byte[] data) throws Exception {
        byte[] tx = frame.encode(data, frameNumber);
        Logger2.logTx(logger, tx);
        if (Thread.currentThread().isInterrupted()){
            throw new InterruptedException();
        }
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
        int b = readByte();
        while (b != Frame.STX) {
            b = readByte();
            Logger2.logRx(logger, (byte) b);
        }

        int len = readWord();
        int num = readWord();
        if (len > 0) {
            rx = readBytes(len - 2);
            Logger2.logRx(logger, rx);
        }

        int crc = readWord();

        CommandOutputStream stream = new CommandOutputStream("");
        stream.writeShort(len);
        stream.writeShort(num);
        stream.writeBytes(rx);
        int frameCrc = frame.getCRC(stream.getData());
        if (crc != frameCrc) {
            throw DeviceException.readAnswerError();
        }
        return num;

    }

    public byte[] readBytes(int count) throws Exception {

        byte[] result = new byte[count];

        for (int i = 0; i < count; i++) {
            result[i] = (byte) readByte();
        }

        return result;
    }

    private int readWord() throws Exception {
        int b1 = readByte();
        int b2 = readByte();

        Logger2.logRx(logger, (byte) b1, (byte) b2);

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

    public static class Frame {

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

        private byte[] encode(byte[] data, int frameNumber) throws Exception {
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
