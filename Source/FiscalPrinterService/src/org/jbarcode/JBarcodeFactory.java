/* ================================================================
 * JBarcode : Java Barcode Library
 * ================================================================
 *
 * Project Info:  http://jbcode.sourceforge.net
 * Project Lead:  Flavio Sampaio (flavio@ronisons.com);
 *
 * (C) Copyright 2005, by Flavio Sampaio
 *
 * This library is free software; you can redistribute it and/or modify it underthe terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */
package org.jbarcode;

import org.jbarcode.encode.CodabarEncoder;
import org.jbarcode.encode.Code11Encoder;
import org.jbarcode.encode.Code128Encoder;
import org.jbarcode.encode.Code39Encoder;
import org.jbarcode.encode.Code39ExtEncoder;
import org.jbarcode.encode.Code93Encoder;
import org.jbarcode.encode.Code93ExtEncoder;
import org.jbarcode.encode.EAN13Encoder;
import org.jbarcode.encode.EAN8Encoder;
import org.jbarcode.encode.Interleaved2of5Encoder;
import org.jbarcode.encode.InvalidAtributeException;
import org.jbarcode.encode.MSIPlesseyEncoder;
import org.jbarcode.encode.PostNetEncoder;
import org.jbarcode.encode.Standard2of5Encoder;
import org.jbarcode.encode.UPCAEncoder;
import org.jbarcode.encode.UPCEEncoder;
import org.jbarcode.paint.BaseLineTextPainter;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.EAN8TextPainter;
import org.jbarcode.paint.HeightCodedPainter;
import org.jbarcode.paint.UPCATextPainter;
import org.jbarcode.paint.UPCETextPainter;
import org.jbarcode.paint.WideRatioCodedPainter;
import org.jbarcode.paint.WidthCodedPainter;


/**
 * JBarcode factory class.
 * 
 * @author Flavio Sampaio
 * @since 0.2
 */
public class JBarcodeFactory {
	
	private static JBarcodeFactory instance;
	
	private JBarcodeFactory(){	
	}
	
	/**
	 * Obtains a JBarcode factory instance.
	 * 
	 * @return JBarcodeFactory instance.
	 */
	public static JBarcodeFactory getInstance(){
		if(instance == null){
			instance = new JBarcodeFactory();
		}
		return instance;
	}
	
	/**
	 * Creates a new JBarcode instance to EAN-13 barcode type. 
	 * 
	 * @return JBarcode instance.
	 */
	public JBarcode createEAN13(){
		JBarcode jbc = new JBarcode(EAN13Encoder.getInstance(), WidthCodedPainter.getInstance(), EAN13TextPainter.getInstance());
		jbc.setBarHeight(17);
		try {
			jbc.setXDimension(0.264583333);
		} catch (InvalidAtributeException e) {}
		jbc.setShowText(true);
        jbc.setCheckDigit(true);
        jbc.setShowCheckDigit(true);
		return jbc;
	}
	
	/**
	 * Creates a new JBarcode instance to EAN-8 barcode type. 
	 * 
	 * @return JBarcode instance.
	 */
	public JBarcode createEAN8(){
		JBarcode jbc = new JBarcode(EAN8Encoder.getInstance(), WidthCodedPainter.getInstance(), EAN8TextPainter.getInstance());
		jbc.setBarHeight(17);
		try {
			jbc.setXDimension(0.264583333);
		} catch (InvalidAtributeException e) {}
		jbc.setShowText(true);
        jbc.setCheckDigit(true);
        jbc.setShowCheckDigit(true);
		return jbc;
	}

	/**
	 * Creates a new JBarcode instance to UPCA barcode type. 
	 * 
	 * @return JBarcode instance.
	 */
	public JBarcode createUPCA(){
		JBarcode jbc = new JBarcode(UPCAEncoder.getInstance(), WidthCodedPainter.getInstance(), UPCATextPainter.getInstance());
		jbc.setBarHeight(17);
		try {
			jbc.setXDimension(0.264583333);
		} catch (InvalidAtributeException e) {}
		jbc.setShowText(true);
        jbc.setCheckDigit(true);
        jbc.setShowCheckDigit(true);
		return jbc;
	}
	
	/**
	 * Creates a new JBarcode instance to UPCE barcode type. 
	 * 
	 * @return JBarcode instance.
	 */
	public JBarcode createUPCE(){
		JBarcode jbc = new JBarcode(UPCEEncoder.getInstance(), WidthCodedPainter.getInstance(), UPCETextPainter.getInstance());
		jbc.setBarHeight(17);
		try {
			jbc.setXDimension(0.264583333);
		} catch (InvalidAtributeException e) {}
		jbc.setShowText(true);
        jbc.setCheckDigit(true);
        jbc.setShowCheckDigit(true);
		return jbc;
	}
	
	/**
	 * Creates a new JBarcode instance to Codabar barcode type. 
	 * 
	 * @return JBarcode instance.
	 */
	public JBarcode createCodabar(){
		JBarcode jbc = new JBarcode(CodabarEncoder.getInstance(), WideRatioCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
		jbc.setBarHeight(17);
		try {
			jbc.setXDimension(0.264583333);
		} catch (InvalidAtributeException e) {}
		jbc.setShowText(true);
        jbc.setCheckDigit(false);
        jbc.setShowCheckDigit(false);
		return jbc;
	}
	
	/**
	 * Creates a new JBarcode instance to Code11 barcode type. 
	 * 
	 * @return JBarcode instance.
	 */
	public JBarcode createCode11(){
		JBarcode jbc = new JBarcode(Code11Encoder.getInstance(), WidthCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
		jbc.setBarHeight(17);
		try {
			jbc.setXDimension(0.264583333);
		} catch (InvalidAtributeException e) {}
		jbc.setShowText(true);
        jbc.setCheckDigit(true);
        jbc.setShowCheckDigit(true);
		return jbc;
	}
	
	/**
	 * Creates a new JBarcode instance to Code39 barcode type. 
	 * 
	 * @return JBarcode instance.
	 */
	public JBarcode createCode39(){
		JBarcode jbc = new JBarcode(Code39Encoder.getInstance(), WideRatioCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
		jbc.setBarHeight(17);
		try {
			jbc.setXDimension(0.264583333);
		} catch (InvalidAtributeException e) {}
		jbc.setShowText(true);
        jbc.setCheckDigit(false);
        jbc.setShowCheckDigit(false);
		return jbc;
	}
	
	/**
	 * Creates a new JBarcode instance to Code39 Extended barcode type. 
	 * 
	 * @return JBarcode instance.
	 */
	public JBarcode createCode39Extended(){
		JBarcode jbc = new JBarcode(Code39ExtEncoder.getInstance(), WideRatioCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
		jbc.setBarHeight(17);
		try {
			jbc.setXDimension(0.264583333);
		} catch (InvalidAtributeException e) {}
		jbc.setShowText(true);
        jbc.setCheckDigit(false);
        jbc.setShowCheckDigit(false);
		return jbc;
	}
	
	/**
	 * Creates a new JBarcode instance to Code93 barcode type. 
	 * 
	 * @return JBarcode instance.
	 */
	public JBarcode createCode93(){
		JBarcode jbc = new JBarcode(Code93Encoder.getInstance(), WidthCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
		jbc.setBarHeight(17);
		try {
			jbc.setXDimension(0.264583333);
		} catch (InvalidAtributeException e) {}
		jbc.setShowText(true);
        jbc.setCheckDigit(true);
        jbc.setShowCheckDigit(false);
		return jbc;
	}
	
	/**
	 * Creates a new JBarcode instance to Code93 Extended barcode type. 
	 * 
	 * @return JBarcode instance.
	 */
	public JBarcode createCode93Extended(){
		JBarcode jbc = new JBarcode(Code93ExtEncoder.getInstance(), WidthCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
		jbc.setBarHeight(17);
		try {
			jbc.setXDimension(0.264583333);
		} catch (InvalidAtributeException e) {}
		jbc.setShowText(true);
        jbc.setCheckDigit(true);
        jbc.setShowCheckDigit(false);
		return jbc;
	}
	
	/**
	 * Creates a new JBarcode instance to Code128 barcode type. 
	 * 
	 * @return JBarcode instance.
	 */
	public JBarcode createCode128(){
		JBarcode jbc = new JBarcode(Code128Encoder.getInstance(), WidthCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
		jbc.setBarHeight(17);
		try {
			jbc.setXDimension(0.264583333);
		} catch (InvalidAtributeException e) {}
		jbc.setShowText(true);
        jbc.setCheckDigit(true);
        jbc.setShowCheckDigit(false);
		return jbc;
	}
	
	/**
	 * Creates a new JBarcode instance to MSIPlessey barcode type. 
	 * 
	 * @return JBarcode instance.
	 */
	public JBarcode createMSIPlessey(){
		JBarcode jbc = new JBarcode(MSIPlesseyEncoder.getInstance(), WidthCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
		jbc.setBarHeight(17);
		try {
			jbc.setXDimension(0.264583333);
		} catch (InvalidAtributeException e) {}
		jbc.setShowText(true);
        jbc.setCheckDigit(true);
        jbc.setShowCheckDigit(true);
		return jbc;
	}
	
	/**
	 * Creates a new JBarcode instance to Standard2of5 barcode type. 
	 * 
	 * @return JBarcode instance.
	 */
	public JBarcode createStandard2of5(){
		JBarcode jbc = new JBarcode(Standard2of5Encoder.getInstance(), WideRatioCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
		jbc.setBarHeight(17);
		try {
			jbc.setXDimension(0.264583333);
		} catch (InvalidAtributeException e) {}
		jbc.setShowText(true);
        jbc.setCheckDigit(true);
        jbc.setShowCheckDigit(false);
		return jbc;
	}
	
	/**
	 * Creates a new JBarcode instance to Interleaved2of5 barcode type. 
	 * 
	 * @return JBarcode instance.
	 */
	public JBarcode createInterleaved2of5(){
		JBarcode jbc = new JBarcode(Interleaved2of5Encoder.getInstance(), WideRatioCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
		jbc.setBarHeight(17);
		try {
			jbc.setXDimension(0.264583333);
		} catch (InvalidAtributeException e) {}
		jbc.setShowText(true);
        jbc.setCheckDigit(true);
        jbc.setShowCheckDigit(false);
		return jbc;
	}
	
	/**
	 * Creates a new JBarcode instance to PostNet barcode type. 
	 * 
	 * @return JBarcode instance.
	 */
	public JBarcode createPostNet(){
		JBarcode jbc = new JBarcode(PostNetEncoder.getInstance(), HeightCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
		jbc.setBarHeight(6);
		try {
			jbc.setXDimension(0.264583333);
		} catch (InvalidAtributeException e) {}
		jbc.setShowText(false);
        jbc.setCheckDigit(true);
        jbc.setShowCheckDigit(false);
		return jbc;
	}
	
}
