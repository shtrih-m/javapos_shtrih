/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import java.util.Vector;

/**
 *
 * @author V.Kravtsov
 */
public class TLVItems {

    private final Vector<TLVItem> items = new Vector<TLVItem>();

    public TLVItems() {
    }

    public int size() {
        return items.size();
    }

    public void add(TLVItem item) {
        items.add(item);
    }

    public void remove(TLVItem item) {
        items.remove(item);
    }
    
    public TLVItem get(int index) {
        return items.get(index);
    }

    public TLVItem itemById(int id) {
        for (int i = 0; i < size(); i++) {
            TLVItem item = get(i);
            if (item.getTag().getId() == id) {
                return item;
            }
        }
        return null;
    }
}
