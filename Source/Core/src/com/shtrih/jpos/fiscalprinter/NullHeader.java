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
    public int getNumHeaderLines() throws Exception {
        return header.getCount();
    }

    @Override
    public int getNumTrailerLines() throws Exception {
        return trailer.getCount();
    }

    // numHeaderLines for device cannot be changed
    @Override
    public void setNumHeaderLines(int numHeaderLines) throws Exception {
        header.setCount(numHeaderLines);
    }

    // numTrailerLines for device cannot be changed
    @Override
    public void setNumTrailerLines(int numTrailerLines) throws Exception {
        trailer.setCount(numTrailerLines);
    }

    @Override
    public void initDevice() throws Exception {
    }

    @Override
    public ReceiptLine getHeaderLine(int number) throws Exception {
        return header.getLine(number);
    }

    @Override
    public ReceiptLine getTrailerLine(int number) throws Exception {
        return trailer.getLine(number);
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

    @Override
    public ReceiptLines getHeaderLines() {
        return header;
    }

    @Override
    public ReceiptLines getTrailerLines() {
        return trailer;
    }
}
