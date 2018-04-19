package com.shtrih.jpos1c.xml.check;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Печать текстовой строки.
 */
@Root
public class TextString {

    /**
     * Строка с произвольным текстом
     */
    @Attribute
    public String Text;

    /**
     * Строка с произвольным текстом
     */
    @Attribute(required = false)
    public int FontNumber = 1;
}

