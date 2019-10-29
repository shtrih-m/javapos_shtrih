/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

import java.util.Vector;
import java.util.HashMap;

import com.shtrih.util.MathUtils;
import com.shtrih.util.StringUtils;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.MethodParameter;
import com.shtrih.fiscalprinter.receipt.*;
import com.shtrih.fiscalprinter.GS1Barcode;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.PriceItem;
import com.shtrih.fiscalprinter.command.AmountItem;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

/**
 * @author V.Kravtsov
 */
public class FSSaleReceiptItem {

    private int pos = 0;
    private long price;
    private long priceWithDiscount;
    private long unitPrice;
    private long quantity;
    private long itemAmount;
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
    private boolean priceUpdated = false;
    private FSSaleReceiptItem splittedItem;
    private final FSDiscounts discounts = new FSDiscounts();
    private Long totalAmount = null;
    private Long taxAmount = null;
    private int paymentType = 4;
    private int subjectType = 1;
    private GS1Barcode barcode = null;
    private final Vector tags = new Vector();
    private HashMap receiptFields = new HashMap();

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
        item.voidAmount = voidAmount;
        item.isStorno = isStorno;
        item.priceUpdated = priceUpdated;
        item.splittedItem = null;
        item.totalAmount = totalAmount;
        item.taxAmount = taxAmount;
        item.paymentType = paymentType;
        item.subjectType = subjectType;
        item.barcode = barcode;
        return item;
    }

    public Vector getTags() {
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

    public long getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(long value) {
        itemAmount = value;
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

    public void addDiscount(AmountItem discount) {
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

    public long getPriceDiscount() {
        return price - priceWithDiscount;
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
        return priceWithDiscount;
    }

    public long getAmount() {
        return Math.abs(PrinterAmount.getAmount(getPrice(), getQuantity()) - discounts.getTotal());
    }

    public long getTotal2() {
        return Math.abs(PrinterAmount.getAmount(getPriceWithDiscount(), getQuantity()));
    }

    public FSSaleReceiptItem getSplittedItem() {
        return splittedItem;
    }

    public long calcPriceWithDiscount() {
        if (quantity == 0) {
            return 0;
        }
        if (discounts.getTotal() == 0) {
            return price;
        }
        long price1 = Math.abs((long) (getTotal() * 1000.0 / quantity));
        return price1;
    }

    public void updatePrice() {
        if (priceUpdated) {
            return;
        }
        splittedItem = null;
        priceWithDiscount = price;
        if (discounts.getTotal() != 0) {
            if (quantity == 1000) {
                priceWithDiscount = price - discounts.getTotal();
            } else {
                priceWithDiscount = calcPriceWithDiscount();
                long amount = Math.round(priceWithDiscount * quantity / 1000.0);
                long total = getTotal();
                long total2 = getTotal2();
                if (total - amount > 0) {
                    long quantity2 = quantity;
                    long price2 = priceWithDiscount;
                    if ((quantity % 1000) == 0) {
                        price2 = priceWithDiscount + 1;
                        quantity2 = (quantity / 1000 - (total - total2)) * 1000;
                    } else {
                        for (int i = 0; i <= quantity; i++) {
                            long itemTotal = Math.round(i * priceWithDiscount / 1000.0)
                                    + Math.round((priceWithDiscount) * (quantity - i) / 1000.0);
                            if (itemTotal == total) {
                                quantity2 = i;
                                price2 = priceWithDiscount;
                                break;
                            }
                            itemTotal = Math.round(i * priceWithDiscount / 1000.0)
                                    + Math.round((priceWithDiscount + 1) * (quantity - i) / 1000.0);
                            if (itemTotal == total) {
                                quantity2 = i;
                                price2 = priceWithDiscount + 1;
                                break;
                            }
                        }
                    }
                    if (quantity2 != quantity) {
                        splittedItem = getCopy();
                        splittedItem.price = price;
                        splittedItem.unitPrice = price;
                        splittedItem.priceWithDiscount = price2;
                        splittedItem.quantity = quantity - quantity2;
                        splittedItem.department = department;
                        splittedItem.tax1 = tax1;
                        splittedItem.tax2 = tax2;
                        splittedItem.tax3 = tax3;
                        splittedItem.tax4 = tax4;
                        splittedItem.text = text;
                    }
                    quantity = quantity2;
                }
            }
        }
        if (discounts.getTotal() == 0) {
            if (totalAmount == null) {
                if (itemAmount != 0) {
                    totalAmount = new Long(itemAmount);
                } else {
                    totalAmount = new Long(getTotal2());
                }
            }
        }
        priceUpdated = true;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public void setSubjectType(int subjectType) {
        this.subjectType = subjectType;
    }

    /**
     * @return the barcode
     */
    public GS1Barcode getBarcode() {
        return barcode;
    }

    /**
     * @param barcode the barcode to set
     */
    public void setBarcode(GS1Barcode barcode) {
        this.barcode = barcode;
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
}
