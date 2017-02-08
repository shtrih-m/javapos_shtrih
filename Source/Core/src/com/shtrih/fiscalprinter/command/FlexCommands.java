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
 * @author V.Kravtsov
 */

import java.util.HashMap;

public class FlexCommands {

    private final HashMap<Integer, FlexCommand> list = new HashMap<Integer, FlexCommand>();

    /** Creates a new instance of FlexCommands */
    public FlexCommands() {
    }

    public int size() {
        return list.size();
    }

    public FlexCommand itemByCode(int code) {
        return list.get(code);
    }

    public void add(FlexCommand item) {
        list.put(item.getCode(), item);
    }

    public void clear() {
        list.clear();
    }

}
