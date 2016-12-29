/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.shtrih.util.StringUtils;
import com.shtrih.util.XmlUtils;

/**
 * @author V.Kravtsov
 */
public class XmlPropWriter {

	private final Document doc;
	private Element root;
	private Element node;

	public XmlPropWriter(String className, String deviceName) throws Exception {
		doc = XmlUtils.newDocument();
		root = doc.createElement("root");
		doc.appendChild(root);
		node = doc.createElement(className);
		root.appendChild(node);
		root = node;

		node = doc.createElement(deviceName);
		root.appendChild(node);
		root = node;
	}

	public void save(String fileName) throws Exception {
		XmlUtils.save(doc, fileName);
	}

	public void addParameter(Element node, String name, Object value)
			throws Exception {
		node.setAttribute(name, String.valueOf(value));
	}

	public void write(PrinterImages images) throws Exception {
		node = doc.createElement("Images");
		root.appendChild(node);
		for (int i = 0; i < images.size(); i++) {
			write(images.get(i));
		}
	}

	public void write(PrinterImage image) throws Exception {
		Element imageNode = doc.createElement("Image");
		node.appendChild(imageNode);

		addParameter(imageNode, "FileName", image.getFileName());
		addParameter(imageNode, "Digest", image.getDigest());
		addParameter(imageNode, "Height", new Integer(image.getHeight()));
		addParameter(imageNode, "FirstLine", new Integer(image.getStartPos()));
		addParameter(imageNode, "IsLoaded",
				StringUtils.boolToStr(image.getIsLoaded()));
	}

	public void write(ReceiptImages images) throws Exception 
        {
		node = doc.createElement("ReceiptImages");
		root.appendChild(node);
		for (int i = 0; i < images.size(); i++) {
			write(images.get(i));
		}
	}

	public void write(ReceiptImage image) throws Exception {
		Element imageNode = doc.createElement("ReceiptImage");
		node.appendChild(imageNode);
		addParameter(imageNode, "ImageIndex",
				new Integer(image.getImageIndex()));
		addParameter(imageNode, "Position", new Integer(image.getPosition()));
	}

	public void writePrinterHeader(PrinterHeader header) throws Exception {
		node = doc.createElement("Header");
		root.appendChild(node);
		for (int i = 1; i <= header.getNumHeaderLines(); i++) {
			write(header.getHeaderLine(i));
		}
		node = doc.createElement("Trailer");
		root.appendChild(node);
		for (int i = 1; i <= header.getNumTrailerLines(); i++) {
			write(header.getTrailerLine(i));
		}
	}

	public void write(HeaderLine line) throws Exception {
		if (line == null)
			return;
		Element lineNode = doc.createElement("Line");
		node.appendChild(lineNode);
		addParameter(lineNode, "Text", line.getText());
		addParameter(lineNode, "DoubleWidth", StringUtils.boolToStr(line.isDoubleWidth()));
	}

}
