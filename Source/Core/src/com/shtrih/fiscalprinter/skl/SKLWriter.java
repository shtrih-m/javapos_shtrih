package com.shtrih.fiscalprinter.skl;

import com.shtrih.util.StringUtils;

import java.io.IOException;

public class SKLWriter {
    private final SKLStorage storage;
    private final int lineLength;

    public SKLWriter(SKLStorage storage, int lineLength) {

        this.storage = storage;
        this.lineLength = lineLength;
    }

    private void writeLn(String line) throws IOException {
        storage.writeLine(line);
    }
}