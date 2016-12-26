package com.shtrih.jpos;

import jpos.JposException;
import jpos.config.JposEntry;

public class DeviceService {

    // --------------------------------------------------------------------------
    // Constants
    // --------------------------------------------------------------------------

    protected static final int deviceVersion12 = 1002000; // 1.2.0
    protected static final int deviceVersion13 = 1003000; // 1.3.0
    protected static final int deviceVersion14 = 1004000; // 1.4.0
    protected static final int deviceVersion15 = 1005000; // 1.5.0
    protected static final int deviceVersion16 = 1006000; // 1.6.0
    protected static final int deviceVersion17 = 1007000; // 1.7.0
    protected static final int deviceVersion18 = 1008000; // 1.8.0
    protected static final int deviceVersion19 = 1009000; // 1.9.0
    protected static final int deviceVersion110 = 1010000; // 1.10.0
    protected static final int deviceVersion111 = 1011000; // 1.11.0
    protected static final int deviceVersion112 = 1012000; // 1.12.0
    protected static final int deviceVersion113 = 1013000; // 1.13.0
    protected static final int deviceVersion114 = 1014000; // 1.14.0

    /** Default ctor */
    public DeviceService() {
    }

    // --------------------------------------------------------------------------
    // Package methods
    //

    /**
     * Allows the JposServiceInstanceFactory to set the JposEntry associated
     * with this DeviceService. Subclasses can access the JposEntry with getter
     * 
     * @since 1.2 (SF2K meeting)
     */
    public void setJposEntry(JposEntry entry) {
        jposEntry = entry;

        // Of course after setting one can do further intialization here...
    }

    /**
     * @return the JposEntry object associated with this DeviceService
     * @since 1.2 (SF2K meeting)
     */
    JposEntry getJposEntry() {
        return jposEntry;
    }

    // --------------------------------------------------------------------------
    // Instance variables
    //
    protected JposEntry jposEntry = null;

    public void deleteInstance() throws JposException {
    }
}
