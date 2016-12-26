/*
 * DownloadActiveFlashSector.java
 *
 * Created on 28 March 2008, 12:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.printer.ncr7167;

/**
 *
 * @author V.Kravtsov
 */

/*********************************************************************
 * Download to Active Flash Sector ASCII: GSDC1al ah cl ch d1…dn Hexadecimal: 1D
 * 11 al ah cl ch d1…dn n = ((ch * 256) + cl)
 **********************************************************************/

public final class DownloadActiveFlashSector extends NCR7167Command {
    /**
     * Creates a new instance of DownloadActiveFlashSector
     */
    public DownloadActiveFlashSector() {
    }

    public final NCR7167Command newInstance(NCR7167Command command) {
        return new DownloadActiveFlashSector();
    }

    public String getText() {
        return "Download active flash sector";
    }

    public void execute(NCR7167Printer printer) throws Exception {
    }
}
