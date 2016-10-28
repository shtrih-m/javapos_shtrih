/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 * @author V.Kravtsov
 */
public class PrinterParameter {

    private final String name;
    private final String text;
    private final int tableNumber;
    private final int rowNumber;
    private final int fieldNumber;
    private final ParameterValues values = new ParameterValues();

    public PrinterParameter(String name, String text, int tableNumber, int rowNumber,
            int fieldNumber) {
        this.name = name;
        this.text = text;
        this.tableNumber = tableNumber;
        this.rowNumber = rowNumber;
        this.fieldNumber = fieldNumber;
    }

    public ParameterValues getValues() {
        return values;
    }

    public int getFieldValue(int value) throws Exception {
        Integer result = values.getFieldValue(value);
        if (result == null) {
            return value;
        }
        return result.intValue();
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
    
    public int getTableNumber() {
        return tableNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
