package com.shtrih.jpos.fiscalprinter.receipt.template;


public class FormatLineParser {

    public static TemplateLine getLineFromString(String line) {
        TemplateLine result = null;
        int font = -1;
        if (line.length() >= 2 && line.charAt(0) == '$' && Character.isDigit(line.charAt(1))) {
            font = Integer.parseInt(String.valueOf(line.charAt(1)));
            line = line.substring(2);
            result = new TemplateLine(font, line);
            return result;
        }
        result = new TemplateLine(line);
        return result;
    }
}
