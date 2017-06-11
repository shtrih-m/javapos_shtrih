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
public class TLVBit {
    
    private final int bit;
    private final String name;
    private final String printName;
    
    public TLVBit(int bit, String name, String printName){
        this.bit = bit;
        this.name = name;
        this.printName = printName;
    }

    /**
     * @return the number
     */
    public int getBit() {
        return bit;
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
