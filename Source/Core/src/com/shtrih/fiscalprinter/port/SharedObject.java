/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.port;

import com.shtrih.util.CompositeLogger;
import gnu.io.SerialPort;

/**
 *
 * @author V.Kravtsov
 */
public class SharedObject {

    private final Object item;
    private final String name;
    private int refCount = 0;
    static CompositeLogger logger = CompositeLogger.getLogger(SharedObject.class);

    public SharedObject(Object item, String name) {
        this.item = item;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Object getItem() {
        return item;
    }

    public void addRef() 
    {
        refCount += 1;
        logger.debug("refCount: " + refCount);
    }

    public void release() {
        refCount -= 1;
        logger.debug("refCount: " + refCount);
    }

    public int getRefCount() {
        return refCount;
    }
}
