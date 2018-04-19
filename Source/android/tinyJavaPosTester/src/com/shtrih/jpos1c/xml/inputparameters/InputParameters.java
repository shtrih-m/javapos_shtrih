package com.shtrih.jpos1c.xml.inputparameters;

import com.shtrih.jpos1c.xml.Xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Текст в формате XML, передаваемый с помощью параметра типа STRING.
 */
@Root(name = "InputParameters")
public class InputParameters {
    
    /**
     * Формирование нового чека с заданным атрибутами
     */
    @Element
    public Parameters Parameters;

    public InputParameters(){
        Parameters = new Parameters();
    }

    public String toXml() throws Exception {
        return Xml.serialize(this);
    }
}
