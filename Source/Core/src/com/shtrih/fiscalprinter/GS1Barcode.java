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

    private final GS1BarcodeParser.ApplicationIdentifiers items;

    public GS1Barcode(GS1BarcodeParser.ApplicationIdentifiers items) {
        this.items = items;
    }

    public GS1BarcodeParser.ApplicationIdentifiers getItems() {
        return items;
    }

    public boolean isValid() {
        return items.size() > 0;
    }
    
    public static boolean isValid(String barcode) throws Exception{
        return parse(barcode).isValid();
    }
    
    public static GS1Barcode parse(String barcode) throws Exception {
        GS1BarcodeParser parser = new GS1BarcodeParser();
        return parser.decode(barcode);
    }

    public boolean hasItem(String id) {
        return items.find(id) != null;
    }
    
    public String getItem(String id) throws Exception{
        return items.get(id).value;
    }
}

/*

        ApplicationIdentifier ai = identifiers.find("01");
        if (ai == null) {
            throw new Exception("Tag not found, GTIN(01)");
        }
        String GTIN = ai.value;
        ai = identifiers.find("21");
        if (ai == null) {
            throw new Exception("Tag not found, SerialNumber(21)");
        }
        String serial = ai.value;

*/
