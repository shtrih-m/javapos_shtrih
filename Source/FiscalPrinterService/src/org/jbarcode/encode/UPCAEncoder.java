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
 * @author Fl�vio Sampaio
 * @since 0.1
 */
public class UPCAEncoder extends EANEncoder {

    private static UPCAEncoder instance;

    private UPCAEncoder() {

    }

    public static UPCAEncoder getInstance() {
        if (instance == null) {
            instance = new UPCAEncoder();
        }
        return instance;
    }

    public String getTextWithCheckSum(String text)
            throws InvalidAtributeException {
        if (text.length() < 11 || text.length() > 12) {
            throw new InvalidAtributeException("Invalid text length (" + text.length() + ").");
        }

        if (text.length() == 12) {
            return text;
        }
        text = text.substring(0, 11);
        return text + computeCheckSum(text);
    }

    public BarSet[] encode(String texto) throws InvalidAtributeException {
        if (texto.length() < 11 || texto.length() > 12) {
            throw new InvalidAtributeException("[UPCA] Invalid text length (" + texto.length() + ").");
        }
        try {
            return EAN13Encoder.getInstance().encode("0" + texto);
        } catch (InvalidAtributeException tiexc) {
            throw new InvalidAtributeException("[UPCA] Only numbers suported.", tiexc);
        }
    }

    /* (non-Javadoc)
     * @see org.jbarcode.BarcodeEncoder#calcularDV(java.lang.String)
     */
    public String computeCheckSum(String texto) throws InvalidAtributeException {
        return super.computeCheckSum("0" + texto);
    }

    public String convertUPCAtoUPCE(String upca) throws InvalidAtributeException {
        if (upca.length() != 11 && upca.length() != 12) {
            throw new InvalidAtributeException("[UPCA] Invalid text length (" + upca.length() + ").");
        }
        StringBuffer result = new StringBuffer();
        result.append(upca.charAt(0));
        if (upca.charAt(0) != '0' && upca.charAt(0) != '1') {
            throw new InvalidAtributeException("[UPCA] Invalid Number System,  only 0 & 1 are valid (" + upca.charAt(0) + ").");
        } else if ("000".equals(upca.substring(3, 6)) || "100".equals(upca.substring(3, 6)) || "200".equals(upca.substring(3, 6))) {
            result.append(upca.substring(1, 3));
            result.append(upca.substring(8, 11));
            result.append(upca.charAt(3));
        } else if ("00".equals(upca.substring(4, 6))) {
            result.append(upca.substring(1, 4));
            result.append(upca.substring(9, 11));
            result.append('3');
        } else if (upca.charAt(5) == '0') {
            result.append(upca.substring(1, 5));
            result.append(upca.charAt(10));
            result.append('4');
        } else if (upca.charAt(10) >= '5' && upca.charAt(10) <= '9') {
            result.append(upca.substring(1, 6));
            result.append(upca.charAt(10));
        } else {
            throw new InvalidAtributeException("[UPCA] Invalid code.");
        }
        if (upca.length() == 12) {
            result.append(upca.charAt(11));
        }
        return result.toString();
    }

    public String toString() {
        return "UPC-A";
    }
}
