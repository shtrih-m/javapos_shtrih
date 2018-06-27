package com.shtrih.fiscalprinter.scoc.commands;

import com.shtrih.fiscalprinter.TLVReader;
import com.shtrih.fiscalprinter.TLVRecord;

public class DeviceStatusResponse {

    public static DeviceStatusResponse read(byte[] data) throws Exception {

        TLVReader parser = new TLVReader();

        for (TLVRecord record : parser.read(data)) {
            if (record.getTagId() == 8101) {
                return parseStatusResponse(parser, record.getData());
            }
        }

        throw new Exception("Tag 8101 was not found");
    }

    private static DeviceStatusResponse parseStatusResponse(TLVReader parser, byte[] data) throws Exception {

        int resultCode = 0;
        int flags = 0;
        long documentNumber = 0;

        for (TLVRecord record : parser.read(data)) {
            if (record.getTagId() == 8213)
                resultCode = (int) record.toInt();

            if (record.getTagId() == 8215)
                flags = (int) record.toInt();

            if (record.getTagId() == 8200)
                documentNumber = record.toInt();
        }

        return new DeviceStatusResponse(resultCode, flags, documentNumber);
    }

    private final int resultCode;
    private final int flags;
    private final long documentNumber;

    public int getResultCode() {
        return resultCode;
    }

    public int getFlags() {
        return flags;
    }

    public long getDocumentNumber() {
        return documentNumber;
    }

    public DeviceStatusResponse(int resultCode, int flags, long documentNumber) {

        this.resultCode = resultCode;
        this.flags = flags;
        this.documentNumber = documentNumber;
    }
}

