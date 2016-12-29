/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 * @author V.Kravtsov
 */
public class ParameterValue {
    private int value;
    private int fieldValue;

    public ParameterValue() {
    }

    public ParameterValue(int value, int fieldValue) {
        this.value = value;
        this.fieldValue = fieldValue;
    }

    public int getValue() {
        return value;
    }

    public int getFieldValue() {
        return fieldValue;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setFieldValue(int fieldValue) {
        this.fieldValue = fieldValue;
    }

}
