/*
 * PrinterFields.java
 *
 * Created on 17 Ноябрь 2009 г., 12:49
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

public class PrinterFields {

    private String modelName = "";
    private final Vector list = new Vector();

    /**
     * Creates a new instance of PrinterFields
     */
    public PrinterFields() {
    }

    public PrinterField itemByName(String name) throws Exception {
        for (int i = 0; i < size(); i++) {
            PrinterField field = get(i);
            if (field.getName().equalsIgnoreCase(name)) {
                return field;
            }
        }
        throw new Exception("Field not found");
    }

    public void clear() {
        list.clear();
        modelName = "";
    }

    public int size() {
        return list.size();
    }

    public void add(PrinterField item) {
        list.add(item);
    }

    public PrinterField get(int index) {
        return (PrinterField) list.get(index);
    }

    public PrinterField find(int table, int row, int field) {
        for (int i = 0; i < size(); i++) {
            PrinterField result = get(i);
            if ((result.getTable() == table) && (result.getRow() == row) && (result.getField() == field)) {
                return result;
            }
        }
        return null;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String value) {
        modelName = value;
    }
}
