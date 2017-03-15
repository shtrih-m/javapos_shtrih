/*
 * SharedSerialPorts.java
 *
 * Created on May 22 2008, 21:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.port;

/**
 *
 * @author V.Kravtsov
 */
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.util.Vector;

import com.shtrih.util.CompositeLogger;

public class SharedSerialPorts {

    private Vector ports = new Vector();
    private static SharedSerialPorts instance;
    static CompositeLogger logger = CompositeLogger.getLogger(SharedSerialPorts.class);

    /** A private Constructor prevents any other class from instantiating. */
    private SharedSerialPorts() {
    }

    public static synchronized SharedSerialPorts getInstance() {
        if (instance == null) {
            instance = new SharedSerialPorts();
        }
        return instance;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public synchronized SharedSerialPort findItem(String portName) {
        for (int i = 0; i < ports.size(); i++) {
            SharedSerialPort port = (SharedSerialPort) ports.get(i);
            if (portName.equalsIgnoreCase(port.getPortName())) {
                return port;
            }
        }
        return null;
    }

    public synchronized SharedSerialPort findItem(SerialPort serialPort) {
        for (int i = 0; i < ports.size(); i++) {
            SharedSerialPort port = (SharedSerialPort) ports.get(i);
            if (port.getSerialPort().equals(serialPort)) {
                return port;
            }
        }
        return null;
    }

    public synchronized SharedSerialPort findItem(String portName, Object owner) {
        for (int i = 0; i < ports.size(); i++) {
            SharedSerialPort port = (SharedSerialPort) ports.get(i);
            if ((portName.equalsIgnoreCase(port.getPortName()) && (port
                    .getOwner().equals(owner)))) {
                return port;
            }
        }
        return null;
    }

    private synchronized SharedSerialPort createPort(SerialPort serialPort,
            String portName, Object owner) {
        SharedSerialPort port = new SharedSerialPort(serialPort, portName,
                owner);
        ports.add(port);
        return port;
    }

    private synchronized SharedSerialPort newPort(String portName, int timeout,
            Object owner) throws Exception {
        CommPortIdentifier portId = CommPortIdentifier
                .getPortIdentifier(portName);
        if (portId == null) {
            throw new Exception("Port does not exist, " + portName);
        }
        SerialPort serialPort = (SerialPort) portId.open(owner.getClass()
                .getName(), timeout);
        if (serialPort == null) {
            throw new Exception("Failed to open port " + portName);
        }
        return createPort(serialPort, portName, owner);
    }

    public synchronized SerialPort openPort(String portName, int timeout,
            Object owner) throws Exception {
        SharedSerialPort item = null;
        item = findItem(portName, owner);
        if (item == null) {
            item = findItem(portName);
            if (item != null) {
                if (!item.getOwner().equals(owner)) {
                    if (owner.getClass().equals(item.getOwner())) {
                        item = newPort(portName, timeout, owner);
                    } else {
                        item = createPort(item.getSerialPort(), portName, owner);
                    }
                }
            } else {
                item = newPort(portName, timeout, owner);
            }
        }
        return item.getSerialPort();
    }

    public synchronized void closePort(SerialPort port) {
        if (port != null) {
            SharedSerialPort sharedPort = findItem(port);
            ports.remove(sharedPort);
            if (findItem(port) == null) {
                port.close();
            }
        }
    }
}
