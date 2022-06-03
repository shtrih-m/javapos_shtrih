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

    public TLVItem add(int tagId) 
    {
        TLVItem item = new TLVItem(tagId);
        items.add(item);
        return item;
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
    
    public void removeEmptySTLV(){
        removeEmptySTLV(this);
    }
            
    public static void removeEmptySTLV(TLVItems items)
    {
        for (int i=items.size()-1;i>=0;i--) 
        {
            TLVItem item = items.get(i);
            if (item.isSTLV())
            {
                if (item.isEmpty()){
                    items.remove(item);
                } else
                {
                    removeEmptySTLV(item.getItems());
                }
            }
        }
    }
    
    public void copyTo(TLVItems dst){
        for (TLVItem item : items) {
            dst.add(item.getCopy());
        }
    }
    
}
