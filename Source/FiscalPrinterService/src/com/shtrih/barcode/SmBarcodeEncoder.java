/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.barcode;

/**
 * @author V.Kravtsov
 */

import com.shtrih.barcode.SmBarcode;

public interface SmBarcodeEncoder {

	public SmBarcode encode(PrinterBarcode barcode) throws Exception;
}
