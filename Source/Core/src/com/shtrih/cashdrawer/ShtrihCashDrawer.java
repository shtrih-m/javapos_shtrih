/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.cashdrawer;

/**
 *
 * @author V.Kravtsov
 */
import jpos.CashDrawerControl113;

public class ShtrihCashDrawer extends ShtrihCashDrawer110 {

	/** Creates a new instance of ShtrihCashDrawer */
	public ShtrihCashDrawer(CashDrawerControl113 driver, String encoding) {
		super(driver, encoding);
	}

	public ShtrihCashDrawer(CashDrawerControl113 driver) {
		super(driver);
	}
        
	public ShtrihCashDrawer(String encoding) {
		super(encoding);
	}
}