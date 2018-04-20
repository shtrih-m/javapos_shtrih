package com.shtrih.fiscalprinter.model;

import com.shtrih.fiscalprinter.command.PrinterConst;

public class PrinterModelShtrihMobileF extends PrinterModelDefault {
    public PrinterModelShtrihMobileF() throws Exception {
        setName("SHTRIH-MOBILE-PTK");
        setId(25);
        setModelID(19);
        setProtocolVersion(1);
        setProtocolSubVersion(0);
        setCapEJPresent(false);
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
        setCapFullCut(false);
        setCapPartialCut(false);
        setCapGraphics(true);
        setCapGraphicsEx(false);
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
        setMaxGraphicsWidth(512);
        setMaxGraphicsHeight(1200);
        setPrintWidth(576);
        setTextLength(new int[] {  48, 24, 48, 24, 57 });
        setFontHeight(new int[] {  });
        setSupportedBaudRates(new int[] {  2400, 4800, 9600, 19200, 38400, 57600, 115200, 230400, 460800 });
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
        setCapFSCloseCheck(true);

        addParameter("FDOServerHost", 19, 1, 1);
        addParameter("FDOServerPort", 19, 1, 2);
        addParameter("FDOServerTimeout", 19, 1, 3);
        addParameter("DrawerEnabled", 1, 1, 5);
        addParameter("CutMode", 1, 1, 6);
    }
}