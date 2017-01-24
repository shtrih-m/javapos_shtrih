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
 * @since 0.1.3
 */
public class Code39ExtEncoder extends Code39Encoder {
    
    protected static String [] CODES_TABLE = new String[]{
        "%U", "$A", "$B", "$C", "$D", "$E", "$F", "$G",
        "$H", "$I", "$J", "$K", "$L", "$M", "$N", "$O",
        "$P", "$Q", "$R", "$S", "$T", "$U", "$V", "$W",
        "$X", "$Y", "$Z", "%A", "%B", "%C", "%D", "%E",
        " ", "/A", "/B", "/C", "/D", "/E", "/F", "/G", 
        "/H", "/I", "/J", "/K", "/L", "-", ".", "/O",
        "0", "1", "2", "3", "4", "5", "6", "7",
        "8", "9", "/Z", "%F", "%G", "%H", "%I", "%J",
        "%V", "A", "B", "C", "D", "E", "F", "G", "H", "I",
        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
        "T", "U", "V", "W", "X", "Y", "Z", "%K", "%L", "%M",
        "%N", "%O", "%W", "+A", "+B", "+C", "+D", "+E",
        "+F", "+G", "+H", "+I", "+J", "+K", "+L", "+M",
        "+N", "+O", "+P", "+Q", "+R", "+S", "+T", "+U",
        "+V", "+W", "+X", "+Y", "+Z", "%P", "%Q", "%R",
        "%S", "%T"  
    };
    
    private static Code39ExtEncoder instance;

    protected Code39ExtEncoder(){
        
    }
    
    public static BarcodeEncoder getInstance(){
        if(instance == null){
            instance = new Code39ExtEncoder();
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
        return "CODE 39 (EXT)";
    }
    
    protected static String convertText(String str){
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            tmp.append(CODES_TABLE[str.charAt(i)]);
        }
        return tmp.toString();
    }
    
}
