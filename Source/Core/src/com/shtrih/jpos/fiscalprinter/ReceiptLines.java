/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import jpos.JposConst;
import jpos.JposException;
import com.shtrih.util.Localizer;

/**
 *
 * @author Виталий
 */
public class ReceiptLines {

    private int count;
    private final Map<Integer, ReceiptLine> lines = new HashMap<Integer, ReceiptLine>();

    public ReceiptLines(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int newCount) throws Exception 
    {
        this.count = newCount;
    }

    public boolean validNumber(int number) {
        return (number >= 1) && (number <= count);
    }

    public ReceiptLine getLine(int number) throws Exception {
        ReceiptLine result = lines.get(number);
        if (result == null){
            result = new ReceiptLine();
        }
        return result;
    }

    public void setLine(int number, String text, boolean doubleWidth)
            throws Exception {
        checkLineNumber(number);
        lines.put(number, new ReceiptLine(text, doubleWidth));
    }

    private void checkLineNumber(int number) throws Exception {
        if (!validNumber(number)) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.InvalidLineNumber));
        }
    }
}
