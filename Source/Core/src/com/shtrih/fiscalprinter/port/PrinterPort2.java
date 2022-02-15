package com.shtrih.fiscalprinter.port;

/**
 * @author V.Kravtsov
 */

import java.io.InputStream;
import java.io.OutputStream;

public interface PrinterPort2
{
    void open() throws Exception;

    void close() throws Exception;

    InputStream getInputStream() throws Exception;

    OutputStream getOutputStream() throws Exception;

    Object getParameter(String name) throws Exception;

    void setParameter(String name, Object value) throws Exception;

    void setPortEvents(IPortEvents2 events);

    public interface IPortEvents2
    {
        void onConnect();
        void onDisconnect();
    }
}
