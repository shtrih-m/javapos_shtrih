package com.shtrih.fiscalprinter;

import com.shtrih.fiscalprinter.command.CommandInputStream;
import java.util.List;
import java.util.ArrayList;

public class TLVReader {

    public TLVReader() {
    }

    public TLVItems read(byte[] data) throws Exception {
        TLVItems items = new TLVItems();
        parse(items, data);
        return items;
    }

    private void parse(TLVItems items, byte[] data) throws Exception {
        TLVTags tags = TLVTags.getInstance();
        CommandInputStream stream = new CommandInputStream("");
        stream.setData(data);

        while (stream.size() >= 4) {
            int tagId = stream.readShort();
            int len = stream.readShort();
            byte[] adata = stream.readBytes(len);

            TLVTag tag = tags.find(tagId);
            TLVItem item = new TLVItem(tagId, tag);
            items.add(item);
            if (item.isSTLV()) {
                parse(item.getItems(), adata);
            } else {
                item.setData(adata);
            }
        }
    }
}
