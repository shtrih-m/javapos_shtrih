/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.util;

/**
 *
 * @author V.Kravtsov
 */
public class FirmwareUpdater 
{
    
    public FirmwareUpdater(){
    }
    
    public static boolean capXModemUpdate(){
        return false;
    }
    
    public static boolean capDFUUpdate(){
        return false;
    }
    
    public static void updateFirmwareDFU(String firmwareFileName) throws Exception
    {
        
    }
    
    public static void updateFirmwareXModem(String portName, String firmwareFileName) throws Exception
    {
    }
    
}
