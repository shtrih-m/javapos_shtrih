package com.shtrih.fiscalprinter.command;

/**
 * @author P.Zhirkov
 */
public class FSDocumentInfo {

    private final int fsDocType;
    private final boolean sentToFDO;
    private final byte[] data;
    private final PrinterDateTime dateTime;
    private final long documentNumber;
    private final long fp;

    public FSDocumentInfo(int fsDocType, boolean sentToFDO, byte[] data, PrinterDateTime dateTime, long documentNumber, long fp) {

        this.fsDocType = fsDocType;
        this.sentToFDO = sentToFDO;
        this.data = data;
        this.dateTime = dateTime;
        this.documentNumber = documentNumber;
        this.fp = fp;
    }

    public int getDocumentType() {
        return fsDocType;
    }

    public boolean getSentToFDO() {
        return sentToFDO;
    }

    public PrinterDateTime getDateTime() {
        return dateTime;
    }

    public long getFP() {
        return fp;
    }

    public long getDocumentNumber() {
        return documentNumber;
    }

    public byte[] getData() {
        return data;
    }
}
