/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.util;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.util.CompositeLogger;

public class Logger2 {

    private Logger2() {
    }

    private static int min(int i1, int i2) {
        if (i1 < i2) {
            return i1;
        } else {
            return i2;
        }
    }

    public static void logTx(CompositeLogger logger, byte b) {
        byte[] data = new byte[1];
        data[0] = b;
        logTx(logger, data);
    }
    
    public static void logTx(CompositeLogger logger, byte[] data) {
        logData(logger, "-> ", data);
    }

    public static void logRx(CompositeLogger logger, byte b) {
        byte[] data = new byte[1];
        data[0] = b;
        logRx(logger, data);
    }
    
    public static void logRx(CompositeLogger logger, byte[] data) {
        logData(logger, "<- ", data);
    }

    public static void logData(CompositeLogger logger, String prefix, byte[] data) {
        int linelen = 20;
        int count = (data.length + linelen - 1) / linelen;
        for (int i = 0; i < count; i++) {
            int len = min(linelen, data.length - linelen * i);
            byte b[] = new byte[len];
            System.arraycopy(data, i * linelen, b, 0, len);
            logger.debug(prefix + (Hex.toHex(b, b.length)).toUpperCase());
        }
    }

    public static void logTimeout(CompositeLogger logger, int timeout) {
        logger.debug("setTimeout: " + timeout);
    }
}
