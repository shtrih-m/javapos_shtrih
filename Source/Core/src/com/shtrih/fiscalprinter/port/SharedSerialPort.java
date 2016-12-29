/*
 * SharedSerialPort.java
 *
 * Created on May 22 2008, 21:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.port;

/**
 *
 * @author V.Kravtsov
 */
import gnu.io.SerialPort;

import com.shtrih.util.CompositeLogger;

public class SharedSerialPort {

    private final Object owner;
    private final String portName;
    private final SerialPort port;
    static CompositeLogger logger = CompositeLogger.getLogger(SharedSerialPort.class);

    public SharedSerialPort(SerialPort port, String portName, Object owner) {
        logger.debug("SharedSerialPort(" + portName + ")");
        this.port = port;
        this.owner = owner;
        this.portName = portName;
    }

    public SerialPort getSerialPort() {
        return port;
    }

    public String getPortName() {
        return portName;
    }

    public Object getOwner() {
        return owner;
    }
}
