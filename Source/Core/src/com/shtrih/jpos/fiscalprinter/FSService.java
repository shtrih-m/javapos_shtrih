/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.DeviceException;
import com.shtrih.fiscalprinter.command.FDOParameters;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Time;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.FileOutputStream;

/**
 * @author V.Kravtsov
 */
public class FSService implements Runnable {

    private CompositeLogger logger = CompositeLogger.getLogger(FSService.class);

    private long packetNumber = 0;
    private final FDOParameters parameters;
    private final int connectTimeout;
    private final SMFiscalPrinter printer;
    private volatile Thread thread = null;
    private volatile boolean stopFlag = false;

    public FSService(SMFiscalPrinter printer, FptrParameters parameters, FDOParameters ofdParameters) {
        if (printer == null) {
            throw new IllegalArgumentException("printer is null");
        }
        if (parameters == null) {
            throw new IllegalArgumentException("parameters is null");
        }
        if (ofdParameters == null) {
            throw new IllegalArgumentException("ofdParameters is null");
        }

        this.printer = printer;
        this.connectTimeout = parameters.FSConnectTimeout;
        this.parameters = ofdParameters;
    }

    private boolean isStarted() {
        return thread != null;
    }

    public void start() throws Exception {
        if (!isStarted()) {
            logger.debug("FSService starting");
            stopFlag = false;
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() throws Exception {

        if (isStarted()) {
            logger.debug("FSService stopping");
            stopFlag = true;
            thread.interrupt();
            thread.join();
            thread = null;
        }
    }

    public void run() {
        try {
            logger.debug("FSService started");
            logger.debug(String.format("OFD %s:%d, connection timeout %d ms, poll period %d ms",
                    parameters.getHost(), parameters.getPort(), connectTimeout,
                    parameters.getPollPeriodSeconds() * 1000));

            while (!stopFlag) {
                checkData();
                Time.delay(parameters.getPollPeriodSeconds() * 1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.debug("FSService stopped");
    }

    private void checkData() throws Exception {
        try {
            byte[] data = printer.fsReadBlockData();
            if (data.length == 0) {
                return;
            }
            // System.out.println("FS -> OFD: " + Hex.toHex(data));
            if (stopFlag) {
                return;
            }
            //packetNumber++;
            //saveToFile(data, String.format("FSDocument_%04d.bin", packetNumber));

            // P-protocol version 0x0102 -> 0x0120
            if ((data.length >= 30) && (data[6] == 0x01) && (data[7] == 0x02)
                    && (data[28] == 0) && (data[29] == 0)) {
                data[7] = 0x20;
            }
            //saveToFile(data, String.format("OFDDocument_%04d.bin", packetNumber));

            byte[] answer = sendData(data);
            if (answer.length == 0) {
                return;
            }
            //saveToFile(answer, String.format("OFDTicket_%04d.bin", packetNumber));
            // System.out.println("FS <- OFD: " + Hex.toHex(answer));
            if (stopFlag) {
                return;
            }
            printer.fsWriteBlockData(answer);

        } catch (IOException e) {
            logger.error("OFD data sending failed", e);
        } catch (DeviceException e) {
            logger.error("OFD data sending failed2", e);
        }
    }

    private void saveToFile(byte[] data, String fileName) {
        try {
            FileOutputStream os = new FileOutputStream(fileName);
            os.write(data);
            os.close();
        } catch (Exception e) {
            logger.error("Failed to save data", e);
        }
    }

    private byte[] sendData(byte[] data) throws Exception {
        Socket socket = new Socket();
        try {
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(connectTimeout);
            socket.connect(new InetSocketAddress(parameters.getHost(), parameters.getPort()));
            socket.getOutputStream().write(data);
            InputStream in = socket.getInputStream();

            int headerSize = 30;
            byte[] header = new byte[headerSize];

            Read(in, header, headerSize);

            int size = ((header[25] << 8)) | (header[24] & 0xFF);

            byte[] answer = new byte[headerSize + size];
            System.arraycopy(header, 0, answer, 0, headerSize);
            if (size > 0) {
                Read(in, answer, headerSize, size);
            }
            return answer;
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                logger.error("Socket close failed", e);
            }
        }
    }

    private void Read(InputStream in, byte[] buffer, int count) throws IOException {
        Read(in, buffer, 0, count);
    }

    private void Read(InputStream in, byte[] buffer, int offset, int count) throws IOException {
        int readCount = 0;
        while (readCount < count) {
            int newBytes = in.read(buffer, offset + readCount, count - readCount);

            if (newBytes < 0) {
                throw new IOException("Connection reset by OFD");
            }

            readCount += newBytes;
        }
    }

}
