/*
 * FiscalPrinterStatistics.java
 *
 * Created on April 6 2008, 15:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import jpos.JposStatisticsConst;

import com.shtrih.jpos.JposDeviceStatistics;
import com.shtrih.jpos.JposStatistics;

public class FiscalPrinterStatistics extends JposDeviceStatistics implements
        JposStatistics, JposStatisticsConst {
    // Number of Barcodes printed
    private int BarcodePrintedCount;
    // Number of forms inserted into the document/slip station
    private int FormInsertionCount;
    // Number of home errors
    private int HomeErrorCount;
    // Number of Journal characters printed
    private int JournalCharacterPrintedCount;
    // Number of Journal lines printed
    private int JournalLinePrintedCount;
    // Number of times Maximum temperature reached
    private int MaximumTempReachedCount;
    // Number of times NVRAM is written to
    private int NVRAMWriteCount;
    // Number of paper cuts
    private int PaperCutCount;
    // Number of failed paper cuts
    private int FailedPaperCutCount;
    // Number of Printer faults
    private int PrinterFaultCount;
    // Number of print side changes (or check flips) performed
    private int PrintSideChangeCount;
    // Number of print side changes (or check flips) failures
    private int FailedPrintSideChangeCount;
    // Number of receipt characters printed
    private int ReceiptCharacterPrintedCount;
    // Number of times the receipt cover was opened
    private int ReceiptCoverOpenCount;
    // Number of receipt line feeds performed
    private int ReceiptLineFeedCount;
    // Number of receipt lines printed
    private int ReceiptLinePrintedCount;
    // Number of document/slip characters printed
    private int SlipCharacterPrintedCount;
    // Number of times the document/slip station cover opened
    private int SlipCoverOpenCount;
    // Number of document/slip line feeds performed
    private int SlipLineFeedCount;
    // Number of document/slip lines printed
    private int SlipLinePrintedCount;
    // Number of Stamps fired
    private int StampFiredCount;

    /** Creates a new instance of FiscalPrinterStatistics */

    public FiscalPrinterStatistics() {
        defineStatistic(JPOS_STAT_BarcodePrintedCount);
        defineStatistic(JPOS_STAT_FormInsertionCount);
        defineStatistic(JPOS_STAT_HomeErrorCount);
        defineStatistic(JPOS_STAT_JournalCharacterPrintedCount);
        defineStatistic(JPOS_STAT_JournalLinePrintedCount);
        defineStatistic(JPOS_STAT_MaximumTempReachedCount);
        defineStatistic(JPOS_STAT_NVRAMWriteCount);
        defineStatistic(JPOS_STAT_PaperCutCount);
        defineStatistic(JPOS_STAT_FailedPaperCutCount);
        defineStatistic(JPOS_STAT_PrinterFaultCount);
        defineStatistic(JPOS_STAT_PrintSideChangeCount);
        defineStatistic(JPOS_STAT_FailedPrintSideChangeCount);
        defineStatistic(JPOS_STAT_ReceiptCharacterPrintedCount);
        defineStatistic(JPOS_STAT_ReceiptCoverOpenCount);
        defineStatistic(JPOS_STAT_ReceiptLineFeedCount);
        defineStatistic(JPOS_STAT_ReceiptLinePrintedCount);
        defineStatistic(JPOS_STAT_SlipCharacterPrintedCount);
        defineStatistic(JPOS_STAT_SlipCoverOpenCount);
        defineStatistic(JPOS_STAT_SlipLineFeedCount);
        defineStatistic(JPOS_STAT_SlipLinePrintedCount);
    }

    public void barcodePrinted() {
        incStatistic(JPOS_STAT_BarcodePrintedCount, 1);
    }
}
