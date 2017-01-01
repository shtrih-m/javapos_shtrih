package com.shtrih.jpos.fiscalprinter.receipt.template;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldFactory {

    public static Field createField(String from) throws ParsingException {
        Field result = null;
        if (from.length() < 1) {
            throw new ParsingException("Empty %% sequence,no tag between %% chars");
        }
        int width = -1;
        String prefix = "";
        Field.Justify justify = Field.Justify.RIGHT;
        String tag;
        Pattern pattern = Pattern.compile("((\\D)\\$)*((\\d{1,2})+([lc])*)*([A-Z_]+)");
        Matcher matcher = pattern.matcher(from);
        if (matcher.matches()) {
            if (matcher.group(2) != null && matcher.group(2).length() > 0) {
                prefix = matcher.group(2);
            }
            if (matcher.group(4) != null && matcher.group(4).length() > 0) {
                width = Integer.parseInt(matcher.group(4));
            }
            if (matcher.group(5) != null && matcher.group(5).length() > 0) {
                justify = Field.flagFromChar(matcher.group(5).charAt(0));
            }
            tag = matcher.group(6);
            result = new Field(prefix, width, tag, justify);
        }else{
            throw new ParsingException("Wrong field format %"+from+ "% make sure your field conform to %[prefix$][width[flags]]TAG%");
        }
        return result;
    }
}
