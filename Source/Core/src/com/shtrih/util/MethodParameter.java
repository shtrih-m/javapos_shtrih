/*
 * MethodParameter.java
 *
 * Created on 2 April 2008, 17:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.util;

/**
 *
 * @author V.Kravtsov
 */
import java.security.InvalidParameterException;

public final class MethodParameter {

    private MethodParameter() {
    }

    public static void checkRange(long parameterValue, long minValue,
            long maxValue, String parameterName) throws Exception {
        if (parameterValue < minValue) {
            throw new InvalidParameterException(parameterName
                    + ": invalid parameter value ("
                    + String.valueOf(parameterValue) + " < "
                    + String.valueOf(minValue) + ")");
        }
        if (parameterValue > maxValue) {
            throw new InvalidParameterException(parameterName
                    + ": invalid parameter value ("
                    + String.valueOf(parameterValue) + " > "
                    + String.valueOf(maxValue) + ")");
        }
    }

    public static void checkByte(long parameterValue, String parameterName)
            throws Exception {
        checkRange(parameterValue, 0, 255, parameterName);
    }
}
