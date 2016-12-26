///////////////////////////////////////////////////////////////////////////////
//
// This software is provided "AS IS".  The JavaPOS working group (including
// each of the Corporate members, contributors and individals)  MAKES NO
// REPRESENTATIONS OR WARRRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED 
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
// NON-INFRINGEMENT. The JavaPOS working group shall not be liable for
// any damages suffered as a result of using, modifying or distributing this
// software or its derivatives. Permission to use, copy, modify, and distribute
// the software and its documentation for any purpose is hereby granted. 
//
///////////////////////////////////////////////////////////////////////////////

package com.shtrih.jpos;

import java.lang.reflect.Constructor;

import jpos.JposConst;
import jpos.JposException;
import jpos.config.JposEntry;
import jpos.loader.JposServiceInstance;
import jpos.loader.JposServiceInstanceFactory;

import com.shtrih.util.CompositeLogger;

/**
 * Sample com.xxx company JposServiceInstanceFactory
 * 
 * @since 0.1 (Philly 99 meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public final class ShtrihJposServiceInstanceFactory extends Object implements
        JposServiceInstanceFactory, JposConst {
    private static CompositeLogger logger = CompositeLogger
            .getLogger(ShtrihJposServiceInstanceFactory.class);

    /**
     * Default ctor
     * 
     * @since 0.1 (Philly 99 meeting)
     */
    public ShtrihJposServiceInstanceFactory() {
    }

    /**
     * Simply creates an instance of a service. NOTE: future version will use
     * reflection to create the service instance NOTE2: modified to show how to
     * pass the JposEntry object
     * 
     * @param logicalName
     *            the logical name for this entry
     * @param entry
     *            the JposEntry with properties for creating the service
     * @since 0.1 (Philly 99 meeting)
     * @exception jpos.JposException
     *                in case the factory cannot create service or service
     *                throws exception
     */
    
    public JposServiceInstance createInstance(String logicalName,
            JposEntry entry) throws JposException {
        if (!entry.hasPropertyWithName(JposEntry.SERVICE_CLASS_PROP_NAME)) {
            throw new JposException(JPOS_E_NOSERVICE,
                    "The JposEntry does not contain the 'serviceClass' property");
        }

        JposServiceInstance serviceInstance = null;

        try {
            String serviceClassName = (String) entry
                    .getPropertyValue(JposEntry.SERVICE_CLASS_PROP_NAME);
            Class serviceClass = Class.forName(serviceClassName);
            Class[] params = new Class[0];
            Constructor ctor = serviceClass.getConstructor(params);
            serviceInstance = (JposServiceInstance) ctor.newInstance(params);

            // Since we know that this is a subclass of
            // com.shtrih.jpos.DeviceService
            // let's cast to that class and pass the JposEntry object for
            // further
            // intialization by the service. This is how RS232 properties would
            // be typically initialized
            DeviceService ds = (DeviceService) serviceInstance;

            ds.setJposEntry(entry);
        } catch (Exception e) {
            logger.fatal("Could not create the service instance!", e);
            throw new JposException(JPOS_E_NOSERVICE,
                    "Could not create the service instance!", e);
        }
        return serviceInstance;
    }
}
