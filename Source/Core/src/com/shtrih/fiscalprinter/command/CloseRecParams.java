/*
 * CloseRecParams.java
 *
 * Created on 15 January 2009, 17:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import com.shtrih.util.MethodParameter;

/**
 * @author V.Kravtsov
 */
public class CloseRecParams {

    private long sum1 = 0;
    private long sum2 = 0;
    private long sum3 = 0;
    private long sum4 = 0;
    private int discount = 0;
    private String text = "";
    private int tax1 = 0;
    private int tax2 = 0;
    private int tax3 = 0;
    private int tax4 = 0;

    public CloseRecParams() {
    }

    public long getSum1() {
        return sum1;
    }

    public long getSum2() {
        return sum2;
    }

    public long getSum3() {
        return sum3;
    }

    public long getSum4() {
        return sum4;
    }

    public int getDiscount() {
        return discount;
    }

    public String getText() {
        return text;
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

    public void setSum1(long sum1) throws Exception {
        MethodParameter.checkRange(sum1, 0, 0xFFFFFFFFFFL, "sum1");
        this.sum1 = sum1;
    }

    public void setSum2(long sum2) throws Exception {
        MethodParameter.checkRange(sum2, 0, 0xFFFFFFFFFFL, "sum2");
        this.sum2 = sum2;
    }

    public void setSum3(long sum3) throws Exception {
        MethodParameter.checkRange(sum3, 0, 0xFFFFFFFFFFL, "sum3");
        this.sum3 = sum3;
    }

    public void setSum4(long sum4) throws Exception {
        MethodParameter.checkRange(sum4, 0, 0xFFFFFFFFFFL, "sum4");
        this.sum4 = sum4;
    }

    public void setDiscount(int discount) throws Exception {
        MethodParameter.checkRange(discount, 0, 9999, "discount");
        this.discount = discount;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTax1(int tax1) throws Exception {
        MethodParameter.checkRange(tax1, 0, 3, "tax1");
        this.tax1 = tax1;
    }

    public void setTax2(int tax2) throws Exception {
        MethodParameter.checkRange(tax2, 0, 3, "tax2");
        this.tax2 = tax2;
    }

    public void setTax3(int tax3) throws Exception {
        MethodParameter.checkRange(tax3, 0, 3, "tax3");
        this.tax3 = tax3;
    }

    public void setTax4(int tax4) throws Exception {
        MethodParameter.checkRange(tax4, 0, 3, "tax4");
        this.tax4 = tax4;
    }

}
