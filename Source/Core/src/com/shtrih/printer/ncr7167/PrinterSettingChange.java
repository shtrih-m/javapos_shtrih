/*
 * PrinterSettingChange.java
 *
 * Created on 28 March 2008, 12:46
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
 * Printer Setting Change ASCII: USDC1[m n], [m n], …[m n] 0FFH Hexadecimal: 1F
 * 11 [m n], [m n], … [m n] 0FFH
 **********************************************************************/

public class PrinterSettingChange extends NCR7167Command {

    /**
     * Creates a new instance of PrinterSettingChange
     */
    public PrinterSettingChange() {
    }

    public String getText() {
        return "Printer setting change";
    }

    public final NCR7167Command newInstance(NCR7167Command command) {
        return new PrinterSettingChange();
    }

    public void execute(NCR7167Printer printer) throws Exception {
    }
}
