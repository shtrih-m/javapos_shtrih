/*
 * ErrorEventRequest.java
 *
 * Created on March 13 2008, 13:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.events;

import jpos.events.ErrorEvent;
import jpos.services.EventCallbacks;

/**
 * @author V.Kravtsov
 */

public final class ErrorEventRequest implements Runnable {
    private final EventCallbacks cb;
    private final ErrorEvent event;

    public ErrorEventRequest(EventCallbacks cb, ErrorEvent event) {
        this.cb = cb;
        this.event = event;
    }

    public void run() {
        cb.fireErrorEvent(event);
    }

    public ErrorEvent getEvent() {
        return event;
    }
}
