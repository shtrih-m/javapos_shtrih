/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Hashtable;

public class PayTypes {

    private final Hashtable items = new Hashtable();

    public PayTypes() {
    }

    public void clear() {
        items.clear();
    }

    public void put(String key, PayType value) {
        items.put(key, value);
    }

    public PayType get(String key) {
        return (PayType) items.get(key);
    }
}
