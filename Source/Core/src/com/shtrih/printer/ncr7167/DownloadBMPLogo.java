/*
 * DownloadBMPLogo.java
 *
 * Created on 28 March 2008, 11:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.printer.ncr7167;

/**
 * @author V.Kravtsov
 */

public final class DownloadBMPLogo extends NCR7167Command {
    private final byte[] data;

    public DownloadBMPLogo(byte[] data) {
        this.data = data;
    }

    public final byte[] getData() {
        return data;
    }

    public final String getText() {
        return "Download BMP logo";
    }

    public final NCR7167Command newInstance(NCR7167Command command) {
        DownloadBMPLogo src = (DownloadBMPLogo) command;
        return new DownloadBMPLogo(src.getData());
    }

    public void execute(NCR7167Printer printer) throws Exception {
    }
}
