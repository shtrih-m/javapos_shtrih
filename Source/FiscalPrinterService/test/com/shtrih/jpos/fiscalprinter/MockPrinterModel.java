/*
 * MockPrinterModel.java
 *
 * Created on 12 Октябрь 2010 г., 18:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter;
/**
 *
 * @author V.Kravtsov
 */


import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.model.PrinterModelDefault;


public class MockPrinterModel //extends PrinterModelDefault
{
    
    public boolean capRecPresent = true;
    public boolean capRecEmptySensor = true;
    public boolean capRecNearEndSensor = true;
    public boolean capRecLeverSensor = true;
    public boolean capJrnPresent = true;
    public boolean capJrnEmptySensor = true;
    public boolean capJrnNearEndSensor = true;
    public boolean capJrnLeverSensor = true;
    public boolean capEJPresent = true;
    public boolean capFMPresent = true;
    public boolean capSlpPresent = true;
    public boolean capSlpEmptySensor = true;
    public boolean capSlpNearEndSensor = true;
    public int numVatRates = 4;
    public int printWidth = 432;
    public boolean capPrintGraphicsLine = false;
    public boolean capHasVatTable = true;
    public boolean capCoverSensor = true;
    public boolean capDoubleWidth = true;
    public boolean capDuplicateReceipt = true;
    public int amountDecimalPlace = 2;
    public int[] textLength = {36, 18, 36, 36, 36, 36, 36};
    public int numHeaderLines = 4;
    public int numTrailerLines = 3;
    public int trailerTableNumber = 4;
    public int headerTableNumber = 4;
    public int headerTableRow = 4;
    public int trailerTableRow = 1;
    public int minHeaderLines = 4;
    public int minTrailerLines = 0;
    public int[] supportedBaudRates =
    {2400, 4800, 9600, 19200, 38400, 57600, 115200};
    public boolean capFullCut = true;
    public boolean capPartialCut = true;
    public boolean capGraphics = true;
    public boolean capPrintStringFont = true;
    public boolean capShortStatus = false;
    public boolean capFontMetrics = false;
    public int maxGraphicsWidth = 320;
    public int maxGraphicsHeight = 255;
    public boolean capOpenReceipt = false;
    
    /** Creates a new instance of MockPrinterModel */
    public MockPrinterModel() 
    {
        /*
        try{
            super();
        }catch(Exception e){
            fail(e);
        }
                */
    }
    
    public String getName() {
        return "Mock printer model";
    }
    
}
