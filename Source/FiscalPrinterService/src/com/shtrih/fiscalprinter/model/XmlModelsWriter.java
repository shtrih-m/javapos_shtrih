/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.model;

/**
 *
 * @author V.Kravtsov
 */

import java.io.FileWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.shtrih.fiscalprinter.command.ParameterValue;
import com.shtrih.fiscalprinter.command.ParameterValues;
import com.shtrih.fiscalprinter.command.PrinterParameter;
import com.shtrih.fiscalprinter.command.PrinterParameters;
import com.shtrih.util.StringUtils;
import com.shtrih.util.XmlUtils;

public class XmlModelsWriter {

    private final Document xmldoc;
    private final PrinterModels models;

    public XmlModelsWriter(PrinterModels models) throws Exception {
        this.models = models;
        xmldoc = XmlUtils.newDocument();
    }

    public void save(String fileName) throws Exception {
        Element node;
        Node modelsNode;
        FileWriter writer = new FileWriter(fileName);
        try {
            Element root = xmldoc.createElement("root");
            xmldoc.appendChild(root);

            modelsNode = xmldoc.createElement("models");
            root.appendChild(modelsNode);
            for (int i = 0; i < models.size(); i++) {
                saveModel(models.get(i), modelsNode);
            }
            XmlUtils.save(xmldoc, fileName);
        } finally {
            writer.close();
        }
    }

    private void saveParameter(Node root, String name, boolean value)
            throws Exception {
        saveParameter(root, name, StringUtils.boolToStr(value));
    }

    private void saveParameter(Node root, String name, int value)
            throws Exception {
        saveParameter(root, name, String.valueOf(value));
    }

    private void saveParameter(Node root, String name, int[] value)
            throws Exception {
        saveParameter(root, name, StringUtils.arrayToStr(value));
    }

    private void saveParameter(Node root, String name, String value)
            throws Exception {
        Element node = xmldoc.createElement(name);
        node.appendChild(xmldoc.createTextNode(value));
        root.appendChild(node);
    }

    private void saveModel(PrinterModel model, Node root) throws Exception {
        Node node = xmldoc.createElement("model");
        root.appendChild(node);

        saveParameter(node, "Name", model.getName());
        saveParameter(node, "Id", model.getId());
        saveParameter(node, "ModelID", model.getModelID());
        saveParameter(node, "ProtocolVersion", model.getProtocolVersion());
        saveParameter(node, "ProtocolSubVersion", model.getProtocolSubVersion());
        saveParameter(node, "CapEJPresent", model.getCapEJPresent());
        saveParameter(node, "CapFMPresent", model.getCapFMPresent());
        saveParameter(node, "CapRecPresent", model.getCapRecPresent());
        saveParameter(node, "CapJrnPresent", model.getCapJrnPresent());
        saveParameter(node, "CapSlpPresent", model.getCapSlpPresent());
        saveParameter(node, "CapSlpEmptySensor", model.getCapSlpEmptySensor());
        saveParameter(node, "CapSlpNearEndSensor",
                model.getCapSlpNearEndSensor());
        saveParameter(node, "CapRecEmptySensor", model.getCapRecEmptySensor());
        saveParameter(node, "CapRecEmptySensor", model.getCapRecEmptySensor());
        saveParameter(node, "CapRecNearEndSensor",
                model.getCapRecNearEndSensor());
        saveParameter(node, "CapRecLeverSensor", model.getCapRecLeverSensor());
        saveParameter(node, "CapJrnEmptySensor", model.getCapJrnEmptySensor());
        saveParameter(node, "CapJrnNearEndSensor",
                model.getCapJrnNearEndSensor());
        saveParameter(node, "CapJrnLeverSensor", model.getCapJrnLeverSensor());
        saveParameter(node, "CapPrintGraphicsLine",
                model.getCapPrintGraphicsLine());
        saveParameter(node, "CapHasVatTable", model.getCapHasVatTable());
        saveParameter(node, "CapCoverSensor", model.getCapCoverSensor());
        saveParameter(node, "CapDoubleWidth", model.getCapDoubleWidth());
        saveParameter(node, "CapDuplicateReceipt",
                model.getCapDuplicateReceipt());
        saveParameter(node, "CapFullCut", model.getCapFullCut());
        saveParameter(node, "CapPartialCut", model.getCapPartialCut());
        saveParameter(node, "CapGraphics", model.getCapGraphics());
        saveParameter(node, "CapGraphicsEx", model.getCapGraphicsEx());
        saveParameter(node, "CapPrintStringFont", model.getCapPrintStringFont());
        saveParameter(node, "CapShortStatus", model.getCapShortStatus());
        saveParameter(node, "CapFontMetrics", model.getCapFontMetrics());
        saveParameter(node, "CapOpenReceipt", model.getCapOpenReceipt());
        saveParameter(node, "NumVatRates", model.getNumVatRates());
        saveParameter(node, "PrintWidth", model.getPrintWidth());
        saveParameter(node, "AmountDecimalPlace", model.getAmountDecimalPlace());
        saveParameter(node, "NumHeaderLines", model.getNumHeaderLines());
        saveParameter(node, "NumTrailerLines", model.getNumTrailerLines());
        saveParameter(node, "TrailerTableNumber", model.getTrailerTableNumber());
        saveParameter(node, "HeaderTableNumber", model.getHeaderTableNumber());
        saveParameter(node, "HeaderTableRow", model.getHeaderTableRow());
        saveParameter(node, "TrailerTableRow", model.getTrailerTableRow());
        saveParameter(node, "MinHeaderLines", model.getMinHeaderLines());
        saveParameter(node, "MinTrailerLines", model.getMinTrailerLines());
        saveParameter(node, "MaxGraphicsWidth", model.getMaxGraphicsWidth());
        saveParameter(node, "MaxGraphicsHeight", model.getMaxGraphicsHeight());
        saveParameter(node, "TextLength", model.getTextLength());
        saveParameter(node, "FontHeight", model.getFontHeight());
        saveParameter(node, "SupportedBaudRates", model.getSupportedBaudRates());
        saveParameter(node, "CapCashInAutoCut", model.getCapCashInAutoCut());
        saveParameter(node, "CapCashOutAutoCut", model.getCapCashOutAutoCut());
        saveParameter(node, "CapPrintBarcode2", model.getCapPrintBarcode2());
        saveParameter(node, "DeviceFontNormal", model.getDeviceFontNormal());
        saveParameter(node, "DeviceFontDouble", model.getDeviceFontDouble());
        saveParameter(node, "DeviceFontSmall", model.getDeviceFontSmall());
        saveParameter(node, "SwapGraphicsLine", model.getSwapGraphicsLine());
        
        saveParameter(node, "MinCashRegister", model.getMinCashRegister());
        saveParameter(node, "MaxCashRegister", model.getMaxCashRegister());
        saveParameter(node, "MinCashRegister2", model.getMinCashRegister2());
        saveParameter(node, "MaxCashRegister2", model.getMaxCashRegister2());
        saveParameter(node, "MinOperationRegister", model.getMinOperationRegister());
        saveParameter(node, "MaxOperationRegister", model.getMaxOperationRegister());
        saveParameter(node, "CapGraphicsLineMargin", model.getCapGraphicsLineMargin());
        saveParameters(node, model.getParameters());
    }

    private void saveParameters(Node node, PrinterParameters items)
            throws Exception {
        Node itemsNode = xmldoc.createElement("parameters");
        node.appendChild(itemsNode);
        for (int i = 0; i < items.size(); i++) {
            PrinterParameter item = items.get(i);
            Node itemNode = xmldoc.createElement("parameter");
            itemsNode.appendChild(itemNode);
            saveParameter(itemNode, "Name", item.getName());
            saveParameter(itemNode, "Text", item.getText());
            saveParameter(itemNode, "TableNumber", item.getTableNumber());
            saveParameter(itemNode, "RowNumber", item.getRowNumber());
            saveParameter(itemNode, "FieldNumber", item.getFieldNumber());
            saveValues(itemNode, item.getValues());
        }
    }

    private void saveValues(Node node, ParameterValues items) throws Exception {
        Node itemsNode = xmldoc.createElement("values");
        node.appendChild(itemsNode);
        for (int i = 0; i < items.size(); i++) {
            ParameterValue item = items.get(i);
            Node itemNode = xmldoc.createElement("value");
            itemsNode.appendChild(itemNode);
            saveParameter(itemNode, "Value", item.getValue());
            saveParameter(itemNode, "FieldValue", item.getFieldValue());
        }
    }
}
