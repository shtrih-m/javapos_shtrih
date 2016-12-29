package com.shtrih.fiscalprinter;

import jpos.FiscalPrinterControl113;

public class ShtrihFiscalPrinter extends ShtrihFiscalPrinter113 {

	public ShtrihFiscalPrinter(FiscalPrinterControl113 printer, String encoding) {
		super(printer, encoding);
	}

	public ShtrihFiscalPrinter(FiscalPrinterControl113 printer) {
		super(printer);
	}

	public ShtrihFiscalPrinter(String encoding) {
		super(encoding);
	}

}
