/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

/**
 *
 * @author V.Kravtsov
 */

import jpos.JposConst;
import jpos.JposException;

import com.shtrih.util.CompositeLogger;

import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.command.PrinterStatus;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.jpos.fiscalprinter.JposPrinterStation;
import com.shtrih.util.Localizer;

public class ReceiptPrinterImpl implements ReceiptPrinter {

    private final SMFiscalPrinter printer;
    private final FptrParameters params;
    private static CompositeLogger logger = CompositeLogger.getLogger(ReceiptPrinterImpl.class);

    public ReceiptPrinterImpl(SMFiscalPrinter printer, FptrParameters params) {
        this.printer = printer;
        this.params = params;
    }
    
    public void printPreLine() throws Exception {
        if (params.preLine.length() > 0) {
            printText(params.preLine);
            params.preLine = "";
        }
    }
   
    public void printPostLine() throws Exception {
        if (params.postLine.length() > 0) {
            printText(params.postLine);
            params.postLine = "";
        }
    }
    
    public void openReceipt(int receiptType) throws Exception 
    {
        if (printer.getCapOpenReceipt()) 
        {
            printer.openReceipt(receiptType);
            printer.waitForPrinting();
        }
    }

    
    public void printText(String text) throws Exception {
        printer.printText(PrinterConst.SMFP_STATION_REC, text, printer
                .getParams().getFont());
    }

    
    public long getSubtotal() throws Exception {
        long total = 0;
        PrinterStatus status = printer.readPrinterStatus();
        if (status.getPrinterMode().isReceiptOpened()) {
            total = printer.getSubtotal();
        }
        return total;
    }

    private String formatStrings(String line1, String line2) throws Exception {
        int len;
        String S = "";
        len = printer.getMessageLength() - line2.length();

        for (int i = 0; i < len; i++) {
            if (i < line1.length()) {
                S = S + line1.charAt(i);
            } else {
                S = S + " ";
            }
        }
        return S + line2;
    }

    
    public void printStrings(String line1, String line2) throws Exception {
        printText(formatStrings(line1, line2));
    }

    
    public SMFiscalPrinter getPrinter() throws Exception {
        return printer;
    }

    
    public void printText(int station, String text, FontNumber font)
            throws Exception {
        printer.printText(station, text, font);
    }

    
    public String printDescription(String description) throws Exception {
        String result = "";
        String[] lines = parseText(description);
        if (lines.length == 1) {
            result = lines[0];
        } else 
        {
            for (int i = 0; i < lines.length-1; i++) {
                printText(lines[i]);
            }
            result = lines[lines.length-1];
        }
        return result;
    }

    public String[] parseText(String text) throws Exception {
        logger.debug("parseText: " + text);
        FontNumber font = printer.getParams().getFont();
        return printer.splitText(text, font);
    }

    public String processEscCommands(String text) throws Exception {
        return text;
    }

    
    public void waitForPrinting() throws Exception {
        printer.waitForPrinting();
    }

    
    public int getTextLength() throws Exception {
        return printer.getMessageLength();
    }

    
    public void printSeparator(int separatorType, int height) throws Exception {
        printer.printSeparator(separatorType, height);
    }

    
    public int getStation(int station) throws Exception {
        // check valid stations
        JposPrinterStation printerStation = new JposPrinterStation(station);
        if (printerStation.isRecStation()
                && (!printer.getModel().getCapRecPresent())) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.receiptStationNotPresent));
        }
        if (printerStation.isJrnStation()
                && (!printer.getModel().getCapJrnPresent())) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.journalStationNotPresent));
        }
        if (printerStation.isSlpStation()
                && (!printer.getModel().getCapSlpPresent())) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.slipStationNotPresent));
        }
        return printerStation.getStation();
    }

    public void checkZeroReceipt() throws Exception {

        if (!params.getZeroReceiptEnabled()) {
            if (getPrinter().getSubtotal() == 0) {
                throw new JposException(JposConst.JPOS_E_ILLEGAL,
                        "Zero receipts sre disabled");
            }
        }
    }     
}
