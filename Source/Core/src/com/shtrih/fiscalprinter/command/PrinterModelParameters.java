package com.shtrih.fiscalprinter.command;

import com.shtrih.util.BitUtils;

public class PrinterModelParameters implements PrinterConst {

    // Параметры модели (8 байт)
    private final long flags;

    // 0 – Весовой датчик контрольной ленты
    private final boolean capJrnNearEndSensor;
    // 1 – Весовой датчик чековой ленты
    private final boolean capRecNearEndSensor;
    // 2 – Оптический датчик контрольной ленты
    private final boolean capJrnEmptySensor;
    // 3 – Оптический датчик чековой ленты
    private final boolean capRecEmptySensor;
    // 4 – Датчик крышки
    private final boolean capCoverSensor;
    // 5 – Рычаг термоголовки контрольной ленты
    private final boolean capJrnLeverSensor;
    // 6 – Рычаг термоголовки чековой ленты
    private final boolean capRecLeverSensor;
    // 7 – Верхний датчик подкладного документа
    private final boolean capSlpNearEndSensor;
    // 8 – Нижний датчик подкладного документа
    private final boolean capSlpEmptySensor;
    // 9 – Презентер поддерживается
    private final boolean capPresenter;
    // 10 – Поддержка команд работы с презентером
    private final boolean capPresenterCommands;
    // 11 – Флаг заполнения ЭКЛЗ
    private final boolean capEJNearFull;
    // 12 – ЭКЛЗ поддерживается
    private final boolean capEJ;
    // 13 – Отрезчик поддерживается
    private final boolean capCutter;
    // 14 – Состояние ДЯ как датчик бумаги в презентере
    private final boolean capDrawerStateAsPaper;
    // 15 – Датчик денежного ящика
    private final boolean capDrawerSensor;
    // 16 – Датчик бумаги на входе в презентер
    private final boolean capPrsInSensor;
    // 17 – Датчик бумаги на выходе из презентера
    private final boolean capPrsOutSensor;
    // 18 – Купюроприемник поддерживается
    private final boolean capBillAcceptor;
    // 19 – Клавиатура НИ поддерживается
    private final boolean capTaxKeyPad;
    // 20 – Контрольная лента поддерживается
    private final boolean capJrnPresent;
    // 21 – Подкладной документ поддерживается
    private final boolean capSlpPresent;
    // 22 – Поддержка команд нефискального документа
    private final boolean capNonfiscalDoc;
    // 23 – Поддержка протокола Кассового Ядра (cashcore)
    private final boolean capCashCore;
    // 24 – Ведущие нули в ИНН
    private final boolean capInnLeadingZero;
    // 25 – Ведущие нули в РНМ
    private final boolean capRnmLeadingZero;
    // 26 – Переворачивать байты при печати линии
    private final boolean swapGraphicsLine;
    // 27 – Блокировка ККТ по неверному паролю налогового инспектора
    private final boolean capTaxPasswordLock;
    // 28 – Поддержка альтернативного нижнего уровня протокола ККТ
    private final boolean capProtocol2;
    // 29 – Поддержка переноса строк символом '\n' (код 10) в командах печати строк 12H, 17H, 2FH
    private final boolean capLFInPrintText;
    // 30 – Поддержка переноса строк номером шрифта (коды 1…9) в команде печати строк 2FH
    private final boolean capFontInPrintText;
    // 31 – Поддержка переноса строк символом '\n' (код 10) в фискальных командах 80H…87H, 8AH, 8BH
    private final boolean capLFInFiscalCommands;
    // 32 – Поддержка переноса строк номером шрифта (коды 1…9) в фискальных командах 80H…87H, 8AH, 8BH
    private final boolean capFontInFiscalCommands;
    // 33 – Права "СТАРШИЙ КАССИР" (28) на снятие отчетов: X, операционных регистров, по отделам, по налогам, по кассирам, почасового, по товарам
    private final boolean capTopCashierReports;
    // 34 – Поддержка Бит 3 "слип чек" в командах печати: строк 12H, 17H, 2FH,расширенной графики 4DH, C3H, графической линии C5H; поддержка
    private final boolean capSlpInPrintCommands;
    // 35 – Поддержка блочной загрузки графики в команде C4H
    private final boolean capGraphicsC4;
    // 36 – Поддержка команды 6BH "Возврат названия ошибоки"
    private final boolean capCommand6B;
    // 37 – Поддержка флагов печати для команд печати расширенной графики C3H и печати графической линии C5H
    private final boolean capGraphicsFlags;
    // 39 – Поддержка МФП
    private final boolean capMFP;
    // 40 – Поддержка ЭКЛЗ5
    private final boolean capEJ5;
    // 41 – Печать графики с масштабированием (команда 4FH)
    private final boolean capScaleGraphics;
    // 42 – Загрузка и печать графики-512 (команды 4DH, 4EH)
    private final boolean capGraphics512;
    // 43 - Поддержка ФН
    private final boolean capFiscalStorage;
    // 44 - Поддержка EoD ("Ethernet" over Driver)
    private final boolean capEoD;


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
    private final int maxGraphics512Height;

    // Номер таблицы Фискального Накопителя (1 байт) 0 - не поддерживается, 1…255
    private final int fsTableNumber;

    // Номер таблицы параметров ОФД (1 байт) 0 - не поддерживается, 1…255
    private final int ofdTableNumber;

    // Номер таблицы встраиваемой и интернет техники (1 байт) 0 - не поддерживается, 1…255
    private final int embeddableAndInternetDeviceTableNumber;

    // Номер таблицы версии ФФД (1 байт) 0 - не поддерживается, 1…255
    private final int ffdTableNumber;

    // Номер поля в таблице версии ФФД (1 байт) 0 - не поддерживается, 1…255
    private final int ffdColumnNumber;

    public PrinterModelParameters(CommandInputStream in) throws Exception {
        flags = in.readLong(8);
        capJrnNearEndSensor = BitUtils.testBit(flags, 0);
        capRecNearEndSensor = BitUtils.testBit(flags, 1);
        capJrnEmptySensor = BitUtils.testBit(flags, 2);
        capRecEmptySensor = BitUtils.testBit(flags, 3);
        capCoverSensor = BitUtils.testBit(flags, 4);
        capJrnLeverSensor = BitUtils.testBit(flags, 5);
        capRecLeverSensor = BitUtils.testBit(flags, 6);
        capSlpNearEndSensor = BitUtils.testBit(flags, 7);
        capSlpEmptySensor = BitUtils.testBit(flags, 8);
        capPresenter = BitUtils.testBit(flags, 9);
        capPresenterCommands = BitUtils.testBit(flags, 10);
        capEJNearFull = BitUtils.testBit(flags, 11);
        capEJ = BitUtils.testBit(flags, 12);
        capCutter = BitUtils.testBit(flags, 13);
        capDrawerStateAsPaper = BitUtils.testBit(flags, 14);
        capDrawerSensor = BitUtils.testBit(flags, 15);
        capPrsInSensor = BitUtils.testBit(flags, 16);
        capPrsOutSensor = BitUtils.testBit(flags, 17);
        capBillAcceptor = BitUtils.testBit(flags, 18);
        capTaxKeyPad = BitUtils.testBit(flags, 19);
        capJrnPresent = BitUtils.testBit(flags, 20);
        capSlpPresent = BitUtils.testBit(flags, 21);
        capNonfiscalDoc = BitUtils.testBit(flags, 22);
        capCashCore = BitUtils.testBit(flags, 23);
        capInnLeadingZero = BitUtils.testBit(flags, 24);
        capRnmLeadingZero = BitUtils.testBit(flags, 25);
        swapGraphicsLine = BitUtils.testBit(flags, 26);
        capTaxPasswordLock = BitUtils.testBit(flags, 27);
        capProtocol2 = BitUtils.testBit(flags, 28);
        capLFInPrintText = BitUtils.testBit(flags, 29);
        capFontInPrintText = BitUtils.testBit(flags, 30);
        capLFInFiscalCommands = BitUtils.testBit(flags, 31);
        capFontInFiscalCommands = BitUtils.testBit(flags, 32);
        capTopCashierReports = BitUtils.testBit(flags, 33);
        capSlpInPrintCommands = BitUtils.testBit(flags, 34);
        capGraphicsC4 = BitUtils.testBit(flags, 35);
        capCommand6B = BitUtils.testBit(flags, 36);
        capGraphicsFlags = BitUtils.testBit(flags, 37);
        capMFP = BitUtils.testBit(flags, 39);
        capEJ5 = BitUtils.testBit(flags, 40);
        capScaleGraphics = BitUtils.testBit(flags, 41);
        capGraphics512 = BitUtils.testBit(flags, 42);
        capFiscalStorage = BitUtils.testBit(flags, 43);
        capEoD = BitUtils.testBit(flags, 44);

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
        graphics512WidthBytes = readByteIfAvailable(in);

        if (in.size() >= 2) {
            maxGraphics512Height = in.readShort();
        } else {
            maxGraphics512Height = 0;
        }

        fsTableNumber = readByteIfAvailable(in);
        ofdTableNumber = readByteIfAvailable(in);
        embeddableAndInternetDeviceTableNumber = readByteIfAvailable(in);
        ffdTableNumber = readByteIfAvailable(in);
        ffdColumnNumber = readByteIfAvailable(in);
    }

    private int readByteIfAvailable(CommandInputStream in) throws Exception {
        if (in.size() >= 1) {
            return in.readByte();
        } else {
            return 0;
        }
    }

    public int getGraphicsWidth() {
        return getGraphicsWidthBytes() * 8;
    }

    public boolean isGraphics512Supported() {
        return capGraphics512;
    }

    public boolean isCapEoD() {
        return capEoD;
    }

    public int getGraphics512Width() {
        return getGraphics512WidthBytes() * 8;
    }

    /**
     * @return the flags
     */
    public long getFlags() {
        return flags;
    }

    /**
     * @return the font1Width
     */
    public int getFont1Width() {
        return font1Width;
    }

    /**
     * @return the font2Width
     */
    public int getFont2Width() {
        return font2Width;
    }

    /**
     * @return the firsGraphicsLineNumber
     */
    public int getFirsGraphicsLineNumber() {
        return firsGraphicsLineNumber;
    }

    /**
     * @return the innLength
     */
    public int getInnLength() {
        return innLength;
    }

    /**
     * @return the rnmLength
     */
    public int getRnmLength() {
        return rnmLength;
    }

    /**
     * @return the longRnmLength
     */
    public int getLongRnmLength() {
        return longRnmLength;
    }

    /**
     * @return the longSerialNumberLength
     */
    public int getLongSerialNumberLength() {
        return longSerialNumberLength;
    }

    /**
     * @return the defaultTaxPassword
     */
    public int getDefaultTaxPassword() {
        return defaultTaxPassword;
    }

    /**
     * @return the defaultSysPassword
     */
    public int getDefaultSysPassword() {
        return defaultSysPassword;
    }

    /**
     * @return the bluethoothSettingsTableNumber
     */
    public int getBluethoothSettingsTableNumber() {
        return bluethoothSettingsTableNumber;
    }

    /**
     * @return the taxModeFieldNumber
     */
    public int getTaxModeFieldNumber() {
        return taxModeFieldNumber;
    }

    /**
     * @return the maxCommandLength
     */
    public int getMaxCommandLength() {
        return maxCommandLength;
    }

    /**
     * @return the graphicsWidthBytes
     */
    public int getGraphicsWidthBytes() {
        return graphicsWidthBytes;
    }

    /**
     * @return the graphics512WidthBytes
     */
    public int getGraphics512WidthBytes() {
        return graphics512WidthBytes;
    }

    /**
     * @return the graphics512HeightLines
     */
    public int getMaxGraphics512Height() {
        return maxGraphics512Height;
    }

    /**
     * @return the capJrnNearEndSensor
     */
    public boolean isCapJrnNearEndSensor() {
        return capJrnNearEndSensor;
    }

    /**
     * @return the capRecNearEndSensor
     */
    public boolean isCapRecNearEndSensor() {
        return capRecNearEndSensor;
    }

    /**
     * @return the capJrnEmptySensor
     */
    public boolean isCapJrnEmptySensor() {
        return capJrnEmptySensor;
    }

    /**
     * @return the capRecEmptySensor
     */
    public boolean isCapRecEmptySensor() {
        return capRecEmptySensor;
    }

    /**
     * @return the capCoverSensor
     */
    public boolean isCapCoverSensor() {
        return capCoverSensor;
    }

    /**
     * @return the capJrnLeverSensor
     */
    public boolean isCapJrnLeverSensor() {
        return capJrnLeverSensor;
    }

    /**
     * @return the capRecLeverSensor
     */
    public boolean isCapRecLeverSensor() {
        return capRecLeverSensor;
    }

    /**
     * @return the capSlpNearEndSensor
     */
    public boolean isCapSlpNearEndSensor() {
        return capSlpNearEndSensor;
    }

    /**
     * @return the capSlpEmptySensor
     */
    public boolean isCapSlpEmptySensor() {
        return capSlpEmptySensor;
    }

    /**
     * @return the capPresenter
     */
    public boolean isCapPresenter() {
        return capPresenter;
    }

    /**
     * @return the capPresenterCommands
     */
    public boolean isCapPresenterCommands() {
        return capPresenterCommands;
    }

    /**
     * @return the capEJNearFull
     */
    public boolean isCapEJNearFull() {
        return capEJNearFull;
    }

    /**
     * @return the capEJ
     */
    public boolean isCapEJ() {
        return capEJ;
    }

    /**
     * @return the capCutter
     */
    public boolean isCapCutter() {
        return capCutter;
    }

    /**
     * @return the capDrawerStateAsPaper
     */
    public boolean isCapDrawerStateAsPaper() {
        return capDrawerStateAsPaper;
    }

    /**
     * @return the capDrawerSensor
     */
    public boolean isCapDrawerSensor() {
        return capDrawerSensor;
    }

    /**
     * @return the capPrsInSensor
     */
    public boolean isCapPrsInSensor() {
        return capPrsInSensor;
    }

    /**
     * @return the capPrsOutSensor
     */
    public boolean isCapPrsOutSensor() {
        return capPrsOutSensor;
    }

    /**
     * @return the capBillAcceptor
     */
    public boolean isCapBillAcceptor() {
        return capBillAcceptor;
    }

    /**
     * @return the capTaxKeyPad
     */
    public boolean isCapTaxKeyPad() {
        return capTaxKeyPad;
    }

    /**
     * @return the capJrnPresent
     */
    public boolean isCapJrnPresent() {
        return capJrnPresent;
    }

    /**
     * @return the capSlpPresent
     */
    public boolean isCapSlpPresent() {
        return capSlpPresent;
    }

    /**
     * @return the capNonfiscalDoc
     */
    public boolean isCapNonfiscalDoc() {
        return capNonfiscalDoc;
    }

    /**
     * @return the capCashCore
     */
    public boolean isCapCashCore() {
        return capCashCore;
    }

    /**
     * @return the capInnLeadingZero
     */
    public boolean isCapInnLeadingZero() {
        return capInnLeadingZero;
    }

    /**
     * @return the capRnmLeadingZero
     */
    public boolean isCapRnmLeadingZero() {
        return capRnmLeadingZero;
    }

    /**
     * @return the swapGraphicsLine
     */
    public boolean isSwapGraphicsLine() {
        return swapGraphicsLine;
    }

    /**
     * @return the capTaxPasswordLock
     */
    public boolean isCapTaxPasswordLock() {
        return capTaxPasswordLock;
    }

    /**
     * @return the capProtocol2
     */
    public boolean isCapProtocol2() {
        return capProtocol2;
    }

    /**
     * @return the capLFInPrintText
     */
    public boolean isCapLFInPrintText() {
        return capLFInPrintText;
    }

    /**
     * @return the capFontInPrintText
     */
    public boolean isCapFontInPrintText() {
        return capFontInPrintText;
    }

    /**
     * @return the capLFInFiscalCommands
     */
    public boolean isCapLFInFiscalCommands() {
        return capLFInFiscalCommands;
    }

    /**
     * @return the capFontInFiscalCommands
     */
    public boolean isCapFontInFiscalCommands() {
        return capFontInFiscalCommands;
    }

    /**
     * @return the capTopCashierReports
     */
    public boolean isCapTopCashierReports() {
        return capTopCashierReports;
    }

    /**
     * @return the capSlpInPrintCommands
     */
    public boolean isCapSlpInPrintCommands() {
        return capSlpInPrintCommands;
    }

    /**
     * @return the capGraphicsC4
     */
    public boolean isCapGraphicsC4() {
        return capGraphicsC4;
    }

    /**
     * @return the capCommand6B
     */
    public boolean isCapCommand6B() {
        return capCommand6B;
    }

    /**
     * @return the capGraphicsFlags
     */
    public boolean isCapGraphicsFlags() {
        return capGraphicsFlags;
    }

    /**
     * @return the capMFP
     */
    public boolean isCapMFP() {
        return capMFP;
    }

    /**
     * @return the capEJ5
     */
    public boolean isCapEJ5() {
        return capEJ5;
    }

    /**
     * @return the capScaleGraphics
     */
    public boolean isCapScaleGraphics() {
        return capScaleGraphics;
    }

    /**
     * @return the capGraphics512
     */
    public boolean isCapGraphics512() {
        return capGraphics512;
    }

    /**
     * @return Номер таблицы Фискального Накопителя (1 байт) 0 - не поддерживается, 1…255
     */
    public int getFsTableNumber() {
        return fsTableNumber;
    }

    /**
     * @return Номер таблицы параметров ОФД (1 байт) 0 - не поддерживается, 1…255
     */
    public int getOfdTableNumber() {
        return ofdTableNumber;
    }

    public boolean capOfdTableNumber() {
        return ofdTableNumber > 0;
    }

    public boolean capFsTableNumber() {
        return fsTableNumber > 0;
    }

    /**
     * @return Номер таблицы встраиваемой и интернет техники (1 байт) 0 - не поддерживается, 1…255
     */
    public int getEmbeddableAndInternetDeviceTableNumber() {
        return embeddableAndInternetDeviceTableNumber;
    }

    /**
     * @return Номер таблицы версии ФФД (1 байт) 0 - не поддерживается, 1…255
     */
    public int getFFDTableNumber() {
        return ffdTableNumber;
    }

    /**
     * @return Номер поля в таблице версии ФФД (1 байт) 0 - не поддерживается, 1…255
     */
    public int getFFDColumnNumber() {
        return ffdColumnNumber;
    }

    public boolean capFFDTableAndColumnNumber() {
        return ffdTableNumber > 0 && ffdColumnNumber > 0;
    }

    /**
     * 43 - Поддержка ФН
     */
    public boolean getCapFiscalStorage() {
        return capFiscalStorage;
    }
}
