/*
 * CsvTablesWriter.java
 *
 * Created on 17 November 2009, 17:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.table;

/**
 *
 * @author V.Kravtsov
 */

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shtrih.util.CompositeLogger;

public class CsvTablesWriter {

    static CompositeLogger logger = CompositeLogger.getLogger(CsvTablesWriter.class);

    /**
     * Creates a new instance of CsvTablesWriter
     */
    public CsvTablesWriter() {
    }

    /**
     * /// Java POS driver /// Current date: 2009/11/18 15:30:40 // Table 1, ..
     * // Table number,row,field,Field size,Min,Max,Name,Value
     * 1,1,1,1,0,1,99,Field name,"1"
     **/

    public void save(String fileName, PrinterTables tables) throws IOException {
        logger.debug("save");

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileName), "UTF8"));
        try {
            writer.write("/// Java POS driver");
            writer.newLine();

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            writer.write("/// Current date: " + dateFormat.format(date));
            writer.newLine();

            for (int i = 0; i < tables.size(); i++) {
                PrinterTable table = tables.get(i);
                PrinterFields fields = table.getFields();

                writer.write("// Table " + String.valueOf(table.getNumber())
                        + ", " + table.getName());
                writer.newLine();

                writer.write("// Table number,row,field,Field size,Min,Max,Name,Value");
                writer.newLine();

                for (int j = 0; j < fields.size(); j++) {
                    PrinterField field = fields.get(j);
                    String line = String.valueOf(field.getTable()) + ","
                            + String.valueOf(field.getRow()) + ","
                            + String.valueOf(field.getField()) + ","
                            + String.valueOf(field.getSize()) + ","
                            + String.valueOf(field.getType()) + ","
                            + String.valueOf(field.getMin()) + ","
                            + String.valueOf(field.getMax()) + ","
                            + field.getName() + "," + "\"" + field.getValue()
                            + "\"";

                    logger.debug(line);

                    writer.write(line);
                    writer.newLine();
                }
            }
            writer.flush();
        } finally {
            writer.close();
        }
    }
}
