package com.shtrih.fiscalprinter.command;

/**
 * @author P.Zhirkov
 */
public class FSDocumentInfo {

    private final FSDocType fsDocType;
    private final boolean sentToOFD;
    private final byte[] data;
    private final FSDateTime dateTime;

    public FSDocumentInfo(CommandInputStream in) {
        fsDocType = new FSDocType(in.readByte());
        sentToOFD = in.readByte() != 0;
        dateTime = in.readFSDate();
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

    public byte[] getData() {
        return data;
    }
}

