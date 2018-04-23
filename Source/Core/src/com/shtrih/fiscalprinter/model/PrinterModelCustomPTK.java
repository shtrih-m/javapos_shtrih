package com.shtrih.fiscalprinter.model;

import com.shtrih.fiscalprinter.command.PrinterConst;

public class PrinterModelCustomPTK extends PrinterModelDefault {
    public PrinterModelCustomPTK() throws Exception {
        setName("CUSTOM-PTK");
        setId(26);
        setModelID(31);
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
        setMaxGraphicsHeight(255);
        setPrintWidth(432);
        setTextLength(new int[] {  50, 25, 50, 25, 60, 50, 50 });
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
        addParameter("CutMode", 1, 1, 6);
    }
}