/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.model;

/**
 * @author V.Kravtsov
 */

import java.io.InputStream;
import java.util.HashMap;

import com.shtrih.util.CompositeLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.shtrih.fiscalprinter.command.ParameterValue;
import com.shtrih.fiscalprinter.command.ParameterValues;
import com.shtrih.fiscalprinter.command.PrinterParameter;
import com.shtrih.fiscalprinter.command.PrinterParameters;
import com.shtrih.util.StringUtils;
import com.shtrih.util.XmlUtils;
import org.w3c.dom.NamedNodeMap;

public class XmlModelsReader {

    private static CompositeLogger logger = CompositeLogger.getLogger(XmlModelsReader.class);

    private final PrinterModels models;

    public XmlModelsReader(PrinterModels models) {
        this.models = models;
    }

    public void load(InputStream stream) throws Exception {
        Document document = XmlUtils.parse(stream);
        parse(document);
    }

    private void parse(Document doc) throws Exception {
        if (doc == null) {
            return;
        }
        Element root = doc.getDocumentElement();
        if (root == null) {
            return;
        }
        Node modelsNode = getChildNode(root, "models");
        if (modelsNode == null) {
            return;
        }
        NodeList list = modelsNode.getChildNodes();
        if (list == null) {
            return;
        }
        models.clear();
        for (int i = 0; i < list.getLength(); i++) {
            Node modelNode = list.item(i);
            if (modelNode.getNodeName().equalsIgnoreCase("model")) {
                loadModel(modelNode);
            }
        }
    }

    private Node getChildNode(Node node, String nodeName)
            throws Exception {
        Node result = findChildNode(node, nodeName);
        if (result == null) {
            throw new Exception("Child node not found");
        }
        return result;
    }

    private Node findChildNode(Node node, String nodeName) {
        Node result = null;
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            result = list.item(i);
            if (result.getNodeName().equalsIgnoreCase(nodeName)) {
                return result;
            }
        }
        return null;
    }

    private String readParameterStr(Node node, String paramName)
            throws Exception {
        //logger.debug("readParameterStr, " + paramName);
        String result = "";
        Element element = (Element) node;
        Node paramNode = element.getElementsByTagName(paramName).item(0);
        if (paramNode != null) {
            Node childNode = paramNode.getFirstChild();
            if (childNode != null) {
                result = childNode.getNodeValue();
            }
        }
        return result;
    }

    private void loadModel(Node node) throws Exception {
        try {
            PrinterModelDefault model = new PrinterModelDefault();
            model.setName(readParameterStr(node, "Name"));
            model.setId(readParameterInt(node, "Id"));
            model.setModelID(readParameterInt(node, "ModelID"));
            model.setProtocolVersion(readParameterInt(node, "ProtocolVersion"));
            model.setProtocolSubVersion(readParameterInt(node,
                    "ProtocolSubVersion"));
            model.setCapEJPresent(readParameterBool(node, "CapEJPresent"));
            model.setCapFMPresent(readParameterBool(node, "CapFMPresent"));
            model.setCapRecPresent(readParameterBool(node, "CapRecPresent"));
            model.setCapJrnPresent(readParameterBool(node, "CapJrnPresent"));
            model.setCapSlpPresent(readParameterBool(node, "CapSlpPresent"));
            model.setCapSlpEmptySensor(readParameterBool(node,
                    "CapSlpEmptySensor"));
            model.setCapSlpNearEndSensor(readParameterBool(node,
                    "CapSlpNearEndSensor"));
            model.setCapRecEmptySensor(readParameterBool(node,
                    "CapRecEmptySensor"));
            model.setCapRecEmptySensor(readParameterBool(node,
                    "CapRecEmptySensor"));
            model.setCapRecNearEndSensor(readParameterBool(node,
                    "CapRecNearEndSensor"));
            model.setCapRecLeverSensor(readParameterBool(node,
                    "CapRecLeverSensor"));
            model.setCapJrnEmptySensor(readParameterBool(node,
                    "CapJrnEmptySensor"));
            model.setCapJrnNearEndSensor(readParameterBool(node,
                    "CapJrnNearEndSensor"));
            model.setCapJrnLeverSensor(readParameterBool(node,
                    "CapJrnLeverSensor"));
            model.setCapPrintGraphicsLine(readParameterBool(node,
                    "CapPrintGraphicsLine"));
            model.setCapHasVatTable(readParameterBool(node, "CapHasVatTable"));
            model.setCapCoverSensor(readParameterBool(node, "CapCoverSensor"));
            model.setCapDoubleWidth(readParameterBool(node, "CapDoubleWidth"));
            model.setCapDuplicateReceipt(readParameterBool(node,
                    "CapDuplicateReceipt"));
            model.setCapFullCut(readParameterBool(node, "CapFullCut"));
            model.setCapPartialCut(readParameterBool(node, "CapPartialCut"));
            model.setCapGraphics(readParameterBool(node, "CapGraphics"));
            model.setCapGraphicsEx(readParameterBool(node, "CapGraphicsEx"));
            model.setCapPrintStringFont(readParameterBool(node,
                    "CapPrintStringFont"));
            model.setCapShortStatus(readParameterBool(node, "CapShortStatus"));
            model.setCapFontMetrics(readParameterBool(node, "CapFontMetrics"));
            model.setCapOpenReceipt(readParameterBool(node, "CapOpenReceipt"));
            model.setNumVatRates(readParameterInt(node, "NumVatRates"));
            model.setAmountDecimalPlace(readParameterInt(node,
                    "AmountDecimalPlace"));
            model.setNumHeaderLines(readParameterInt(node, "NumHeaderLines"));
            model.setNumTrailerLines(readParameterInt(node, "NumTrailerLines"));
            model.setTrailerTableNumber(readParameterInt(node,
                    "TrailerTableNumber"));
            model.setHeaderTableNumber(readParameterInt(node,
                    "HeaderTableNumber"));
            model.setHeaderTableRow(readParameterInt(node, "HeaderTableRow"));
            model.setTrailerTableRow(readParameterInt(node, "TrailerTableRow"));
            model.setMinHeaderLines(readParameterInt(node, "MinHeaderLines"));
            model.setMinTrailerLines(readParameterInt(node, "MinTrailerLines"));
            model.setMaxGraphicsWidth(readParameterInt(node, "MaxGraphicsWidth"));
            model.setMaxGraphicsHeight(readParameterInt(node,
                    "MaxGraphicsHeight"));
            model.setPrintWidth(readParameterInt(node, "PrintWidth"));
            model.setTextLength(readParameterIntArray(node, "TextLength"));
            model.setFontHeight(readParameterIntArray(node, "FontHeight"));
            model.setSupportedBaudRates(readParameterIntArray(node,
                    "SupportedBaudRates"));
            model.setCapCashInAutoCut(readParameterBool(node,
                    "CapCashInAutoCut"));
            model.setCapCashOutAutoCut(readParameterBool(node,
                    "CapCashOutAutoCut"));
            model.setCapPrintBarcode2(readParameterBool(node,
                    "CapPrintBarcode2"));

            model.setDeviceFontNormal(readParameterInt(node, "DeviceFontNormal"));
            model.setDeviceFontDouble(readParameterInt(node, "DeviceFontDouble"));
            model.setDeviceFontSmall(readParameterInt(node, "DeviceFontSmall"));
            model.setSwapGraphicsLine(readParameterBool(node, "SwapGraphicsLine"));

            model.setMinCashRegister(readParameterInt(node, "MinCashRegister", 0));
            model.setMaxCashRegister(readParameterInt(node, "MaxCashRegister", 0xFF));
            model.setMinCashRegister2(readParameterInt(node, "MinCashRegister2", 0));
            model.setMaxCashRegister2(readParameterInt(node, "MaxCashRegister2", -1));
            model.setMinOperationRegister(readParameterInt(node, "MinOperationRegister", 0));
            model.setMaxOperationRegister(readParameterInt(node, "MaxOperationRegister", 0xFF));
            model.setCapGraphicsLineMargin(readParameterBool(node, "CapGraphicsLineMargin"));
            model.setCapFSCloseCheck(readParameterBool(node, "CapFSCloseCheck", true));

            loadParameters(node, model.getParameters());

            models.add(model);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void loadParameters(Node node, PrinterParameters items)
            throws Exception {
        Element element = (Element) node;
        NodeList parametersNodes = element.getElementsByTagName("parameters");
        if (parametersNodes == null) {
            return;
        }
        Element element2 = (Element) parametersNodes.item(0);
        if (element2 == null) {
            return;
        }
        NodeList nodes = element2.getElementsByTagName("parameter");
        if (nodes == null) {
            return;
        }

        for (int i = 0; i < nodes.getLength(); i++) {
            Node paramNode = nodes.item(i);
            String name = readParameterStr(paramNode, "Name");
            String text = readParameterStr(paramNode, "Text");
            int table = readParameterInt(paramNode, "TableNumber");
            int row = readParameterInt(paramNode, "RowNumber");
            int field = readParameterInt(paramNode, "FieldNumber");
            PrinterParameter parameter = new PrinterParameter(name, text, table, row,
                    field);
            loadValues(parameter.getValues(), paramNode);
            items.add(parameter);
        }
    }

    private void loadValues(ParameterValues values, Node node) throws Exception {
        Element element = (Element) node;
        Node paramsNode = element.getElementsByTagName("values").item(0);
        NodeList nodes = element.getElementsByTagName("value");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node itemNode = nodes.item(i);
            ParameterValue item = new ParameterValue();
            item.setValue(readParameterInt(itemNode, "Value"));
            item.setFieldValue(readParameterInt(itemNode, "FieldValue"));
            values.add(item);
        }
    }

    private int readParameterInt(Node node, String paramName) throws Exception {
        String s = readParameterStr(node, paramName);
        return Integer.valueOf(s).intValue();
    }

    private int readParameterInt(Node node, String paramName, int defValue)
            throws Exception {
        int result = defValue;
        try {
            String s = readParameterStr(node, paramName);
            result = Integer.valueOf(s).intValue();
        } catch (Exception e) {
            //logger.error(e);
        }
        return result;
    }

    private int[] readParameterIntArray(Node node, String paramName)
            throws Exception {
        String s = readParameterStr(node, paramName);
        return StringUtils.strToIntArray(s);
    }

    private boolean readParameterBool(Node node, String paramName)
            throws Exception {
        return readParameterBool(node, paramName, false);
    }

    private boolean readParameterBool(Node node, String paramName, boolean defaultValue)
            throws Exception {
        String paramValue = readParameterStr(node, paramName);
        if (paramValue.equals("")) {
            return defaultValue;
        }

        if (paramValue.equalsIgnoreCase("0")) {
            return false;
        }
        
        return true;
    }
}
