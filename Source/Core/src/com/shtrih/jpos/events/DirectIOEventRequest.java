/*
 * DirectIOEventRequest.java
 *
 * Created on March 13 2008, 13:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.events;

import jpos.events.DirectIOEvent;
import jpos.services.EventCallbacks;
import com.shtrih.util.CompositeLogger;

/**
 * @author V.Kravtsov
 */

public final class DirectIOEventRequest implements Runnable {
    private final EventCallbacks cb;
    private final DirectIOEvent event;

    private static CompositeLogger logger = CompositeLogger.getLogger(DirectIOEventRequest.class);
    
    public DirectIOEventRequest(EventCallbacks cb, DirectIOEvent event) {
        this.cb = cb;
        this.event = event;
    }

    public void run() 
    {
        logger.debug("fireDirectIOEvent(" + event.getEventNumber() + ", " + event.getData() + ")");
        cb.fireDirectIOEvent(event);
    }
}
