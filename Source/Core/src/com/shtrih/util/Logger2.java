package com.shtrih.util;

/**
 * @author V.Kravtsov
 */
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
        logger.debug("-> " + Hex.toHex(b));
    }

    public static void logTx(CompositeLogger logger, byte[] data) {
        logData(logger, "->", data);
    }

    public static void logRx(CompositeLogger logger, byte b) {
        logger.debug("<- " + Hex.toHex(b));
    }

    public static void logRx(CompositeLogger logger, byte b1, byte b2) {
        logger.debug("<- " + Hex.toHex(b1) + " " + Hex.toHex(b2));
    }

    public static void logRx(CompositeLogger logger, byte[] data) {
        logData(logger, "<-", data);
    }

    private static void logData(CompositeLogger logger, String prefix, byte[] data) {
        final int lineLen = 20;
        int count = (data.length + lineLen - 1) / lineLen;
        for (int i = 0; i < count; i++) {
            StringBuilder sb = new StringBuilder();

            int len = min(lineLen, data.length - lineLen * i);

            sb.append(prefix);

            for (int j = 0; j < len; j++) {
                sb.append(" ");
                sb.append(Hex.toHex(data[j + i * lineLen]));
            }

            logger.debug(sb.toString());
        }
    }
}
