/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.port;

import com.shtrih.util.CompositeLogger;
import ru.shtrih_m.kktnetd.Api;
import ru.shtrih_m.kktnetd.Config;
import com.shtrih.util.StringUtils;

/**
 *
 * @author Виталий
 */
public class PPPThread implements Runnable {
    
    private long ctx = 0;
    private Thread thread = null;
    private final String portName;
    private static CompositeLogger logger = CompositeLogger.getLogger(PPPThread.class);

    public PPPThread(String portName){
        this.portName = portName;
    }
            
    public boolean isStarted() {
        return (thread != null);
    }

    public void start() throws Exception {
        if (isStarted()) {
            return;
        }

        logger.debug("PPP thread starting...");
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            logger.debug("PPP thread started");
            String configText = null;
            //configText = new Config().toJson();
            configText = StringUtils.InputStreamToString(getClass().getResourceAsStream("PPPConfig.json"));
            
            ctx = Api.api_init(configText);
            logger.debug(String.format("api_init returned %d", ctx));
            
            long rc = Api.api_run(ctx);
            Api.api_deinit(ctx);
            logger.debug(String.format("PPP thread exited with code %d", rc));
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void stop() {
        if (!isStarted()) {
            return;
        }

        logger.debug("PPP thread stopping...");
        try {
            Api.api_stop(ctx);
            thread.join();
            thread = null;
        } catch (Exception e) {
            logger.error(e);
        }
    }
    
}