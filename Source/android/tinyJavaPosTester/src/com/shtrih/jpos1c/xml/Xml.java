package com.shtrih.jpos1c.xml;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

import java.io.StringWriter;

public class Xml {
    public static String serialize(Object object) throws Exception{
        Serializer serializer = new Persister(new Format(2, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));

        StringWriter writer = new StringWriter();
        serializer.write(object, writer);
        return writer.toString();
    }

    public static <T> T deserialize(Class<? extends T> type, String source) throws Exception {
        Serializer serializer = new Persister();

        return serializer.read(type, source, false);
    }
}
