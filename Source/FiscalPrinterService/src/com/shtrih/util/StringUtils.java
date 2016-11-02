/*
 * StringUtils.java
 *
 * Created on 30 August 2007, 20:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 *
 * @author V.Kravtsov
 */
package com.shtrih.util;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

public class StringUtils {

    /**
     * Creates a new instance of StringUtils
     */
    private StringUtils() {
    }

    public static String[] split(String text, char c) {
        String data = "";
        Vector result = new Vector();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == c) {
                result.add(data);
                data = "";
            } else {
                data += text.charAt(i);
            }
        }
        if (data.length() != 0) {
            result.add(data);
        }
        return (String[]) result.toArray(new String[0]);
    }

    public static String centerLine(String data, int lineLength) {
        int len = Math.min(data.length(), lineLength);
        String s = data.substring(0, len);
        len = (lineLength - len) / 2;
        for (int i = 0; i < len; i++) {
            s = " " + s;
        }
        return s;
    }

    public static String alignLeft(String data, int lineLength) {
        int len = Math.min(data.length(), lineLength);
        String s = data.substring(0, len);
        len = lineLength - len;
        for (int i = 0; i < len; i++) {
            s = s + " ";
        }
        return s;
    }

    public static String alignRight(String data, int lineLength) {
        int len = Math.min(data.length(), lineLength);
        String s = data.substring(0, len);
        len = lineLength - len;
        for (int i = 0; i < len; i++) {
            s = " " + s;
        }
        return s;
    }

    public static String appendLeft(String data, int lineLength) {
        int len = lineLength - data.length();
        for (int i = 0; i < len; i++) {
            data = " " + data;
        }
        return data;
    }

    public static String appendRight(String data, int lineLength) {
        int len = lineLength - data.length();
        for (int i = 0; i < len; i++) {
            data = data + " ";
        }
        return data;
    }

    public static String intToStr(int value, int len) {
        String result = String.valueOf(value);
        if (result.length() > len) {
            result = String.copyValueOf(result.toCharArray(), 0, len);
        }
        int count = len - result.length();
        for (int i = 0; i < count; i++) {
            result = "0" + result;
        }
        return result;
    }

    public static String decimalsToStr(int value, int len) {
        String result = String.valueOf(value);
        int count = len - result.length();
        for (int i = 0; i < count; i++) {
            result = result + "0";
        }
        return result;
    }

    public static String boolToStr(boolean value) {
        if (value) {
            return "1";
        } else {
            return "0";
        }
    }

    public static String amountToString(long amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(
                Locale.getDefault());
        symbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("0.00", symbols);
        return formatter.format(amount / 100.0);
    }

    public static String quantityToString(double value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(
                Locale.getDefault());
        symbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("0.000", symbols);
        return formatter.format(value);
    }

    public static String arrayToStr(int[] value) {
        String result = "";
        for (int i = 0; i < value.length; i++) {
            result += String.valueOf(value[i]) + ";";
        }
        return result;
    }

    public static int[] strToIntArray(String value) {
        StringTokenizer tokenizer = new StringTokenizer(value, ";");
        int count = tokenizer.countTokens();
        int[] result = new int[count];
        for (int i = 0; i < count; i++) {
            String token = tokenizer.nextToken();
            result[i] = Integer.parseInt(token);
        }
        return result;
    }

    public static byte[] getBytes(String text, String charsetName)
            throws UnsupportedEncodingException {
        if (charsetName == "") {
            return text.getBytes();
        } else {
            return text.getBytes(charsetName);
        }
    }

    public static String stringOfChar(char c, int count) {
        String result = "";
        for (int i = 0; i < count; i++) {
            result += c;
        }
        return result;
    }

    public static String trimRight(String text) {
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == 0) {
                break;
            }
            result += text.charAt(i);
        }
        return result;
    }
}
