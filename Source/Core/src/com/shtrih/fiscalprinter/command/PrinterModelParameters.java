package com.shtrih.fiscalprinter.command;

/**
 * @author P.Zhirkov
 */
public class PrinterModelParameters implements PrinterConst {

    // Параметры модели (8 байт)
    private final long flags;

    // Ширина печати шрифтом 1(1 байт)
    private final int font1Width;

    // Ширина печати шрифтом 2 (1 байт)
    private final int font2Width;

    // Номер первой печатаемой линии в графике (1 байт)
    private final int firsGraphicsLineNumber;

    // Количество цифр в ИНН (1 байт)
    private final int innLength;

    // Количество цифр в РНМ (1 байт)
    private final int rnmLength;

    // Количество цифр в длинном РНМ (1 байт)
    private final int longRnmLength;

    // Количество цифр в длинном заводскомно мере (1 байт)
    private final int longSerialNumberLength;

    // Пароль налогового инспектора по умолчанию (4 байта)
    private final int defaultTaxPassword;

    // Пароль сист.админа по умолчанию (4 байта)
    private final int defaultSysPassword;

    // Номер таблицы "BLUETOOTH БЕСПРОВОДНОЙ МОДУЛЬ" настроек Bluetooth (1 байт)
    private final int bluethoothSettingsTableNumber;

    // Номер поля "НАЧИСЛЕНИЕ НАЛОГОВ" (1 байт)
    private final int taxModeFieldNumber;

    // Максимальная длина команды (N/LEN16) (2 байта)
    private final int maxCommandLength;

    // Ширина произвольной графической линии в байтах (печать одномерного штрих-кода) (1 байт)
    private final int graphicsWidthBytes;

    // Ширина графической линии в буфере графики-512 (1 байт)
    private final int graphics512WidthBytes;

    // Количество линий в буфере графики-512 (2 байта)
    private final int graphics512HeightLines;

    public PrinterModelParameters(CommandInputStream in) throws Exception {
        flags = in.readLong(8);
        font1Width = in.readByte();
        font2Width = in.readByte();
        firsGraphicsLineNumber = in.readByte();
        innLength = in.readByte();
        rnmLength = in.readByte();
        longRnmLength = in.readByte();
        longSerialNumberLength = in.readByte();
        defaultTaxPassword = in.readInt();
        defaultSysPassword = in.readInt();
        bluethoothSettingsTableNumber = in.readByte();
        taxModeFieldNumber = in.readByte();
        maxCommandLength = in.readShort();
        graphicsWidthBytes = in.readByte();
        graphics512WidthBytes = in.readByte();
        graphics512HeightLines = in.readShort();
    }

    public int getGraphicsWidth() {
        return graphicsWidthBytes * 8;
    }

    public boolean isGraphics512Supported(){
        return graphics512WidthBytes > 0;
    }

    public int getGraphics512Width() {
        return graphics512WidthBytes * 8;
    }
}
