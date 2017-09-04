package com.shtrih.fiscalprinter;

public class DocumentTLV {
    private final int number;
    private final int type;
    private final byte[] data;

    public DocumentTLV(int documentNumber, int documentType, byte[] tlv) {

        this.number = documentNumber;
        this.type = documentType;
        this.data = tlv;
    }

    public int getDocumentNumber() {
        return number;
    }

    public int getDocumentType() {
        return type;
    }

    public byte[] getTLV() {
        return data;
    }
}
