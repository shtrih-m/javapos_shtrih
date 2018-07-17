package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.SMFiscalPrinter;

public class DriverHeader2 extends DriverHeader {
	public DriverHeader2(SMFiscalPrinter printer) {
		super(printer);
	}

	@Override
	public void endFiscal(String additionalHeader, String additionalTrailer)
			throws Exception {
		printTrailer(additionalTrailer);
		printSpaceLines(getNumHeaderLines());
		getPrinter().cutPaper();
	}
        
	public void endNonFiscal(String additionalHeader, String additionalTrailer)
			throws Exception {
		endFiscal(additionalHeader, additionalTrailer);
	}

        @Override
	public void beginDocument(String additionalHeader, String additionalTrailer)
			throws Exception {
		printHeaderBeforeCutter();
		printHeaderAfterCutter(additionalHeader);
	}

}
