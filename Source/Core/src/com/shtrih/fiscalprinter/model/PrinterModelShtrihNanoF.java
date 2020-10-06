package com.shtrih.fiscalprinter.model;

import com.shtrih.fiscalprinter.command.PrinterConst;

public class PrinterModelShtrihNanoF extends PrinterModelDefault {
    public PrinterModelShtrihNanoF() throws Exception {
        setName("SHTRIH-NANO-F");
        setId(28);
        setModelID(152);
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
        setCapRecLeverSensor(true);
        setCapJrnEmptySensor(false);
        setCapJrnNearEndSensor(false);
        setCapJrnLeverSensor(false);
        setCapPrintGraphicsLine(true);
        setCapHasVatTable(true);
        setCapCoverSensor(true);
        setCapDoubleWidth(true);
        setCapDuplicateReceipt(true);
        setCapFullCut(false);
        setCapPartialCut(false);
        setCapGraphics(true);
        setCapGraphicsEx(true);
        setCapPrintStringFont(true);
        setCapShortStatus(true);
        setCapFontMetrics(true);
        setCapOpenReceipt(true);
        setNumVatRates(4);
        setAmountDecimalPlace(2);
        setNumHeaderLines(2);
        setNumTrailerLines(0);
        setTrailerTableNumber(4);
        setHeaderTableNumber(4);
        setHeaderTableRow(4);
        setTrailerTableRow(1);
        setMinHeaderLines(12);
        setMinTrailerLines(0);
        setMaxGraphicsWidth(320);
        setMaxGraphicsHeight(100000);
        setPrintWidth(384);
        setTextLength(new int[] {  42, 21, 42, 21, 42, 42, 56, 24, 23 });
        setFontHeight(new int[] {  });
        setSupportedBaudRates(new int[] {  2400, 4800, 9600, 19200, 38400, 57600, 115200 });
        setCapCashInAutoCut(true);
        setCapCashOutAutoCut(true);
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
    }
}