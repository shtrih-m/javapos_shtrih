package com.shtrih.jpos.fiscalprinter;

import jpos.JposConst;
import jpos.JposException;

import com.shtrih.util.CompositeLogger;

import com.shtrih.util.Localizer;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.command.ReadTableInfo;
import com.shtrih.fiscalprinter.table.PrinterTable;
import java.util.Vector;

public class DeviceHeader implements PrinterHeader {

    private int count = 0;
    private final SMFiscalPrinter printer;

    public DeviceHeader(SMFiscalPrinter printer) {
        this.printer = printer;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void clear() {
    }

    public void initDevice() throws Exception {
        String[] fieldValue = new String[1];
        ReadTableInfo command = printer.readTableInfo(PrinterConst.SMFP_TABLE_TEXT);
        PrinterTable table = command.getTable();
        int rowCount = table.getRowCount();
        for (int row = 1; row <= rowCount; row++) {
            int result = printer.readTable(PrinterConst.SMFP_TABLE_TEXT, row,
                    1, fieldValue);
            if (printer.failed(result)) {
                break;
            }
            if (fieldValue[0].length() == 0) {
                result = printer.writeTable(PrinterConst.SMFP_TABLE_TEXT, row,
                        1, " ");
                if (printer.failed(result)) {
                    {
                        break;
                    }
                }
            }
        }
    }

    public void setLine(int lineNumber, String text)
            throws Exception {
    }

    public void addLine(String text) throws Exception {
    }

    public void writeLine(int lineNumber, String text) throws Exception {
        checkLineNumber(lineNumber);
        int table = printer.getModel().getHeaderTableNumber();
        int row = printer.getModel().getHeaderTableRow();
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
        printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER);
        printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_AFTER_HEADER);
    }

    public HeaderLine get(int index) throws Exception {
        return null;
    }

    public void setAdditionalText(String text) {
    }

    public void printRecMessages(Vector printItems) throws Exception {
    }
}
