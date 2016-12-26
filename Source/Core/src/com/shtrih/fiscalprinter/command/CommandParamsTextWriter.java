/*
 * CommandParamsTextWriter.java
 *
 * Created on 14 Май 2010 г., 12:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 * @author V.Kravtsov
 */
public class CommandParamsTextWriter {

    public static String DefaultParameterSeparator = ";";

    /** Creates a new instance of CommandParamsTextWriter */
    private CommandParamsTextWriter() {
    }

    public static String write(CommandParams items) {
        String result = "";
        for (int i = 0; i < items.size(); i++) {
            result += items.get(i).getValue() + DefaultParameterSeparator;
        }
        return result;
    }

}
