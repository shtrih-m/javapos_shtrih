package com.shtrih.fiscalprinter.command;

/**
 * @author P.Zhirkov
 */
public class FSDocumentInfo {

    private final FSDocType fsDocType;
    private final boolean sentToOFD;
    private final byte[] data;
    private final FSDateTime dateTime;
    private final int documentNumber;
    private final long fp;

    public FSDocumentInfo(CommandInputStream in) throws Exception {
        fsDocType = new FSDocType(in.readByte());
        sentToOFD = in.readByte() != 0;
        dateTime = in.readFSDate();
        documentNumber = in.readInt();
        fp = in.readLong(4);
        data = in.readBytesToEnd();
    }

    public FSDocType getDocumentType() {
        return fsDocType;
    }

    public boolean getSentToOFD() {
        return sentToOFD;
    }

    public FSDateTime getDateTime() {
        return dateTime;
    }

    public long getFP() {
        return fp;
    }

    public int getDocumentNumber() {
        return documentNumber;
    }

    public byte[] getData() {
        return data;
    }
}
