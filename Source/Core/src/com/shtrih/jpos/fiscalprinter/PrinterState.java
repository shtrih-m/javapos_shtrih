/*
 * PrinterState.java
 *
 * Created on 10 Март 2011 г., 14:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import java.util.HashMap;
import java.util.Map;

import jpos.FiscalPrinterConst;

public class PrinterState implements FiscalPrinterConst {
    /** Creates a new instance of PrinterState */
    private PrinterState() {
    }

    public static String getText(int state) {
        Map m = new HashMap();
        m.put(new Integer(FPTR_PS_MONITOR), "FPTR_PS_MONITOR");
        m.put(new Integer(FPTR_PS_FISCAL_RECEIPT), "FPTR_PS_FISCAL_RECEIPT");
        m.put(new Integer(FPTR_PS_FISCAL_RECEIPT_TOTAL),
                "FPTR_PS_FISCAL_RECEIPT_TOTAL");
        m.put(new Integer(FPTR_PS_FISCAL_RECEIPT_ENDING),
                "FPTR_PS_FISCAL_RECEIPT_ENDING");
        m.put(new Integer(FPTR_PS_FISCAL_DOCUMENT), "FPTR_PS_FISCAL_DOCUMENT");
        m.put(new Integer(FPTR_PS_FIXED_OUTPUT), "FPTR_PS_FIXED_OUTPUT");
        m.put(new Integer(FPTR_PS_ITEM_LIST), "FPTR_PS_ITEM_LIST");
        m.put(new Integer(FPTR_PS_LOCKED), "FPTR_PS_LOCKED");
        m.put(new Integer(FPTR_PS_NONFISCAL), "FPTR_PS_NONFISCAL");
        m.put(new Integer(FPTR_PS_REPORT), "FPTR_PS_REPORT");

        String result = (String) m.get(new Integer(state));
        if (result == null) {
            result = "Unknown printer state";
        }
        return result;
    }
}
