/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.ej;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.util.StringUtils;

public class EJTime {

    private final int hour;
    private final int min;

    public EJTime(int hour, int min) {
        this.hour = hour;
        this.min = min;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public String toString() {
        return StringUtils.intToStr(hour, 2) + ":"
                + StringUtils.intToStr(min, 2);
    }

}
