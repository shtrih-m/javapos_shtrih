/*
 * PrinterModelYarus01K.java
 *
 * Created on 13 Июль 2010 г., 12:27
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

public class PrinterModelYarus01K extends PrinterModelDefault implements
		PrinterModel {

	private static final int[] textLength2 = { 50, 25, 50, 25, 60, 50, 50 };
	private static final int[] supportedBaudRates2 = { 2400, 4800, 9600, 19200,
			38400, 57600, 115200 };

	/** Creates a new instance of PrinterModelYarus01K */
	public PrinterModelYarus01K() throws Exception {
		id = PrinterConst.SMFP_MODELID_YARUS01K;
		modelID = PrinterConst.PRINTER_MODEL_YARUS01K2;
		protocolVersion = 1;
		protocolSubVersion = 0;

		name = "YARUS-01K";
		capRecPresent = true;
		capRecEmptySensor = true;
		capRecNearEndSensor = true;
		capRecLeverSensor = true;
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
		printWidth = 432;
		capPrintGraphicsLine = false;
		capHasVatTable = true;
		capCoverSensor = true;
		capDoubleWidth = true;
		capDuplicateReceipt = true;
		amountDecimalPlace = 2;
		numHeaderLines = 4;
		numTrailerLines = 3;
		trailerTableNumber = 4;
		headerTableNumber = 4;
		headerTableRow = 4;
		trailerTableRow = 1;
		minHeaderLines = 4;
		minTrailerLines = 0;
		capFullCut = true;
		capPartialCut = true;
		capCashInAutoCut = true;
		capCashOutAutoCut = true;
		capGraphics = true;
		capPrintStringFont = false;
		capShortStatus = false;
		capFontMetrics = false;
		maxGraphicsWidth = 320;
		maxGraphicsHeight = 255;
		capOpenReceipt = false;
		textLength = textLength2;
		supportedBaudRates = supportedBaudRates2;
		addParameter(PrinterConst.SMFP_PARAMID_CUT_MODE, 1, 1, 6);
	}
}
