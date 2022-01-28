/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.TLVList;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Hex;

public class DIOFSWriteTag extends DIOItem {

    private static CompositeLogger logger = CompositeLogger.getLogger(DIOFSWriteTag.class);
    
    public DIOFSWriteTag(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        DIOUtils.checkDataMinLength(data, 1);
        int tagId = data[0];
        boolean print = true;
        if (data.length > 1){
            print = data[1] == 1;
        }
        String tagValue = (String) object;
        logger.debug(String.format("directIO(SMFPTR_DIO_FS_WRITE_TAG, Tag: %s, Valus: %s)", tagId, tagValue));
                
        byte[] tlv = getPrinter().getTLVData(tagId, tagValue);
        service.fsWriteTLV(tlv, print);
    }

}
