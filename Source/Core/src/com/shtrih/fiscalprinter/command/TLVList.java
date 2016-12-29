/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import java.io.ByteArrayOutputStream;

/**
 *
 * @author V.Kravtsov
 */
public class TLVList 
{
    private final ByteArrayOutputStream stream = new ByteArrayOutputStream();
    
    public void add(int tagId, String tagValue) throws Exception
    {
        addInt(tagId);
        addInt(tagValue.length());
        stream.write(tagValue.getBytes("cp866"));
    }
  
    public void addInt(int value) throws Exception{
        stream.write((value >>> 0) & 0xFF);
        stream.write((value >>> 8) & 0xFF);
    }

    public byte[] getData(){
        return stream.toByteArray();
    }
    
}
