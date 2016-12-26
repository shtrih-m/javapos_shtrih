/*
 * PrinterStatus.java
 *
 * Created on 11 Ноябрь 2009 г., 17:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 * @author V.Kravtsov
 */
public class PrinterStatus {

    private  int mode;
    private  int flags;
    private  int submode;
    private  int operator;

    /** Creates a new instance of PrinterStatus */
    public PrinterStatus(){}

    public int getMode() {
        return mode;
    }

    public int getFlags() {
        return flags;
    }

    public int getSubmode() {
        return submode;
    }

    public int getOperator() {
        return operator;
    }

    public PrinterFlags getPrinterFlags() {
        return new PrinterFlags(getFlags());
    }

    public PrinterMode getPrinterMode() {
        return new PrinterMode(getMode());
    }

    public PrinterSubmode getPrinterSubmode() {
        return new PrinterSubmode(getSubmode());
    }

    public String getText() {
        return String.valueOf(getMode()) + ", " + getPrinterMode().getText();
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * @param flags the flags to set
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * @param submode the submode to set
     */
    public void setSubmode(int submode) {
        this.submode = submode;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(int operator) {
        this.operator = operator;
    }
}
