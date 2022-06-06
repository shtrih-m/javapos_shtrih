package com.shtrih.jpos;

import jpos.JposConst;
import jpos.JposException;
import jpos.config.JposEntry;

import com.shtrih.util.CompositeLogger;

public class JposPropertyReader implements JposConst {
    
    private static final CompositeLogger logger = CompositeLogger.getLogger(JposPropertyReader.class);

    private JposEntry jposEntry;

    public JposPropertyReader(JposEntry jposEntry) {
        this.jposEntry = jposEntry;
    }

    
    public String readString(String propertyName, String defaultValue)
            {
        String result;
        if (propertyExists(propertyName)) {
            result = (String) jposEntry.getPropertyValue(propertyName);
        } else {
            result = defaultValue;
        }
        logger.debug(propertyName + ": \"" + result + "\"");
        return result;
    }

    public int readInteger(String propertyName, int defaultValue)
            throws Exception {
        int result = defaultValue;
        if (propertyExists(propertyName)) {
            try {
                result = Integer.parseInt((String) jposEntry
                        .getPropertyValue(propertyName));
            } catch (Exception e) {
                logger.error(e);
                throw new JposException(JPOS_E_FAILURE, e.getMessage(), e);
            }
        }
        logger.debug(propertyName + ": \"" + String.valueOf(result) + "\"");
        return result;
    }

    public double readDouble(String propertyName, double defaultValue)
            throws Exception {
        double result = defaultValue;
        if (propertyExists(propertyName)) {
            try {
                result = Double.parseDouble((String) jposEntry
                        .getPropertyValue(propertyName));
            } catch (Exception e) {
                logger.error(e);
                throw new JposException(JPOS_E_FAILURE, e.getMessage());
            }
        }
        logger.debug(propertyName + ": \"" + String.valueOf(result) + "\"");
        return result;
    }

    public boolean readBoolean(String propertyName, boolean defaultValue)
            throws Exception {
        boolean result = defaultValue;
        if (propertyExists(propertyName)) {
            try {
                result = ((String) jposEntry.getPropertyValue(propertyName))
                        .equalsIgnoreCase("1");
            } catch (Exception e) {
                logger.error(e);
                throw new JposException(JPOS_E_FAILURE, e.getMessage());
            }
        }
        logger.debug(propertyName + ": \"" + String.valueOf(result) + "\"");
        return result;
    }

    public boolean propertyExists(String propertyName) {
        return jposEntry.hasPropertyWithName(propertyName);
    }
}
