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
public interface ReceiptVisitor {
 
    public void visitSalesReceipt(Object element);
    public void visitCustomReceipt(Object element);
}
