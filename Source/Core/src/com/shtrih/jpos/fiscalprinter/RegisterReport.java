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
import com.shtrih.fiscalprinter.command.FMTotals;

public class RegisterReport {

    public int dayNumber = 0;
    private final Vector cashRegisters = new Vector();
    private final Vector operRegisters = new Vector();
    private FMTotals lastFiscalization = new FMTotals();
    private FMTotals allFiscalizations = new FMTotals();
    
    public RegisterReport() {
    }

    public Vector getCashRegisters() {
        return cashRegisters;
    }

    public Vector getOperRegisters() {
        return operRegisters;
    }

    public FMTotals getLastFiscalization() {
        return lastFiscalization;
    }

    public FMTotals getAllFiscalizations() {
        return allFiscalizations;
    }

    public void setLastFiscalization(FMTotals lastFiscalization) {
        this.lastFiscalization = lastFiscalization;
    }

    public void setAllFiscalizations(FMTotals allFiscalizations) {
        this.allFiscalizations = allFiscalizations;
    }
}
