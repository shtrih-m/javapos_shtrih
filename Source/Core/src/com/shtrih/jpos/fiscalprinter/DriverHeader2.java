package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.PrinterConst;

public class DriverHeader2 extends DriverHeader {
	public DriverHeader2(SMFiscalPrinter printer) {
		super(printer);
	}

	@Override
	public void endFiscal(String additionalTrailer)
			throws Exception {
		printTrailer(additionalTrailer);
		printSpaceLines(getNumHeaderLines());
		getPrinter().cutPaper();
	}
        
	public void endNonFiscal(String additionalTrailer)
			throws Exception {
		endFiscal(additionalTrailer);
	}

        @Override
	public void beginDocument(String additionalHeader)
			throws Exception {
		printHeaderBeforeCutter();
		printHeaderAfterCutter();
                printText(additionalHeader);
	}

}
