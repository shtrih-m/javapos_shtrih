package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.fiscalprinter.command.FSReadFiscalizationTag;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOReadFiscalizationTag extends DIOItem {

    public DIOReadFiscalizationTag(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        DIOUtils.checkDataMinLength(data, 2);
        int fiscalizationNumber = data[0];
        int tagNumber = data[1];

        FSReadFiscalizationTag readDocument = getPrinter().fsReadFiscalizationTag(fiscalizationNumber, tagNumber);

        Object[] outParams = (Object[]) object;
        outParams[0] = readDocument.getTagData();
    }
}
