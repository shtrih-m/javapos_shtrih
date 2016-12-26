/*
 * FptrPaymentNames.java
 *
 * Created on 23 Ноябрь 2009 г., 20:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import java.util.Vector;

public class FptrPaymentNames {

    private final Vector list = new Vector();

    /** Creates a new instance of FptrPaymentNames */
    public FptrPaymentNames() {
    }

    public int size() {
        return list.size();
    }

    public void clear() {
        list.clear();
    }

    public void add(FptrPaymentName item) {
        list.add(item);
    }

}
