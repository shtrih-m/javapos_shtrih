/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.FDOParameters;
import com.shtrih.util.CompositeLogger;

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

    private Thread thread = null;
    private volatile boolean stopFlag = true;

    public FSService(SMFiscalPrinter printer, FptrParameters parameters, FDOParameters ofdParameters) {
        if (printer == null)
            throw new IllegalArgumentException("printer is null");
        if (parameters == null)
            throw new IllegalArgumentException("parameters is null");
        if (ofdParameters == null)
            throw new IllegalArgumentException("ofdParameters is null");

        this.printer = printer;
        this.connectTimeout = parameters.FSConnectTimeout;
        this.parameters = ofdParameters;
    }

    public void run() {
        try {
            logger.debug(String.format("Starting FSService, OFD %s:%d, connection timeout %d ms, poll period %d ms",
                    parameters.getHost(),
                    parameters.getPort(),
                    connectTimeout,
                    parameters.getPollPeriodSeconds() * 1000));

            while (!stopFlag) {
                checkData();

                if (stopFlag)
                    break;

                Thread.sleep(parameters.getPollPeriodSeconds() * 1000);
            }

            logger.error("FSService stopped");
        } catch (InterruptedException e) {
            logger.error("FSService stopped");
        } catch (Exception e) {
            logger.error("FSService unexpected exception", e);
        }
    }

    private void checkData() {
        try {
            if (stopFlag)
                return;

            byte[] data = printer.fsReadBlockData();

            if (data.length == 0) {
                return;
            }

            // System.out.println("FS -> OFD: " + Hex.toHex(data));

            if (stopFlag)
                return;

            byte[] answer = sendData(data);
            if (answer.length == 0) {
                return;
            }

            // System.out.println("FS <- OFD: " + Hex.toHex(answer));

            if (stopFlag)
                return;

            printer.fsWriteBlockData(answer);

        } catch (Exception e) {
            logger.error("OFD data sending failed", e);
        }
    }

    private byte[] sendData(byte[] data) throws Exception {
        Socket socket = new Socket();
        try {
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
            readCount += in.read(buffer, offset + readCount, count - readCount);
        }
    }

    private boolean isStarted() {
        return !stopFlag;
    }

    public void start() throws Exception {
        if (!isStarted()) {
            stopFlag = false;
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() throws Exception {
        stopFlag = true;
        if (thread != null) {
            thread.interrupt();
//            thread.join();
        }

        thread = null;
    }
}
