package com.shtrih.fiscalprinter.model;

import com.shtrih.fiscalprinter.command.PrinterConst;

public class PrinterModelShtrihLightFRK2 extends PrinterModelDefault {
    public PrinterModelShtrihLightFRK2() throws Exception {
        setName("SHTRIH-LIGHT-FR-K2");
        setId(22);
        setModelID(252);
        setProtocolVersion(1);
        setProtocolSubVersion(0);
        setCapEJPresent(true);
        setCapFMPresent(true);
        setCapRecPresent(true);
        setCapJrnPresent(false);
        setCapSlpPresent(false);
        setCapSlpEmptySensor(false);
        setCapSlpNearEndSensor(false);
        setCapRecEmptySensor(true);
        setCapRecEmptySensor(true);
        setCapRecNearEndSensor(true);
        setCapRecLeverSensor(false);
        setCapJrnEmptySensor(false);
        setCapJrnNearEndSensor(false);
        setCapJrnLeverSensor(false);
        setCapPrintGraphicsLine(true);
        setCapHasVatTable(true);
        setCapCoverSensor(true);
        setCapDoubleWidth(true);
        setCapDuplicateReceipt(true);
        setCapFullCut(true);
        setCapPartialCut(true);
        setCapGraphics(true);
        setCapGraphicsEx(true);
        setCapPrintStringFont(true);
        setCapShortStatus(true);
        setCapFontMetrics(true);
        setCapOpenReceipt(true);
        setNumVatRates(4);
        setAmountDecimalPlace(2);
        setNumHeaderLines(3);
        setNumTrailerLines(3);
        setTrailerTableNumber(4);
        setHeaderTableNumber(4);
        setHeaderTableRow(12);
        setTrailerTableRow(1);
        setMinHeaderLines(3);
        setMinTrailerLines(0);
        setMaxGraphicsWidth(320);
        setMaxGraphicsHeight(500);
        setPrintWidth(320);
        setTextLength(new int[] {  24, 12, 24, 24, 24, 24, 24 });
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
        addParameter("DrawerEnabled", 1, 1, 6);
        addParameter("CutMode", 1, 1, 7);
    }
}