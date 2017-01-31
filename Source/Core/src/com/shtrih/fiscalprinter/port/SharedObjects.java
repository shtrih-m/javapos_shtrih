/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.port;

import java.util.Vector;
import com.shtrih.util.CompositeLogger;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Vector;

import com.shtrih.util.CompositeLogger;
import java.net.Socket;

public class SharedObjects {

    private Vector items = new Vector();
    private static SharedObjects instance;
    static CompositeLogger logger = CompositeLogger.getLogger(SharedObjects.class);

    /**
     * A private Constructor prevents any other class from instantiating.
     */
    private SharedObjects() {
    }

    public static synchronized SharedObjects getInstance() {
        if (instance == null) {
            instance = new SharedObjects();
        }
        return instance;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public int size() {
        return items.size();
    }

    public synchronized SharedObject find(String name) {
        for (int i = 0; i < items.size(); i++) {
            SharedObject item = (SharedObject) items.get(i);
            if (name.equalsIgnoreCase(item.getName())) {
                return item;
            }
        }
        return null;
    }

    public synchronized Object findObject(String name) {
        SharedObject item = find(name);
        if (item == null) {
            return null;
        }
        return item.getItem();
    }

    public synchronized void add(Object item, String name) {
        items.add(new SharedObject(item, name));
    }

    public synchronized void release(String name) {
        SharedObject item = find(name);
        if (item != null) {
            item.release();
            items.remove(item);
        }
    }

    public synchronized void addref(String name) {
        SharedObject item = SharedObjects.getInstance().find(name);
        if (item != null) {
            item.addRef();
        }
    }
}
