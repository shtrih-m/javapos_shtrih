/*
 * CommandParams.java
 *
 * Created on 19 Ноябрь 2009 г., 16:39
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

import com.shtrih.util.CompositeLogger;

public class CommandParams {

    static CompositeLogger logger = CompositeLogger.getLogger(CommandParams.class);
    private final Vector list = new Vector();

    /** Creates a new instance of CommandParams */
    public CommandParams() {
    }

    public int size() {
        return list.size();
    }

    public CommandParam get(int index) {
        return (CommandParam) list.get(index);
    }

    public void add(CommandParam item) {
        list.add(item);
    }

    public void encode(CommandOutputStream out) throws Exception {
        for (int i = 0; i < size(); i++) {
            CommandParam param = get(i);
            param.encode(out);

        }
    }

    public void decode(CommandInputStream in) throws Exception {
        for (int i = 0; i < size(); i++) {
            CommandParam param = get(i);
            param.decode(in);
        }
    }

}
