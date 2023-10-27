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

    private Node findChildNode(Node node, String nodeName) throws Exception {
        Node result = null;
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            result = list.item(i);
            if (result.getNodeName().equalsIgnoreCase(nodeName)) {
                break;
            }
        }
        return result;
    }

    private Node getChildNode(Node node, String nodeName) throws Exception {
        Node result = findChildNode(node, nodeName);
        if (result == null) {
            throw new Exception("Child node not found, " + nodeName);
        }
        return result;
    }

    public void read(FptrParameters params) throws Exception
    {
        readPrinterImages(params.getPrinterImages());
        readReceiptImages(params.getReceiptImages());
        readPrinterHeader(params);
        params.nonFiscalDocNumber = readNonFiscalDocNumber();
        params.isTableTextCleared = readParameterBool("isTableTextCleared");
    }
    
    private void readPrinterImages(PrinterImages images) throws Exception {
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

    private PrinterImage readPrinterImage(Node imageNode) throws Exception {
        PrinterImage image = new PrinterImage();
        image.setFileName(readParameterStr(imageNode, "FileName"));
        image.setDigest(readParameterStr(imageNode, "Digest"));
        image.setHeight(readParameterInt(imageNode, "Height"));
        image.setStartPos(readParameterInt(imageNode, "FirstLine"));
        image.setIsLoaded(readParameterBool(imageNode, "IsLoaded"));
        return image;
    }

    private void readReceiptImages(ReceiptImages images) throws Exception {
        images.clear();
        Node imagesNode = findChildNode(root, "ReceiptImages");
        if (imagesNode == null) {
            return;
        }
        for (int i = 0; i < imagesNode.getChildNodes().getLength(); i++) {
            Node imageNode = imagesNode.getChildNodes().item(i);
            if (imageNode.getNodeName().equalsIgnoreCase("ReceiptImage")) {
                images.add(readReceiptImage(imageNode));
            }
        }
    }

    private int readNonFiscalDocNumber() throws Exception {
        int result = 1;
        Node childNode = findChildNode(root, "NonFiscal");
        if (childNode != null) {
            result = readParameterInt(childNode, "DocumentNumber");
        }
        return result;
    }

    private ReceiptImage readReceiptImage(Node imageNode)
            throws Exception {
        int imageIndex = readParameterInt(imageNode, "ImageIndex");
        int position = readParameterInt(imageNode, "Position");
        return new ReceiptImage(imageIndex, position);
    }

    private void readPrinterHeader(FptrParameters params) throws Exception {
        try {
            for (int i = 1; i <= params.getNumHeaderLines(); i++) {
                params.setHeaderLine(i, "", false);
            }
            for (int i = 1; i <= params.getNumTrailerLines(); i++) {
                params.setTrailerLine(i, "", false);
            }

            // header
            int number = 1;
            Node headerNode = findChildNode(root, "Header");
            if (headerNode ==  null) return;
            
            int count = headerNode.getChildNodes().getLength();
            for (int i = 0; i < count; i++) {
                if (number > params.getNumHeaderLines()) {
                    break;
                }
                Node lineNode = headerNode.getChildNodes().item(i);
                if (lineNode.getNodeName().equalsIgnoreCase("Line")) {
                    String text = readParameterStr(lineNode, "Text");
                    boolean doubleWidth = readParameterBool(lineNode, "DoubleWidth");
                    params.setHeaderLine(number, text, doubleWidth);
                    number++;
                }
            }
            number = 1;
            headerNode = getChildNode(root, "Trailer");
            if (headerNode ==  null) return;
            
            count = headerNode.getChildNodes().getLength();
            for (int i = 0; i < count; i++) {
                if (number > params.getNumTrailerLines()) {
                    break;
                }
                Node lineNode = headerNode.getChildNodes().item(i);
                if (lineNode.getNodeName().equalsIgnoreCase("Line")) {
                    String text = readParameterStr(lineNode, "Text");
                    boolean doubleWidth = readParameterBool(lineNode, "DoubleWidth");
                    params.setTrailerLine(number, text, doubleWidth);
                    number++;
                }
            }
        } catch (Exception e) {
            logger.error("readPrinterHeader", e);
        }
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
        int result = 0;
        try {
            String s = readParameterStr(node, paramName);
            result = Integer.valueOf(s).intValue();
        } catch (Exception e) {
            logger.error("readParameterInt failed, " + paramName, e);
        }
        return result;
    }

    private boolean readParameterBool(Node node, String paramName)
            throws Exception {
        String paramValue = readParameterStr(node, paramName);
        if ((paramValue.equals("")) || paramValue.equalsIgnoreCase("0")) {
            return false;
        }
        return true;
    }
    private Node getRoot(){
        return root;
    }

    private String readParameterStr(String paramName)
            throws Exception {
        return readParameterStr(root, paramName);
    }

    private int readParameterInt(String paramName) throws Exception {
        return readParameterInt(root, paramName);
    }

    private boolean readParameterBool(String paramName) throws Exception{
        return readParameterBool(root, paramName);
    }

}
