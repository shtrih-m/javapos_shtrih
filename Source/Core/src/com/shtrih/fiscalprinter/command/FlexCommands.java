package com.shtrih.fiscalprinter.command;


import java.util.Collection;
import java.util.HashMap;

/**
 * @author V.Kravtsov
 */
public class FlexCommands {

    private final HashMap<Integer, FlexCommand> list = new HashMap<Integer, FlexCommand>();

    /** Creates a new instance of FlexCommands */
    public FlexCommands() {
    }

    public Collection<FlexCommand> list(){
        return list.values();
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
