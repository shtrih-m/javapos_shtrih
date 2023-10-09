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
 
    public void visitSalesReceipt(Object element) throws Exception;
    public void visitCashInReceipt(Object element) throws Exception;
    public void visitCashOutReceipt(Object element) throws Exception;
    public void visitCustomReceipt(Object element) throws Exception;
    public void visitNonfiscalReceipt(Object element) throws Exception;
}
