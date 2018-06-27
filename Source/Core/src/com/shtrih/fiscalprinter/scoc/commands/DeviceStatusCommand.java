package com.shtrih.fiscalprinter.scoc.commands;

import com.shtrih.fiscalprinter.TLVWriter;

public class DeviceStatusCommand {
    
    private final long documentNumber;
    private final long dataTime;
    private final long version;

    public DeviceStatusCommand(long documentNumber, long dataTime, long version) {
        this.documentNumber = documentNumber;
        this.dataTime = dataTime;
        this.version = version;
    }

    public byte[] toBytes() {

        return new TLVWriter()
                .add(8007, new TLVWriter()
                        .add(8200, documentNumber, 4)
                        .add(7000, dataTime, 4)
                        .add(8223, new TLVWriter()
                                .add(8219, version, 4)
                                .getBytes())
                        .getBytes())
                .getBytes();
    }
}

