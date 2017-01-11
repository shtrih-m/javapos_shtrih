/*
 * JposDeviceStatistics.java
 *
 * Created on April 6 2008, 15:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.jpos;

/**
 *
 * @author V.Kravtsov
 */
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.Vector;

import jpos.JposConst;
import jpos.JposException;
import jpos.JposStatisticsConst;

import com.shtrih.util.CompositeLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.shtrih.util.SysUtils;
import com.shtrih.util.XmlUtils;

public class JposDeviceStatistics implements JposStatistics,
        JposStatisticsConst, JposConst {

    protected static CompositeLogger logger = CompositeLogger
            .getLogger(JposDeviceStatistics.class);

    public static final String JPOS_STAT_UnifiedPOS = "U_";
    public static final String JPOS_STAT_Manufacturer = "M_";
    //
    private static final String JPOS_STAT_UnifiedPOSVersion = "UnifiedPOSVersion";
    private static final String JPOS_STAT_DeviceCategory = "DeviceCategory";
    private static final String JPOS_STAT_ManufacturerName = "ManufacturerName";
    private static final String JPOS_STAT_ModelName = "ModelName";
    private static final String JPOS_STAT_SerialNumber = "SerialNumber";
    private static final String JPOS_STAT_FirmwareRevision = "FirmwareRevision";
    private static final String JPOS_STAT_Interface = "Interface";
    private static final String JPOS_STAT_InstallationDate = "InstallationDate";

    public String unifiedPOSVersion = "";
    public String deviceCategory = "";
    public String manufacturerName = "";
    public String modelName = "";
    public String serialNumber = "";
    public String firmwareRevision = "";
    public String physicalInterface = "";
    public String installationDate = "";

    public final StatisticItems items = new StatisticItems(JPOS_STAT_UnifiedPOS);

    /**
     * Creates a new instance of JposDeviceStatistics
     */
    public JposDeviceStatistics() {
    }

    public void incStatistic(String statisticName, long value) {
        StatisticItem item = items.itemByName(statisticName);
        if (item != null) {
            item.inc(value);
        }
    }

    public void save(String fileName) {
        try {
            FileWriter writer = new FileWriter(SysUtils.getFilesPath() + fileName);
            try {
                retrieveData("", writer);
            } finally {
                writer.close();
            }
        } catch (Exception e) {
            logger.error("Error saving file", e);
        }
    }

    public void load(String xmlFileName) {
        try {
            Node node = null;
            File xmlFile = new File(SysUtils.getFilesPath() + xmlFileName);
            if (xmlFile.exists()) {
                clear();
                Document xmldoc = XmlUtils.parse(xmlFile);
                Node root = xmldoc.getDocumentElement();
                // Equipment
                node = findChildNode(root, "Equipment");
                if (node != null) {
                    unifiedPOSVersion = getParameter(node,
                            JPOS_STAT_UnifiedPOSVersion);
                    deviceCategory = getAttribute(node,
                            JPOS_STAT_DeviceCategory, "UPOS");
                    manufacturerName = getParameter(node,
                            JPOS_STAT_ManufacturerName);
                    modelName = getParameter(node, JPOS_STAT_ModelName);
                    serialNumber = getParameter(node, JPOS_STAT_SerialNumber);
                    firmwareRevision = getParameter(node,
                            JPOS_STAT_FirmwareRevision);
                    physicalInterface = getParameter(node, JPOS_STAT_Interface);
                    installationDate = getParameter(node,
                            JPOS_STAT_InstallationDate);
                }
                // Event
                node = findChildNode(root, "Event");
                if (node != null) {
                    NodeList nodes = node.getChildNodes();
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Node child = nodes.item(i);
                        if (child.getNodeName().equalsIgnoreCase("Parameter")) {
                            String paramName = getParameter(child, "Name");
                            String paramValue = getParameter(child, "Value");
                            if (paramName.length() != 0) {
                                StatisticItem item = new StatisticItem(
                                        paramName, paramValue);
                                items.add(item);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error parsing xml file", e);
        }
        if (installationDate.length() == 0) {
            installationDate = getCurrentDate();
        }
    }

    
    public void reset(String statisticsBuffer) throws JposException {
        logger.debug("reset");
        items.select(statisticsBuffer).reset();
    }

    /*
     * This is a comma-separated list of name-value pair(s), where an empty
     * string name (““”=value1”) means ALL resettable statistics are to be set
     * to the value “value1”, “U_=value2” means all UnifiedPOS defined
     * resettable statistics are to be set to the value “value2”, “M_=value3”
     * means all manufacturer defined resettable statistics are to be set to the
     * value “value3”, and “actual_name1=value4, actual_name2=value5” (from the
     * XML file definitions) means that the specifically defined resettable
     * statistic(s) are to be set to the specified value(s)
     */
    
    public void update(String statisticsBuffer) throws JposException {
        logger.debug("update");
        items.update(statisticsBuffer);
    }

    
    public void retrieve(String[] statisticsBuffer) throws JposException {
        logger.debug("retrieve");
        try {
            statisticsBuffer[0] = retrieveData(statisticsBuffer[0]);
        } catch (Exception e) {
            logger.error(e);
            throw new JposException(JPOS_E_FAILURE, e.getMessage(), e);
        }
    }

    public void setParameter(Node root, Document xmldoc, String parameterName,
            String parameterValue) {
        // logger.debug("retrieve");
        Node node = xmldoc.createElement(parameterName);
        node.appendChild(xmldoc.createTextNode(parameterValue));
        root.appendChild(node);
    }

    private Node findChildNode(Node root, String nodeName) {
        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName().equalsIgnoreCase(nodeName)) {
                return node;
            }
        }
        return null;
    }

    private String getParameter(Node root, String parameterName) {
        String result = "";
        Node node = findChildNode(root, parameterName);
        if (node != null) {
            node = node.getFirstChild();
            if (node != null) {
                result = node.getNodeValue();
            }
        }
        return result;
    }

    private String getAttribute(Node root, String nodeName, String attributeName) {
        Element node = (Element) findChildNode(root, nodeName);
        if (node != null) {
            return node.getAttribute(attributeName);
        }
        return "";
    }

    public void setAttribute(Node root, Document xmldoc, String nodeName,
            String attributeName, String attributeValue) {
        Element node = xmldoc.createElement(nodeName);
        node.setAttribute(attributeName, attributeValue);
        root.appendChild(node);
    }

    private void clear() {
        items.clear();
        unifiedPOSVersion = "";
        deviceCategory = "";
        manufacturerName = "";
        modelName = "";
        serialNumber = "";
        firmwareRevision = "";
        physicalInterface = "";
        installationDate = "";
    }

    // 2000-03-01
    private String getCurrentDate() {
        GregorianCalendar calendar = new GregorianCalendar();
        return String.valueOf(calendar.get(GregorianCalendar.YEAR)) + "-"
                + String.valueOf(calendar.get(GregorianCalendar.MONTH)) + "-"
                + String.valueOf(calendar.get(GregorianCalendar.DAY_OF_MONTH));
    }

    private void retrieveData(String statisticsBuffer, Writer writer)
            throws Exception {
        Node node;
        Document xmldoc = XmlUtils.newDocument();
        // UPOSStat
        Element root = xmldoc.createElement("UPOSStat");
        xmldoc.appendChild(root);
        root.setAttribute("version", "1.13.0");
        root.setAttribute("xmlns:xsi",
                "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xmlns",
                "http://www.nrf-arts.org/IXRetail/namespace/");
        root.setAttribute("xsi:schemaLocation",
                "http://www.nrf-arts.org/IXRetail/namespace/UPOSStat.xsd");
        // Events
        Node eventsNode = xmldoc.createElement("Event");
        root.appendChild(eventsNode);
        items.retrieve(eventsNode, xmldoc, statisticsBuffer);
        // Equipment
        node = xmldoc.createElement("Equipment");
        root.appendChild(node);
        setParameter(node, xmldoc, JPOS_STAT_UnifiedPOSVersion,
                unifiedPOSVersion);
        setAttribute(node, xmldoc, JPOS_STAT_DeviceCategory, "UPOS",
                deviceCategory);
        setParameter(node, xmldoc, JPOS_STAT_ManufacturerName, manufacturerName);
        setParameter(node, xmldoc, JPOS_STAT_ModelName, modelName);
        setParameter(node, xmldoc, JPOS_STAT_SerialNumber, serialNumber);
        setParameter(node, xmldoc, JPOS_STAT_FirmwareRevision, firmwareRevision);
        setParameter(node, xmldoc, JPOS_STAT_Interface, physicalInterface);
        setParameter(node, xmldoc, JPOS_STAT_InstallationDate, installationDate);
        XmlUtils.save(xmldoc, writer);
    }

    private String retrieveData(String statisticsBuffer) throws Exception {
        StringWriter writer = new StringWriter();
        retrieveData(statisticsBuffer, writer);
        return writer.toString();
    }

    public class StatisticItems extends Vector {

        private final String prefix;

        public StatisticItems(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }

        public StatisticItem itemByName(String name) {
            for (int i = 0; i < size(); i++) {
                StatisticItem item = (StatisticItem) get(i);
                if (item.getName().equalsIgnoreCase(name)) {
                    return item;
                }
            }
            return null;
        }

        public void reset() {
            for (int i = 0; i < size(); i++) {
                ((StatisticItem) get(i)).reset();
            }
        }

        public void updateBySingleToken(String token) {

            StringTokenizer paramTokenizer = new StringTokenizer(token, "=",
                    false);
            String statisticName = paramTokenizer.nextToken();
            String statisticValue = paramTokenizer.nextToken();
            StatisticItems items = select(statisticName);

            for (int i = 0; i < items.size(); i++) {
                StatisticItem item = (StatisticItem) items.get(i);
                item.setText(statisticValue);
            }
        }

        public void update(String statisticNames) {
            StringTokenizer tokenizer = new StringTokenizer(statisticNames,
                    ",", false);
            if (tokenizer.countTokens() > 0) {
                while (tokenizer.hasMoreTokens()) {
                    updateBySingleToken(tokenizer.nextToken());
                }
            } else {
                updateBySingleToken(statisticNames);
            }
        }

        public void addItems(StatisticItems items, String statisticName) {
            for (int i = 0; i < size(); i++) {
                StatisticItem item = (StatisticItem) get(i);

                if ((statisticName.length() == 0)
                        || (statisticName.equalsIgnoreCase("\"\""))
                        || (statisticName.equalsIgnoreCase(prefix))
                        || (statisticName.equalsIgnoreCase(item.getName()))) {
                    if (!items.contains(item)) {
                        items.add(item);
                    }
                }
            }
        }

        public StatisticItems select(String statisticNames) {
            StatisticItems result = new StatisticItems(getPrefix());
            addItems(result, statisticNames);
            StringTokenizer tokenizer = new StringTokenizer(statisticNames,
                    ",", false);
            while (tokenizer.hasMoreTokens()) {
                addItems(result, tokenizer.nextToken());
            }
            return result;
        }

        public void retrieve(Node node, Document xmldoc, String statisticNames) {
            StatisticItems items = select(statisticNames);
            for (int i = 0; i < items.size(); i++) {
                StatisticItem item = (StatisticItem) items.get(i);
                saveItem(node, xmldoc, item.getName(), item.getText());
            }
        }

        public void saveItem(Node root, Document xmldoc, String name,
                String value) {
            // parameterNode
            Node parameterNode = xmldoc.createElement("Parameter");
            root.appendChild(parameterNode);
            // parameterName
            Node node = xmldoc.createElement("Name");
            node.appendChild(xmldoc.createTextNode(name));
            parameterNode.appendChild(node);
            // parameterValue
            node = xmldoc.createElement("Value");
            node.appendChild(xmldoc.createTextNode(value));
            parameterNode.appendChild(node);
        }
    }

    public class StatisticItem {

        private final String name;
        private String text = "";

        public StatisticItem(String name) {
            this.name = name;
        }

        public StatisticItem(String name, String text) {
            this.name = name;
            this.text = text;
        }

        public void inc(long value) {
            setText(String.valueOf(Long.parseLong(text) + value));
        }

        public String getName() {
            return name;
        }

        public void reset() {
            text = "";
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public StatisticItem defineStatistic(String statisticName) {
        StatisticItem item = new StatisticItem(statisticName);
        items.add(item);
        return item;
    }
}
