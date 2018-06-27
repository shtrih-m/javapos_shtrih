package com.shtrih.fiscalprinter.scoc.commands;

import com.shtrih.fiscalprinter.TLVWriter;

public class DeviceFirmwareCommand {

    private final long dataTime;
    private final long version;
    private final int partNumber;

    public DeviceFirmwareCommand(long dataTime, long version, int partNumber) {
        this.dataTime = dataTime;
        this.version = version;
        this.partNumber = partNumber;
    }

    public byte[] toBytes() {

        return new TLVWriter()
                .add(8003, new TLVWriter()
                        .add(7000, dataTime, 4)
                        .add(8219, version, 4)
                        .add(8220, partNumber, 1)
                        .getBytes())
                .getBytes();
    }
}
