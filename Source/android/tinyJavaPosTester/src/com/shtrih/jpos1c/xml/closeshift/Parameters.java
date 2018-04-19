package com.shtrih.jpos1c.xml.closeshift;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class Parameters {

    /**
     * Количество кассовых чеков за смену
     */
    @Attribute
    public long NumberOfChecks;

    /**
     * Количество общее ФД за смену
     */
    @Attribute
    public long NumberOfDocuments;

    /**
     * Количество непереданных документов
     */
    @Attribute
    public long BacklogDocumentsCounter;

    /**
     * Номер первого непереданного документа
     */
    @Attribute(required = false)
    public Long BacklogDocumentFirstNumber;

    /**
     * Дата и время первого из непереданных документов
     */
    @Attribute(required = false)
    public String BacklogDocumentFirstDateTime;

    /**
     * Признак необходимости срочной замены ФН
     */
    @Attribute(required = false)
    public Boolean UrgentReplacementFN;

    /**
     * Признак переполнения памяти ФН
     */
    @Attribute(required = false)
    public Boolean MemoryOverflowFN;

    /**
     * Признак исчерпания ресурса ФН
     */
    @Attribute(required = false)
    public Boolean ResourcesExhaustionFN;
    
    /**
     * Срок действия ключей фискального признака.
     * Текущее значение реквизита определяется как остаток срока действия ключей в днях.
     */
    @Attribute(required = false)
    public Long ResourcesFN;

    /**
     * Признак того, что подтверждение оператора для переданного фискального документа
     * отсутствует более двух дней. Для ФД с версией ФФД 1.0 более 5 дней.
     */
    @Attribute(required = false)
    public Boolean OFDtimeout;
}
