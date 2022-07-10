/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
public class FDOParameters {

    private final String host;
    private final int port;
    private final int timeoutInSec;

    public FDOParameters(String host, int port, int timeoutInSec) {
        this.host = host;
        this.port = port;
        this.timeoutInSec = timeoutInSec;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getTimeoutInSec() {
        return timeoutInSec;
    }

    public int getTimeoutInMSec() {
        return timeoutInSec * 1000;
    }
}
