/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import java.util.Vector;

/**
 *
 * @author Виталий
 */
public class MCNotifications {

    private final Vector<MCNotification> items;
    
    public MCNotifications(){
        items = new Vector<MCNotification>();
    }
    
    public int size(){
        return items.size();
    }
    
    public MCNotification get(int index){
        return items.get(index);
    }
    
    public void put(MCNotification item){
        items.add(item);
    }
}
