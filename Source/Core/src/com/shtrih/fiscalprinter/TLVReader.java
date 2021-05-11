package com.shtrih.fiscalprinter;

import com.shtrih.fiscalprinter.command.CommandInputStream;
import java.util.List;
import java.util.ArrayList;


public class TLVReader {

    public TLVReader() {
    }

    public List<TLVItem> read(byte[] data) throws Exception {
        List<TLVItem> items = new ArrayList<TLVItem>();
        parse(items, data);
        return items;
    }

    private void parse(List<TLVItem> items, byte[] data) throws Exception {
        TLVTags tags = TLVTags.getInstance();
        CommandInputStream stream = new CommandInputStream("");
        stream.setData(data);

        while (stream.size() >= 4) {
            int tagId = stream.readShort();
            int len = stream.readShort();
            byte[] adata = stream.readBytes(len);

            TLVTag tag = tags.find(tagId);
            TLVItem item = new TLVItem(tagId, adata, tag);
            items.add(item);
            if (tag != null) {
                if (tag.getType() == TLVTag.TLVType.itSTLV) {
                    parse(item.getItems(), adata);
                }
            }
        }
    }
}
