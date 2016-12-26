/*
 * FlexCommands.java
 *
 * Created on 19 Ноябрь 2009 г., 16:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */

import java.util.Vector;

public class FlexCommands {

    private final Vector list = new Vector();

    /** Creates a new instance of FlexCommands */
    public FlexCommands() {
    }

    public int size() {
        return list.size();
    }

    public FlexCommand get(int index) {
        return (FlexCommand) list.get(index);
    }

    public FlexCommand itemByCode(int code) {
        for (int i = 0; i < size(); i++) {
            FlexCommand command = get(i);
            if (command.getCode() == code) {
                return command;
            }
        }
        return null;
    }

    public void add(FlexCommand item) {
        list.add(item);
    }

    public void clear() {
        list.clear();
    }

}
