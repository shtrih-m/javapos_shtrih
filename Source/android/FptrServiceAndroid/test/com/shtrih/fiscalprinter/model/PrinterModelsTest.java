package com.shtrih.fiscalprinter.model;

import com.shtrih.fiscalprinter.command.ParameterValue;
import com.shtrih.fiscalprinter.command.ParameterValues;
import com.shtrih.fiscalprinter.command.PrinterParameter;
import com.shtrih.fiscalprinter.command.PrinterParameters;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class PrinterModelsTest {

    private final String path = "C:\\dev\\javapos_shtrih\\Source\\Core\\src\\com\\shtrih\\fiscalprinter\\model\\";

    @Test
    @Ignore
    public void xml_to_classes() throws Exception {

        PrinterModels models = new PrinterModels();
        try (InputStream stream = new FileInputStream("C:\\dev\\javapos_shtrih\\Source\\android\\FptrServiceAndroid\\assets\\models.xml")) {

            XmlModelsReader reader = new XmlModelsReader(models);
            reader.load(stream);
        }

        generateModelClass(models, -1, "PrinterModelDefault2", 0);
        generateModelClass(models, 5, "PrinterModelShtrih950K", 7);
        generateModelClass(models, 11, "PrinterModelShtrih950K2", 15);
        generateModelClass(models, 9, "PrinterModelShtrihComboFRK", 12);
        generateModelClass(models, 12, "PrinterModelShtrihComboFRK2", 13);
        generateModelClass(models, 0, "PrinterModelShtrihFRFv3", 1);
        generateModelClass(models, 0, "PrinterModelShtrihFRFv4", 2);
        generateModelClass(models, 4, "PrinterModelShtrihFRK", 6);
        generateModelClass(models, 15, "PrinterModelShtrihKioskFRK", 16);
        generateModelClass(models, 251, "PrinterModelShtrihLightFRK", 21);
        generateModelClass(models, 252, "PrinterModelShtrihLightFRK2", 22);
        generateModelClass(models, 250, "PrinterModelShtrihMFRK", 20);
        generateModelClass(models, 239, "PrinterModelShtrihMPTK", 19);
        generateModelClass(models, 7, "PrinterModelShtrihMiniFRK", 9);
        generateModelClass(models, 14, "PrinterModelShtrihMiniFRK2", 10);
        generateModelClass(models, -1, "PrinterModelASPDElves", 23);
        generateModelClass(models, 8, "PrinterModelShtrihFRFBelarus", 11);
        generateModelClass(models, 17, "PrinterModelNCR001", 24);
        generateModelClass(models, 19, "PrinterModelShtrihMobileF", 25);
        generateModelClass(models, 31, "PrinterModelCustomPTK", 26);
        generateModelClass(models, 243, "PrinterModelYarus01K", 27);
        generateModelClass(models, 248, "PrinterModelYarus02K", 17);
        generateModelClass(models, 22, "PrinterModelRetail01K", 18);
        generateModelClass(models, 24, "PrinterModelASPDRR01K", 28);
    }

    private void generateModelClass(PrinterModels models, int modelId, String className, int id) throws Exception {
        PrinterModels matchingModels = models.itemsByModel(modelId);

        PrinterModel model = null;

        for (int i = 0; i < matchingModels.size(); i++) {
            PrinterModel m = matchingModels.get(i);
            if (m.getId() == id)
                model = m;
        }

        if (model == null)
            throw new Exception("Matching model was not found modelId: " + modelId + ", id: " + id);

        StringBuilder sb = new StringBuilder();

        sb.append("package com.shtrih.fiscalprinter.model;\n" +
                "\n" +
                "import com.shtrih.fiscalprinter.command.PrinterConst;\n" +
                "\n" +
                "public class " + className + " extends PrinterModelDefault {\n" +
                "    public " + className + "() throws Exception {\n");

        sb.append("        setName(\"").append(model.getName()).append("\");\n");
        sb.append("        setId(").append(model.getId()).append(");\n");
        sb.append("        setModelID(").append(model.getModelID()).append(");\n");
        sb.append("        setProtocolVersion(").append(model.getProtocolVersion()).append(");\n");
        sb.append("        setProtocolSubVersion(").append(model.getProtocolSubVersion()).append(");\n");
        sb.append("        setCapEJPresent(").append(model.getCapEJPresent()).append(");\n");
        sb.append("        setCapFMPresent(").append(model.getCapFMPresent()).append(");\n");
        sb.append("        setCapRecPresent(").append(model.getCapRecPresent()).append(");\n");
        sb.append("        setCapJrnPresent(").append(model.getCapJrnPresent()).append(");\n");
        sb.append("        setCapSlpPresent(").append(model.getCapSlpPresent()).append(");\n");
        sb.append("        setCapSlpEmptySensor(").append(model.getCapSlpEmptySensor()).append(");\n");
        sb.append("        setCapSlpNearEndSensor(").append(model.getCapSlpNearEndSensor()).append(");\n");
        sb.append("        setCapRecEmptySensor(").append(model.getCapRecEmptySensor()).append(");\n");
        sb.append("        setCapRecEmptySensor(").append(model.getCapRecEmptySensor()).append(");\n");
        sb.append("        setCapRecNearEndSensor(").append(model.getCapRecNearEndSensor()).append(");\n");
        sb.append("        setCapRecLeverSensor(").append(model.getCapRecLeverSensor()).append(");\n");
        sb.append("        setCapJrnEmptySensor(").append(model.getCapJrnEmptySensor()).append(");\n");
        sb.append("        setCapJrnNearEndSensor(").append(model.getCapJrnNearEndSensor()).append(");\n");
        sb.append("        setCapJrnLeverSensor(").append(model.getCapJrnLeverSensor()).append(");\n");
        sb.append("        setCapPrintGraphicsLine(").append(model.getCapPrintGraphicsLine()).append(");\n");
        sb.append("        setCapHasVatTable(").append(model.getCapHasVatTable()).append(");\n");
        sb.append("        setCapCoverSensor(").append(model.getCapCoverSensor()).append(");\n");
        sb.append("        setCapDoubleWidth(").append(model.getCapDoubleWidth()).append(");\n");
        sb.append("        setCapDuplicateReceipt(").append(model.getCapDuplicateReceipt()).append(");\n");
        sb.append("        setCapFullCut(").append(model.getCapFullCut()).append(");\n");
        sb.append("        setCapPartialCut(").append(model.getCapPartialCut()).append(");\n");
        sb.append("        setCapGraphics(").append(model.getCapGraphics()).append(");\n");
        sb.append("        setCapGraphicsEx(").append(model.getCapGraphicsEx()).append(");\n");
        sb.append("        setCapPrintStringFont(").append(model.getCapPrintStringFont()).append(");\n");
        sb.append("        setCapShortStatus(").append(model.getCapShortStatus()).append(");\n");
        sb.append("        setCapFontMetrics(").append(model.getCapFontMetrics()).append(");\n");
        sb.append("        setCapOpenReceipt(").append(model.getCapOpenReceipt()).append(");\n");
        sb.append("        setNumVatRates(").append(model.getNumVatRates()).append(");\n");
        sb.append("        setAmountDecimalPlace(").append(model.getAmountDecimalPlace()).append(");\n");
        sb.append("        setNumHeaderLines(").append(model.getNumHeaderLines()).append(");\n");
        sb.append("        setNumTrailerLines(").append(model.getNumTrailerLines()).append(");\n");
        sb.append("        setTrailerTableNumber(").append(model.getTrailerTableNumber()).append(");\n");
        sb.append("        setHeaderTableNumber(").append(model.getHeaderTableNumber()).append(");\n");
        sb.append("        setHeaderTableRow(").append(model.getHeaderTableRow()).append(");\n");
        sb.append("        setTrailerTableRow(").append(model.getTrailerTableRow()).append(");\n");
        sb.append("        setMinHeaderLines(").append(model.getMinHeaderLines()).append(");\n");
        sb.append("        setMinTrailerLines(").append(model.getMinTrailerLines()).append(");\n");
        sb.append("        setMaxGraphicsWidth(").append(model.getMaxGraphicsWidth()).append(");\n");
        sb.append("        setMaxGraphicsHeight(").append(model.getMaxGraphicsHeight()).append(");\n");
        sb.append("        setPrintWidth(").append(model.getPrintWidth()).append(");\n");
        sb.append("        setTextLength(").append(intArray(model.getTextLength())).append(");\n");
        sb.append("        setFontHeight(").append(intArray(model.getFontHeight())).append(");\n");
        sb.append("        setSupportedBaudRates(").append(intArray(model.getSupportedBaudRates())).append(");\n");
        sb.append("        setCapCashInAutoCut(").append(model.getCapCashInAutoCut()).append(");\n");
        sb.append("        setCapCashOutAutoCut(").append(model.getCapCashOutAutoCut()).append(");\n");
        sb.append("        setCapPrintBarcode2(").append(model.getCapPrintBarcode2()).append(");\n");

        sb.append("        setDeviceFontNormal(").append(model.getDeviceFontNormal()).append(");\n");
        sb.append("        setDeviceFontDouble(").append(model.getDeviceFontDouble()).append(");\n");
        sb.append("        setDeviceFontSmall(").append(model.getDeviceFontSmall()).append(");\n");
        sb.append("        setSwapGraphicsLine(").append(model.getSwapGraphicsLine()).append(");\n");

        sb.append("        setMinCashRegister(").append(model.getMinCashRegister()).append(");\n");
        sb.append("        setMaxCashRegister(").append(model.getMaxCashRegister()).append(");\n");
        sb.append("        setMinCashRegister2(").append(model.getMinCashRegister2()).append(");\n");
        sb.append("        setMaxCashRegister2(").append(model.getMaxCashRegister2()).append(");\n");
        sb.append("        setMinOperationRegister(").append(model.getMinOperationRegister()).append(");\n");
        sb.append("        setMaxOperationRegister(").append(model.getMaxOperationRegister()).append(");\n");
        sb.append("        setCapGraphicsLineMargin(").append(model.getCapGraphicsLineMargin()).append(");\n");
        sb.append("        setCapFSCloseCheck(").append(model.getCapFSCloseCheck()).append(");\n");

        sb.append("\n");

        PrinterParameters parameters = model.getParameters();

        for (int i = 0; i < parameters.size(); i++) {
            PrinterParameter p = parameters.get(i);

            sb.append("        addParameter(\"")
                    .append(p.getName()).append("\", ")
                    .append(p.getTableNumber()).append(", ")
                    .append(p.getRowNumber()).append(", ")
                    .append(p.getFieldNumber()).append(")");

            ParameterValues values = p.getValues();
            for (int j = 0; j < values.size(); j++) {
                ParameterValue v = values.get(j);

                sb.append("\n            addValue(")
                        .append(v.getValue()).append(", ")
                        .append(v.getFieldValue()).append(")");
            }

            sb.append(";\n");
        }

        sb.append("    }\n" +
                "}");

        String classPath = path + className + ".java";

        try (OutputStream os = new FileOutputStream(new File(classPath))) {

            os.write(sb.toString().getBytes("utf-8"));
            os.flush();

        }

        System.out.println("            add(new " + className + "());");
    }

    private String intArray(int[] array) {
        StringBuilder sb = new StringBuilder();

        sb.append("new int[] { ");

        for (int i = 0; i < array.length; i++) {
            sb.append(" ").append(array[i]);

            if (array.length - 1 != i)
                sb.append(",");
        }

        sb.append(" }");

        return sb.toString();
    }
}