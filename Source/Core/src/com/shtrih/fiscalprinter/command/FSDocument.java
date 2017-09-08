package com.shtrih.fiscalprinter.command;

/**
 * @author P.Zhirkov
 */
public class FSDocument{

    private final int docType;
    private final boolean ticketReceived;
    private final FSDateTime dateTime;
    private final long docNumber;
    private final long docSign;

    public FSDocument(CommandInputStream in, int docType) throws Exception
    {
        this.docType = docType;
        ticketReceived = in.readByte() != 0;
        dateTime = in.readFSDate();
        docNumber = in.readInt();
        docSign = in.readLong(4);
    }

    /**
     * @return the docType
     */
    public int getDocType() {
        return docType;
    }

    /**
     * @return the sentToOFD
     */
    public boolean isTicketReceived() {
        return ticketReceived;
    }

    /**
     * @return the dateTime
     */
    public FSDateTime getDateTime() {
        return dateTime;
    }

    /**
     * @return the docNumber
     */
    public long getDocNumber() {
        return docNumber;
    }

    /**
     * @return the docSign
     */
    public long getDocSign() {
        return docSign;
    }
}

/*
//  SDocType3 = 'Кассовый чек';

{Дата и время	DATE_TIME	5
Номер ФД	Uint32, LE	4
Фискальный признак	Uint32, LE	4
Тип операции	Byte	1
Сумма операции	Uint40, LE	5}

procedure TFiscalPrinter.DecodeDocType3(const Data: string);
begin
  CheckMinLength(Data, 19);
  DecodeDataTime(Data);
  DocumentNumber := BinToInt(Data, 6, 4);
  FiscalSign := BinToInt(Data, 10, 4);
  OperationType := Ord(Data[14]);
  Summ1 := BinToAmount(Data, 15, 5);
end;

//6 Отчёт о закрытии фискального накопителя
{Дата и время	DATE_TIME	5
Номер ФД	Uint32, LE	4
Фискальный признак	Uint32, LE	4
ИНН	ASCII	12
Регистрационный номер ККТ	ASCII	20}

procedure TFiscalPrinter.DecodeDocType6(const Data: string);
begin
  CheckMinLength(Data, 45);
  DecodeDataTime(Data);
  DocumentNumber := BinToInt(Data, 6, 4);
  FiscalSign := BinToInt(Data, 10, 4);
  INN := Copy(Data, 14, 12);
  KKTRegistrationNumber := Copy(Data, 26, 20);
end;

//11 Отчёт об изменении параметров регистрации
{Дата и время	DATE_TIME	5
Номер ФД	Uint32, LE	4
Фискальный признак	Uint32, LE	4
ИНН	ASCII	12
Регистрационный номер ККТ	ASCII	20
Код налогообложения	Byte	1
Режим работы	Byte	1
Код причины перерегистрации	Byte	1}

procedure TFiscalPrinter.DecodeDocType11(const Data: string);
begin
  CheckMinLength(Data, 48);
  DecodeDataTime(Data);
  DocumentNumber := BinToInt(Data, 6, 4);
  FiscalSign := BinToInt(Data, 10, 4);
  INN := Copy(Data, 14, 12);
  KKTRegistrationNumber := Copy(Data, 26, 20);
  TaxType := Ord(Data[46]);
  WorkMode := Ord(Data[47]);
  RegistrationReasonCode := Ord(Data[48]);
end;

//21 Отчет о состоянии расчетов
{Дата и время	DATE_TIME	5
Номер ФД	Uint32, LE	4
Фискальный признак	Uint32, LE	4
Кол-во неподтвержденных документов	Uint32, LE	4
Дата первого неподтвержденного документа	DATE_TIME	5}
procedure TFiscalPrinter.DecodeDocType21(const Data: string);
begin
  CheckMinLength(Data, 20);
  DecodeDataTime(Data);
  DocumentNumber := BinToInt(Data, 6, 4);
  FiscalSign := BinToInt(Data, 10, 4);
  DocumentCount := BinToInt(Data, 14, 2);
  DecodeDataTime(Copy(Data, 16, 5));
  Date2 := ECRDate;
  Time2 := ECRTime;
  DecodeDataTime(Data);
end;


*/