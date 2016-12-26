/*
 * NCR7167Command.java
 *
 * Created on 21 March 2008, 7:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.printer.ncr7167;

/**
 * @author V.Kravtsov
 */

public abstract class NCR7167Command {

    public abstract NCR7167Command newInstance(NCR7167Command command);

    public int getId() {
        return 0;
    }

    public String getText() {
        return "";
    }

    public void execute(NCR7167Printer printer)
            throws Exception {
    }
}
