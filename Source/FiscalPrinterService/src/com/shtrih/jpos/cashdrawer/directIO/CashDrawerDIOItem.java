/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.cashdrawer.directIO;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.jpos.cashdrawer.CashDrawerImpl;

public interface CashDrawerDIOItem {

    public void execute(CashDrawerImpl service, int[] data, Object object)
            throws Exception;
}
