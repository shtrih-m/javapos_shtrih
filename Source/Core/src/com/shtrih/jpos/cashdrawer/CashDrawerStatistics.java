/*
 * CashDrawerStatistics.java
 *
 * Created on April 21 2008, 14:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.cashdrawer;

/**
 *
 * @author V.Kravtsov
 */

import jpos.JposStatisticsConst;

import com.shtrih.jpos.JposDeviceStatistics;
import com.shtrih.jpos.JposStatistics;

public class CashDrawerStatistics extends JposDeviceStatistics implements
        JposStatistics, JposStatisticsConst {
    /** Creates a new instance of CashDrawerStatistics */
    public CashDrawerStatistics() {
        defineStatistic(JPOS_STAT_DrawerGoodOpenCount);
        defineStatistic(JPOS_STAT_DrawerFailedOpenCount);
    }

    public void drawerOpenSucceeded() {
        incStatistic(JPOS_STAT_DrawerGoodOpenCount, 1);
    }

    public void drawerOpenFailed() {
        incStatistic(JPOS_STAT_DrawerFailedOpenCount, 1);
    }
}
