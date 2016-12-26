/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterTime;

public class ReceiptReport {

    // /////////////////////////////////////////////////////////////////////////
    // Receipt type
    public static final int XML_RT_CASH_IN = 1;
    public static final int XML_RT_CASH_OUT = 2;
    public static final int XML_RT_SALE = 3;
    public static final int XML_RT_BUY = 4;
    public static final int XML_RT_RETSALE = 5;
    public static final int XML_RT_RETBUY = 6;
    public static final int XML_RT_NONFISCAL = 7;

    public int id;
    public int docID;
    public int dayNumber;
    public int recType;
    public int state;
    public long amount;
    public long[] payments = new long[4];
    public PrinterTime time = new PrinterTime();
    public PrinterDate date = new PrinterDate();

    public long getPaymentTotal() {
        return payments[0] + payments[1] + payments[2] + payments[3];
    }
}
