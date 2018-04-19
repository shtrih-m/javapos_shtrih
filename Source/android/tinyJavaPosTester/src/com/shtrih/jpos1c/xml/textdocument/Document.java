package com.shtrih.jpos1c.xml.textdocument;

import com.shtrih.jpos1c.xml.Xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Текст в формате XML содержит описание текстового документа.
 * Структура описывает последовательность формирования текстового документа.
 */
@Root(name = "Document")
public class Document {

    @Element
    public Positions Positions = new Positions();

    public String toXml() throws Exception {
        return Xml.serialize(this);
    }
}

