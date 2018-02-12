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

    public void add(String s1, String s2) throws Exception {
        if (s1.length() > lineLength && s2.length() < lineLength && lineLength - (s1.length() % lineLength) - s2.length() > 0) {

            int len = lineLength - (s1.length() % lineLength) - s2.length();
            String line = s1 + StringUtils.stringOfChar(' ', len) + s2;
            add(line);
            return;
        }

        if (s1.length() + s2.length() + 1 > lineLength) {
            addPaddedLeft(s1);
            addPaddedRight(s2);

            return;
        }

        int len = lineLength - s1.length() - s2.length();
        String line = s1 + StringUtils.stringOfChar(' ', len) + s2;
        writeLn(line);
    }

    private void addPaddedLeft(String s1) throws Exception {
        if (s1.length() > lineLength) {
            add(s1);
            return;
        }

        if (s1.length() == lineLength) {
            writeLn(s1);
            return;
        }

        int len = lineLength - s1.length();
        String line = s1 + StringUtils.stringOfChar(' ', len);
        writeLn(line);
    }

    private void addPaddedRight(String s2) throws Exception {
        if (s2.length() > lineLength) {
            add(s2);
            return;
        }

        if (s2.length() == lineLength) {
            writeLn(s2);
            return;
        }

        int len = lineLength - s2.length();
        String line = StringUtils.stringOfChar(' ', len) + s2;
        writeLn(line);
    }

    public void addCenter(char c, String text) throws Exception {
        int l = (lineLength - text.length()) / 2;
        String line = StringUtils.stringOfChar(c, l) + text;
        line = line + StringUtils.stringOfChar(c, lineLength - line.length());
        writeLn(line);
    }

    public void add(String line) throws Exception {

        if (line.length() <= lineLength) {
            addPaddedLeft(line);
            return;
        }

        int lineNumber = 0;

        while (true) {
            int beginIndex = lineNumber * lineLength;
            if (line.length() <= beginIndex) {
                break;
            }

            int endIndex = (lineNumber + 1) * lineLength;

            if (endIndex > line.length()) {
                endIndex = line.length();
            }

            String substring = line.substring(beginIndex, endIndex);

            addPaddedLeft(substring);

            lineNumber++;
        }
    }

    private void writeLn(String line) throws IOException {
        storage.writeLine(line);
    }
}