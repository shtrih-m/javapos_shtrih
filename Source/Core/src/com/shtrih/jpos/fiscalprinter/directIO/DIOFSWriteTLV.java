package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Hex;

public class DIOFSWriteTLV extends DIOItem {

    private static CompositeLogger logger = CompositeLogger.getLogger(DIOFSWriteTLV.class);
    
    public DIOFSWriteTLV(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        byte[] tlv = (byte[])object;
        boolean print = true;
        if (data != null) {
            print = (data[0] == 1);
        }
        logger.debug("directIO(SMFPTR_DIO_FS_WRITE_TLV, " + Hex.toHex(tlv) + ")");
        service.fsWriteTLV(tlv, print);
    }
}
