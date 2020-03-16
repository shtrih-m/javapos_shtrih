/*
 * SmFiscalPrinterTest.java
 *
 * Created on 11 Март 2011 г., 11:54
 *
 * To cange tis template, coose Tools | Template Manager
 * and open te template in te editor.
 */
package com.shtrih.fiscalprinter;

import com.shtrih.util.Hex;
import junit.framework.TestCase;

/**
 *
 * @autor V.Kravtsov
 */
public class SmFiscalPrinterTest extends TestCase {

    public void testbarcodeTo1162Value() throws Exception
    {
        String data;
        String validData;
        String barcode;
        // EAN-8
        validData = "45 08 00 00 02 C0 EE D8";
        data = Hex.toHex(SMFiscalPrinterImpl.barcodeTo1162Value("46198488"));
        assertEquals(validData, data);
        // EAN-13 
        validData = "45 0D 04 30 77 19 57 61";
        data = Hex.toHex(SMFiscalPrinterImpl.barcodeTo1162Value("4606203090785"));
        assertEquals(validData, data);
        // ITF-14
        validData = "49 0E 0D 47 9D 66 52 D2";
        data = Hex.toHex(SMFiscalPrinterImpl.barcodeTo1162Value("14601234567890"));
        assertEquals(validData, data);
        
        validData = "44 4D 04 2F 1F 96 81 78 4A 67 58 4A 35 2E 54 31 31 32 30 30 30";
        barcode = "010460043993125621JgXJ5.T\u001d8005112000\u001d930001\u001d923zbrLA==\u001d24014276281";
        data = Hex.toHex(SMFiscalPrinterImpl.barcodeTo1162Value(barcode));
        assertEquals(validData, data);
        
        barcode = "010460406000600021N4N57RSCBUZTQ\u001d2403004002910161218\u001d1724010191ffd0" + 
            "\u001d92tIAF/YVoU4roQS3M/m4z78yFq0fc/WsSmLeX5QkF/YVWwy8IMYAeiQ91Xa2z/fFSJcOkb" + 
            "2N+uUUmfr4n0mOX0Q==";
        validData = "44 4D 04 2F F7 5C 76 70 4E 34 4E 35 37 52 53 43 42 55 5A 54 51";
        data = Hex.toHex(SMFiscalPrinterImpl.barcodeTo1162Value(barcode));
        assertEquals(validData, data);
        
        barcode = "00000046198488X?io+qCABm8wAYa";
        validData = "44 4D 00 00 02 C0 EE D8 58 3F 69 6F 2B 71 43 41 42 6D 38 20 20";
        data = Hex.toHex(SMFiscalPrinterImpl.barcodeTo1162Value(barcode));
        assertEquals(validData, data);
        
        barcode = "RU-401301-AAA02770301";
        validData = "52 46 52 55 2D 34 30 31 33 30 31 2D 41 41 41 30 32 37 37 30 33 30 31";
        data = Hex.toHex(SMFiscalPrinterImpl.barcodeTo1162Value(barcode));
        assertEquals(validData, data);
        
        barcode = "22N00002NU5DBKYDOT17ID980726019019608CW1A4XR5EJ7JKFX50FHHGV92ZR2GZRZ";
        validData = "C5 14 4E 55 35 44 42 4B 59 44 4F 54 31 37 49 44 39 38 30 37 32 36 30 31 39";
        data = Hex.toHex(SMFiscalPrinterImpl.barcodeTo1162Value(barcode));
        assertEquals(validData.toUpperCase(), data);
        
        barcode = "136222000058810918QWERDFEWT5123456YGHFDSWERT56YUIJHGFDSAERTYUIOKJ8H" + 
            "GFVCXZSDLKJHGFDSAOIPLMNBGHJYTRDFGHJKIREWSDFGHJIOIUTDWQASDFRETY" + 
            "UIUYGTREDFGHUYTREWQWE";
        validData = "C5 1E 31 33 36 32 32 32 30 30 30 30 35 38 38 31";
        data = Hex.toHex(SMFiscalPrinterImpl.barcodeTo1162Value(barcode));
        assertEquals(validData.toUpperCase(), data);
        
        barcode = "2710124190";
        validData = "45 41 00 00 A1 89 36 9E";
        data = Hex.toHex(SMFiscalPrinterImpl.barcodeTo1162Value(barcode));
        assertEquals(validData.toUpperCase(), data);
    }
}
