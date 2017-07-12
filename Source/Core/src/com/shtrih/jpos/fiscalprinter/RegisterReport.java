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
import com.shtrih.fiscalprinter.command.CashRegister;
import com.shtrih.fiscalprinter.command.CashRegisters;
import com.shtrih.fiscalprinter.command.FSReadCommStatus;

public class RegisterReport {

    private int dayNumber = 0;
    private String fsSerial = "";
    private final CashRegisters cashRegisters = new CashRegisters();
    private final Vector operRegisters = new Vector();
    private FMTotals lastFiscalization = new FMTotals();
    private FMTotals allFiscalizations = new FMTotals();
    private boolean capCommStatus = false;
    private FSReadCommStatus commStatus = new FSReadCommStatus();

    
    public RegisterReport() {
    }

    public CashRegisters getCashRegisters() {
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

    /**
     * @return the dayNumber
     */
    public int getDayNumber() {
        return dayNumber;
    }

    /**
     * @param dayNumber the dayNumber to set
     */
    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    /**
     * @return the commStatus
     */
    public FSReadCommStatus getCommStatus() {
        return commStatus;
    }

    /**
     * @param commStatus the commStatus to set
     */
    public void setCommStatus(FSReadCommStatus commStatus) {
        this.commStatus = commStatus;
    }

    /**
     * @return the capCommStatus
     */
    public boolean getCapCommStatus() {
        return capCommStatus;
    }

    /**
     * @param capCommStatus the capCommStatus to set
     */
    public void setCapCommStatus(boolean capCommStatus) {
        this.capCommStatus = capCommStatus;
    }

    /**
     * @return the fsSerial
     */
    public String getFsSerial() {
        return fsSerial;
    }

    /**
     * @param fsSerial the fsSerial to set
     */
    public void setFsSerial(String fsSerial) {
        this.fsSerial = fsSerial;
    }
}
