/*
 * TextLine.java
 *
 * Created on 10 Март 2010 г., 16:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.jpos.fiscalprinter.PrintItem;

/**
 * @author V.Kravtsov
 */
public class TextLine implements PrintItem {

    private final int station;
    private final FontNumber font;
    private final String line;

    /** Creates a new instance of TextLine */
    public TextLine(int station, FontNumber font, String line) {
        this.station = station;
        this.font = font;
        this.line = line;
    }

    public void print(SMFiscalPrinter printer) throws Exception {
        printer.printText(station, line, font);
    }
}
