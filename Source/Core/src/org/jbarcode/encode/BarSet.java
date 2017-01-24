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
package org.jbarcode.encode;

/**
 * TODO: Description.
 * 
 * @author Flavio Sampaio
 * @since 0.1
 */
public class BarSet extends java.util.BitSet {
	
	private static final long serialVersionUID = 1L;
    
    private int length;

	
	public BarSet(int nbits){
		super(nbits);
		this.length = nbits;		
	}
	
	public BarSet(String bits) {
		super(bits.length());
		for( int i = 0; i < bits.length(); i++ ){
			if( bits.charAt(i) == '1' ){
				this.set(i);
			} else if( bits.charAt(i) != '0' ){
				throw new RuntimeException("Invalid Bit value: " + bits.charAt(i));
			}
		}
		length = bits.length();
	}
	
	public BarSet reverse(){
		BarSet result = new BarSet(this.length());
		for( int i = 0; i < this.length(); i++ ){
			if( this.get(i) ){
				result.set(result.length() - (i + 1));
			}
		}
		return result;
	}
	
	public BarSet xorTrue(){
		BarSet result = new BarSet(this.length());
		for( int i = 0; i < result.length(); i++ ){
			result.set(i);
		}
		result.xor(this);
		return result; 
	}
	
	public int length(){
		return length;
	}
	
	public String toString(){
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < length; i++) {
			if(get(i)){
				result.append(1);
			} else {
				result.append(0);
			}
		}
		return result.toString();
	}

}
