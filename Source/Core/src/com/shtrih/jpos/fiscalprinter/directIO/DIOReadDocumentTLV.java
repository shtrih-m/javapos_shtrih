package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.fiscalprinter.command.FSReadDocument;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

import java.io.ByteArrayOutputStream;

public class DIOReadDocumentTLV extends DIOItem {

    public DIOReadDocumentTLV(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        DIOUtils.checkDataMinLength(data, 1);
        int documentNumber = data[0];

        FSReadDocument readDocument = getPrinter().fsRequestDocumentTLV(documentNumber);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            while (stream.size() < readDocument.getDocSize()) {
                byte[] tlvBlock = getPrinter().fsReadDocumentTLVBlock();
                stream.write(tlvBlock);
            }

            Object[] outParams = (Object[]) object;
            outParams[0] = readDocument.getDocType();
            outParams[1] = stream.toByteArray();
        } finally {
            stream.close();
        }
    }
}

