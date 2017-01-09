/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import com.shtrih.util.CompositeLogger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.shtrih.util.XmlUtils;

/**
 * @author V.Kravtsov
 */
public class XmlPropReader {

    private Document doc;
    private Node root;
    private Node node;
    private static CompositeLogger logger = CompositeLogger.getLogger(XmlPropReader.class);

    public XmlPropReader() {
    }

    public void load(String className, String deviceName, String fileName)
            throws Exception {
        doc = XmlUtils.parse(fileName);
        if (doc == null) {
            throw new Exception("Error loading document");
        }
        root = doc.getDocumentElement();
        if (root == null) {
            throw new Exception("No root node");
        }
        root = getChildNode(root, className);
        root = getChildNode(root, deviceName);
        node = root;
    }

    public Node getChildNode(Node node, String nodeName) throws Exception {
        Node result = null;
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            result = list.item(i);
            if (result.getNodeName().equalsIgnoreCase(nodeName)) {
                return result;
            }
        }
        throw new Exception("Child node not found, " + nodeName);
    }

    public void read(PrinterImages images) throws Exception {
        images.clear();
        Node imagesNode = getChildNode(root, "Images");
        if (imagesNode == null) {
            return;
        }
        NodeList nodes = imagesNode.getChildNodes();
        if (nodes == null) {
            return;
        }
        int count = nodes.getLength();
        for (int i = 0; i < count; i++) {
            Node imageNode = nodes.item(i);
            if (imageNode.getNodeName().equalsIgnoreCase("Image")) {
                PrinterImage image = readPrinterImage(imageNode);
                images.add(image);
            }
        }
    }

    public PrinterImage readPrinterImage(Node imageNode) throws Exception {
        PrinterImage image = new PrinterImage();
        image.setFileName(readParameterStr(imageNode, "FileName"));
        image.setDigest(readParameterStr(imageNode, "Digest"));
        image.setHeight(readParameterInt(imageNode, "Height"));
        image.setStartPos(readParameterInt(imageNode, "FirstLine"));
        image.setIsLoaded(readParameterBool(imageNode, "IsLoaded"));
        return image;
    }

    public void read(ReceiptImages images) throws Exception {
        images.clear();
        Node imagesNode = getChildNode(root, "ReceiptImages");
        if (imagesNode == null) {
            return;
        }
        for (int i = 0; i < imagesNode.getChildNodes().getLength(); i++) {
            Node imageNode = imagesNode.getChildNodes().item(i);
            if (imageNode.getNodeName().equalsIgnoreCase("ReceiptImage")) {
                ReceiptImage image = new ReceiptImage();
                readReceiptImage(imageNode, image);
                images.add(image);
            }
        }
    }

    public int readNonFiscalDocNumber() throws Exception {
        int result = 1;
        Node childNode = getChildNode(root, "NonFiscal");
        if (childNode != null) {
            result = readParameterInt(childNode, "DocumentNumber");
        }
        return result;
    }

    public void readReceiptImage(Node imageNode, ReceiptImage image)
            throws Exception {
        image.setImageIndex(readParameterInt(imageNode, "ImageIndex"));
        image.setPosition(readParameterInt(imageNode, "Position"));
    }

    public void readHeader(PrinterHeader header) throws Exception {
        readLines(header, "Header");
    }

    public void readTrailer(PrinterHeader trailer) throws Exception {
        readLines(trailer, "Trailer");
    }

    public void readLines(PrinterHeader header, String nodeName) throws Exception {
        header.clear();
        Node headerNode = getChildNode(root, nodeName);
        int count = headerNode.getChildNodes().getLength();
        for (int i = 0; i < count; i++) {
            Node lineNode = headerNode.getChildNodes().item(i);
            if (lineNode.getNodeName().equalsIgnoreCase("Line")) {
                read(lineNode, header);
            }
        }
    }

    public void read(Node node, PrinterHeader trailer)
            throws Exception {
        String text = readParameterStr(node, "Text");
        trailer.addLine(text);
    }

    private String readParameterStr(Node node, String paramName)
            throws Exception {
        String result = "";
        NamedNodeMap nodeMap = node.getAttributes();
        if (nodeMap != null) {
            Node paramNode = nodeMap.getNamedItem(paramName);
            if (paramNode != null) {
                result = paramNode.getNodeValue();
            }
        }
        return result;
    }

    private int readParameterInt(Node node, String paramName) throws Exception {
        String s = readParameterStr(node, paramName);
        return Integer.valueOf(s).intValue();
    }

    private boolean readParameterBool(Node node, String paramName)
            throws Exception {
        String paramValue = readParameterStr(node, paramName);
        if ((paramValue.equals("")) || paramValue.equalsIgnoreCase("0")) {
            return false;
        }
        return true;
    }
}
