package com.shtrih.fiscalprinter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CashCoreInstaller {

    public CashCoreInstaller(Object appContext) {
            
    }

    public void install(byte[] firmware) throws IOException {
        throw new UnsupportedOperationException("CashCore installation only supported on Android");
    }
}
