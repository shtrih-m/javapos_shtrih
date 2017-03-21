package com.shtrih.jpos.fiscalprinter.receipt.template;

import java.util.Vector;

public class TemplateLine {
    private final int m_font_num;
    private final String m_source_contents;

    private enum CharState {REGULAR, ESCAPED, IN_FIELD}

    public boolean isPlainFontLine() {
        return m_plainFontLine;
    }

    private boolean m_plainFontLine = false;

    public String getOutputFmt() {
        return m_output_fmt;
    }

    private String m_output_fmt = "";

    public Vector<Field> getTags() {
        return m_fmt_tags;
    }

    private Vector<Field> m_fmt_tags = new Vector<Field>();

    public TemplateLine(int font, String contents) {
        m_font_num = font;
        m_source_contents = contents;
    }

    public void parseLine() throws ParsingException {
        CharState state = CharState.REGULAR;
        StringBuilder sb = new StringBuilder();
        int field_begin = -1;
        int field_end = -1;
        for (int i = 0; i < m_source_contents.length(); i++) {
            switch (state) {
                case REGULAR:
                    if (m_source_contents.charAt(i) == '\\') {
                        state = CharState.ESCAPED;
                    } else if (m_source_contents.charAt(i) == '%') {
                        state = CharState.IN_FIELD;
                        field_begin = i;
                    } else {
                        sb.append(m_source_contents.charAt(i));
                    }
                    break;
                case IN_FIELD:
                    if (m_source_contents.charAt(i) == '%') {
                        state = CharState.REGULAR;
                        field_end = i;
                        Field f = FieldFactory.createField(m_source_contents.substring(field_begin + 1, field_end));
                        m_fmt_tags.add(f);
                        sb.append("%s");
                        break;
                    }
                    break;
                case ESCAPED:
                    if (m_source_contents.charAt(i) == '%') {
                        //escape % sign for later String.format
                        sb.append(m_source_contents.charAt(i));
                    }
                    sb.append(m_source_contents.charAt(i));
                    state = CharState.REGULAR;
                    break;
            }
        }
        if (state == CharState.IN_FIELD) {
            throw new ParsingException("Field opened and not closed, '" + m_source_contents + "'");
        } else if (state == CharState.ESCAPED) {
            throw new ParsingException("Character escaped and no next char");
        }
        m_output_fmt = sb.toString();
    }


    public TemplateLine(String contents) {
        this(-1, contents);
    }

    public int getFont_number() {
        return m_font_num;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }


}
