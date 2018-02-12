package com.shtrih.fiscalprinter.skl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileSKLStorage implements SKLStorage {

    private final String filePath;

    public FileSKLStorage(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void writeLine(String line) throws IOException {
        File file = new File(filePath);
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        try {
            writer.println(line);
            writer.flush();
        } finally {
            writer.close();
        }
    }
}
