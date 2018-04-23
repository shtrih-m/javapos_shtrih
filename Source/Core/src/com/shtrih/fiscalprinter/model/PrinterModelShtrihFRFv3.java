package com.shtrih.fiscalprinter.model;

import com.shtrih.fiscalprinter.command.PrinterConst;

public class PrinterModelShtrihFRFv3 extends PrinterModelDefault {
    public PrinterModelShtrihFRFv3() throws Exception {
        setName("SHTRIH-FR-F, version 3");
        setId(1);
        setModelID(0);
        setProtocolVersion(1);
        setProtocolSubVersion(0);
        setCapEJPresent(true);
        setCapFMPresent(true);
        setCapRecPresent(true);
        setCapJrnPresent(true);
        setCapSlpPresent(false);
        setCapSlpEmptySensor(false);
        setCapSlpNearEndSensor(false);
        setCapRecEmptySensor(true);
        setCapRecEmptySensor(true);
        setCapRecNearEndSensor(true);
        setCapRecLeverSensor(true);
        setCapJrnEmptySensor(true);
        setCapJrnNearEndSensor(true);
        setCapJrnLeverSensor(true);
        setCapPrintGraphicsLine(false);
        setCapHasVatTable(true);
        setCapCoverSensor(true);
        setCapDoubleWidth(true);
        setCapDuplicateReceipt(true);
        setCapFullCut(true);
        setCapPartialCut(true);
        setCapGraphics(true);
        setCapGraphicsEx(true);
        setCapPrintStringFont(false);
        setCapShortStatus(false);
        setCapFontMetrics(false);
        setCapOpenReceipt(false);
        setNumVatRates(4);
        setAmountDecimalPlace(2);
        setNumHeaderLines(4);
        setNumTrailerLines(3);
        setTrailerTableNumber(4);
        setHeaderTableNumber(4);
        setHeaderTableRow(4);
        setTrailerTableRow(1);
        setMinHeaderLines(4);
        setMinTrailerLines(0);
        setMaxGraphicsWidth(320);
        setMaxGraphicsHeight(200);
        setPrintWidth(432);
        setTextLength(new int[] {  36, 18, 36, 18, 36, 40, 36 });
        setFontHeight(new int[] {  });
        setSupportedBaudRates(new int[] {  2400, 4800, 9600, 19200, 38400, 57600, 115200 });
        setCapCashInAutoCut(false);
        setCapCashOutAutoCut(false);
        setCapPrintBarcode2(false);
        setDeviceFontNormal(1);
        setDeviceFontDouble(2);
        setDeviceFontSmall(3);
        setSwapGraphicsLine(false);
        setMinCashRegister(0);
        setMaxCashRegister(255);
        setMinCashRegister2(0);
        setMaxCashRegister2(-1);
        setMinOperationRegister(0);
        setMaxOperationRegister(255);
        setCapGraphicsLineMargin(false);

        addParameter("FDOServerHost", 19, 1, 1);
        addParameter("FDOServerPort", 19, 1, 2);
        addParameter("FDOServerTimeout", 19, 1, 3);
        addParameter("DrawerEnabled", 1, 1, 7);
        addParameter("CutMode", 1, 1, 8);
    }
}