/*
 * PrinterMode.java
 *
 * Created on June 4 2008, 15:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 * @author V.Kravtsov
 */

public class PrinterMode implements PrinterConst {

    private final int mode;

    /** Creates a new instance of PrinterMode */
    public PrinterMode(int mode) {
        this.mode = mode;
    }

    public int getLoMode() {
        return mode & 0x0F;
    }

    public int getHiMode() {
        return mode >> 4;
    }

    public int getValue() {
        return mode;
    }

    public boolean isReceiptOpened() {
        return getLoMode() == MODE_REC;
    }

    public boolean getDayOpened() {
        return (getLoMode() == ECRMODE_24OVER)
                || (getLoMode() == ECRMODE_24NOTOVER);
    }

    public boolean isDayClosed() {
        return getLoMode() == ECRMODE_CLOSED;
    }

    public boolean isTestMode() {
        return getLoMode() == ECRMODE_TEST;
    }

    public boolean canPrintZReport() {
        return getDayOpened();
    }

    public boolean isDayEndRequired() {
        return (getLoMode() == MODE_24OVER);
    }

    public static String getText(int value) {
        switch (value) {
            case MODE_DUMPMODE:
                return S_MODE_DUMPMODE;
            case MODE_24NOTOVER:
                return S_MODE_24NOTOVER;
            case MODE_24OVER:
                return S_MODE_24OVER;
            case MODE_CLOSED:
                return S_MODE_CLOSED;
            case MODE_LOCKED:
                return S_MODE_LOCKED;
            case MODE_WAITDATE:
                return S_MODE_WAITDATE;
            case MODE_POINTPOS:
                return S_MODE_POINTPOS;
            case MODE_REC:
                return S_MODE_REC;
            case MODE_TECH:
                return S_MODE_TECH;
            case MODE_TEST:
                return S_MODE_TEST;
            case MODE_FULLREPORT:
                return S_MODE_FULLREPORT;
            case MODE_EJREPORT:
                return S_MODE_EJREPORT;
            case MODE_SLP:
                return S_MODE_SLP;
            case MODE_SLPPRINT:
                return S_MODE_SLPPRINT;
            case MODE_SLPREADY:
                return S_MODE_SLPREADY;
            default:
                return S_MODE_UNKNOWN;
        }
    }

    public String getText() {
        return mode + ", " + getText(mode);
    }

}
