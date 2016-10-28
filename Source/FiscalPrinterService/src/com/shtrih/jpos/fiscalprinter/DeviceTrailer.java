package com.shtrih.jpos.fiscalprinter;

import jpos.JposConst;
import jpos.JposException;

import com.shtrih.util.CompositeLogger;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.util.Localizer;
import java.util.Vector;

public class DeviceTrailer implements PrinterHeader {

    private int count = 0;
    private final SMFiscalPrinter printer;

    public DeviceTrailer(SMFiscalPrinter printer) {
        this.printer = printer;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void clear() {
    }

    public void initDevice() throws Exception {
    }

    public void setLine(int lineNumber, String text)
            throws Exception {
    }

    public void addLine(String text) throws Exception {
    }

    public void writeLine(int lineNumber, String text) throws Exception {
        checkLineNumber(lineNumber);
        int table = printer.getModel().getTrailerTableNumber();
        int row = printer.getModel().getTrailerTableRow();
        printer.writeTable(table, row + lineNumber - 1, 1, text);
    }

    public int size() {
        return count;
    }

    public boolean validNumber(int number) throws Exception {
        return ((number >= 1) && (number <= count));
    }

    public void checkLineNumber(int lineNumber) throws Exception {
        if (!validNumber(lineNumber)) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.InvalidLineNumber));
        }
    }

    public void print() throws Exception {
    }

    public HeaderLine get(int index) throws Exception {
        return null;
    }
    
    public void setAdditionalText(String text){
    }

    public void printRecMessages(Vector printItems) throws Exception{
    }
    
}
