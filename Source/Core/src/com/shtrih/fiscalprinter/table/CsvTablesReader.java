/*
 * CsvTablesReader.java
 *
 * Created on 17 November 2009, 13:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.table;

/**
 *
 * @author V.Kravtsov
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import com.shtrih.util.CompositeLogger;
import com.shtrih.fiscalprinter.command.FieldInfo;

public class CsvTablesReader {

    static CompositeLogger logger = CompositeLogger.getLogger(CsvTablesReader.class);

    /**
     * Creates a new instance of CsvTablesReader
     */
    public CsvTablesReader() {
    }

    /**
     * /// Java POS driver /// Current date: 2009/11/18 19:49:22 // Table 1,
     * ... // Table number,row,field,Field size,Min,Max,Name,Value
     * 1,1,1,1,0,1,99,STORE NUMBER,"1"
     *
     */
    public boolean isComment(String line) {
        return line.startsWith("//");
    }

    public String getParamStr(String line, int index) {
        String result = "";
        StringTokenizer tokenizer = new StringTokenizer(line, ",");
        for (int i = 0; i <= index; i++) {
            if (!tokenizer.hasMoreTokens()) {
                break;
            }
            String token = tokenizer.nextToken();
            if (i == index) {
                result = token;
            }
        }
        result = result.replaceAll("\'", "");
        result = result.replaceAll("\"", "");
        return result;
    }

    public int getParamInt(String line, int index) {
        return Integer.parseInt(getParamStr(line, index));
    }

    public void load(String fileName, PrinterFields fields) throws Exception {
        logger.debug("load(" + fileName + ")");

        fields.clear();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), "UTF8"));
        if (reader.ready()) {
            String line = reader.readLine();
            if (!line.contains("///")) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-16LE"));
            }
        }

        try {
            while (reader.ready()) {
                String line = reader.readLine();
                if (line != null) {
                    try {
                        loadLine(line, fields);
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
            }
            logger.debug("load: OK. Fields number: "
                    + String.valueOf(fields.size()));
        } finally {
            reader.close();
        }
    }

    public void loadLine(String line, PrinterFields fields) throws Exception {
        if (isComment(line)) {
            line = line.toUpperCase();
            if (fields.getModelName().isEmpty())
            {
                String modelName = getModelName(line);
                if (!modelName.isEmpty()){
                    fields.setModelName(modelName);
                }
            }
        } else {
            int table = getParamInt(line, 0);
            int row = getParamInt(line, 1);
            int number = getParamInt(line, 2);
            int size = getParamInt(line, 3);
            int type = getParamInt(line, 4);
            int min = getParamInt(line, 5);
            int max = getParamInt(line, 6);
            String name = getParamStr(line, 7);
            String fieldValue = getParamStr(line, 8);

            FieldInfo fieldInfo = new FieldInfo(table, number, size, type, min, max, name);
            PrinterField field = new PrinterField(fieldInfo, row);
            field.setValue(fieldValue);
            fields.add(field);
        }
    }
    
    /// Модель: ЯРУС-01К; №00001000
    public static String getModelName(String line)
    {
        line = line.toUpperCase();
        int index = line.indexOf("МОДЕЛЬ:");
        if (index != -1)
        {
            return line.substring(index + 7).trim();
        }
        index = line.indexOf("MODEL:");
        if (index != -1){
            return line.substring(index + 6).trim();
        }
        return "";
    }

}
