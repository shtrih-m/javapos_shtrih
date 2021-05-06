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

public class DIOFSWriteTag extends DIOItem {

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
        byte[] tlv = getPrinter().getTLVData(tagId, tagValue);
        service.fsWriteTLV(tlv, print);
    }

}
