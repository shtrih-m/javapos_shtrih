/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

import com.shtrih.util.StringUtils;
import java.util.Vector;

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
    
    public void add(FSDiscount item) {
        items.add(item);
    }

    public FSDiscount get(int index) {
        return (FSDiscount) items.get(index);
    }

    public int size() {
        return items.size();
    }
    public String getDiscountStrings(){
        String result="";
        for (int i = 0; i < size(); i++) {
            result += get(i).getText()+((i+1)<size()?"\n":"");
        }
        return result;
    }
    public long getTotal() {
        long total = 0;
        for (int i = 0; i < size(); i++) {
            total += get(i).getAmount();
        }
        return total;
    }

}
