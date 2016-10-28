/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Vector;

public class PrinterFonts {

    private final Vector list = new Vector();

    /**
     * Creates a new instance of PrinterFonts
     */
    public PrinterFonts() {
    }

    public void clear() {
        list.clear();
    }

    public void add(PrinterFont item) {
        list.add(item);
    }

    public PrinterFont add(int number, int charWidth, int charHeight, int paperWidth)
            throws Exception {
        PrinterFont font = new PrinterFont(new FontNumber(number), charWidth,
                charHeight, paperWidth);
        list.add(font);
        return font;
    }

    public int size() {
        return list.size();
    }

    public boolean validIndex(int index) {
        return (index >= 0) && (index < size());
    }

    public PrinterFont get(int index) {
        return (PrinterFont) list.get(index);
    }

    public PrinterFont find(FontNumber number) {
        for (int i = 0; i < size(); i++) {
            PrinterFont item = get(i);
            if (item.getNumber().isEqual(number)) 
            {
                return item;
            }
        }
        return null;
    }

    public PrinterFont itemByNumber(FontNumber number) throws Exception {
        PrinterFont result = find(number);
        if (result == null) {
            throw new Exception("Font not found, " + String.valueOf(number));
        }
        return result;
    }
}
