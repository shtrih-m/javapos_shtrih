package com.shtrih.jpos.fiscalprinter.receipt.template;


import com.shtrih.fiscalprinter.command.AmountItem;
import com.shtrih.fiscalprinter.command.PriceItem;
import com.shtrih.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class TemplatePriceItem extends PriceItem {
    private HashMap<String, String> m_tagHash = new HashMap<String, String>();
    static String taxLetters = "АБВГДЕ";

    private long m_totalPrice = 0;
    private long m_quantity = 0;
    private long m_pos = 0;
    private Vector<AmountItem> m_discountItems=new Vector<AmountItem>();

    public long getTotalPrice() {
        long result=m_totalPrice;
        return result;
    }

    public void setTotalPrice(long totalPrice) {
        this.m_totalPrice = totalPrice;
    }

    public long getTotalQuantity() {
        return m_quantity;
    }

    public String getTaxLetter(int tax) {

        if ((tax < 1) || (tax > 6)) {
            tax = 4;
        }
        return String.valueOf(taxLetters.charAt(tax - 1));
    }

    public void setTotalQuantity(long lastItemQuantity) {
        this.m_quantity = lastItemQuantity;
    }
    public void appendDiscountItem(AmountItem item){
        m_discountItems.add(item);
    }
    public Vector<AmountItem> getDiscountItems(){
        return m_discountItems;
    }

    public long getPosition() {
        return m_pos;
    }
    public long getTotalDiscount(){
        long sum=0;
        for (AmountItem item:m_discountItems){
            sum+=item.getAmount();
        }
        return sum;

    }
    public long getTotalWithDiscount(){
        return getAmount()-getTotalDiscount();
    }
    public void setPosition(long pos) {
        this.m_pos = pos;
    }
    public  String getTagContents(Field f) throws Exception {
        StringBuilder sb=new StringBuilder();

        if (m_tagHash.containsKey(f.tag)){
            return m_tagHash.get(f.tag);
        }
        String result;
        if (f.tag.equals("POS")){
            result=String.valueOf(m_pos);
        }else if(f.tag.equals("TITLE")){
            result=getText();
        }else if(f.tag.equals("PRICE")){
            result=StringUtils.amountToString(getPrice());
        }else if (f.tag.equals("QUAN")){
            result=StringUtils.quantityToStr(getQuantity());
        }else if (f.tag.equals("SUM")){
            long d= getQuantity()%1000 == 0 ? getQuantity()/1000:getQuantity();
            result=StringUtils.amountToString(getPrice()-(getTotalDiscount()/d));
        }else if (f.tag.equals("DISCOUNT")){
            long d= getQuantity()%1000 == 0 ? getQuantity()/1000:getQuantity();
            result=StringUtils.amountToString(getTotalDiscount()/d);
        }else if (f.tag.equals("TOTAL")){
            result=StringUtils.amountToString(getTotalWithDiscount());
        }else if (f.tag.equals("TOTAL_TAX")){
            result = StringUtils.amountToString(getTotalWithDiscount())+"_"+getTaxLetter(getTax1());
        }else{
            throw new ParsingException("Unknown tag, valid tags are:"+sb.toString());
        }
        m_tagHash.put(f.tag,result);
        return result;
    }
}
