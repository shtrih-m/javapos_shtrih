/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.util;

import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Writer;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import com.shtrih.util.CompositeLogger;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.serialize.OutputFormat;

/**
 * @author V.Kravtsov
 */
public class XmlUtils {

    private static final String xmlFileEncoding = "ISO-8859-1";
    private static CompositeLogger logger = CompositeLogger.getLogger(XmlUtils.class);

    private XmlUtils() {
    }

    public static Document parse(File file) throws Exception {
        return parse(new FileInputStream(file));
    }

    public static Document parse(String fileName) throws Exception {
        return parse(new FileInputStream(fileName));
    }

    public static DocumentBuilder getBuilder() throws Exception {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            return f.newDocumentBuilder();
        } catch (Exception e) {
            logger.error("getBuilder1", e);
        }
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = f.newDocumentBuilder();
        return builder;
    }

    public static Document parse(InputStream stream) throws Exception {
        return getBuilder().parse(stream, xmlFileEncoding);
    }

    public static Document newDocument() throws Exception {
        return getBuilder().newDocument();
    }

    public static void save(Document document, String fileName, String encoding)
            throws Exception {
        save(document, new FileWriter(fileName), encoding);
    }

    public static void save(Document document, String fileName)
            throws Exception {
        save(document, new FileWriter(fileName));
    }
    
    public static void save(Document document, Writer writer, String encoding) throws Exception {
        // convert document to string
        OutputFormat of = new OutputFormat();
        of.setIndent(1);
        of.setIndenting(true);
        of.setEncoding(encoding);
        XMLSerializer serializer = new XMLSerializer(writer, of);
        // As a DOM Serializer
        serializer.asDOMSerializer();
        serializer.serialize(document.getDocumentElement());
    }

    
    public static void save(Document document, Writer writer) throws Exception {
        // convert document to string
        OutputFormat of = new OutputFormat();
        of.setIndent(1);
        of.setIndenting(true);
        of.setEncoding(xmlFileEncoding);
        XMLSerializer serializer = new XMLSerializer(writer, of);
        // As a DOM Serializer
        serializer.asDOMSerializer();
        serializer.serialize(document.getDocumentElement());
    }

}
