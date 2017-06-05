/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

import com.shtrih.fiscalprinter.receipt.*;
import java.util.Vector;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.PriceItem;
import com.shtrih.fiscalprinter.command.AmountItem;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.MathUtils;
import com.shtrih.util.MethodParameter;
import com.shtrih.util.StringUtils;

/**
 * @author V.Kravtsov
 */
public class FSSaleReceiptItem {

    private int pos = 0;
    private long price;
    private long unitPrice;
    private long quantity;
    private int department;
    private int tax1;
    private int tax2;
    private int tax3;
    private int tax4;
    private String text;
    private String preLine = "";
    private String postLine = "";
    private String unitName = "";
    private long voidAmount = 0;
    private boolean isStorno;
    private final FSDiscounts discounts = new FSDiscounts();
    private CompositeLogger logger = CompositeLogger.getLogger(FiscalPrinterImpl.class);

    public FSSaleReceiptItem() {
    }

    public PriceItem getPriceItem() throws Exception {
        PriceItem item = new PriceItem();
        item.setDepartment(department);
        item.setPrice(price);
        item.setQuantity(quantity);
        item.setTax1(tax1);
        item.setTax2(tax2);
        item.setTax3(tax3);
        item.setTax4(tax4);
        item.setText(text);
        return item;
    }

    public boolean getIsStorno(){
        return isStorno;
    }
    
    public void setIsStorno(boolean value){
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

    public void addVoidAmount(long amount) {
        voidAmount += amount;
    }

    public long getVoidAmount() {
        return voidAmount;
    }

    public long getTotalWithVoids() {
        return getTotal() - voidAmount;
    }

    public long getTotal() {
        return getAmount();
    }

    public void addDiscount(FSDiscount discount) {
        getDiscounts().add(discount);
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

    public long getPriceDiscount() 
    {
        if (quantity == 0) return 0;
        return Math.abs(Math.round(discounts.getTotal() * 1000.0 / quantity));
    }

    public long getTotalDiscount() {
        return discounts.getTotal();
    }

    
    public long getPrice() {
        return price;
    }

    public long getUnitPrice() {
        return unitPrice;
    }
    
    public long getQuantity() {
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
    public void setQuantity(long quantity) throws Exception {
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
        if (quantity == 0) return 0;
        if (discounts.getTotal() == 0) return price;
        return Math.abs(Math.round(getTotal() * 1000.0 / quantity));
    }

    public long getAmount() {
        return Math.abs(PrinterAmount.getAmount(getPrice(), getQuantity()) - discounts.getTotal());
    }
}
