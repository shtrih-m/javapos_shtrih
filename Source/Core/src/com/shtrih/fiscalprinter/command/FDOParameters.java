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
    private final int pollPeriodSeconds;

    public FDOParameters(String host, int port, int pollPeriodSeconds) {
        this.host = host;
        this.port = port;
        this.pollPeriodSeconds = pollPeriodSeconds;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    public int getPollPeriodSeconds() {
        return pollPeriodSeconds;
    }
}
