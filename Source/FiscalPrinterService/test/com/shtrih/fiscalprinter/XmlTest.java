/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlTest extends TestCase {

    public XmlTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of clear method, of class PrinterModels.
     */
    public void testTextElement() {
        System.out.println("testTextElement");

        /*
        Document xmldoc = new DocumentImpl();
        Node node = xmldoc.createElement("root");
        xmldoc.appendChild(node);
        Node paramNode = xmldoc.createElement("parameter1");
        node.appendChild(paramNode);
        paramNode.appendChild(xmldoc.createTextNode("123"));
        
        Element element = (Element)node;
        Node childNode = element.getElementsByTagName("parameter1").item(0);
        assertEquals("parameter1", childNode.getNodeName());
        assertEquals("123", childNode.getFirstChild().getNodeValue());
                */
    }
}
