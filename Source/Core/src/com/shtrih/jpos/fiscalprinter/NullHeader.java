package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.util.Localizer;
import java.util.List;
import jpos.JposConst;
import jpos.JposException;

import java.util.Vector;

public class NullHeader implements PrinterHeader {

    private final SMFiscalPrinter printer;
    private final ReceiptLines header = new ReceiptLines(4);
    private final ReceiptLines trailer = new ReceiptLines(3);

    public NullHeader(SMFiscalPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void initDevice() throws Exception {
    }

    @Override
    public void setHeaderLine(int number, String text, boolean doubleWidth)
            throws Exception {
        header.setLine(number, text, doubleWidth);
    }

    @Override
    public void setTrailerLine(int number, String text, boolean doubleWidth)
            throws Exception {
        trailer.setLine(number, text, doubleWidth);
    }

    @Override
    public void beginDocument(String additionalHeader)
            throws Exception {
    }

    @Override
    public void endFiscal(String additionalTrailer)
            throws Exception {
    }

    @Override
    public void endNonFiscal(String additionalTrailer)
            throws Exception {
    }
}
