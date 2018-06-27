package com.shtrih.fiscalprinter;

import com.shtrih.fiscalprinter.command.CommandInputStream;

import java.util.ArrayList;
import java.util.List;

public class TLVReader {
    public List<TLVRecord> read(byte[] tlv) throws Exception {
        CommandInputStream reader = new CommandInputStream(tlv);

        List<TLVRecord> tags = new ArrayList<TLVRecord>();

        while (reader.getSize() > 4) {
            int tagId = (int) reader.readLong(2);
            int len = (int) reader.readLong(2);
            byte[] data = reader.readBytes(len);

            tags.add(new TLVRecord(tagId, data));
        }

        return tags;
    }
}
