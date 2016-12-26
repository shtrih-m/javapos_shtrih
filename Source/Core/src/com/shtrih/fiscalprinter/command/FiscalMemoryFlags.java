/*
 * FiscalMemoryFlags.java
 *
 * Created on 2 April 2008, 17:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.util.BitUtils;

public class FiscalMemoryFlags {

    private final int value;

    public FiscalMemoryFlags(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isFm1Present() {
        return BitUtils.testBit(value, 0);
    }

    public boolean isFm2Present() {
        return BitUtils.testBit(value, 1);
    }

    public boolean isLicensePresent() {
        return BitUtils.testBit(value, 2);
    }

    public boolean isFmOverflow() {
        return BitUtils.testBit(value, 3);
    }

    public boolean isBatteryLow() {
        return BitUtils.testBit(value, 4);
    }

    public boolean isFmLastRecordCorrupted() {
        return BitUtils.testBit(value, 5);
    }

    public boolean isDayOpened() {
        return BitUtils.testBit(value, 6);
    }

    public boolean isEndDayRequired() {
        return BitUtils.testBit(value, 7);
    }
}
