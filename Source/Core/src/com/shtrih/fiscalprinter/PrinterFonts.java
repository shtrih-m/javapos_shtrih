package com.shtrih.fiscalprinter;

import com.shtrih.fiscalprinter.command.PrinterException;

import java.util.HashMap;
import java.util.Map;

public class PrinterFonts {

    private final Map<FontNumber, PrinterFont> list = new HashMap<>();

    private final SMFiscalPrinterImpl printer;

    public PrinterFonts(SMFiscalPrinterImpl printer) {
        this.printer = printer;
    }

    public PrinterFont find(FontNumber number) throws Exception {
        if (list.containsKey(number))
            return list.get(number);

        try {
            PrinterFont font = printer.readFont(number.getValue());
            list.put(number, font);
            return font;
        } catch (PrinterException e) {
            return null;
        }
    }

    public PrinterFont itemByNumber(FontNumber number) throws Exception {
        PrinterFont result = find(number);
        if (result == null) {
            throw new Exception("Font not found, " + String.valueOf(number));
        }
        return result;
    }
}
