/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.util;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * @author V.Kravtsov
 */
public class XmlUtils {

	private XmlUtils() {
	}

	public static Document parse(File file) throws Exception {
		return parse(new FileInputStream(file));
	}

	public static Document parse(String fileName) throws Exception {
		return parse(new FileInputStream(fileName));
	}

	public static DocumentBuilder getBuilder() throws Exception {
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		return f.newDocumentBuilder();
	}

	public static Document parse(InputStream stream) throws Exception {
		return getBuilder().parse(stream, "ISO8859-1");
	}

	public static Document newDocument() throws Exception {
		return getBuilder().newDocument();
	}

	public static void save(Document document, Writer writer) throws Exception {
		save(document, new StreamResult(writer));
	}

	public static void save(Document document, String fileName)
			throws Exception {
		save(document, new StreamResult(new File(fileName)));
	}

	public static void save(Document document, String fileName, String encoding)
			throws Exception {
		save(document, new StreamResult(new File(fileName)), encoding);
	}

	public static void save(Document document, StreamResult result)
			throws Exception {
		save(document, result, "ISO8859-1");
	}

	public static void save(Document document, StreamResult result, String encoding)
			throws Exception {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
		transformer.setOutputProperty(OutputKeys.INDENT, "no");
		transformer.transform(source, result);
	}

}
