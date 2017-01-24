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
 * TODO: Description
 *
 * @author Flavio Sampaio
 * @since 0.2.0
 */
public class Code93ExtEncoder extends Code93Encoder {
    
    protected static String [] CODES_TABLE = new String[]{
        "\u00B2U", "\u00B9A", "\u00B9B", "\u00B9C", "\u00B9D", "\u00B9E", "\u00B9F", "\u00B9G",
        "\u00B9H", "\u00B9I", "\u00B9J", "\u00B9K", "\u00B9L", "\u00B9M", "\u00B9N", "\u00B9O",
        "\u00B9P", "\u00B9Q", "\u00B9R", "\u00B9S", "\u00B9T", "\u00B9U", "\u00B9V", "\u00B9W",
        "\u00B9X", "\u00B9Y", "\u00B9Z", "\u00B2A", "\u00B2B", "\u00B2C", "\u00B2D", "\u00B2E",
        " ", "\u00B3A", "\u00B3B", "\u00B3C", "\u00B3D", "\u00B3E", "\u00B3F", "\u00B3G", 
        "\u00B3H", "\u00B3I", "\u00B3J", "\u00B3K", "\u00B3L", "-", ".", "\u00B3O",
        "0", "1", "2", "3", "4", "5", "6", "7",
        "8", "9", "\u00B3Z", "\u00B2F", "\u00B2G", "\u00B2H", "\u00B2I", "\u00B2J",
        "\u00B2V", "A", "B", "C", "D", "E", "F", "G", "H", "I",
        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
        "T", "U", "V", "W", "X", "Y", "Z", "\u00B2K", "\u00B2L", "\u00B2M",
        "\u00B2N", "\u00B2O", "\u00B2W", "\u00A3A", "\u00A3B", "\u00A3C", "\u00A3D", "\u00A3E",
        "\u00A3F", "\u00A3G", "\u00A3H", "\u00A3I", "\u00A3J", "\u00A3K", "\u00A3L", "\u00A3M",
        "\u00A3N", "\u00A3O", "\u00A3P", "\u00A3Q", "\u00A3R", "\u00A3S", "\u00A3T", "\u00A3U",
        "\u00A3V", "\u00A3W", "\u00A3X", "\u00A3Y", "\u00A3Z", "\u00B2P", "\u00B2Q", "\u00B2R",
        "\u00B2S", "\u00B2T"  
    };
    
    private static Code93ExtEncoder instance;

    protected Code93ExtEncoder(){
        
    }
    
    public static BarcodeEncoder getInstance(){
        if(instance == null){
            instance = new Code93ExtEncoder();
        }
        return instance;
    }
    
    public BarSet[] encode(String text) throws InvalidAtributeException {
        return super.encode(convertText(text));
    }
    
    public String computeCheckSum(String text) throws InvalidAtributeException {
        return super.computeCheckSum(convertText(text));
    }

    public String toString(){
        return "CODE 93 (EXT)";
    }
    
    protected String convertText(String str) throws InvalidAtributeException{
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
        	if(str.charAt(i) < 128){
        		tmp.append(CODES_TABLE[str.charAt(i)]);
        	} else if(str.charAt(i) == '\u00B9' || str.charAt(i) == '\u00B2' || str.charAt(i) == '\u00B3' || str.charAt(i) == '\u00A3'){
        		tmp.append(str.charAt(i));
        	} else {
        		throw new InvalidAtributeException("[Code93] The text contains unsuported chars.");
        	}
            
        }
        return tmp.toString();
    }

}
