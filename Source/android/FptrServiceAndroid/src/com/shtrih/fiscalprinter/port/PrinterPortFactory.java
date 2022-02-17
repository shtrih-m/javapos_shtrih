/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.port;

/**
 *
 * @author V.Kravtsov
 */

import java.lang.reflect.Constructor;
import com.shtrih.util.CompositeLogger;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;

public class PrinterPortFactory {

    private static CompositeLogger logger = CompositeLogger.getLogger(PrinterPortFactory.class);

    private PrinterPortFactory() {
    }

    public static PrinterPort createInstance(FptrParameters params)
            throws Exception {
        PrinterPort port = createPort(params);
        return new CancellablePrinterPort(port, params);
    }

    public static PrinterPort createPort(FptrParameters params) throws Exception
    {
        switch (params.getPortType()) {
            case SmFptrConst.PORT_TYPE_SERIAL:
                return new SerialPrinterPort(params.portName);

            case SmFptrConst.PORT_TYPE_SOCKET:
                return SocketPort.getInstance(params.portName, params.getByteTimeout());

            case SmFptrConst.PORT_TYPE_BLUETOOTH:
                return new BluetoothPort();

            case SmFptrConst.PORT_TYPE_BLUETOOTH_LE:
                return new BluetoothLEPort();

            case SmFptrConst.PORT_TYPE_PPP:
                return new PPPPort(params);

            case SmFptrConst.PORT_TYPE_FROMCLASS:
                Class portClass = Class.forName(params.portClass);
                Class[] parameters = new Class[0];
                Constructor ctor = portClass.getConstructor(parameters);
                return (PrinterPort) ctor.newInstance(parameters);

            default:
                throw new Exception("Invalid portType value");
        }
    }

}
