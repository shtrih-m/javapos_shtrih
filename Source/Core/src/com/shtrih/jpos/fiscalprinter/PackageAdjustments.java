/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Vector;

import com.shtrih.util.StringUtils;

public class PackageAdjustments extends Vector {

    public PackageAdjustment addItem(int vat, long amount) {
        PackageAdjustment result = new PackageAdjustment();
        result.vat = vat;
        result.amount = amount;
        add(result);
        return result;
    }

    public PackageAdjustment getItem(int Index) {
        return (PackageAdjustment) get(Index);
    }

    public void parse(String s) {
        String[] items = StringUtils.split(s, ';');
        for (int i = 0; i < items.length; i++) {
            String[] fields = StringUtils.split(items[i], ',');
            if (fields.length >= 2) {
                addItem(Integer.parseInt(fields[0]), Long.parseLong(fields[1]));
            }
        }
    }
}
