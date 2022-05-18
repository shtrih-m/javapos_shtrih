package com.shtrih.fiscalprinter.port;

/**
 * @author V.Kravtsov
 */

import java.io.InputStream;
import java.io.OutputStream;

public interface PrinterPort2 extends PrinterPort
{
    InputStream getInputStream() throws Exception;
}
