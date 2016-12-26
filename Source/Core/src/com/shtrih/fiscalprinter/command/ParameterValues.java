/*
 * PrinterTables.java
 *
 * Created on 17 Ноябрь 2009 г., 12:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Vector;

public class ParameterValues {

    private final Vector list = new Vector();

    /** Creates a new instance of PrinterTables */
    public ParameterValues() {
    }

    public void clear() {
        list.clear();
    }

    public void add(ParameterValue item) {
        list.add(item);
    }

    public int size() {
        return list.size();
    }

    public ParameterValue get(int index) {
        return (ParameterValue) list.get(index);
    }

    public Integer getFieldValue(int value) {
        for (int i = 0; i < size(); i++) {
            if (get(i).getValue() == value) {
                int result = get(i).getFieldValue();
                return new Integer(result);
            }
        }
        return null;
    }
}
