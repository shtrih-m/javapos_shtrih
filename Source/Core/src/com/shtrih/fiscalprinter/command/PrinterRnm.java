/*
 * PrinterRnm.java
 *
 * Created on April 2 2008, 17:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

import com.shtrih.util.MethodParameter;

/**
 * @author V.Kravtsov
 */

class PrinterRnm {
    private long value = 0;

    public long getValue() {
        return value;
    }

    public void setValue(long value) throws Exception {
        MethodParameter.checkRange(value, 0, 99999999999999L,
                "registration number");
        this.value = value;
    }
}
