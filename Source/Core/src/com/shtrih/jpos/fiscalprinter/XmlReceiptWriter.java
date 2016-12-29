/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import java.io.File;

import com.shtrih.util.CompositeLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.shtrih.util.XmlUtils;

/**
 * @author V.Kravtsov
 */
public class XmlReceiptWriter {

    private static CompositeLogger logger = CompositeLogger.getLogger(XmlReceiptWriter.class);

    public XmlReceiptWriter() {
    }

    public void save(ReceiptReport report, String fileName) {
        try {
            Document doc = XmlUtils.newDocument();
            File file = new File(fileName);
            if (file.exists()) {
                doc = XmlUtils.parse(file);
            }
            add(report, doc);
            XmlUtils.save(doc, fileName);
        } catch (Exception e) {
            logger.error("save", e);
        }
    }

    public Node getChildNode(Document doc, Node node, String nodeName)
            throws Exception {
        Node result = null;
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            result = list.item(i);
            if (result.getNodeName().equalsIgnoreCase(nodeName)) {
                return result;
            }
        }
        result = doc.createElement(nodeName);
        node.appendChild(result);
        return result;
    }

    public void setNodeInt(Document doc, Node root, String name, long value) {
        Node node = root.appendChild(doc.createElement(name));
        Text text = doc.createTextNode(name);
        text.setData(String.valueOf(value));
        node.appendChild(text);
    }

    public void setNodeStr(Document doc, Node root, String name, String value) {
        Node node = root.appendChild(doc.createElement(name));
        Text text = doc.createTextNode(name);
        text.setData(value);
        node.appendChild(text);
    }

    public void add(ReceiptReport report, Document doc) throws Exception {

        Node root = doc.getDocumentElement();
        if (root == null) {
            root = doc.createElement("root");
            doc.appendChild(root);
        }
        root = getChildNode(doc, root, "receipts");
        Node node = root.appendChild(doc.createElement("receipt"));
        setNodeInt(doc, node, "id", report.id);
        setNodeInt(doc, node, "docid", report.docID);
        setNodeInt(doc, node, "type", report.recType);
        setNodeInt(doc, node, "state", report.state);
        setNodeInt(doc, node, "amount", report.amount);
        String dateText = report.date.toString() + " " + report.time.toString();
        setNodeStr(doc, node, "date", dateText);

        long paymentAmount = report.getPaymentTotal();
        if (paymentAmount != 0) {
            Node paymentsNode = node.appendChild(doc.createElement("payments"));
            for (int i = 0; i < 4; i++) {
                if (report.payments[i] != 0) {
                    Node paymentNode = paymentsNode.appendChild(doc
                            .createElement("payment"));
                    setNodeInt(doc, paymentNode, "type", i);
                    setNodeInt(doc, paymentNode, "amount", report.payments[i]);
                }
            }
        }
    }

}
