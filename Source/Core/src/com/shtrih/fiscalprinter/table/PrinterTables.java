/*
 * PrinterTables.java
 *
 * Created on 17 Ноябрь 2009 г., 12:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.table;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Vector;

public class PrinterTables {

    private final Vector list = new Vector();

    /**
     * Creates a new instance of PrinterTables
     */
    public PrinterTables() {
    }

    public PrinterTable itemByName(String name) throws Exception {
        for (int i = 0; i < size(); i++) {
            PrinterTable table = get(i);
            if (table.getName().equalsIgnoreCase(name)) {
                return table;
            }
        }
        throw new Exception("Table not found");
    }

    public PrinterTable find(int number) throws Exception {
        for (int i = 0; i < size(); i++) {
            PrinterTable table = get(i);
            if (table.getNumber() == number) {
                return table;
            }
        }
        return null;
    }

    public PrinterTable itemByNumber(int number) throws Exception {
        PrinterTable table = find(number);
        if (table == null) {
            throw new Exception("Table not found");
        }
        return table;
    }

    public void clear() {
        list.clear();
    }

    public void add(PrinterTable item) {
        list.add(item);
    }

    public int size() {
        return list.size();
    }

    public PrinterTable get(int index) {
        return (PrinterTable) list.get(index);
    }
}
