/*
 * DefineParsingFormatNVRAM.java
 *
 * Created on 28 March 2008, 11:56
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
 * Define Parsing Format, Save in NVRAM ASCII: ESC w P d1 d2…dn CR Hexadecimal:
 * 1B 77 50 d1 d2…dn 0D
 **********************************************************************/

public final class DefineParsingFormatNVRAM extends NCR7167Command {

    /**
     * Creates a new instance of DefineParsingFormatNVRAM
     */
    public DefineParsingFormatNVRAM() {
    }

    public final NCR7167Command newInstance(NCR7167Command command) {
        return new DefineParsingFormatNVRAM();
    }

    public String getText() {
        return "Define parsing format";
    }

    public void execute(NCR7167Printer printer) throws Exception {
    }
}
