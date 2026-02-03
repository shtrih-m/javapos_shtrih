package com.shtrih.jpos.fiscalprinter;

public enum VatRate {
    VAT_20("НДС 20%", 20.0, false, 1),
    VAT_10("НДС 10%", 10.0, false, 2),
    VAT_0("НДС 0%", 0.0, false, 4),
    NO_VAT("БЕЗ НДС", 0.0, false, 8),
    VAT_20_120("НДС 20/120", 20.0, true, 16),
    VAT_10_110("НДС 10/110", 10.0, true, 32),
    VAT_5("НДС 5%", 5.0, false, 0x81),
    VAT_7("НДС 7%", 7.0, false, 0x82),
    VAT_5_105("НДС 5/105%", 5.0, true, 0x84),
    VAT_7_107("НДС 7/107%", 7.0, true, 0x90),
    VAT_22("НДС 22%", 22.0, false, 0xCB),
    VAT_22_122("НДС 22/122", 22.0, true, 0xCC);
    
    
    private final String displayName;
    private final double rate;
    private final boolean isInclusive;
    private final int code;
    
    VatRate(String displayName, double rate, boolean isInclusive, int code) {
        this.displayName = displayName;
        this.rate = rate;
        this.isInclusive = isInclusive;
        this.code = code;
    }
    
    public int getCode(){
        return code;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public double getRate() {
        return rate;
    }
    
    public boolean isInclusive() {
        return isInclusive;
    }
    
    public double calculateTax(double amount) {
        if (isInclusive) {
            return amount * (rate / (100 + rate));
        }
        return amount * (rate / 100);
    }
    
    public double calculateAmountWithoutTax(double amountWithTax) {
        if (isInclusive) {
            return amountWithTax / (1 + rate / 100);
        }
        return amountWithTax;
    }
    
    public static VatRate fromDisplayName(String displayName) {
        for (VatRate taxRate : values()) {
            if (taxRate.displayName.equals(displayName)) {
                return taxRate;
            }
        }
        throw new IllegalArgumentException("Unknown tax rate: " + displayName);
    }
}