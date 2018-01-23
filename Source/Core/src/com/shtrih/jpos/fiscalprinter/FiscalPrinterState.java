/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import jpos.FiscalPrinterConst;

/**
 * @author V.Kravtsov
 */

public class FiscalPrinterState {

    private int value = 0;

    public FiscalPrinterState() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    public boolean isEnding(){
        return FiscalPrinterConst.FPTR_PS_FISCAL_RECEIPT_ENDING == value;
    }
}
