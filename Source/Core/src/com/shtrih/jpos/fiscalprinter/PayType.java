/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 * @author V.Kravtsov
 */
public class PayType {

    private final int value;

    public PayType(int value) throws Exception {
        if ((value < 0) || (value > 3)) {
            throw new Exception("Invalid payment value");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isCashless() {
        return value != 0;
    }
}
