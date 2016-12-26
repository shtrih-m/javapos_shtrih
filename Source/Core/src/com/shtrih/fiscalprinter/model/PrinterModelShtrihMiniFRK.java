/*
 * PrinterModelShtrihMiniFRK.java
 *
 * Created on 8 Июль 2010 г., 14:49
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

public class PrinterModelShtrihMiniFRK extends PrinterModelDefault {

	private static final int[] textLength2 = { 50, 25, 50, 25, 60, 50, 50 };
	private static final int[] supportedBaudRates2 = { 2400, 4800, 9600, 19200,
			38400, 57600, 115200 };

	/** Creates a new instance of PrinterModelShtrihMiniFRK */
	public PrinterModelShtrihMiniFRK() throws Exception {
		id = PrinterConst.SMFP_MODELID_SHTRIH_MINI_FRK;
		modelID = PrinterConst.PRINTER_MODEL_SHTRIH_MINI_FRK;
		protocolVersion = 1;
		protocolSubVersion = 0;
		name = "SHTRIH-MINI-FR-K";
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
		printWidth = 600;
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
		headerTableRow = 23;
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
		maxGraphicsHeight = 500;
		capOpenReceipt = true;
		textLength = textLength2;
		supportedBaudRates = supportedBaudRates2;
		addParameter(PrinterConst.SMFP_PARAMID_DRAWER_ENABLED, 1, 1, 6);
		addParameter(PrinterConst.SMFP_PARAMID_CUT_MODE, 1, 1, 7);
	}
}
