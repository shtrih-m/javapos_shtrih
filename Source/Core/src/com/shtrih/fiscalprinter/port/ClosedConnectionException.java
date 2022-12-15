package com.shtrih.fiscalprinter.port;

import java.lang.Exception;
public class ClosedConnectionException extends Exception {

    public ClosedConnectionException(String detailMessage){
        super(detailMessage);
    }
}
