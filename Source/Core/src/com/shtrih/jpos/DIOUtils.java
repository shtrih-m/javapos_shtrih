/*
 * DIOUtils.java
 *
 * Created on 13 Май 2010 г., 17:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos;

/**
 *
 * @author V.Kravtsov
 */

import jpos.JposConst;
import jpos.JposException;

import com.shtrih.util.Localizer;

public class DIOUtils {

    /** Creates a new instance of DIOUtils */
    private DIOUtils() {
    }

    public static void checkDataNotNull(int[] data) throws JposException {
        if (data == null) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.NullDataParameter));
        }
    }

    public static void checkObjectNotNull(Object object) throws JposException {
        if (object == null) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.NullObjectParameter));
        }
    }

    public static void checkDataMinLength(int[] data, int minLength)
            throws JposException {
        checkDataNotNull(data);
        if (data.length < minLength) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.InsufficientDataLen)
                            + String.valueOf(minLength));
        }
    }

    public static void checkObjectMinLength(String[] object, int minLength)
            throws JposException {
        checkObjectNotNull(object);
        if (object.length < minLength) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.InsufficientObjectLen)
                            + String.valueOf(minLength));
        }
    }

}
