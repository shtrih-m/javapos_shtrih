package com.shtrih.jpos1c.xml.check;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root
public class Positions {

    @ElementListUnion({
            @ElementList(entry = "FiscalString", type = FiscalString.class, required = false, inline = true),
            @ElementList(entry = "TextString", type = TextString.class, required = false, inline = true),
            @ElementList(entry = "Barcode", type = Barcode.class, required = false, inline = true),
    })
    public List<Object> FiscalStrings = new ArrayList<>();
}
