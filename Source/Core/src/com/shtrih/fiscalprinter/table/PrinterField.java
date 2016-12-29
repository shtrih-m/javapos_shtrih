/*
 * PrinterField.java
 *
 * Created on April 22 2008, 13:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.table;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.command.FieldInfo;
import com.shtrih.fiscalprinter.SMFiscalPrinterImpl;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.util.CompositeLogger;

public class PrinterField {

    private String value;
    private final int row;
    private final FieldInfo fieldInfo;
    public static CompositeLogger logger = CompositeLogger.getLogger(PrinterField.class);

    // Номер таблицы,Ряд,Поле,Размер поля,Тип поля,Мин. значение, Макс.значение,
    // Название,Значение
    /** Creates a new instance of PrinterField */
    public PrinterField(FieldInfo fieldInfo, int row) 
    {
        this.fieldInfo = fieldInfo;
        this.row = row;
    }

    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }

    
    public int getTable(){
        return fieldInfo.getTable();
    }
    
    public int getField(){
        return fieldInfo.getField();
    }
    
    public int getRow() {
        return row;
    }

    public String getValue() {
        return value;
    }
    
    private static String SInvalidFieldValue = "Invalid field value '%s' (%d).\r\n Valid values: %d..%d";

    
        public void checkValue(String value) throws Exception {
            
        }

    public void setValue(String value) throws Exception {
        checkValue(value);
        this.value = value;
    }
    
    public int getSize() {
        return fieldInfo.getSize();
    }

    public int getType() {
        return fieldInfo.getType();
    }

    public long getMin() {
        return fieldInfo.getMin();
    }

    public long getMax() {
        return fieldInfo.getMax();
    }

    public String getName() {
        return fieldInfo.getName();
    }

    public boolean isInteger() {
        return fieldInfo.isInteger();
    }

    public boolean isString() {
        return fieldInfo.isString();
    }
    
}
