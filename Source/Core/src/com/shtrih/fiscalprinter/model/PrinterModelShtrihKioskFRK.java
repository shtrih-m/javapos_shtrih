/*
 * PrinterModelShtrihKioskFRK.java
 *
 * Created on 13 Июль 2010 г., 12:25
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

public class PrinterModelShtrihKioskFRK extends PrinterModelDefault {

	private static final int[] textLength2 = { 36, 18, 36, 36, 36, 36, 36 };
	private static final int[] supportedBaudRates2 = { 2400, 4800, 9600, 19200,
			38400, 57600, 115200 };

	/** Creates a new instance of PrinterModelShtrihKioskFRK */
	public PrinterModelShtrihKioskFRK() throws Exception {
		id = PrinterConst.SMFP_MODELID_SHTRIH_KIOSK_FRK;
		modelID = PrinterConst.PRINTER_MODEL_SHTRIH_KIOSK_FRK;
		protocolVersion = 1;
		protocolSubVersion = 0;

		name = "SHTRIH-KIOSK-FRK";
		capRecPresent = true;
		capRecEmptySensor = true;
		capRecNearEndSensor = true;
		capRecLeverSensor = true;
		capJrnPresent = true;
		capJrnEmptySensor = true;
		capJrnNearEndSensor = true;
		capJrnLeverSensor = true;
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
		capGraphics = true;
		capPrintStringFont = false;
		capShortStatus = false;
		capFontMetrics = false;
		maxGraphicsWidth = 320;
		maxGraphicsHeight = 255;
		capOpenReceipt = false;

		textLength = textLength2;
		supportedBaudRates = supportedBaudRates2;
	}
}
