/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.model;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.command.PrinterConst;

public class PrinterModelASPDElves extends PrinterModelDefault {

	private final int[] defTextLength = { 36, 18, 36, 36, 36, 36, 36 };
	private final int[] defSupportedBaudRates = { 2400, 4800, 9600, 19200,
			38400, 57600, 115200 };

	/** Creates a new instance of PrinterModelASPDElves */
	public PrinterModelASPDElves() throws Exception {
		id = PrinterConst.SMFP_MODELID_ELVES_ASPD;
		modelID = -1;
		protocolVersion = 1;
		protocolSubVersion = 0;
		name = "ASPD-ELVES";
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
		textLength = defTextLength;
		numHeaderLines = 4;
		numTrailerLines = 3;
		trailerTableNumber = 4;
		headerTableNumber = 4;
		headerTableRow = 4;
		trailerTableRow = 1;
		minHeaderLines = 4;
		minTrailerLines = 0;
		supportedBaudRates = defSupportedBaudRates;
		capFullCut = true;
		capPartialCut = true;
		capGraphics = true;
		capGraphicsEx = true;
		capShortStatus = false;
		capFontMetrics = false;
		maxGraphicsWidth = 320;
		maxGraphicsHeight = 255;
		capOpenReceipt = false;
		// Auto open drawer
		addParameter(PrinterConst.SMFP_PARAMID_DRAWER_ENABLED, 1, 1, 6);
	}

}
