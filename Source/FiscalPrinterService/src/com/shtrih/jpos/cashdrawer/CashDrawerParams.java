/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.cashdrawer;

/**
 *
 * @author V.Kravtsov
 */
import jpos.config.JposEntry;

import com.shtrih.jpos.JposPropertyReader;
import com.shtrih.jpos.fiscalprinter.FptrParameters;

public class CashDrawerParams {

    public boolean capStatus = true;
    public int drawerNumber = 0;
    public String statisticFileName = "ShtrihCashDrawer.xml";
    public String fiscalPrinterDevice = "ShtrihFptr";

    public void load(JposEntry entry) throws Exception {
        if (entry != null) {
            JposPropertyReader reader = new JposPropertyReader(entry);

            capStatus = reader.readBoolean("capStatus", true);
            drawerNumber = reader.readInteger("drawerNumber", 0);
            statisticFileName = reader.readString("statisticFileName",
                    "ShtrihCashDrawer.xml");
            fiscalPrinterDevice = reader.readString("fiscalPrinterDevice",
                    "defaultFiscalPrinterDevice");
        }
    }
}
