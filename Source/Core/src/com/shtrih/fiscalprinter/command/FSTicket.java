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
public class FSTicket 
{
    private final int resultCode;
    private final byte[] data;
    
    public FSTicket(int resultCode, byte[] data){
        this.resultCode = resultCode;
        this.data = data;
    }

    /**
     * @return the resultCode
     */
    public int getResultCode() {
        return resultCode;
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }
    
}
