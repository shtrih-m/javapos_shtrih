/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.TLVItems;
import java.util.Vector;
import java.util.HashMap;

import com.shtrih.util.MethodParameter;
import com.shtrih.fiscalprinter.receipt.*;
import com.shtrih.fiscalprinter.command.PriceItem;
import com.shtrih.fiscalprinter.command.AmountItem;
import java.util.List;

/**
 * @author V.Kravtsov
 */
public class FSSaleReceiptItem {

    private int pos = 0;
    private long price;
    private long priceWithDiscount;
    private long unitPrice;
    private double quantity;
    private int department;
    private int tax1;
    private int tax2;
    private int tax3;
    private int tax4;
    private String text;
    private String preLine = "";
    private String postLine = "";
    private String unitName = "";
    private boolean isStorno;
    private final FSDiscounts discounts = new FSDiscounts();
    private Long totalAmount = null;
    private Long taxAmount = null;
    private int paymentType = 4;
    private int subjectType = 1;
    private HashMap receiptFields = new HashMap();
    private double taxRate = 0;
    private Integer unit;
    private final List<FSTLVItem> tags = new Vector<FSTLVItem>();
    private final List<byte[]> itemCodes = new Vector<byte[]>();

    public FSSaleReceiptItem() {
    }

    public FSSaleReceiptItem getCopy() {
        FSSaleReceiptItem item = new FSSaleReceiptItem();
        item.pos = pos;
        item.price = price;
        item.priceWithDiscount = priceWithDiscount;
        item.unitPrice = unitPrice;
        item.quantity = quantity;
        item.department = department;
        item.tax1 = tax1;
        item.tax2 = tax2;
        item.tax3 = tax3;
        item.tax4 = tax4;
        item.text = text;
        item.preLine = preLine;
        item.postLine = postLine;
        item.unitName = unitName;
        item.isStorno = isStorno;
        item.totalAmount = totalAmount;
        item.taxAmount = taxAmount;
        item.paymentType = paymentType;
        item.subjectType = subjectType;
        item.itemCodes.clear();
        item.itemCodes.addAll(itemCodes);
        item.tags.clear();
        item.tags.addAll(tags);
        item.taxRate = taxRate;
        return item;
    }

    public List<FSTLVItem> getTags() {
        return tags;
    }

    public PriceItem getPriceItem() throws Exception {
        PriceItem item = new PriceItem();
        item.setDepartment(department);
        item.setPrice(getPriceWithDiscount());
        item.setQuantity(quantity);
        item.setTax1(tax1);
        item.setTax2(tax2);
        item.setTax3(tax3);
        item.setTax4(tax4);
        item.setText(text);
        item.setTaxAmount(taxAmount);
        item.setTotalAmount(totalAmount);
        item.setPaymentType(paymentType);
        item.setSubjectType(subjectType);
        item.setUnit(getUnit());

        return item;
    }

    public void setTotalAmount(Long value) {
        totalAmount = value;
    }

    public void setTaxAmount(Long value) {
        taxAmount = value;
    }

    public boolean getIsStorno() {
        return isStorno;
    }

    public void setIsStorno(boolean value) {
        this.isStorno = value;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String value) {
        this.unitName = value;
    }

    public void setUnitPrice(long value) {
        this.unitPrice = value;
    }

    public void setPreLine(String value) {
        this.preLine = value;
    }

    public void setPostLine(String value) {
        this.postLine = value;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int value) {
        pos = value;
    }

    public long getTotal() {
        return Math.abs(PrinterAmount.getAmount(getPriceWithDiscount(), getQuantity()));
    }

    public void addDiscount(AmountItem discount) {
        getDiscounts().add(discount);
        updatePrice();
    }

    /**
     * @return the preLine
     */
    public String getPreLine() {
        return preLine;
    }

    /**
     * @return the postLine
     */
    public String getPostLine() {
        return postLine;
    }

    /**
     * @return the discounts
     */
    public FSDiscounts getDiscounts() {
        return discounts;
    }

    public long getPriceDiscount() {
        return price - priceWithDiscount;
    }

    public long getPrice() {
        return price;
    }

    public long getUnitPrice() {
        return unitPrice;
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
     * @param price the price to set
     */
    public void setPrice(long price) throws Exception {
        MethodParameter.checkRange(price, 0, 0xFFFFFFFFFFL, "price");
        this.price = price;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(double quantity) throws Exception {
        this.quantity = quantity;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(int department) {
        this.department = department;
    }

    /**
     * @param tax1 the tax1 to set
     */
    public void setTax1(int tax1) {
        this.tax1 = tax1;
    }

    /**
     * @param tax2 the tax2 to set
     */
    public void setTax2(int tax2) {
        this.tax2 = tax2;
    }

    /**
     * @param tax3 the tax3 to set
     */
    public void setTax3(int tax3) {
        this.tax3 = tax3;
    }

    /**
     * @param tax4 the tax4 to set
     */
    public void setTax4(int tax4) {
        this.tax4 = tax4;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    public long getPriceWithDiscount() {
        return priceWithDiscount;
    }

    public long getAmount() {
        return Math.abs(PrinterAmount.getAmount(getPrice(), getQuantity()) - discounts.getTotal());
    }

    public long calcPriceWithDiscount() {
        if (quantity == 0) {
            return 0;
        }
        if (discounts.getTotal() == 0) {
            return price;
        }
        long price1 = Math.abs(Math.round((totalAmount - discounts.getTotal()) / (double)quantity));
        return price1;
    }

    public void updatePrice() {
        priceWithDiscount = price;
        if (discounts.getTotal() != 0) {
            if (quantity == 1.0) {
                priceWithDiscount = price - discounts.getTotal();
            } else {
                priceWithDiscount = calcPriceWithDiscount();
            }
        }
        totalAmount = new Long(getTotal());
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public void setSubjectType(int subjectType) {
        this.subjectType = subjectType;
    }

    public HashMap getReceiptFields() {
        return receiptFields;
    }

    public String getReceiptField(String fieldName) throws Exception {
        String fieldValue = (String) receiptFields.get(fieldName);
        if (fieldValue == null) {
            fieldValue = "";
        }
        return fieldValue;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public long getTaxAmount() throws Exception {
        return Math.round((getTotal()) * taxRate / (double)(1 + taxRate));
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public List<byte[]> getItemCodes(){
        return itemCodes;
    }
}
