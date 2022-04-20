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

    private final int count;
    private final Map<Integer, ReceiptLine> lines = new HashMap<Integer, ReceiptLine>();

    public ReceiptLines(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int newCount) throws Exception 
    {
        if (newCount <= 0) {
            lines.clear();
            return;
        }

        if (newCount > count) {
            for (int i = count; i < newCount; i++) {
                lines.put(i, new ReceiptLine());
            }
            return;
        }

        for (int i = count; i > newCount; i--) {
            lines.remove(i);
        }
    }

    public boolean validNumber(int number) {
        return (number >= 1) && (number <= count);
    }

    public ReceiptLine getLine(int number) throws Exception {
        if (validNumber(number)) {
            return lines.get(number);
        }
        return new ReceiptLine();
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
