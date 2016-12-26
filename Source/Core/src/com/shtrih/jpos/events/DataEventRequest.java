/*
 * DataEventRequest.java
 *
 * Created on March 13 2008, 13:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.events;

import jpos.events.DataEvent;
import jpos.services.EventCallbacks;

/**
 * @author V.Kravtsov
 */

public class DataEventRequest implements Runnable {
    private final EventCallbacks cb;
    private final DataEvent event;

    public DataEventRequest(EventCallbacks cb, DataEvent event) {
        this.cb = cb;
        this.event = event;
    }

    public void run() {
        cb.fireDataEvent(event);
    }
}
