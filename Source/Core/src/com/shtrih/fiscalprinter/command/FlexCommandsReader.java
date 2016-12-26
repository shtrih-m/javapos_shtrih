/*
 * FlexCommandsReader.java
 *
 * Created on November 19, 2009, 16:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
import java.io.InputStream;

import com.shtrih.util.CompositeLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.shtrih.util.ResourceLoader;
import com.shtrih.util.XmlUtils;

public class FlexCommandsReader {

    static CompositeLogger logger = CompositeLogger.getLogger(FlexCommandsReader.class);

    /**
     * Creates a new instance of FlexCommandsReader
     */
    public FlexCommandsReader() {
    }

    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if ((nl != null) && (nl.getLength() > 0)) {
            Element el = (Element) nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }
        return textVal;
    }

    /**
     * Calls getTextValue and returns a int value
     * 
     * @param ele
     * @param tagName
     * @return
     */
    private int getIntValue(Element ele, String tagName) {
        try {
            return Integer.decode(getTextValue(ele, tagName)).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    private int getIntValue(Element ele, String tagName, int defValue) {
        try {
            return Integer.parseInt(getTextValue(ele, tagName));
        } catch (Exception e) {
            return defValue;
        }
    }

    private CommandParam readParam(Element element) {
        String name = getTextValue(element, "Name");
        int size = getIntValue(element, "Size");
        int type = getIntValue(element, "Type");
        int min = getIntValue(element, "MinValue");
        int max = getIntValue(element, "MaxValue");
        // String defaultValue = getTextValue(element, "defaultValue");
        String defaultValue = "";
        return new CommandParam(name, size, type, min, max, defaultValue);
    }

    private CommandParams readParams(Element element, String tagName) {
        CommandParams result = new CommandParams();
        NodeList nl = element.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                Element el = (Element) nl.item(i);
                NodeList nlparam = el.getElementsByTagName("Param");
                if (nlparam != null) {
                    for (int j = 0; j < nlparam.getLength(); j++) {
                        Element elparam = (Element) nlparam.item(j);
                        result.add(readParam(elparam));
                    }
                }
            }
        }
        return result;
    }

    private FlexCommand getCommand(Element element) {
        int code = getIntValue(element, "Code");
        String text = getTextValue(element, "Name");
        int timeout = getIntValue(element, "Timeout",
                PrinterCommand.getDefaultTimeout(code));

        CommandParams inParams = readParams(element, "InParams");
        CommandParams outParams = readParams(element, "OutParams");
        FlexCommand command = new FlexCommand();
        command.setCode(code);
        command.setText(text);
        command.setTimeout(timeout);
        command.setInParams(inParams);
        command.setOutParams(outParams);
        return command;
    }

    public void load(String fileName, FlexCommands commands) throws Exception {
        InputStream stream = ResourceLoader.load(fileName);
        load(stream, commands);
    }
    
    public void load(FlexCommands commands) throws Exception {
        InputStream stream = ResourceLoader.load("commands.xml");
        load(stream, commands);
    }

    public void load(InputStream stream, FlexCommands commands)
            throws Exception {
        Document dom = XmlUtils.parse(stream);
        load(dom, commands);
    }

    public void load(Document document, FlexCommands commands) throws Exception {
        commands.clear();
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("Commands");
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                NodeList nlcommand = element.getElementsByTagName("Command");
                if (nlcommand != null) {
                    for (int j = 0; j < nlcommand.getLength(); j++) {
                        Element elcommand = (Element) nlcommand.item(j);
                        try {
                            FlexCommand command = getCommand(elcommand);
                            commands.add(command);
                        } catch (Exception e) {
                            logger.error("loadFromXml error", e);
                        }
                    }
                }
            }
        }
    }
}
