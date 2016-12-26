package com.shtrih.jpos.events;

/*
 * EventRequest.java
 *
 * Created on 28 August 2007, 18:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author V.Kravtsov
 */

import jpos.events.StatusUpdateEvent;
import jpos.services.EventCallbacks;

import com.shtrih.util.CompositeLogger;

public final class StatusUpdateEventRequest implements Runnable {
    private final EventCallbacks cb;
    private final StatusUpdateEvent event;
    static CompositeLogger logger = CompositeLogger.getLogger(StatusUpdateEventRequest.class);

    public StatusUpdateEventRequest(EventCallbacks cb, StatusUpdateEvent event) {
        this.cb = cb;
        this.event = event;
    }

    public void run() {
        cb.fireStatusUpdateEvent(event);
    }
}
