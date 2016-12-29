/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Vector;

public class PrinterParameters {

    private final Vector list = new Vector();

    /**
     * Creates a new instance of PrinterParameters
     */
    public PrinterParameters() {
    }

    public void clear() {
        list.clear();
    }

    public int size() {
        return list.size();
    }

    public void add(PrinterParameter item) {
        list.add(item);
    }

    public PrinterParameter get(int index) {
        return (PrinterParameter) list.get(index);
    }

    public PrinterParameter itemByName(String name) throws Exception {
        for (int i = 0; i < size(); i++) {
            PrinterParameter result = get(i);
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    public PrinterParameter itemByText(String text) throws Exception {
        for (int i = 0; i < size(); i++) {
            PrinterParameter result = get(i);
            if (result.getText().equals(text)) {
                return result;
            }
        }
        return null;
    }

    public PrinterParameter getByName(String name) throws Exception {
        PrinterParameter result = itemByName(name);
        if (result == null) {
            throw new Exception("Parameter not found, NAME=" + name);
        }
        return result;
    }

}
