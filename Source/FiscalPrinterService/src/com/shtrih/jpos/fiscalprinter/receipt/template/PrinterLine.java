package com.shtrih.jpos.fiscalprinter.receipt.template;

import java.io.Serializable;

/**
 * Класс шаблонной строки в общем виде строка имеет печатается "как есть" в строке может быть 0 или более тэгов,
 * содержимое которых подставляется в runtime. Если строка начинается с $\d - эта строка печатается шрифтом \d
 * Если длина строки в итоге больше чем возможно данным шрифтом - строка просто обрезается справа.
 */
public class PrinterLine implements Serializable {
    private final int m_font_num;

    public String getSource_contents() {
        return m_source_contents;
    }

    private final String m_source_contents;

    public PrinterLine(int font, String contents) {
        m_font_num = font;
        m_source_contents = contents;
    }


    public PrinterLine(String contents) {
        this(-1, contents);
    }

    public int getFont_number() {
        return m_font_num;
    }


}
