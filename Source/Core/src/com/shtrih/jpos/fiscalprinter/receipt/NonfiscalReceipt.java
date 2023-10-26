/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import java.util.Vector;

/**
 * @author V.Kravtsov
 */
public class NonfiscalReceipt extends CustomReceipt implements FiscalReceipt {

    public Vector<TextReceiptItem> items = new Vector<TextReceiptItem>();

    public NonfiscalReceipt(SMFiscalPrinter printer) {
        super(printer);
    }

    private void addTextItem(String text, FontNumber font) throws Exception {
        items.add(new TextReceiptItem(text, font));
    }

    public void printRecMessage(int station, FontNumber font, String message)
            throws Exception {
        addTextItem(message, font);
        getPrinter().printText(getStation(station), message, font);
    }

    public void printNormal(int station, String data) throws Exception {
        addTextItem(data, getParams().getFont());
        getPrinter().printText(getStation(station), data, getParams().font);
    }
    
    public void accept(ReceiptVisitor visitor) throws Exception{
        visitor.visitNonfiscalReceipt(this);
    }
}
