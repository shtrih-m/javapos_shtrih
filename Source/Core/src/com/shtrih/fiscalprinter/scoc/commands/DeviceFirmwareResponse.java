package com.shtrih.fiscalprinter.scoc.commands;

import com.shtrih.fiscalprinter.TLVReader;
import com.shtrih.fiscalprinter.TLVRecord;

public class DeviceFirmwareResponse {

    public static DeviceFirmwareResponse read(byte[] data) throws Exception {

        TLVReader parser = new TLVReader();

        for (TLVRecord record : parser.read(data)) {

            if (record.getTagId() == 8103) {
                return parseFirmwareResponse(parser, record.getData());
            }
        }

        throw new Exception("Tag 8103 was not found");
    }

    private static DeviceFirmwareResponse parseFirmwareResponse(TLVReader parser, byte[] data) throws Exception {

        int partsCount = 0;
        int partNumber = 0;
        long firmwareVersion = 0;
        byte[] firmwareData = new byte[0];

        for (TLVRecord record : parser.read(data)) {

            if (record.getTagId() == 8219)
                firmwareVersion = record.toInt();

            if (record.getTagId() == 8220)
                partNumber = (int) record.toInt();

            if (record.getTagId() == 8221)
                partsCount = (int) record.toInt();

            if (record.getTagId() == 8222)
                firmwareData = record.getData();
        }

        return new DeviceFirmwareResponse(firmwareVersion, partsCount, partNumber, firmwareData);
    }

    private final long firmwareVersion;
    private final int partsCount;
    private final int partNumber;
    private final byte[] data;

    public long getFirmwareVersion() {
        return firmwareVersion;
    }

    public int getPartsCount() {
        return partsCount;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public byte[] getData() {
        return data;
    }

    public DeviceFirmwareResponse(long firmwareVersion, int partsCount, int partNumber, byte[] data) {
        this.firmwareVersion = firmwareVersion;
        this.partsCount = partsCount;
        this.partNumber = partNumber;
        this.data = data;
    }
}
