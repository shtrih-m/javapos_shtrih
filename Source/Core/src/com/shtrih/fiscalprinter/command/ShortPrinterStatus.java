/*
 * ShortPrinterStatus.java
 *
 * Created on April 2 2008, 18:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 * @author V.Kravtsov
 */

public class ShortPrinterStatus implements PrinterConst {

    private int mode;
    private int flags;
    private int submode;
    private int FMResultCode;
    private int EJResultCode;
    private int receiptOperations;
    private double batteryVoltage;
    private double powerVoltage;
    private int operatorNumber;

    /**
     * Creates a new instance of ShortPrinterStatus
     */
    public ShortPrinterStatus() {
    }

    public ShortPrinterStatus(int mode, int flags, int submode,
            int FMResultCode, int EJResultCode, int receiptOperations,
            double batteryVoltage, double powerVoltage, int operatorNumber) {
        this.mode = mode;
        this.flags = flags;
        this.submode = submode;
        this.FMResultCode = FMResultCode;
        this.EJResultCode = EJResultCode;
        this.receiptOperations = receiptOperations;
        this.batteryVoltage = batteryVoltage;
        this.powerVoltage = powerVoltage;
        this.operatorNumber = operatorNumber;
    }

    public int getMode() {
        return mode;
    }

    public int getFlags() {
        return flags;
    }

    public int getSubmode() {
        return submode;
    }

    public int getFMResultCode() {
        return FMResultCode;
    }

    public int getEJResultCode() {
        return EJResultCode;
    }

    public int getReceiptOperations() {
        return receiptOperations;
    }

    public double getBatteryVoltage() {
        return batteryVoltage;
    }

    public double getPowerVoltage() {
        return powerVoltage;
    }

    public int getOperatorNumber() {
        return operatorNumber;
    }

    public PrinterMode getPrinterMode() {
        return new PrinterMode(getMode());
    }

    public PrinterSubmode getPrinterSubmode() {
        return new PrinterSubmode(getSubmode());
    }

    public PrinterFlags getPrinterFlags() {
        return new PrinterFlags(getFlags());
    }

    /**
     * @param mode
     *            the mode to set
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * @param flags
     *            the flags to set
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * @param submode
     *            the submode to set
     */
    public void setSubmode(int submode) {
        this.submode = submode;
    }

    /**
     * @param FMResultCode
     *            the FMResultCode to set
     */
    public void setFMResultCode(int FMResultCode) {
        this.FMResultCode = FMResultCode;
    }

    /**
     * @param EJResultCode
     *            the EJResultCode to set
     */
    public void setEJResultCode(int EJResultCode) {
        this.EJResultCode = EJResultCode;
    }

    /**
     * @param receiptOperations
     *            the receiptOperations to set
     */
    public void setReceiptOperations(int receiptOperations) {
        this.receiptOperations = receiptOperations;
    }

    /**
     * @param batteryVoltage
     *            the batteryVoltage to set
     */
    public void setBatteryVoltage(double batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    /**
     * @param powerVoltage
     *            the powerVoltage to set
     */
    public void setPowerVoltage(double powerVoltage) {
        this.powerVoltage = powerVoltage;
    }

    /**
     * @param operatorNumber
     *            the operatorNumber to set
     */
    public void setOperatorNumber(int operatorNumber) {
        this.operatorNumber = operatorNumber;
    }
    
    public PrinterStatus getPrinterStatus() 
    {
        PrinterStatus status = new PrinterStatus();
        status.setMode(mode);
        status.setSubmode(submode);
        status.setFlags(flags);
        status.setOperator(operatorNumber);
        return status;
    }
}
