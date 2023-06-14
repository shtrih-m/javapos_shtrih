/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author Виталий
 */
public class ItemCode 
{
    private byte[] data; 
    private boolean volumeAccounting;
    
    public ItemCode(byte[] data){
        this.data = data;
        this.volumeAccounting = false;
    }
    
    public ItemCode(byte[] data, boolean volumeAccounting){
        this.data = data;
        this.volumeAccounting = volumeAccounting;
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @return the isVolumeAccounting
     */
    public boolean isVolumeAccounting() {
        return volumeAccounting;
    }
    
}
