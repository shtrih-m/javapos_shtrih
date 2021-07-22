/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
public class TLVIntValue {
    
    private final int value;
    private final String name;
    private final String printName;
    
    public TLVIntValue(int value, String name, String printName){
        this.value = value;
        this.name = name;
        this.printName = printName;
    }

    /**
     * @return the number
     */
    public int getValue() {
        return value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return the printName
     */
    
    public String getPrintName() {
        return printName;
    }
}
