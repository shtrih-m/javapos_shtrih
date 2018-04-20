package com.shtrih.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ResourceLoader {

    private static ResourceLoader instance;

    private static ResourceLoader getInstance() {
        if (instance == null) {
            instance = new ResourceLoader();
        }
        return instance;
    }

    public static InputStream load(String fileName) throws Exception {
        return getInstance().loadResource(fileName);
    }

    private final ClassLoader classLoader;

    private ResourceLoader() {
        this.classLoader = getClassLoaderOfClass(this.getClass());
    }

    private InputStream loadResource(String fileName) {
        if (fileName.equals("messages_en.txt"))
            return toStream(getMessagesEnTxt());

        if (fileName.equals("messages_ru.txt"))
            return toStream(getMessagesRuTxt());

        return classLoader.getResourceAsStream("assets/" + fileName);
    }

    private InputStream toStream(String value) {
        return toStream(value, "UTF-8");
    }

    private InputStream toStream(String value, String endoding) {
        return new ByteArrayInputStream(value.getBytes(Charset.forName(endoding)));
    }

    private static ClassLoader getClassLoaderOfClass(final Class<?> clazz) {
        ClassLoader cl = clazz.getClassLoader();
        if (cl == null) {
            return ClassLoader.getSystemClassLoader();
        } else {
            return cl;
        }
    }

    private String getMessagesEnTxt() {
        return "#\n" +
                "# String constants for shtrihjavapos.jar file, SHTRIH-M, 2010\n" +
                "#\n" +
                "#\n" +
                "\n" +
                "ReceiptDuplicationNotSupported = Receipt duplication is not supported\n" +
                "DeviceIsEnabled = Device is enabled\n" +
                "AdditionalHeaderNotSupported = Additional header is not supported\n" +
                "AdditionalTrailerNotSupported = Additional trailer is not supported\n" +
                "ChangeDueTextNotSupported = Change due text is not supported\n" +
                "MultipleContractorsNotSupported = Multiple contractors are not supported\n" +
                "MessageTypeNotSupported = MessageType is not supported\n" +
                "MethodNotSupported = Method not supported for selected FiscalReceiptType\n" +
                "InvalidParameterValue = Invalid parameter value\n" +
                "InvalidPropertyValue = Invalid property value\n" +
                "NotPaidReceiptsNotSupported = Not paid receipts is not supported\n" +
                "InvalidFiscalReceiptType = Invalid FiscalReceiptType value\n" +
                "FailedConfirmDate = Failed to confirm date. \n" +
                "CanNotChangeDate = Day is opened. Date can not be changed.\n" +
                "WrongPrinterState = Wrong printer state\n" +
                "SlipStationNotPresent = Slip station not present\n" +
                "ReceiptStationNotPresent = Receipt station not present\n" +
                "JournalStationNotPresent = Journal station not present\n" +
                "GraphicsNotSupported = Graphics not supported\n" +
                "EndDumpFailed = Can not change dump state\n" +
                "LockedTaxPassword = Locked for invalid tax officer password\n" +
                "ConfirmDateFailed = Can not change wait date state\n" +
                "WriteDecimalPointFailed = Can not change point state\n" +
                "StopTestFailed = Can not change test state\n" +
                "PrinterSupportesEAN13Only = Printer supportes barcode EAN-13 only\n" +
                "InvalidBarcodeHeight = Barcode height <= 0\n" +
                "InvalidBarcodePrintType = Invalid barcode print type\n" +
                "InvalidAnswerLength = Invalid answer data length\n" +
                "InvalidFieldValue = Invalid field value\n" +
                "NoConnection = No connection\n" +
                "ReceiveTimeoutNotSupported = Receive timeout is not supported by port driver);\n" +
                "NotImplemented = Not implemented\n" +
                "CommandNotFound = Command not found\n" +
                "NullDataParameter = Data parameter cannot be null\n" +
                "NullObjectParameter = Object parameter cannot be null\n" +
                "InsufficientDataLen = Data parameter length must be greater than \n" +
                "InsufficientObjectLen = Object parameter length must be greater than \n" +
                "BarcodeTypeNotSupported = Barcode type is not supported\n" +
                "BarcodeExceedsPrintWidth = Barcode exceeds print width\n" +
                "FailedCancelReceipt = Can not cancel receipt\n" +
                "BaudrateNotSupported = Baud rate is not supported\n" +
                "InvalidLineNumber = Invalid line number value\n" +
                "InvalidImageHeight = Image height exceeds maximum\n" +
                "InvalidImageWidth = Image width exceeds maximum\n" +
                "InvalidImageIndex = Invalid image index\n" +
                "PropNotFound = Property not found, \n" +
                "FSPrinterError01 = FS: Unknown command or invalid format\n" +
                "FSPrinterError02 = FS: Incorrect status\n" +
                "FSPrinterError03 = FS: Error, read extended error code\n" +
                "FSPrinterError04 = FS: CRC error, read extended error code\n" +
                "FSPrinterError05 = FS: Device expired\n" +
                "FSPrinterError06 = FS: Archive overflow error\n" +
                "FSPrinterError07 = FS: Invalid date/time value\n" +
                "FSPrinterError08 = FS: No data available\n" +
                "FSPrinterError09 = FS: Invalid parameter value\n" +
                "FSPrinterError10 = FS: TLV size too large\n" +
                "FSPrinterError11 = FS: No transport connection \n" +
                "FSPrinterError12 = FS: Cryptographic coprocessor resource exhausted\n" +
                "FSPrinterError14 = FS: Documents resource exhausted\n" +
                "FSPrinterError15 = FS: Maximum send time limit reached\n" +
                "FSPrinterError16 = FS: Fiscal day last for 24 hours\n" +
                "FSPrinterError17 = FS: Invalid time range betseen two operations\n" +
                "FSPrinterError20 = FS: Server message can not be received\n" +
                "PrinterError00 = No errors\n" +
                "PrinterError01 = FM: FM1 FM2 or RTC error\n" +
                "PrinterError02 = FM: FM1 missing\n" +
                "PrinterError03 = FM: FM2 missing\n" +
                "PrinterError04 = FM: Incorrect parameters in FM command\n" +
                "PrinterError05 = FM: No data requested available\n" +
                "PrinterError06 = FM: FM is in data output mode\n" +
                "PrinterError07 = FM: Invalid FM command parameters\n" +
                "PrinterError08 = FM: Command is not supported by FM\n" +
                "PrinterError09 = FM: Invalid command length\n" +
                "PrinterError0A = FM: Data format is not BCD\n" +
                "PrinterError0B = FM: FM memory failure\n" +
                "PrinterError0C = FM: Gross amount overflow\n" +
                "PrinterError0D = FM: Day totals overflow\n" +
                "PrinterError11 = FM: License in not entered\n" +
                "PrinterError12 = FM: Serial number is already entered\n" +
                "PrinterError13 = FM: Current date less than last record date\n" +
                "PrinterError14 = FM: Day total area overflow\n" +
                "PrinterError15 = FM: Day is opened\n" +
                "PrinterError16 = FM: Day is closed\n" +
                "PrinterError17 = FM: First day number more than last day number\n" +
                "PrinterError18 = FM: First day date more than last day date\n" +
                "PrinterError19 = FM: No data available\n" +
                "PrinterError1A = FM: Fiscalization area overflow\n" +
                "PrinterError1B = FM: Serial number not assigned\n" +
                "PrinterError1C = FM: There is corrupted record in the defined range\n" +
                "PrinterError1D = FM: Last day record is corrupted\n" +
                "PrinterError1E = FM: Fiscalizations overflow\n" +
                "PrinterError1F = FM: Registers memory is missing\n" +
                "PrinterError20 = FM: Cash register overflow after add\n" +
                "PrinterError21 = FM: Subtracted amount is more then cash register value\n" +
                "PrinterError22 = FM: Invalid date\n" +
                "PrinterError23 = FM: No activation record available\n" +
                "PrinterError24 = FM: Activation area overflow\n" +
                "PrinterError25 = FM: Activation with specified number not found\n" +
                "PrinterError26 = FM: More than 3 records corrupted\n" +
                "PrinterError27 = FM: Check sum error\n" +
                "PrinterError28 = FM: Technological sign is present\n" +
                "PrinterError29 = FM: Technological sign is not present\n" +
                "PrinterError2A = FM: FM size is not matched with firmware version\n" +
                "PrinterError2B = Previous command cannot be cancelled\n" +
                "PrinterError2C = Fiscal day is closed\n" +
                "PrinterError2D = Receipt total less that discount amount\n" +
                "PrinterError2E = There is not enough money in ECR\n" +
                "PrinterError2F = Serial number in memory and FM are mismatched\n" +
                "\n" +
                "PrinterError30 = Waiting for tax inspector password   \n" +
                "PrinterError31 = FM size is not matched with firmware version   \n" +
                "PrinterError32 = Technological reset is needed\n" +
                "PrinterError33 = Incorrect command parameters\n" +
                "PrinterError34 = No data available\n" +
                "PrinterError35 = Settings: Incorrect command parameters\n" +
                "PrinterError36 = Model: Incorrect command parameters\n" +
                "PrinterError37 = Command is not supported\n" +
                "PrinterError38 = PROM error\n" +
                "PrinterError39 = Internal software error\n" +
                "PrinterError3A = Day charge total overflow\n" +
                "PrinterError3B = Day total overflow\n" +
                "PrinterError3C = EJ: Invalid registration number\n" +
                "PrinterError3D = Day closed, operation is invalid\n" +
                "PrinterError3E = Day departments amount overflow\n" +
                "PrinterError3F = Day discount total overflow\n" +
                "PrinterError40 = Discount range error\n" +
                "PrinterError41 = Cash amount range overflow\n" +
                "PrinterError42 = Pay type 2 amount range overflow\n" +
                "PrinterError43 = Pay type 3 amount range overflow\n" +
                "PrinterError44 = Pay type 4 amount range overflow\n" +
                "PrinterError45 = Payment total less than receipt total\n" +
                "PrinterError46 = No enough cash in ECR\n" +
                "PrinterError47 = Day tax accumulator overflow\n" +
                "PrinterError48 = Receipt total overflow\n" +
                "PrinterError49 = Receipt is opened. Command is invalid\n" +
                "PrinterError4A = Receipt is opened. Command is invalid\n" +
                "PrinterError4B = Receipt buffer overflow\n" +
                "PrinterError4C = Dayly tax turnover accumulator overflow\n" +
                "PrinterError4D = Cashless amount is greater than receipt total\n" +
                "PrinterError4E = Day exceedes 24 hours\n" +
                "PrinterError4F = Invalid password\n" +
                "PrinterError50 = Printing previous command\n" +
                "PrinterError51 = Day cash accumulator overflow\n" +
                "PrinterError52 = Day payment type 2 accumulator overflow\n" +
                "PrinterError53 = Day payment type 3 accumulator overflow\n" +
                "PrinterError54 = Day payment type 4 accumulator overflow\n" +
                "PrinterError55 = Receipt closed, operation is invalid\n" +
                "PrinterError56 = No document to repeat\n" +
                "PrinterError57 = EJ: Day count not equal to FM day count\n" +
                "PrinterError58 = Waiting for repeat print command\n" +
                "PrinterError59 = Document is opened by another operator\n" +
                "PrinterError5A = Discount sum more than receipt sum\n" +
                "PrinterError5B = Charge accumulator overflow\n" +
                "PrinterError5C = Low power supply voltage, 24v\n" +
                "PrinterError5D = Table is undefined\n" +
                "PrinterError5E = Invalid command\n" +
                "PrinterError5F = Negative receipt total\n" +
                "PrinterError60 = Overflow on multiplication\n" +
                "PrinterError61 = Price range overflow\n" +
                "PrinterError62 = Quantity range overflow\n" +
                "PrinterError63 = Department range overflow\n" +
                "PrinterError64 = FM missing\n" +
                "PrinterError65 = No cash in department\n" +
                "PrinterError66 = Department total overflow\n" +
                "PrinterError67 = FM connection error\n" +
                "PrinterError68 = Insufficient tax turnover amount\n" +
                "PrinterError69 = Tax turnover overflow\n" +
                "PrinterError6A = Power supply error on reading I2C answer\n" +
                "PrinterError6B = No receipt paper\n" +
                "PrinterError6C = No journal paper\n" +
                "PrinterError6D = Insufficient tax amount\n" +
                "PrinterError6E = Tax amount overflow\n" +
                "PrinterError6F = Daily cash out overflow\n" +
                "PrinterError70 = FM overflow\n" +
                "PrinterError71 = Cutter failure\n" +
                "PrinterError72 = Command not supported in this submode\n" +
                "PrinterError73 = Command not supported in this mode\n" +
                "PrinterError74 = RAM failure\n" +
                "PrinterError75 = Power supply failure\n" +
                "PrinterError76 = Printer failure: no pulse\n" +
                "PrinterError77 = Printer failure: no sensor\n" +
                "PrinterError78 = Software replaced\n" +
                "PrinterError79 = FM replaced\n" +
                "PrinterError7A = Field cannot be edited\n" +
                "PrinterError7B = Hardware failure\n" +
                "PrinterError7C = Invalid date\n" +
                "PrinterError7D = Invalid date format\n" +
                "PrinterError7E = Invalid frame length\n" +
                "PrinterError7F = Total amount overflow\n" +
                "PrinterError80 = FM connection error\n" +
                "PrinterError81 = FM connection error\n" +
                "PrinterError82 = FM connection error\n" +
                "PrinterError83 = FM connection error\n" +
                "PrinterError84 = Cash amount overflow\n" +
                "PrinterError85 = Daily sale total overflow\n" +
                "PrinterError86 = Daily buy total overflow\n" +
                "PrinterError87 = Daily return sale total overflow\n" +
                "PrinterError88 = Daily return buy total overflow\n" +
                "PrinterError89 = Daily cash in total overflow\n" +
                "PrinterError8A = Receipt charge total overflow\n" +
                "PrinterError8B = Receipt discount total overflow\n" +
                "PrinterError8C = Negative receipt charge total\n" +
                "PrinterError8D = Negative receipt discount total\n" +
                "PrinterError8E = Zero receipt total\n" +
                "PrinterError8F = Non fiscal printer\n" +
                "PrinterError90 = Field range overflow\n" +
                "PrinterError91 = Print width error\n" +
                "PrinterError92 = Field overflow\n" +
                "PrinterError93 = RAM recovery successful\n" +
                "PrinterError94 = Receipt operation limit\n" +
                "PrinterError95 = Unknown electronic journal error\n" +
                "PrinterError96 = Day end required\n" +
                "PrinterError9B = Incorrect operation\n" +
                "PrinterError9C = Item code is not found\n" +
                "PrinterError9D = Incorrect item data\n" +
                "PrinterError9E = Invalid item data size \n" +
                "PrinterErrorA0 = EJ connection error\n" +
                "PrinterErrorA1 = EJ missing\n" +
                "PrinterErrorA2 = EJ: Invalid parameter or command format\n" +
                "PrinterErrorA3 = EJ: Invalid state\n" +
                "PrinterErrorA4 = EJ: Failure\n" +
                "PrinterErrorA5 = EJ: Cryptoprocessor failure\n" +
                "PrinterErrorA6 = EJ: Time limit exceeded\n" +
                "PrinterErrorA7 = EJ: Overflow\n" +
                "PrinterErrorA8 = EJ: Invalid date or time\n" +
                "PrinterErrorA9 = EJ: No data available\n" +
                "PrinterErrorAA = EJ: Negative document total\n" +
                "PrinterErrorAF = Incorrect received EJ data\n" +
                "PrinterErrorB0 = EJ: Quantity parameter overflow\n" +
                "PrinterErrorB1 = EJ: Amount parameter overflow\n" +
                "PrinterErrorB2 = EJ: Already activated\n" +
                "PrinterErrorB4 = Fiscalization record corrupted\n" +
                "PrinterErrorB5 = Serial number record corrupted\n" +
                "PrinterErrorB6 = Activation record corrupted\n" +
                "PrinterErrorB7 = Day totals records not found\n" +
                "PrinterErrorB8 = Last day total record not written\n" +
                "PrinterErrorB9 = FM data version mismatch\n" +
                "PrinterErrorBA = FM data corrupted\n" +
                "PrinterErrorBB = Current date less than EJ activation date\n" +
                "PrinterErrorBC = Current date less than fiscalization date\n" +
                "PrinterErrorBD = Current date less than last Z-report date\n" +
                "PrinterErrorBE = Command is not supported in current state\n" +
                "PrinterErrorBF = FM initialization is impossible\n" +
                "PrinterErrorC0 = Confirm date and time\n" +
                "PrinterErrorC1 = EJ report can not be interrupted\n" +
                "PrinterErrorC2 = Increased supply voltage\n" +
                "PrinterErrorC3 = Receipt total not equal to EJ receipt total\n" +
                "PrinterErrorC4 = Day number mismatch\n" +
                "PrinterErrorC5 = Slip buffer is empty\n" +
                "PrinterErrorC6 = Slip paper is missing\n" +
                "PrinterErrorC7 = Field is not editable in this mode\n" +
                "PrinterErrorC8 = Printer connection error\n" +
                "PrinterErrorC9 = Thermal head overheated\n" +
                "PrinterErrorCA = Temperature is not in valid range\n" +
                "PrinterErrorCB = Invalid receipt subtotal\n" +
                "PrinterErrorCC = Fiscal day in EJ is opened\n" +
                "PrinterErrorCD = EJ archive test is failed\n" +
                "PrinterErrorCE = RAM or ROM overflow\n" +
                "PrinterErrorCF = Invalid date. Please, set date\n" +
                "PrinterErrorD0 = EJ: Daily journal not printed\n" +
                "PrinterErrorD1 = No document in buffer\n" +
                "PrinterErrorD5 = Critical error when loading ERRxx\n" +
                "PrinterErrorE0 = Bill acceptor connection error\n" +
                "PrinterErrorE1 = Bill acceptor is busy\n" +
                "PrinterErrorE2 = Bill acceptor receipt total mismatch\n" +
                "PrinterErrorE3 = Bill acceptor error\n" +
                "PrinterErrorE4 = Bill acceptor total not zero\n" +
                "UnknownPrinterError = Unknown printer error\n" +
                "InternalHealthCheck = Internal health check\n" +
                "RecPaperEmpty = Receipt paper is empty.\n" +
                "RecPaperNearEnd = Receipt paper is near end.\n" +
                "RecLeverUp = Receipt station lever is up.\n" +
                "JrnPaperEmpty = Journal paper is empty.\n" +
                "JrnPaperNearEnd = Journal paper is near end.\n" +
                "JrnLeverUp = Journal station lever is up.\n" +
                "EJNearFull = Electronic journal is near full.\n" +
                "FMOverflow = Fiscal memory overflow.\n" +
                "FMLowBattery = Low fiscal memory battery voltage.\n" +
                "FMLastRecordCorrupted = Last fiscal memory record currupted.\n" +
                "FMEndDayRequired = Fiscal memory fiscal day is over.\n" +
                "NoErrors = No errors\n" +
                "PhysicalDeviceDescription = %s,  %s, Printer firmware: %s.%d, %s, FM firmware: %s.%d, %s\n";
    }

    private String getMessagesRuTxt() {
        return "#\n" +
                "# String constants for shtrihjavapos.jar file, SHTRIH-M, 2010\n" +
                "#\n" +
                "#\n" +
                "\n" +
                "ReceiptDuplicationNotSupported = Печать копии чека не поддерживается\n" +
                "DeviceIsEnabled = Устройство включено\n" +
                "AdditionalHeaderNotSupported = Дополнительный заголовок не поддерживается\n" +
                "AdditionalTrailerNotSupported = Дополнительный рекламный текст не поддерживается\n" +
                "ChangeDueTextNotSupported = Текст сдачи не поддерживается\n" +
                "MultipleContractorsNotSupported = Не поддерживается несколько ИНН\n" +
                "MessageTypeNotSupported = MessageType is not supported\n" +
                "MethodNotSupported = Метод не поддерживается для выбранного типа чека\n" +
                "InvalidParameterValue = Неверное значение параметра\n" +
                "InvalidPropertyValue = Неверное значение свойства\n" +
                "NotPaidReceiptsNotSupported = Не оплаченные чеки не поддерживаются\n" +
                "InvalidFiscalReceiptType = Неверное значение FiscalReceiptType\n" +
                "FailedConfirmDate = Ошибка подтверждения даты\n" +
                "CanNotChangeDate = Смена открыта, изменение даты невозможно\n" +
                "WrongPrinterState = Неверное состояние принтера\n" +
                "SlipStationNotPresent = Печать подкладных документов не поддерживается\n" +
                "ReceiptStationNotPresent = Чековая лента не поддерживается\n" +
                "JournalStationNotPresent = Контрольная лента не поддерживается\n" +
                "GraphicsNotSupported = Печать графики не поддерживается\n" +
                "EndDumpFailed = Невозможно изменить состояние принтера, выдача дампа\n" +
                "LockedTaxPassword = Блокировка по неправильному паролю налогового инспектора\n" +
                "ConfirmDateFailed = Невозможно изменить состояние принтера\n" +
                "WriteDecimalPointFailed = Невозможно изменить состояние принтера, изменение положения точки\n" +
                "StopTestFailed = Невозможно изменить состояние принтера, тестовый прогон\n" +
                "PrinterSupportesEAN13Only = Принтер поддерживает печать только EAN-13\n" +
                "InvalidBarcodeHeight = Высота штрихкода <= 0\n" +
                "InvalidBarcodePrintType = Неверный тип печати штрихкода\n" +
                "InvalidAnswerLength = Неверная длина ответа\n" +
                "InvalidFieldValue = Неверное значение поля\n" +
                "NoConnection = Нет связи\n" +
                "ReceiveTimeoutNotSupported = Таймаут чтения данных не поддерживается драйвером порта\n" +
                "NotImplemented = Не реализовано\n" +
                "CommandNotFound = Команда не найдена\n" +
                "NullDataParameter = Параметр Data должен иметь значение\n" +
                "NullObjectParameter = Параметр Object должен иметь значение\n" +
                "InsufficientDataLen = Длина параметра Data меньше чем \n" +
                "InsufficientObjectLen = Длина параметра Object меньше чем \n" +
                "BarcodeTypeNotSupported = Тип штрихкода не поддерживается\n" +
                "BarcodeExceedsPrintWidth = Ширина штрихкода больше ширины печати\n" +
                "FailedCancelReceipt = Ошибка отмены чека\n" +
                "BaudrateNotSupported = Указанная скорость не поддерживается\n" +
                "InvalidLineNumber = Неверный номер строки\n" +
                "InvalidImageHeight = Высота изображения больше максимальной\n" +
                "InvalidImageWidth = Ширина изображения больше максимальной\n" +
                "InvalidImageIndex = Неверный индекс изображения\n" +
                "PropNotFound = Свойство не найдено, \n" +
                "FSPrinterError01 = ФН: Неизвестная команда, неверный формат посылки или неизвестные параметры\n" +
                "FSPrinterError02 = ФН: Неверное состояние ФН\n" +
                "FSPrinterError03 = ФН: Ошибка ФН\n" +
                "FSPrinterError04 = ФН: Ошибка КС\n" +
                "FSPrinterError05 = ФН: Закончен срок эксплуатации ФН\n" +
                "FSPrinterError06 = ФН: Архив ФН переполнен\n" +
                "FSPrinterError07 = ФН: Неверные дата и/или время\n" +
                "FSPrinterError08 = ФН: Нет запрошенных данных\n" +
                "FSPrinterError09 = ФН: Некорректное значение параметров команды\n" +
                "FSPrinterError10 = ФН: Превышение размеров TLV данных\n" +
                "FSPrinterError11 = ФН: Нет транспортного соединения\n" +
                "FSPrinterError12 = ФН: Исчерпан ресурс КС (криптографического сопроцессора)\n" +
                "FSPrinterError14 = ФН: Исчерпан ресурс хранения\n" +
                "FSPrinterError15 = ФН: Исчерпан ресурс ожидания передачи сообщения\n" +
                "FSPrinterError16 = ФН: Продолжительность смены более 24 часов\n" +
                "FSPrinterError17 = ФН: Неверная разница во времени между 2 операциями\n" +
                "FSPrinterError20 = ФН: Сообщение от ОФД не может быть принято\n" +
                "PrinterError00 = Ошибок нет\n" +
                "PrinterError01 = ФП: Неисправен накопитель ФП 1, ФП 2 или часы\n" +
                "PrinterError02 = ФП: Отсутствует ФП 1\n" +
                "PrinterError03 = ФП: Отсутствует ФП 2\n" +
                "PrinterError04 = ФП: Некорректные параметры в команде обращения к ФП\n" +
                "PrinterError05 = ФП: Нет запрошенных данных\n" +
                "PrinterError06 = ФП: ФП в режиме вывода данных\n" +
                "PrinterError07 = ФП: Некорректные параметры в команде для данной реализации ФП\n" +
                "PrinterError08 = ФП: Команда не поддерживается в данной реализации ФП\n" +
                "PrinterError09 = ФП: Некорректная длина команды\n" +
                "PrinterError0A = ФП: Формат данных не BCD\n" +
                "PrinterError0B = ФП: Неисправна ячейка памяти ФП при записи итога\n" +
                "PrinterError0C = ФП: Переполнение необнуляемой суммы\n" +
                "PrinterError0D = ФП: Переполнение суммы итогов смен\n" +
                "PrinterError11 = ФП: Не введена лицензия\n" +
                "PrinterError12 = ФП: Заводской номер уже введен\n" +
                "PrinterError13 = ФП: Текущая дата меньше даты последней записи в ФП\n" +
                "PrinterError14 = ФП: Область сменных итогов ФП переполнена\n" +
                "PrinterError15 = ФП: Смена уже открыта\n" +
                "PrinterError16 = ФП: Смена не открыта\n" +
                "PrinterError17 = ФП: Номер первой смены больше номера последней смены\n" +
                "PrinterError18 = ФП: Дата первой смены больше даты последней смены\n" +
                "PrinterError19 = ФП: Нет данных в ФП\n" +
                "PrinterError1A = ФП: Область перерегистраций в ФП переполнена\n" +
                "PrinterError1B = ФП: Заводской номер не введен\n" +
                "PrinterError1C = ФП: В заданном диапазоне есть поврежденная запись\n" +
                "PrinterError1D = ФП: Повреждена последняя запись сменных итогов\n" +
                "PrinterError1E = ФП: Запись фискализации в накопителе не найдена\n" +
                "PrinterError1F = ФП: Отсутствует память регистров\n" +
                "PrinterError20 = ФП: Переполнение денежного регистра при добавлении\n" +
                "PrinterError21 = ФП: Вычитаемая сумма больше содержимого денежного регистра\n" +
                "PrinterError22 = ФП: Неверная дата\n" +
                "PrinterError23 = ФП: Нет записи активизации\n" +
                "PrinterError24 = ФП: Область активизаций переполнена\n" +
                "PrinterError25 = ФП: Нет активизации с запрашиваемым номером\n" +
                "PrinterError26 = ФП: В ФП больше 3 поврежденных записей\n" +
                "PrinterError27 = ФП: Повреждение контрольных сумм ФП\n" +
                "PrinterError28 = ФП: Технологическая метка в накопителе присутствует\n" +
                "PrinterError29 = ФП: Технологическая метка в накопителе отсутствует\n" +
                "PrinterError2A = ФП: Емкость микросхемы накопителя не соответствует текущей версии ПО\n" +
                "PrinterError2B = Невозможно отменить предыдущую команду\n" +
                "PrinterError2C = Обнулённая касса (повторное гашение невозможно)\n" +
                "PrinterError2D = Сумма чека по секции меньше суммы сторно\n" +
                "PrinterError2E = В ККТ нет денег для выплаты\n" +
                "PrinterError2F = Не совпадает заводской номер ККМ в оперативной памяти ФП с номером в накопителе\n" +
                "PrinterError30 = ККТ заблокирован, ждет ввода пароля налогового инспектора   \n" +
                "PrinterError31 = Сигнатура емкости накопителя не соответствует текущей версии ПО   \n" +
                "PrinterError32 = Требуется выполнение общего гашения\n" +
                "PrinterError33 = Некорректные параметры в команде\n" +
                "PrinterError34 = Нет данных\n" +
                "PrinterError35 = Некорректный параметр при данных настройках\n" +
                "PrinterError36 = Некорректные параметры в команде для данной реализации\n" +
                "PrinterError37 = Команда не поддерживается в данной реализации\n" +
                "PrinterError38 = Ошибка в ПЗУ\n" +
                "PrinterError39 = Внутренняя ошибка ПО\n" +
                "PrinterError3A = Переполнение накопления по надбавкам в смене\n" +
                "PrinterError3B = Переполнение накопления в смене\n" +
                "PrinterError3C = ЭКЛЗ: Неверный регистрационный номер\n" +
                "PrinterError3D = Смена не открыта - операция невозможна\n" +
                "PrinterError3E = Переполнение накопления по секциям в смене\n" +
                "PrinterError3F = Переполнение накопления по скидкам в смене\n" +
                "PrinterError40 = Переполнение диапазона скидок\n" +
                "PrinterError41 = Переполнение диапазона оплаты наличными\n" +
                "PrinterError42 = Переполнение диапазона оплаты типом 2\n" +
                "PrinterError43 = Переполнение диапазона оплаты типом 3\n" +
                "PrinterError44 = Переполнение диапазона оплаты типом 4\n" +
                "PrinterError45 = Cумма всех типов оплаты меньше итога чека\n" +
                "PrinterError46 = Не хватает наличности в кассе\n" +
                "PrinterError47 = Переполнение накопления по налогам в смене\n" +
                "PrinterError48 = Переполнение итога чека\n" +
                "PrinterError49 = Операция невозможна в открытом чеке данного типа\n" +
                "PrinterError4A = Открыт чек - операция невозможна\n" +
                "PrinterError4B = Буфер чека переполнен\n" +
                "PrinterError4C = Переполнение накопления по обороту налогов в смене\n" +
                "PrinterError4D = Вносимая безналичной оплатой сумма больше суммы чека\n" +
                "PrinterError4E = Смена превысила 24 часа\n" +
                "PrinterError4F = Неверный пароль\n" +
                "PrinterError50 = Идет печать предыдущей команды\n" +
                "PrinterError51 = переполнение накоплений наличными в смене\n" +
                "PrinterError52 = переполнение накоплений по типу оплаты 2 в смене\n" +
                "PrinterError53 = переполнение накоплений по типу оплаты 3 в смене\n" +
                "PrinterError54 = переполнение накоплений по типу оплаты 4 в смене\n" +
                "PrinterError55 = Чек закрыт - операция невозможна\n" +
                "PrinterError56 = Нет документа для повтора\n" +
                "PrinterError57 = ЭКЛЗ: Количество закрытых смен не совпадает с ФП\n" +
                "PrinterError58 = Ожидание команды продолжения печати\n" +
                "PrinterError59 = Документ открыт другим оператором\n" +
                "PrinterError5A = Скидка превышает накопления в чеке\n" +
                "PrinterError5B = Переполнение диапазона надбавок\n" +
                "PrinterError5C = Понижено напряжение 24В\n" +
                "PrinterError5D = Таблица не определена\n" +
                "PrinterError5E = Некорректная операция\n" +
                "PrinterError5F = Отрицательный итог чека\n" +
                "PrinterError60 = Переполнение при умножении\n" +
                "PrinterError61 = Переполнение диапазона цены\n" +
                "PrinterError62 = Переполнение диапазона количества\n" +
                "PrinterError63 = Переполнение диапазона отдела\n" +
                "PrinterError64 = ФП отсутствует\n" +
                "PrinterError65 = Не хватает денег в секции\n" +
                "PrinterError66 = Переполнение денег в секции\n" +
                "PrinterError67 = Ошибка связи с ФП\n" +
                "PrinterError68 = Не хватает денег по обороту налогов\n" +
                "PrinterError69 = Переполнение денег по обороту налогов\n" +
                "PrinterError6A = Ошибка питания в момент ответа по I2C\n" +
                "PrinterError6B = Нет чековой ленты\n" +
                "PrinterError6C = Нет контрольной ленты\n" +
                "PrinterError6D = Не хватает денег по налогу\n" +
                "PrinterError6E = Переполнение денег по налогу\n" +
                "PrinterError6F = Переполнение по выплате в смене\n" +
                "PrinterError70 = Переполнение ФП\n" +
                "PrinterError71 = Ошибка отрезчика\n" +
                "PrinterError72 = Команда не поддерживается в данном подрежиме\n" +
                "PrinterError73 = Команда не поддерживается в данном режиме\n" +
                "PrinterError74 = Ошибка ОЗУ\n" +
                "PrinterError75 = Ошибка питания\n" +
                "PrinterError76 = Ошибка принтера: нет импульсов с тахогенератора\n" +
                "PrinterError77 = Ошибка принтера: нет сигнала с датчиков\n" +
                "PrinterError78 = Замена ПО\n" +
                "PrinterError79 = Замена ФП\n" +
                "PrinterError7A = Поле не редактируется\n" +
                "PrinterError7B = Ошибка оборудования\n" +
                "PrinterError7C = Не совпадает дата\n" +
                "PrinterError7D = Неверный формат даты\n" +
                "PrinterError7E = Неверное значение в поле длины\n" +
                "PrinterError7F = Переполнение диапазона итога\n" +
                "PrinterError80 = Ошибка связи с ФП\n" +
                "PrinterError81 = Ошибка связи с ФП\n" +
                "PrinterError82 = Ошибка связи с ФП\n" +
                "PrinterError83 = Ошибка связи с ФП\n" +
                "PrinterError84 = Переполнение наличности\n" +
                "PrinterError85 = Переполнение по продажам в смене\n" +
                "PrinterError86 = Переполнение по покупкам в смене\n" +
                "PrinterError87 = Переполнение по возвратам продаж в смене\n" +
                "PrinterError88 = Переполнение по возвратам покупок в смене\n" +
                "PrinterError89 = Переполнение по внесению в смене\n" +
                "PrinterError8A = Переполнение по надбавкам в чеке\n" +
                "PrinterError8B = Переполнение по скидкам в чеке\n" +
                "PrinterError8C = Отрицательный итог надбавки в чеке\n" +
                "PrinterError8D = Отрицательный итог скидки в чеке\n" +
                "PrinterError8E = Нулевой итог чека\n" +
                "PrinterError8F = Касса не фискализирована\n" +
                "PrinterError90 = Поле превышает размер установленный в настройках\n" +
                "PrinterError91 = Выход за границу поля печати при данных настройках шрифта\n" +
                "PrinterError92 = Наложение полей\n" +
                "PrinterError93 = Восстановление ОЗУ прошло успешно\n" +
                "PrinterError94 = Исчерпан лимит операций в чеке\n" +
                "PrinterError95 = Неизвестная ошибка ЭКЛЗ\n" +
                "PrinterError96 = Выполните суточный отчет с гашением\n" +
                "PrinterError9B = Некорректное действие\n" +
                "PrinterError9C = Товар не найден по коду в базе товаров\n" +
                "PrinterError9D = Неверные данные в записе о товаре в базе товаров\n" +
                "PrinterError9E = Неверный размер файла базы или регистров товаров\n" +
                "PrinterErrorA0 = Ошибка связи с ЭКЛЗ\n" +
                "PrinterErrorA1 = ЭКЛЗ отсутствует\n" +
                "PrinterErrorA2 = ЭКЛЗ: Некорректный формат или параметр команды\n" +
                "PrinterErrorA3 = ЭКЛЗ: Некорректное состояние ЭКЛЗ\n" +
                "PrinterErrorA4 = ЭКЛЗ: Авария ЭКЛЗ\n" +
                "PrinterErrorA5 = ЭКЛЗ: Авария КС в составе ЭКЛЗ\n" +
                "PrinterErrorA6 = ЭКЛЗ: Исчерпан временной ресурс ЭКЛЗ\n" +
                "PrinterErrorA7 = ЭКЛЗ: ЭКЛЗ переполнена\n" +
                "PrinterErrorA8 = ЭКЛЗ: Неверные дата или время\n" +
                "PrinterErrorA9 = ЭКЛЗ: Нет запрошенных данных\n" +
                "PrinterErrorAA = ЭКЛЗ: Переполнение ЭКЛЗ (отрицательный итог документа)\n" +
                "PrinterErrorAF = Некорректные значения принятых данных от ЭКЛЗ\n" +
                "PrinterErrorB0 = ЭКЛЗ: Переполнение в параметре количество\n" +
                "PrinterErrorB1 = ЭКЛЗ: Переполнение в параметре сумма\n" +
                "PrinterErrorB2 = ЭКЛЗ: Уже активизирована\n" +
                "PrinterErrorB4 = Найденная запись фискализации повреждена\n" +
                "PrinterErrorB5 = Запись заводского номера ККМ повреждена\n" +
                "PrinterErrorB6 = Найденная запись активизации ЭКЛЗ повреждена\n" +
                "PrinterErrorB7 = Записи сменных итогов в накопителе не найдены\n" +
                "PrinterErrorB8 = Последняя запись сменных итогов не записана\n" +
                "PrinterErrorB9 = Сигнатура версии структуры данных в накопителе не совпадает с текущей версией ПО\n" +
                "PrinterErrorBA = Структура накопителя повреждена\n" +
                "PrinterErrorBB = Текущая дата меньше даты последней записи активизации ЭКЛЗ\n" +
                "PrinterErrorBC = Текущая дата меньше даты последней записи фискализации\n" +
                "PrinterErrorBD = Текущая дата меньше даты последней записи сменного итога\n" +
                "PrinterErrorBE = Команда не поддерживается в текущем состоянии\n" +
                "PrinterErrorBF = Инициализация накопителя невозможна\n" +
                "PrinterErrorC0 = Контроль даты и времени (подтвердите дату и время)\n" +
                "PrinterErrorC1 = ЭКЛЗ: суточный отчет с гашением прервать нельзя\n" +
                "PrinterErrorC2 = Превышение напряжения блока питания\n" +
                "PrinterErrorC3 = Несовпадение итогов чека с ЭКЛЗ\n" +
                "PrinterErrorC4 = Несовпадение номеров смен\n" +
                "PrinterErrorC5 = Буфер подкладного документа пуст\n" +
                "PrinterErrorC6 = Подкладной документ отсутствует\n" +
                "PrinterErrorC7 = Поле не редактируется в данном режиме\n" +
                "PrinterErrorC8 = Ошибка связи с принтером\n" +
                "PrinterErrorC9 = Перегрев печатающей головки\n" +
                "PrinterErrorCA = Температура вне условий эксплуатации\n" +
                "PrinterErrorCB = Неверный подытог чека\n" +
                "PrinterErrorCC = Смена в ЭКЛЗ уже закрыта\n" +
                "PrinterErrorCD = Тест целостности архива ЭКЛЗ не прошел\n" +
                "PrinterErrorCE = Объем ОЗУ или ПЗУ на ККМ исчерпан\n" +
                "PrinterErrorCF = Неверная дата (Часы сброшены? Установите дату!)\n" +
                "PrinterErrorD0 = Не распечатана контрольная лента по смене из ЭКЛЗ\n" +
                "PrinterErrorD1 = Нет данных в буфере\n" +
                "PrinterErrorD5 = Критическая ошибка при загрузке ERRxx\n" +
                "PrinterErrorE0 = Ошибка связи с купюроприемником\n" +
                "PrinterErrorE1 = Купюроприемник занят\n" +
                "PrinterErrorE2 = Итог чека не соответствует итогу купюроприемника\n" +
                "PrinterErrorE3 = Ошибка купюроприемника\n" +
                "PrinterErrorE4 = Итог купюроприемника не нулевой\n" +
                "UnknownPrinterError = Неизвестный код ошибки\n" +
                "InternalHealthCheck = Проверка состояния принтера\n" +
                "RecPaperEmpty = Нет чековой ленты\n" +
                "RecPaperNearEnd = Чековая лента близка к завершению\n" +
                "RecLeverUp = Поднят рычаг чековой ленты\n" +
                "JrnPaperEmpty = Нет контрольной ленты\n" +
                "JrnPaperNearEnd = Контрольная лента близка к завершению\n" +
                "JrnLeverUp = Поднят рычаг контрольной ленты\n" +
                "EJNearFull = ЭКЛЗ близка к заполнению\n" +
                "FMOverflow = Переполнение фискальной памяти\n" +
                "FMLowBattery = Низкое напряжение батареи фискальной памяти\n" +
                "FMLastRecordCorrupted = Последняя запись фискальной памяти повреждена\n" +
                "FMEndDayRequired = 24 часа ФП истекли\n" +
                "NoErrors = Ошибок нет\n" +
                "PhysicalDeviceDescription = %s,  %s, ПО ФР: %s.%d, %s, ПО ФП: %s.%d, %s\n";
    }
}