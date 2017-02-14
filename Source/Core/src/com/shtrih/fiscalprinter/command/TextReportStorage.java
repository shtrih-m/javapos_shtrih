package com.shtrih.fiscalprinter.command;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TextReportStorage {
    private final String filePath;

    public TextReportStorage(String filePath) {

        this.filePath = filePath;
    }

    public List<String> searchZReport(int dayNumber) throws Exception {
        List<String> lines = loadLines();
        int index1 = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            int dayNum = getDayNumber(line);
            if (dayNum == -1)
                continue;

            if (dayNum == dayNumber) {
                int index2 = findNextDocument(lines, i + 1);
                if (index2 != -1)
                    index2 -= 1;

                return copyLines(lines, index1, index2);
            } else {
                index1 = findNextDocument(lines, i + 1);
            }
        }

        return null;
    }

    private List<String> loadLines() throws Exception {
        List<String> result = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
        } finally {
            reader.close();
        }
        return result;
    }

    private int getDayNumber(String line) throws Exception {
        if (line.contains(TextDocumentFilter.SZReportText)) {
            int startIndex = line.length() - 4;
            String number = line.substring(startIndex, startIndex + 4);
            return Integer.parseInt(number);
        }
        return -1;
    }

    private int findNextDocument(List<String> lines, int index) {
        for (int i = index; i < lines.size(); i++) {
            String line = lines.get(i);
            if (isDocumentHeader(line)) {
                return i;
            }
        }
        return lines.size();
    }

    private boolean isDocumentHeader(String line) {
        return line.contains(TextDocumentFilter.SINN);
    }

    private List<String> copyLines(List<String> lines, int index1, int index2) {
        List<String> result = new ArrayList<String>();
        for (int i = index1; i <= index2; i++) {
            if (i < 0)
                return result;
            if (i >= lines.size())
                return result;
            result.add(lines.get(i));
        }
        return result;
    }

    public List<String> getDocument(int docNumber) throws Exception {
        int index1 = -1;
        int index2 = -1;
        List<String> lines = loadLines();

        for (int i = lines.size() - 1; i >= 0; i--) {
            String line = lines.get(i);
            int docNum = getDocumentNumber(line);

            if (docNum == -1)
                continue;

            if (docNum == docNumber) {
                if (index2 == -1)
                    index2 = findNextDocument(lines, i + 1);

                if (index2 != -1)
                    index2 -= 1;

                index1 = i;
                break;
            }
        }
        if (index1 == -1) {
            return null;
        }

        return copyLines(lines, index1, index2);
    }

    private int getDocumentNumber(String line) throws Exception {
        if (line.contains(TextDocumentFilter.SINN) && line.contains("№")) {
            int startIndex = line.indexOf("№") + 1;
            String number = line.substring(startIndex, startIndex + 4);
            return Integer.parseInt(number);
        }
        return -1;
    }

    public List<String> getCurrentDayReport() throws Exception {
        List<String> lines = loadLines();
        int index1 = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            int dayNum = getDayNumber(line);
            if (dayNum == -1)
                continue;

            index1 = findNextDocument(lines, i + 1);
        }

        if(index1 == -1 || index1 == lines.size())
            return null;

        return copyLines(lines, index1, lines.size());
    }
}
