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
            throws Exception 
    {
        PrinterPort result = null;
        switch (params.getPortType()) {
            case SmFptrConst.PORT_TYPE_SERIAL:
                PrinterPort2 port = new SerialPrinterPort(params.portName);
                if (params.pppConnection){
                    return new PPPPort(params, port);
                }
                return port;                

            case SmFptrConst.PORT_TYPE_SOCKET:
                result = SocketPort.getInstance(params.portName, params.getByteTimeout());
                break;

            case SmFptrConst.PORT_TYPE_FROMCLASS:
                Class portClass = Class.forName(params.portClass);
                Class[] parameters = new Class[0];
                Constructor ctor = portClass.getConstructor(parameters);
                result = (PrinterPort) ctor.newInstance(parameters);
                break;

            default:
                throw new Exception("Invalid portType value");
        }
        return new CancellablePrinterPort(result, params);
    }
}
