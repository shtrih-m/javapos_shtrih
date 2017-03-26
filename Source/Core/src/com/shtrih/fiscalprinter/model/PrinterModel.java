/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.model;

/**
 *
 * @author V.Kravtsov
 */
import java.util.HashMap;
import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.PrinterFonts;
import com.shtrih.fiscalprinter.command.PrinterParameter;
import com.shtrih.fiscalprinter.command.PrinterParameters;
import com.shtrih.fiscalprinter.table.ReceiptFormatTable;

public interface PrinterModel 
{
    int getAmountDecimalPlace();

    boolean getCapCoverSensor();

    boolean getCapDoubleWidth();

    boolean getCapDuplicateReceipt();

    boolean getCapEJPresent();

    boolean getCapFMPresent();

    boolean getCapFontMetrics();

    boolean getCapFullCut();

    boolean getCapGraphics();

    boolean getCapGraphicsEx();

    boolean getCapHasVatTable();

    boolean getCapJrnEmptySensor();

    boolean getCapJrnLeverSensor();

    boolean getCapJrnNearEndSensor();

    boolean getCapJrnPresent();

    boolean getCapOpenReceipt();

    boolean getCapPartialCut();

    boolean getCapCutter();

    boolean getCapPrintGraphicsLine();

    boolean getCapPrintStringFont();

    boolean getCapRecEmptySensor();

    boolean getCapRecLeverSensor();

    boolean getCapRecNearEndSensor();

    boolean getCapRecPresent();

    boolean getCapShortStatus();

    boolean getCapSlpEmptySensor();

    boolean getCapSlpNearEndSensor();

    boolean getCapSlpPresent();

    int getHeaderTableNumber();

    int getHeaderTableRow();

    int getId();

    int getMaxGraphicsHeight();

    int getMaxGraphicsWidth();

    int getMinHeaderLines();

    int getMinTrailerLines();

    int getModelID();

    String getName();

    int getNumHeaderLines();

    int getNumTrailerLines();

    int getNumVatRates();

    int getProtocolSubVersion();

    int getProtocolVersion();

    int[] getSupportedBaudRates();

    int getFontHeight(FontNumber number) throws Exception;

    int[] getTextLength();

    int[] getFontHeight();
    
    int getTrailerTableNumber();

    int getTrailerTableRow();

    void setAmountDecimalPlace(int amountDecimalPlace);

    void setCapCoverSensor(boolean capCoverSensor);

    void setCapDoubleWidth(boolean capDoubleWidth);

    void setCapDuplicateReceipt(boolean capDuplicateReceipt);

    void setCapEJPresent(boolean capEJPresent);

    void setCapFMPresent(boolean capFMPresent);

    void setCapFontMetrics(boolean capFontMetrics);

    void setCapFullCut(boolean capFullCut);

    void setCapGraphics(boolean capGraphics);

    void setCapHasVatTable(boolean capHasVatTable);

    void setCapJrnEmptySensor(boolean capJrnEmptySensor);

    void setCapJrnLeverSensor(boolean capJrnLeverSensor);

    void setCapJrnNearEndSensor(boolean capJrnNearEndSensor);

    void setCapJrnPresent(boolean capJrnPresent);

    void setCapOpenReceipt(boolean capOpenReceipt);

    void setCapPartialCut(boolean capPartialCut);

    void setCapPrintGraphicsLine(boolean capPrintGraphicsLine);

    void setCapPrintStringFont(boolean capPrintStringFont);

    void setCapRecEmptySensor(boolean capRecEmptySensor);

    void setCapRecLeverSensor(boolean capRecLeverSensor);

    void setCapRecNearEndSensor(boolean capRecNearEndSensor);

    void setCapRecPresent(boolean capRecPresent);

    void setCapShortStatus(boolean capShortStatus);

    void setCapSlpEmptySensor(boolean capSlpEmptySensor);

    void setCapSlpNearEndSensor(boolean capSlpNearEndSensor);

    void setCapSlpPresent(boolean capSlpPresent);

    void setHeaderTableNumber(int headerTableNumber);

    void setHeaderTableRow(int headerTableRow);

    void setId(int id);

    void setMaxGraphicsHeight(int maxGraphicsHeight);

    void setMaxGraphicsWidth(int maxGraphicsWidth);

    void setMinHeaderLines(int minHeaderLines);

    void setMinTrailerLines(int minTrailerLines);

    void setModelID(int modelID);

    void setName(String name);

    void setNumHeaderLines(int numHeaderLines);

    void setNumTrailerLines(int numTrailerLines);

    void setNumVatRates(int numVatRates);

    void setProtocolSubVersion(int protocolSubVersion);

    void setProtocolVersion(int protocolVersion);

    void setSupportedBaudRates(int[] supportedBaudRates);

    void setTextLength(int[] textLength);

    void setFontHeight(int[] fontHeight);
    
    void setTrailerTableNumber(int trailerTableNumber);

    void setTrailerTableRow(int trailerTableRow);

    void setCapGraphicsEx(boolean CapGraphicsEx);

    PrinterParameters getParameters();

    PrinterParameter getParameter(String name) throws Exception;

    PrinterParameter findParameter(String name) throws Exception;
    
    boolean getCapParameter(String name) throws Exception;

    int getLineSpacing() throws Exception;

    int getTextLength(FontNumber number) throws Exception;
    
    PrinterFonts getFonts() throws Exception;

    void setFonts(PrinterFonts fonts) throws Exception;
    
    int getHeaderHeight() throws Exception;

    int getTrailerHeight() throws Exception;
    
    boolean getCapCashInAutoCut();

    boolean getCapCashOutAutoCut();

    public void setCapCashOutAutoCut(boolean capCashOutAutoCut);

    public void setCapCashInAutoCut(boolean capCashInAutoCut);

    public boolean isCommandSupported(int code);

    public boolean getCapPrintBarcode2();

    public boolean getCapBarcodeSupported(int barcodeType);
    
    public void setCapPrintBarcode2(boolean value);
    
    public int getDeviceFontNormal();
    
    public int getDeviceFontDouble();
    
    public int getDeviceFontSmall();
    
    public void setDeviceFontNormal(int value);
    
    public void setDeviceFontDouble(int value);
    
    public void setDeviceFontSmall(int value);
    
    boolean getSwapGraphicsLine();
    
    public void setSwapGraphicsLine(boolean value);
    
    public int getMinCashRegister();
    
    public int getMaxCashRegister();
    
    public int getMinCashRegister2();
    
    public int getMaxCashRegister2();
    
    public int getMinOperationRegister();
    
    public int getMaxOperationRegister();
    
    public boolean getCapGraphicsLineMargin();
    
    public int getPrintWidth();
    
    public void setPrintWidth(int value);

    boolean getCapFSCloseCheck();

    void setCapFSCloseCheck(boolean value);
}
