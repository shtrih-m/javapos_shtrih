/*
 * DefineParsingFormat.java
 *
 * Created on 28 March 2008, 11:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.printer.ncr7167;

/**
 *
 * @author V.Kravtsov
 */

/*********************************************************************
 * Define Parsing Format, Do Not Save Permanently ASCII: ESC w p d1 d2…dn CR
 * Hexadecimal: 1B 77 70 d1 d2…dn CR
 **********************************************************************/

public final class DefineParsingFormat extends NCR7167Command {

    /**
     * Creates a new instance of DefineParsingFormat
     */
    public DefineParsingFormat() {
    }

    public String getText() {
        return "Define parsing format";
    }

    public final NCR7167Command newInstance(NCR7167Command command) {
        return new DefineParsingFormat();
    }

    public void execute(NCR7167Printer printer) throws Exception {
    }
}
