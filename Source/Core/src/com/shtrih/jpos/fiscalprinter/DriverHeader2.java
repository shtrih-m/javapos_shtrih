package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.SMFiscalPrinter;

public class DriverHeader2 extends DriverHeader {
	public DriverHeader2(SMFiscalPrinter printer) {
		super(printer);
	}

	@Override
	public void endDocument(String additionalHeader, String additionalTrailer)
			throws Exception {
		printTrailer(additionalTrailer);
		printSpaceLines(getModel().getNumHeaderLines());
		getPrinter().cutPaper();
	}

		@Override
	public void beginDocument(String additionalHeader, String additionalTrailer)
			throws Exception {
		printHeaderBeforeCutter();
		printHeaderAfterCutter(additionalHeader);
	}

}
