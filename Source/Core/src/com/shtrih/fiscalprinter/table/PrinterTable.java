/*
 * PrinterTable.java
 *
 * Created on April 22 2008, 13:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.table;

/**
 * @author V.Kravtsov
 */
public class PrinterTable {

    private final int number;
    private final String name;
    private final int rowCount;
    private final int fieldCount;
    private final PrinterFields fields = new PrinterFields();

    /**
     * Creates a new instance of PrinterTable
     */
    public PrinterTable(int number, String name, int rowCount, int fieldCount) {
        this.number = number;
        this.name = name;
        this.rowCount = rowCount;
        this.fieldCount = fieldCount;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getFieldCount() {
        return fieldCount;
    }

    public PrinterFields getFields() {
        return fields;
    }

    public boolean isValid(PrinterField field) {
        return (field.getRow() >= 1)
                && (field.getRow() <= getRowCount())
                && (field.getField() >= 1)
                && (field.getField() <= getFieldCount());
    }

}
