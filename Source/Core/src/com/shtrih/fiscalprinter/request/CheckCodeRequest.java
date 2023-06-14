/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.request;

import com.shtrih.fiscalprinter.command.ItemCode;

/**
 *
 * @author Виталий
 */
public class CheckCodeRequest 
{
    private byte[] data;
    private boolean sale;
    private double quantity;
    private int unit;
    private long numerator;
    private long denominator;

    public CheckCodeRequest(){}

    /**
     * @return the itemCode
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param itemCode the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * @return the isSale
     */
    public boolean isSale() {
        return sale;
    }

    /**
     * @param isSale the isSale to set
     */
    public void setIsSale(boolean sale) {
        this.sale = sale;
    }

    /**
     * @return the quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the unit
     */
    public int getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(int unit) {
        this.unit = unit;
    }

    /**
     * @return the numerator
     */
    public long getNumerator() {
        return numerator;
    }

    /**
     * @param numerator the numerator to set
     */
    public void setNumerator(long numerator) {
        this.numerator = numerator;
    }

    /**
     * @return the denominator
     */
    public long getDenominator() {
        return denominator;
    }

    /**
     * @param denominator the denominator to set
     */
    public void setDenominator(long denominator) {
        this.denominator = denominator;
    }
    
}
