package com.shtrih.fiscalprinter.skl;

import java.io.IOException;

public interface SKLStorage {
    void writeLine(String line) throws IOException;
}
