package com.shtrih.fiscalprinter.model;

import com.shtrih.fiscalprinter.command.PrinterConst;

public class PrinterModelNCR001 extends PrinterModelDefault {
    public PrinterModelNCR001() throws Exception {
        setName("NCR-001K");
        setId(24);
        setModelID(17);
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
        setCapGraphicsEx(false);
        setCapPrintStringFont(true);
        setCapShortStatus(true);
        setCapFontMetrics(true);
        setCapOpenReceipt(true);
        setNumVatRates(4);
        setAmountDecimalPlace(2);
        setNumHeaderLines(5);
        setNumTrailerLines(3);
        setTrailerTableNumber(4);
        setHeaderTableNumber(4);
        setHeaderTableRow(11);
        setTrailerTableRow(1);
        setMinHeaderLines(5);
        setMinTrailerLines(0);
        setMaxGraphicsWidth(320);
        setMaxGraphicsHeight(200);
        setPrintWidth(576);
        setTextLength(new int[] {  44, 22, 44, 22, 48, 48, 56, 28 });
        setFontHeight(new int[] {  25, 25, 25, 25, 16, 16, 15, 15 });
        setSupportedBaudRates(new int[] {  2400, 4800, 9600, 19200, 38400, 57600, 115200, 230400, 460800 });
        setCapCashInAutoCut(false);
        setCapCashOutAutoCut(false);
        setCapPrintBarcode2(true);
        setDeviceFontNormal(1);
        setDeviceFontDouble(2);
        setDeviceFontSmall(7);
        setSwapGraphicsLine(false);
        setMinCashRegister(0);
        setMaxCashRegister(255);
        setMinCashRegister2(0);
        setMaxCashRegister2(-1);
        setMinOperationRegister(0);
        setMaxOperationRegister(252);
        setCapGraphicsLineMargin(true);

        addParameter("FDOServerHost", 19, 1, 1);
        addParameter("FDOServerPort", 19, 1, 2);
        addParameter("FDOServerTimeout", 19, 1, 3);
        addParameter("DrawerEnabled", 1, 1, 5);
        addParameter("CutMode", 1, 1, 6);
    }
}