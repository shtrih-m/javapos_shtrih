package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOOpenFiscalDay extends DIOItem {

	public DIOOpenFiscalDay(FiscalPrinterImpl service) {
		super(service);
	}

	public void execute(int[] data, Object object) throws Exception {
		service.printer.openFiscalDay();
	}
}
