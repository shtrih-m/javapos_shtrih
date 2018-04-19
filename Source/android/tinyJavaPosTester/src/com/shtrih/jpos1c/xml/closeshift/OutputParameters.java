package com.shtrih.jpos1c.xml.closeshift;

import com.shtrih.jpos1c.xml.Xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Структура OutputParameters для метода "ОткрытьСмену (CloseShift)"
 * Текст в формате XML, передаваемый с помощью параметра типа STRING.
 */
@Root(name = "OutputParameters")
public class OutputParameters {
    
    @Element
    public Parameters Parameters = new Parameters();

    public String toXml() throws Exception {
        return Xml.serialize(this);
    }
}

