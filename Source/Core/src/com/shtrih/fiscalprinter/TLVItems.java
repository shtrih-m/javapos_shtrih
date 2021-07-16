/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import java.util.*;

/**
 *
 * @author Виталий
 */
public class TLVItems {

    private final List<TLVItem> items = new ArrayList<TLVItem>();

    public TLVItems() {
    }

    public void clear() {
        items.clear();
    }
    
    public void add(TLVItem item) {
        items.add(item);
    }

    public void add(TLVItems a) {
        this.items.addAll(a.items);
    }
    
    public void remove(TLVItem item) {
        items.remove(item);
    }
    
    public int size() {
        return items.size();
    }

    public TLVItem get(int index) {
        return items.get(index);
    }

    public TLVItem find(int tagId) {
        for (TLVItem item : items) {
            TLVItem tlvItem = item.find(tagId);
            if (tlvItem != null) {
                return tlvItem;
            }
        }
        return null;
    }
}
