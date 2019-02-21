/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.fiscalprinter.TLVParser;
import com.shtrih.fiscalprinter.command.FSDocType;
import com.shtrih.fiscalprinter.command.FSFindDocument;
import com.shtrih.fiscalprinter.command.FSReadDocument;
import com.shtrih.fiscalprinter.command.FSReadStatus;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import java.io.ByteArrayOutputStream;
import com.shtrih.fiscalprinter.TLVItems;
import com.shtrih.fiscalprinter.command.FSDocument;
import java.util.Vector;
import com.shtrih.fiscalprinter.command.FSDocumentReceipt;
import com.shtrih.util.StringUtils;

/**
 *
 * @author V.Kravtsov
 */

/*
*/

public class DIOFSFindDocument extends DIOItem {

    public DIOFSFindDocument(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        int docNumber = data[0];
        FSFindDocument command = getPrinter().fsFindDocument(docNumber);
        getPrinter().check(command.getResultCode());
        String[] lines = (String[])object;
       
        FSDocument document = command.getDocument();
        lines[0] = String.valueOf(document.getDocType());
        lines[1] = StringUtils.boolToStr(document.isTicketReceived());
        lines[2] = document.getDateTime().toString();
        lines[3] = String.valueOf(document.getDocNumber());
        lines[4] = String.valueOf(document.getDocSign());
     }
    
}
