/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Vector;
import java.util.HashMap;
import java.util.StringTokenizer;

public class GS1BarcodeParser {

    private final ApplicationIdentifiers ids = new ApplicationIdentifiers();

    public GS1BarcodeParser() {
        ids.initialize();
    }

    public GS1Barcode decode(String barcode) throws Exception {
        ApplicationIdentifiers identifiers = decodeBarcode(barcode);
        ApplicationIdentifier ai = identifiers.find("01");
        if (ai == null) {
            throw new Exception("Tag not found, GTIN(01)");
        }
        String GTIN = ai.value;
        ai = identifiers.find("21");
        if (ai == null) {
            throw new Exception("Tag not found, SerialNumber(21)");
        }
        String serial = ai.value;
        return new GS1Barcode(GTIN, serial);
    }

    public String decodeText(String barcode) throws Exception 
    {
        String result = "";
        ApplicationIdentifiers identifiers = decodeBarcode(barcode);
        for (int i = 0; i < identifiers.size(); i++) {
            ApplicationIdentifier ai = identifiers.get(i);
            result += String.format("(%s)%s", ai.id, ai.value);
        }
        return result;
    }
    
    public ApplicationIdentifiers decodeBarcode(String barcode) throws Exception {
        ApplicationIdentifiers rc = new ApplicationIdentifiers();
        String GS = "\u001D";
        if (barcode.isEmpty()) {
            return rc;
        }
        barcode.replace("(", GS);
        barcode.replace(")", "");

        if (barcode.length() == 29) {
            ApplicationIdentifier ai;
            ai = ids.find("01");
            ai.value = barcode.substring(0, 14);
            rc.add(ai);

            ai = ids.find("21");
            ai.value = barcode.substring(14, 21);
            rc.add(ai);

            ai = ids.find("9099");
            ai.value = barcode.substring(21);
            rc.add(ai);

            return rc;
        }

        StringTokenizer tokenizer = new StringTokenizer(barcode, GS);
        while (tokenizer.hasMoreTokens()) {
            rc.add(decodeToken(tokenizer.nextToken()));
        }
        return rc;
    }

    public ApplicationIdentifiers decodeToken(String barcode) {
        String id = "";
        ApplicationIdentifiers rc = new ApplicationIdentifiers();

        int i = 0;
        while (i < barcode.length()) {
            id = id + barcode.charAt(i);
            ApplicationIdentifier ai = ids.find(id);
            i++;

            if (ai != null) {
                int count = barcode.length() - i;
                if (count > ai.max) {
                    count = ai.max;
                }
                ai.value = barcode.substring(i, i + count);
                rc.add(ai);
                i += count;
                id = "";
            }
        }
        return rc;
    }

    class ApplicationIdentifier {

        public final String id;
        public final int min;
        public final int max;
        public String value = "";

        public ApplicationIdentifier(String id, int min, int max) {
            this.id = id;
            this.min = min;
            this.max = max;
        }
    }

    class ApplicationIdentifiers {

        private final Vector<ApplicationIdentifier> items = new Vector<ApplicationIdentifier>();

        public ApplicationIdentifiers() {
        }

        public int size() {
            return items.size();
        }

        public ApplicationIdentifier get(int index) {
            return items.get(index);
        }

        public void add(ApplicationIdentifier identifier) {
            items.add(identifier);
        }

        public void add(ApplicationIdentifiers identifiers) {
            items.addAll(identifiers.items);
        }

        public ApplicationIdentifier add(String id, int min, int max) {
            ApplicationIdentifier ai = new ApplicationIdentifier(id, min, max);
            items.add(ai);
            return ai;
        }

        public ApplicationIdentifier find(String id) {
            for (int i = 0; i < size(); i++) {
                ApplicationIdentifier ai = get(i);
                if (ai.id.equals(id)) {
                    return ai;
                }
            }
            return null;
        }

        public void initialize() {
            add("00", 18, 18); // Serial Shipping Container Code (SSCC)
            add("01", 14, 14); // Global Trade Item Number (GTIN)
            add("02", 14, 14); // GTIN of contained trade items
            add("03", 14, 14); // ?
            add("04", 16, 16); // ?
            add("10", 1, 20); // Batch or lot number
            add("11", 6, 6); // Production date (YYMMDD)
            add("12", 6, 6); // Due date (YYMMDD)
            add("13", 6, 6); // Packaging date (YYMMDD)
            add("14", 6, 6); // ?
            add("15", 6, 6); // Best before date (YYMMDD)
            add("16", 6, 6); // Sell by date (YYMMDD)
            add("17", 6, 6); // Expiration date (YYMMDD)
            add("18", 6, 6); // ?
            add("19", 6, 6); // ?
            add("20", 2, 2); // Internal product variant
            add("21", 1, 20); // Serial number (FNC1) 
            add("22", 1, 20); // Consumer product variant (FNC1)
            add("240", 1, 30); // Additional item identification (FNC1)
            add("241", 1, 30); // Customer part number
            add("242", 1, 6); // Made-to-Order variation number
            add("243", 1, 20); // Packaging component number
            add("250", 1, 30); // Secondary serial number
            add("251", 1, 30); // Reference to source entity
            add("253", 1, 17); // Global Document Type Identifier (GDTI)
            add("254", 1, 20); // GLN extension component
            add("255", 1, 12); // Global Coupon Number (GCN)
            add("291", 1, 30); // CRC
            add("30", 8, 8); // Variable count of items (variable measure trade item)
            add("310", 6, 6); // Net weight, kilograms (variable measure trade item)
            add("311", 6, 6); // Length or first dimension, metres (variable measure trade item)
            add("312", 6, 6); // Width, diameter, or second dimension, metres (variable measure trade item)
            add("313", 6, 6); // Depth, thickness, height, or third dimension, metres (variable measure trade item)
            add("314", 6, 6); // Area, square metres (variable measure trade item)
            add("315", 6, 6); // Net volume, litres (variable measure trade item)
            add("316", 6, 6); // Net volume, cubic metres (variable measure trade item)
            add("320", 6, 6); // Net weight, pounds (variable measure trade item)
            add("321", 6, 6); // Length or first dimension, inches (variable measure trade item)
            add("322", 6, 6); // Length or first dimension, feet (variable measure trade item)
            add("323", 6, 6); // Length or first dimension, yards (variable measure trade item)
            add("324", 6, 6); // Width, diameter, or second dimension, inches (variable measure trade item)
            add("325", 6, 6); // Width, diameter, or second dimension, feet (variable measure trade item)
            add("326", 6, 6); // Width, diameter, or second dimension, yards (variable measure trade item)
            add("327", 6, 6); // Depth, thickness, height, or third dimension, inches (variable measure trade item)
            add("328", 6, 6); // Depth, thickness, height, or third dimension, feet (variable measure trade item)
            add("329", 6, 6); // Depth, thickness, height, or third dimension, yards (variable measure trade item)
            add("330", 6, 6); // Logistic weight, kilograms
            add("331", 6, 6); // Length or first dimension, metres
            add("332", 6, 6); // Width, diameter, or second dimension, metres
            add("333", 6, 6); // Depth, thickness, height, or third dimension, metres
            add("334", 6, 6); // Area, square metres
            add("335", 6, 6); // Logistic volume, litres
            add("336", 6, 6); // Logistic volume, cubic metres
            add("337", 6, 6); // Kilograms per square metre
            add("340", 6, 6); // Logistic weight, pounds
            add("341", 6, 6); // Length or first dimension, inches
            add("342", 6, 6); // Length or first dimension, feet
            add("343", 6, 6); // Length or first dimension, yards
            add("344", 6, 6); // Width, diameter, or second dimension, inches
            add("345", 6, 6); // Width, diameter, or second dimension, feet
            add("346", 6, 6); // Width, diameter, or second dimension, yard
            add("347", 6, 6); // Depth, thickness, height, or third dimension, inches
            add("348", 6, 6); // Depth, thickness, height, or third dimension, feet
            add("349", 6, 6); // Depth, thickness, height, or third dimension, yards
            add("350", 6, 6); // Area, square inches (variable measure trade item)
            add("351", 6, 6); // Area, square feet (variable measure trade item)
            add("352", 6, 6); // Area, square yards (variable measure trade item)
            add("353", 6, 6); // Area, square inches
            add("354", 6, 6); // Area, square feet
            add("355", 6, 6); // Area, square yards
            add("356", 6, 6); // Net weight, troy ounces (variable measure trade item)
            add("357", 6, 6); // Net weight (or volume); ounces (variable measure trade item)
            add("360", 6, 6); // Net volume, quarts (variable measure trade item)
            add("361", 6, 6); // Net volume, gallons U.S. (variable measure trade item)
            add("362", 6, 6); // Logistic volume, quarts
            add("363", 6, 6); // Logistic volume, gallons U.S.
            add("364", 6, 6); // Net volume, cubic inches (variable measure trade item)
            add("365", 6, 6); // Net volume, cubic feet (variable measure trade item)
            add("366", 6, 6); // Net volume, cubic yards (variable measure trade item)
            add("367", 6, 6); // Logistic volume, cubic inches
            add("368", 6, 6); // Logistic volume, cubic feet
            add("369", 6, 6); // Logistic volume, cubic yards
            add("369", 6, 6); // Logistic volume, cubic yards
            add("37", 1, 8); // Count of trade items
            add("390", 1, 15); // Applicable amount payable or Coupon value, local currency
            add("391", 4, 18); // Applicable amount payable with ISO currency code
            add("392", 1, 15); // Applicable amount payable, single monetary area (variable measure trade item)
            add("393", 4, 18); // Applicable amount payable with ISO currency code (variable measure trade item)
            add("394", 4, 4); // Percentage discount of a coupon
            add("400", 1, 30); // Customer"s purchase order number
            add("401", 1, 30); // Global Identification Number for Consignment (GINC)
            add("402", 17, 17); // Global Shipment Identification Number (GSIN)
            add("403", 1, 30); // Routing code
            add("410", 13, 13); // Ship to - Deliver to Global Location Number
            add("411", 13, 13); // Bill to - Invoice to Global Location Number
            add("412", 13, 13); // Purchased from Global Location Number
            add("413", 13, 13); // Ship for - Deliver for - Forward to Global Location Number
            add("414", 13, 13); // Identification of a physical location - Global Location Number
            add("415", 13, 13); // Global Location Number of the invoicing party
            add("416", 13, 13); // GLN of the production or service location
            add("420", 1, 20); // Deliver to postal code within a single postal authority
            add("421", 4, 12); // Ship to - Deliver to postal code with ISO country code
            add("422", 3, 3); // Country of origin of a trade item
            add("423", 4, 15); // Country of initial processing
            add("424", 3, 3); // Country of processing
            add("425", 3, 15); // Country of disassembly
            add("426", 3, 3); // Country covering full process chain
            add("427", 1, 3); // Country subdivision Of origin
            add("7001", 13, 13); // NATO Stock Number (NSN)
            add("7002", 1, 30); // UN/ECE meat carcasses and cuts classification
            add("7003", 10, 10); // Expiration date and time
            add("7004", 1, 4); // Active potency
            add("7005", 1, 12); // Catch area
            add("7006", 6, 6); // First freeze date
            add("7007", 6, 12); // Harvest date
            add("7008", 1, 3); // Species for fishery purposes
            add("7009", 1, 10); // Fishing gear type
            add("7010", 1, 2); // Production method
            add("7020", 1, 20); // Refurbishment lot ID
            add("7021", 1, 20); // Functional status
            add("7022", 1, 20); // Revision status
            add("7023", 1, 30); // Global Individual Asset Identifier (GIAI) of an assembly
            add("7023", 1, 30); // Global Individual Asset Identifier (GIAI) of an assembly
            add("703", 4, 30); // Number of processor with ISO Country Code
            add("710", 1, 20); // National Healthcare Reimbursement Number (NHRN) – Germany PZN
            add("711", 1, 20); // National Healthcare Reimbursement Number (NHRN) – France CIP
            add("712", 1, 20); // National Healthcare Reimbursement Number (NHRN) – Spain CN
            add("713", 1, 20); // National Healthcare Reimbursement Number (NHRN) – Brasil DRN
            add("8001", 14, 14); // Roll products (width, length, core diameter, direction, splices)
            add("8002", 1, 20); // Cellular mobile telephone identifier
            add("8003", 15, 30); // Global Returnable Asset Identifier (GRAI)
            add("8004", 1, 30); // Global Individual Asset Identifier (GIAI)
            add("8005", 6, 6); // Price per unit of measure
            add("8006", 18, 18); // Identification of an individual trade item piece
            add("8007", 1, 34); // International Bank Account Number (IBAN)
            add("8008", 9, 12); // Date and time of production
            add("8010", 1, 30); // Component / Part Identifier (CPID)
            add("8011", 1, 12); // Component / Part Identifier serial number (CPID SERIAL)
            add("8012", 1, 20); // Software version
            add("8017", 18, 18); // Global Service Relation Number to identify the relationship between an organisation offering services and the provider of services
            add("8018", 18, 18); // Global Service Relation Number to identify the relationship between an organisation offering services and the recipient of services
            add("8019", 1, 10); // Service Relation Instance Number (SRIN)
            add("8020", 1, 25); // Payment slip reference number
            add("8110", 1, 70); // Coupon code identification for use in North America
            add("8111", 4, 4); // Loyalty points of a coupon
            add("8112", 1, 70); // Paperless coupon code identification for use in North America (AI 8112)
            add("8200", 1, 70); // Extended Packaging URL
            add("90", 1, 30); // Information mutually agreed between trading partners
            add("9099", 8, 8); // Information mutually agreed between trading partners
            add("91", 1, 90); // Company internal information
            add("92", 1, 90); // Company internal information
            add("93", 1, 90); // Company internal information
            add("94", 1, 90); // Company internal information
            add("95", 1, 90); // Company internal information
            add("96", 1, 90); // Company internal information
            add("97", 1, 90); // Company internal information
            add("98", 1, 90); // Company internal information
            add("99", 1, 90); // Company internal information
        }
    }

}
