package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.fiscalprinter.SmFiscalPrinterException;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

import java.io.ByteArrayOutputStream;

public class DIOReadFiscalizationTLV extends DIOItem {

    public DIOReadFiscalizationTLV(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        DIOUtils.checkDataMinLength(data, 1);
        int fiscalizationNumber = data[0];

        getPrinter().fsReadFiscalizationTag(fiscalizationNumber, 0xFFFF);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            while (true) {
                byte[] tlvBlock = getPrinter().fsReadDocumentTLVBlock();
                stream.write(tlvBlock);
            }
        }
        catch (SmFiscalPrinterException e){
            if(e.getCode() == 8){
                Object[] outParams = (Object[]) object;
                outParams[0] = stream.toByteArray();
                return;
            }

            throw e;
        }
        finally {
            stream.close();
        }
    }
}
