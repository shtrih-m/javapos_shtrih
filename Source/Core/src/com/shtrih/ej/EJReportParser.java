/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.ej;

/**
 * @author V.Kravtsov
 */
/*
 * ------------------------------------------ 40 символов
 * ------------------------------------------ ШТРИХ-М-ФР-К ККМ 000012345678 ИНН
 * 000000012345 ЭКЛЗ 0000000018 ИТОГ АКТИВИЗАЦИИ 10/02/14 12:18 ЗАКРЫТИЕ СМЕНЫ
 * 0000 РЕГИСТРАЦИОННЫЙ НОМЕР 000000123456 00000001 #037110
 * ------------------------------------------ 16 символов
 * ------------------------------------------ ШТРИХ-LIGHT-ФР-К ККМ 000000001012
 * ИНН 000000012345 ЭКЛЗ 0113154054 ИТОГ АКТИВИЗАЦИИ 10/02/14 13:57 ЗАКР.СМЕНЫ
 * 0000 РЕГ 000000123456 00000001 #013047
 * ------------------------------------------
 */

public final class EJReportParser {

    private EJReportParser() {
    }

    private static int getMaxLineLength(String[] lines) {
        int result = 0;
        for (int i = 0; i < lines.length; i++) {
            int l = lines[i].length();
            if (l > result) {
                result = l;
            }
        }
        return result;
    }

    private static String getParam(String line, int num) {
        String tocken = "";
        int tockenNumber = 1;
        boolean wasSeparator = false;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ' ') {
                if (tockenNumber == num) {
                    break;
                }
                wasSeparator = true;
            } else {
                if (wasSeparator) {
                    tocken = "";
                    tockenNumber++;
                    wasSeparator = false;
                }
                tocken += line.charAt(i);
            }
        }
        return tocken;
    }

    public static EJActivation parseEJActivation(String[] lines)
            throws Exception {
        EJActivation result = new EJActivation();
        if (getMaxLineLength(lines) > 16) {
            if (lines.length >= 4) {
                result.setEJSerial(getParam(lines[2], 2));
                result.setDate(getParam(lines[4], 1));
                result.setTime(getParam(lines[4], 2));
            }
        } else {
            if (lines.length >= 5) {
                result.setEJSerial(getParam(lines[3], 2));
                result.setDate(getParam(lines[5], 1));
                result.setTime(getParam(lines[5], 2));
            }
        }
        return result;
    }
}
