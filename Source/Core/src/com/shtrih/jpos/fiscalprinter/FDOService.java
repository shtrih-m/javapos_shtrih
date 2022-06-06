/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.DeviceException;
import static com.shtrih.fiscalprinter.SMFiscalPrinterImpl.logger;
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
public class FDOService implements Runnable {

    private boolean started = false;
    private final SMFiscalPrinter printer;
    private volatile Thread thread = null;
    private CompositeLogger logger = CompositeLogger.getLogger(FDOService.class);

    public FDOService(SMFiscalPrinter printer) {
        if (printer == null) {
            throw new IllegalArgumentException("Printer is null");
        }
        this.printer = printer;
    }

    public boolean isStarted() {
        return started;
    }

    public void start() throws Exception {
        if (!isStarted()) {
            logger.debug("FSService starting");
            started = true;
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() throws Exception {

        if (isStarted()) {
            logger.debug("FSService stopping");
            thread.interrupt();
            thread.join();
            thread = null;
        }
    }

    public void run() {
        try {
            logger.debug("FSService started");
            printer.sendFDODocuments();
            
        } catch (Exception e) {
            logger.error("FDO data sending failed", e);
            logger.error(e.getMessage());
        }
        started = false;
        logger.debug("FSService stopped");
    }

}
