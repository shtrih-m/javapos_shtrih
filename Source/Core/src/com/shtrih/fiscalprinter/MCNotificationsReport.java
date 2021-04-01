/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 *f
 * @author Виталий
 */
public class MCNotificationsReport {
    
    public MCNotificationsReport(){
    }
    
    public void save(MCNotifications items, String fileName) throws Exception
    {
        if (items.size() == 0){
            throw new Exception("Notification count = 0");
        }
        
        BufferedWriter stream = new BufferedWriter(new OutputStreamWriter
            (new FileOutputStream(fileName),"UTF8"));        
        
        String line = "Отчет о реализации маркированного товара";
        stream.write(line);
    }
    
}
