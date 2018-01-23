/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

import java.util.Vector;
import com.shtrih.fiscalprinter.command.AmountItem;

/**
 *
 * @author V.Kravtsov
 */
public class FSDiscounts {

    private final Vector items = new Vector();

    public FSDiscounts() {
    }

    public void clear(){
        items.clear();
    }
    
    public void add(AmountItem item) {
        items.add(item);
    }

    public AmountItem get(int index) {
        return (AmountItem) items.get(index);
    }

    public int size() {
        return items.size();
    }

    public long getTotal() {
        long total = 0;
        for (int i = 0; i < size(); i++) {
            total += get(i).getAmount();
        }
        return total;
    }

}
