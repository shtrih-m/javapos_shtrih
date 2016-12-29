///*
// * XmlZReport.java
// *
// * Created on 25 Январь 2011 г., 18:06
// *
// * To change this template, choose Tools | Template Manager
// * and open the template in the editor.
// */
//
//package com.shtrih.jpos.fiscalprinter;
//
///**
// *
// * @author V.Kravtsov
// */
//
//import jpos.*;
//import java.io.*;
//import java.util.*;
//import java.util.Vector;
//import java.util.StringTokenizer;
//// DOM
//import org.w3c.dom.*;
//import org.xml.sax.*;
//// apache
//import org.apache.log4j.*;
//import org.xml.sax.InputSource;
//import org.apache.xml.serialize.*;
//import org.apache.xerces.dom.DocumentImpl;
//import org.apache.xerces.parsers.DOMParser;
//
//import com.shtrih.fiscalprinter.*;
//import com.shtrih.fiscalprinter.SMFiscalPrinter;
//import com.shtrih.fiscalprinter.command.CashRegister;
//import com.shtrih.fiscalprinter.command.ReadCashRegister;
//import com.shtrih.fiscalprinter.command.OperationRegister;
//import com.shtrih.fiscalprinter.command.ReadOperationRegister;
//import com.shtrih.util.XmlUtils;
//
//
//public class XmlZReport {
//
//    private final SMFiscalPrinter printer;
//
//    /** Creates a new instance of XmlZReport */
//    public XmlZReport(SMFiscalPrinter printer) {
//        this.printer = printer;
//    }
//
//    private void setParameter(Node root, Document xmldoc, String parameterName,
//        String parameterValue) {
//        Node node = xmldoc.createElement(parameterName);
//        node.appendChild(xmldoc.createTextNode(parameterValue));
//        root.appendChild(node);
//    }
//
//    public void execute(String fileName)
//    throws Exception {
//        Vector cashRegisters = new Vector();
//        for (int i=0;i<=255;i++) {
//            ReadCashRegister command = printer.readCashRegister2(i);
//            long value = command.getValue();
//            cashRegisters.add(new CashRegister(i, value));
//        }
//        Vector operRegisters = new Vector();
//        for (int i=0;i<=255;i++) {
//            ReadOperationRegister command = printer.readOperationRegister2(i);
//            int value = command.getValue();
//            operRegisters.add(new OperationRegister(i, value));
//        }
//
//        Element node;
//        Node registersNode;
//        FileWriter writer = new FileWriter(fileName);
//        Document xmldoc = XmlUtils.newDocument();
//        Element root = xmldoc.createElement("ZReport");
//        xmldoc.appendChild(root);
//
//        registersNode = xmldoc.createElement("CashRegisters");
//        root.appendChild(registersNode);
//        for (int i=0;i<cashRegisters.size();i++) {
//            CashRegister cashRegister = (CashRegister)cashRegisters.get(i);
//            node = xmldoc.createElement("CashRegister");
//            registersNode.appendChild(node);
//            node.setAttribute("Number", String.valueOf(cashRegister.getNumber()));
//            node.setAttribute("Value", String.valueOf(cashRegister.getValue()));
//        }
//        registersNode = xmldoc.createElement("OperationRegisters");
//        root.appendChild(registersNode);
//        for (int i=0;i<operRegisters.size();i++) {
//            OperationRegister operRegister = (OperationRegister)operRegisters.get(i);
//            node = xmldoc.createElement("OperationRegister");
//            registersNode.appendChild(node);
//            node.setAttribute("Number", String.valueOf(operRegister.getNumber()));
//            node.setAttribute("Value", String.valueOf(operRegister.getValue()));
//        }
//        // convert document to string
//        OutputFormat of = new OutputFormat();
//        of.setIndent(1);
//        of.setIndenting(true);
//        XMLSerializer serializer = new XMLSerializer(writer, of);
//        // As a DOM Serializer
//        serializer.asDOMSerializer();
//        serializer.serialize( xmldoc.getDocumentElement());
//    }
//
//}
