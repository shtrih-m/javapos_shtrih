/*
 * FptrPaymentName.java
 *
 * Created on 25 Октябрь 2009 г., 19:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter;

/**
 * @author V.Kravtsov
 */
public class FptrPaymentName {

    private final int code;
    private final String name;

    /** Creates a new instance of FptrPaymentName */
    public FptrPaymentName(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
