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

    public Vector<Object> items = new Vector<Object>();

    public NonfiscalReceipt(SMFiscalPrinter printer) {
        super(printer);
    }

    private void addTextItem(String text, FontNumber font) throws Exception {
        items.add(new TextReceiptItem(text, font));
    }

    public void printRecMessage(int station, FontNumber font, String message)
            throws Exception {
        addTextItem(message, getParams().getFont());
    }

    public void printNormal(int station, String data) throws Exception {
        addTextItem(data, getParams().getFont());
    }
    
    public void accept(ReceiptVisitor visitor) throws Exception{
        visitor.visitNonfiscalReceipt(this);
    }
    

}
