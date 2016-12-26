/*
 * CommandParamsTextReader.java
 *
 * Created on 14 Май 2010 г., 12:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */

import java.util.StringTokenizer;

public class CommandParamsTextReader {

    public static String DefaultParameterSeparator = ";";

    /**
     * Creates a new instance of CommandParamsTextReader
     */
    private CommandParamsTextReader() {
    }

    public static void read(String text, CommandParams items) {
        StringTokenizer tokenizer = new StringTokenizer(text,
                DefaultParameterSeparator);
        int count = tokenizer.countTokens();
        for (int i = 0; i < count; i++) {
            if (i >= items.size()) {
                break;
            }
            String token = tokenizer.nextToken();
            items.get(i).setValue(token);
        }
    }
}
