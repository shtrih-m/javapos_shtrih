/*
 * PriceItem.java
 *
 * Created on January 15 2009, 15:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.fiscalprinter.receipt.PrinterAmount;
import com.shtrih.util.MethodParameter;

public class PriceItem {

    private long price;
    private double quantity;
    private int department;
    private int tax1;
    private int tax2;
    private int tax3;
    private int tax4;
    private String text;
    private Long taxAmount;
    private Long totalAmount;
    private int paymentType = 4;
    private int subjectType = 1;
    private Integer unit;

    /**
     * Creates a new instance of PriceItem
     */
    public PriceItem() {
    }

    public long getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public int getDepartment() {
        return department;
    }

    public int getTax1() {
        return tax1;
    }

    public int getTax2() {
        return tax2;
    }

    public int getTax3() {
        return tax3;
    }

    public int getTax4() {
        return tax4;
    }

    public String getText() {
        return text;
    }

    /**
     * @param price
     * the price to set
     */
    public void setPrice(long price) throws Exception 
    {
        price = Math.abs(price);
        MethodParameter.checkRange(price, 0, 0xFFFFFFFFFFL, "price");
        this.price = price;
    }

    /**
     * @param quantity
     * the quantity to set
     */
    public void setQuantity(double quantity) throws Exception {
        quantity = Math.abs(quantity);
        this.quantity = quantity;
    }

    /**
     * @param department
     * the department to set
     */
    public void setDepartment(int department) {
        this.department = department;
    }

    /**
     * @param tax1
     * the tax1 to set
     */
    public void setTax1(int tax1) {
        this.tax1 = tax1;
    }

    /**
     * @param tax2
     * the tax2 to set
     */
    public void setTax2(int tax2) {
        this.tax2 = tax2;
    }

    /**
     * @param tax3
     * the tax3 to set
     */
    public void setTax3(int tax3) {
        this.tax3 = tax3;
    }

    /**
     * @param tax4
     * the tax4 to set
     */
    public void setTax4(int tax4) {
        this.tax4 = tax4;
    }

    /**
     * @param text
     * the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    public long getAmount() {
        return PrinterAmount.getAmount(getPrice(), getQuantity());
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTaxAmount(Long taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Long getTaxAmount() {
        return taxAmount;
    }
    
    public int getPaymentType() {
        return paymentType;
    }

    public int getSubjectType() {
        return subjectType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public void setSubjectType(int subjectType) {
        this.subjectType = subjectType;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }
}
