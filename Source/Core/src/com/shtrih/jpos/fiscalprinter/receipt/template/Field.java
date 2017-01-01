package com.shtrih.jpos.fiscalprinter.receipt.template;


import java.util.HashMap;
import java.util.HashSet;

/**
 * Класс поля имеет вид
 * %[prefix][width[flags]]TAG%
 * где все поля опциональны кроме названия тэга
 */
public class Field {

    public enum Justify {RIGHT, LEFT, CENTER}

    public final Justify justify;
    public final int width;
    public final String tag;
    public final String prefix;
    public static final char LEFT_JUSTIFY = 'l';
    public static final char CENTER_JUSTIFY = 'c';
    static HashSet<String> single_tags;

    static{
        single_tags=new HashSet<String>();
        single_tags.add("CUT");
        single_tags.add("FN_QR_CODE");

    }
    public  boolean isSinbleTag(){
            return single_tags.contains(tag);
    }

    public Field(String prefix, int width, String tag, Justify justify) {
        this.prefix = prefix;
        this.width = width;
        this.justify = justify;
        this.tag = tag;
    }

    public static Justify flagFromChar(char c) {
        switch (c) {
            case 'l':
                return Justify.LEFT;
            case 'c':
                return Justify.CENTER;
            default:
                return Justify.RIGHT;
        }
    }
}
