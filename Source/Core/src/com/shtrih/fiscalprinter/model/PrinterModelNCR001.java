/*
 * PrinterModelShtrihMFRK.java
 *
 * Created on 8 Июль 2010 г., 14:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.model;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.fiscalprinter.command.PrinterConst;

public class PrinterModelNCR001 extends PrinterModelDefault {

    private static final int[] textLength2 = {44, 22, 44, 22, 44, 44, 56, 22};
    private static final int[] supportedBaudRates2 = {2400, 4800, 9600, 19200,
        38400, 57600, 115200, 230400, 460800, 921600};

    /**
     * Creates a new instance of PrinterModelShtrihMFRK
     */
    public PrinterModelNCR001() throws Exception {
        id = PrinterConst.SMFP_MODELID_NCR001;
        modelID = PrinterConst.PRINTER_MODEL_NCR001;
        protocolVersion = 1;
        protocolSubVersion = 0;

        name = "NCR-001K";
        capRecPresent = true;
        capRecEmptySensor = true;
        capRecNearEndSensor = true;
        capRecLeverSensor = false;
        capJrnPresent = false;
        capJrnEmptySensor = false;
        capJrnNearEndSensor = false;
        capJrnLeverSensor = false;
        capEJPresent = true;
        capFMPresent = true;
        capSlpPresent = false;
        capSlpEmptySensor = false;
        capSlpNearEndSensor = false;
        numVatRates = 4;
        printWidth = 572;
        capPrintGraphicsLine = true;
        capHasVatTable = true;
        capCoverSensor = true;
        capDoubleWidth = true;
        capDuplicateReceipt = true;
        amountDecimalPlace = 2;
        numHeaderLines = 4;
        numTrailerLines = 3;
        trailerTableNumber = 4;
        headerTableNumber = 4;
        headerTableRow = 11;
        trailerTableRow = 1;
        minHeaderLines = 4;
        minTrailerLines = 0;
        capFullCut = true;
        capPartialCut = true;
        capGraphics = true;
        capPrintStringFont = true;
        capShortStatus = true;
        capFontMetrics = true;
        maxGraphicsWidth = 320;
        maxGraphicsHeight = 200;
        capOpenReceipt = true;
        textLength = textLength2;
        supportedBaudRates = supportedBaudRates2;
        addParameter(PrinterConst.SMFP_PARAMID_DRAWER_ENABLED, 1, 1, 6);
        addParameter(PrinterConst.SMFP_PARAMID_CUT_MODE, 1, 1, 7);
        addParameter(PrinterConst.SMFP_PARAMID_LINE_SPACING, 1, 1, 29);

    }

}
