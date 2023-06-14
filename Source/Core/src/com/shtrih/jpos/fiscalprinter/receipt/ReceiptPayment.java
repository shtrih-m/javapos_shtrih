/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

/**
 *
 * @author Виталий
 */
public class ReceiptPayment 
{
    private final long payment; 
    private final long payType;
    private final String description;
    
    public ReceiptPayment(long payment, long payType, String description){
        this.payment = payment;
        this.payType = payType;
        this.description = description;
    }
}
