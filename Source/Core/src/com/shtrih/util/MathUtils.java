/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.util;

/**
 * @author V.Kravtsov
 */
public class MathUtils {

    private MathUtils() {
    }

    public static int min(int i1, int i2) {
        if (i1 < i2) {
            return i1;
        } else {
            return i2;
        }
    }

    public static long round(double amount) {
        if (amount == 0) {
            return 0;
        }
        if (amount > 0) {
            return (long) (amount + 0.5);
        } else {
            return (long) (amount - 0.5);
        }
    }

}
