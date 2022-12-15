/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.port;

import com.shtrih.NativeResource;
import com.shtrih.util.CompositeLogger;

import com.shtrih.LibManager;
import ru.shtrih_m.kktnetd.Api;
import ru.shtrih_m.kktnetd.PPPConfig;
import ru.shtrih_m.kktnetd.PPPStatus;

import com.shtrih.util.StringUtils;

import java.io.InputStream;
import java.util.Calendar;

/**
 *
 * @author Виталий
 */
public class PPPThread implements Runnable {

    private long ctx = 0;
    private String status = "";
    private Thread thread = null;
    private final PPPConfig config;
    private static CompositeLogger logger = CompositeLogger.getLogger(PPPThread.class);

    public PPPThread(PPPConfig config) {
        this.config = config;
    }

    public boolean isStarted() {
        return (thread != null);
    }

    public void start() throws Exception {
        if (isStarted()) {
            return;
        }

        logger.debug("PPP thread starting...");
        ctx = 0;
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try
        {
            logger.debug("PPP thread started");
            String configText = config.toJson();
            ctx = Api.api_init(configText);
            logger.debug(String.format("api_init returned %d", ctx));
            logger.debug(String.format("api_status returned %s", Api.api_status(ctx)));
            long rc = Api.api_run(ctx);
            synchronized (this) {
                Api.api_deinit(ctx);
                ctx = 0;
            }
            logger.debug(String.format("PPP thread exited with code %d", rc));
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public String getStatus()
    {
        String result = "";
        if (isStarted() && (ctx != 0))
        {
            result = Api.api_status(ctx);
            if (result == null){
                result = "";
            }
            if (!status.equalsIgnoreCase(result))
            {
                status = result;
                logger.debug("PPP status =" + result);
            }
        }
        return result;
    }

    public void stop() {
        if (!isStarted()) {
            return;
        }

        logger.debug("PPP thread stopping...");
        try {
            synchronized (this) {
                if (ctx != 0) {
                    Api.api_stop(ctx);
                }
            }
            thread.join();
            thread = null;
            status = "";
        } catch (Exception e) {
            logger.error(e);
        }
        logger.debug("PPP thread stopped");
    }

    public boolean waitForContext(long timeout, long timeToSleep) throws InterruptedException
    {
        long time = Calendar.getInstance().getTimeInMillis() + timeout;
        for (;;){
            if (ctx != 0) {
                return true;
            }
            if (Calendar.getInstance().getTimeInMillis() > time){
                return false;
            }
            Thread.sleep(timeToSleep);
        }
    }

    public boolean waitForStatus(String statusToWait, long timeout) throws Exception
    {
        long time = Calendar.getInstance().getTimeInMillis() + timeout;
        for (;;)
        {
            String statusJson = getStatus();
            if (!statusJson.isEmpty()) {
                PPPStatus status = PPPStatus.fromJson(statusJson);
                if (statusToWait.equals(status.status)) {
                    return true;
                }
            }
            if (Calendar.getInstance().getTimeInMillis() > time){
                return false;
            }
            Thread.sleep(1000);
        }
    }

    public boolean waitForStatus(PPPStatus statusToWait, long timeout) throws Exception
    {
        long time = Calendar.getInstance().getTimeInMillis() + timeout;
        for (;;)
        {
            String statusJson = getStatus();
            if (!statusJson.isEmpty()) {
                PPPStatus status = PPPStatus.fromJson(statusJson);
                if (statusToWait.equals(status)) {
                    return true;
                }
            }
            if (Calendar.getInstance().getTimeInMillis() > time){
                return false;
            }
            Thread.sleep(1000);
        }
    }

    public boolean waitForPhase(String phase, long timeout) throws Exception
    {
        long time = Calendar.getInstance().getTimeInMillis() + timeout;
        for (;;)
        {
            String statusJson = getStatus();
            if (!statusJson.isEmpty()) {
                PPPStatus status = PPPStatus.fromJson(statusJson);
                if (phase.equals(status.phase)) {
                    return true;
                }
            }
            if (Calendar.getInstance().getTimeInMillis() > time){
                return false;
            }
            Thread.sleep(1000);
        }
    }

    public void showStatus() throws InterruptedException
    {
        String status = "";
        for (;;) {
            String newStatus = getStatus();
            if (!newStatus.equals(status)) {
                status = newStatus;
                logger.debug("PPP status: " + status);
                Thread.sleep(100);
            }
        }
    }
}
