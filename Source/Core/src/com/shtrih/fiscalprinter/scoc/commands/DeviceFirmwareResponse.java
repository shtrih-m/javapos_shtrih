package com.shtrih.fiscalprinter.scoc.commands;

import com.shtrih.fiscalprinter.TLVReader;
import com.shtrih.fiscalprinter.TLVItem;

public class DeviceFirmwareResponse {

    public static DeviceFirmwareResponse read(byte[] data) throws Exception {

        TLVReader parser = new TLVReader();

        for (TLVItem record : parser.read(data)) {

            if (record.getId() == 8103) {
                return parseFirmwareResponse(parser, record.getData());
            }
        }

        throw new Exception("Tag 8103 was not found");
    }

    private static DeviceFirmwareResponse parseFirmwareResponse(TLVReader reader, byte[] data) throws Exception {

        int partsCount = 0;
        int partNumber = 0;
        long firmwareVersion = 0;
        byte[] firmwareData = new byte[0];

        for (TLVItem item : reader.read(data)) {

            if (item.getId() == 8219)
                firmwareVersion = item.toInt();

            if (item.getId() == 8220)
                partNumber = (int) item.toInt();

            if (item.getId() == 8221)
                partsCount = (int) item.toInt();

            if (item.getId() == 8222)
                firmwareData = item.getData();
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
