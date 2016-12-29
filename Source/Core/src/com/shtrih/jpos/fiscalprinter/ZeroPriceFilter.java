/*
 * ZeroPriceFilter.java
 *
 * Created on 13 Январь 2011 г., 16:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.shtrih.util.CompositeLogger;

public class ZeroPriceFilter extends FiscalPrinterFilter {

    private final Date time1;
    private final Date time2;
    private final boolean enabled;
    private final String errorText;
    private static CompositeLogger logger = CompositeLogger.getLogger(ZeroPriceFilter.class);

    /** Creates a new instance of ZeroPriceFilter */
    public ZeroPriceFilter(boolean enabled, Date time1, Date time2,
            String errorText) {
        this.enabled = enabled;
        this.time1 = time1;
        this.time2 = time2;
        this.errorText = errorText;
    }

    public static Calendar getTime(Date time) {
        Calendar result = Calendar.getInstance();
        Calendar temp = Calendar.getInstance();

        temp.setTime(time);
        result.set(Calendar.HOUR, temp.get(Calendar.HOUR));
        result.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
        result.set(Calendar.SECOND, 0);
        result.set(Calendar.MILLISECOND, 0);
        return result;
    }

    public void checkTime() throws Exception {
        if (enabled) {
            Calendar time = Calendar.getInstance();
            Calendar sTime = getTime(time1);
            Calendar eTime = getTime(time2);

            boolean timeInRange = false;
            if (sTime.before(eTime)) {
                timeInRange = time.after(sTime) && time.before(eTime);
            } else {
                timeInRange = time.after(sTime) || time.before(eTime);
            }

            if (timeInRange) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "dd.MM.yyyy HH:mm:ss.SSS");
                logger.debug("Time: " + dateFormat.format(time.getTime()));
                logger.debug("Time1: " + dateFormat.format(sTime.getTime()));
                logger.debug("Time2: " + dateFormat.format(eTime.getTime()));

                throw new Exception(errorText);
            }
        }
    }

    
    public void printRecItem(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        if (price == 0) {
            checkTime();
        }
    }

    public void printRecItemVoid(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        if (price == 0) {
            checkTime();
        }
    }

    
    public void printRecItemFuel(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName, long specialTax,
            String specialTaxName) throws Exception {
        if (price == 0) {
            checkTime();
        }
    }

    
    public void printRecItemFuelVoid(String description, long price,
            int vatInfo, long specialTax) throws Exception {
        if (price == 0) {
            checkTime();
        }
    }
}
