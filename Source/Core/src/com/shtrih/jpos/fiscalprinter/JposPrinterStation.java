/*
 * JposPrinterStation.java
 *
 * Created on April 18 2008, 12:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import jpos.FiscalPrinterConst;

import com.shtrih.fiscalprinter.command.PrinterConst;

public class JposPrinterStation implements FiscalPrinterConst {
    private final int station;

    public JposPrinterStation(int station) {
        this.station = station;
    }

    public int getStation() {
        int result = 0;
        if (isRecStation()) {
            result += PrinterConst.SMFP_STATION_REC;
        }
        if (isJrnStation()) {
            result += PrinterConst.SMFP_STATION_JRN;
        }
        if (isSlpStation()) {
            result += PrinterConst.SMFP_STATION_SLP;
        }
        return result;
    }

    public boolean isRecStation() {
        return ((station & FPTR_S_RECEIPT) != 0);
    }

    public boolean isJrnStation() {
        return (station & FPTR_S_JOURNAL) != 0;
    }

    public boolean isSlpStation() {
        return (station & FPTR_S_SLIP) != 0;
    }
}
