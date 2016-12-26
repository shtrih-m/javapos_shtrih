/*
 * DownloadBMPFile.java
 *
 * Created on 21 March 2008, 7:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.printer.ncr7167;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

//////////////////////////////////////////////////////////////////////////
//
// This command is virtual and is not supported by NCR 7167 printer
// This command intended to load BML logo from file 
// 0x1B 0x62 fileName 0x0A
//
//////////////////////////////////////////////////////////////////////////

public final class DownloadBMPFile extends NCR7167Command {
    private final String fileName;
    private final FiscalPrinterImpl service;

    public DownloadBMPFile(String fileName, FiscalPrinterImpl service) {
        this.fileName = fileName;
        this.service = service;
    }

    public String getFileName() {
        return fileName;
    }

    public int getId() {
        return NCR7167Printer.ID_DOWNLOAD_BMP_FILE;
    }

    public void execute(NCR7167Printer printer)
            throws Exception {
        service.loadLogo(fileName, service.logoPosition);
    }

    public String getText() {
        return "Download BMP file";
    }

    public final NCR7167Command newInstance(NCR7167Command command) {
        DownloadBMPFile src = (DownloadBMPFile) command;
        return new DownloadBMPFile(src.getFileName(), service);
    }
}
