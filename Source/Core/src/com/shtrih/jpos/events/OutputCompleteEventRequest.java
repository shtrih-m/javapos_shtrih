/*
 * OutputCompleteEventRequest.java
 *
 * Created on March 13 2008, 13:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.events;

import jpos.events.OutputCompleteEvent;
import jpos.services.EventCallbacks;

/**
 * @author V.Kravtsov
 */

public final class OutputCompleteEventRequest implements Runnable {
    private final EventCallbacks cb;
    private final OutputCompleteEvent event;

    public OutputCompleteEventRequest(EventCallbacks cb,
            OutputCompleteEvent event) {
        this.cb = cb;
        this.event = event;
    }

    public void run() {
        cb.fireOutputCompleteEvent(event);
    }
}
