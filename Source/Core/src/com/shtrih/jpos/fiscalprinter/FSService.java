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

/**
 * @author V.Kravtsov
 */
public class FSService implements Runnable {

    private CompositeLogger logger = CompositeLogger.getLogger(FSService.class);

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
            byte[] answer = sendData(data);
            if (answer.length == 0) {
                return;
            }
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

    private byte[] sendData(byte[] data) throws Exception {
        Socket socket = new Socket();
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
        socket.close();
        return answer;
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
