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
public class GS1Barcode {
    
    public final String GTIN;
    public final String serial;

    public GS1Barcode(String GTIN, String serial) {
        this.GTIN = GTIN;
        this.serial = serial;
    }
}
