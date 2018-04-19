package com.shtrih.jpos1c.xml.openshift;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class Parameters {

    /**
     * Признак необходимости срочной замены ФН
     */
    @Attribute
    public boolean UrgentReplacementFN;

    /**
     * Признак переполнения памяти ФН
     */
    @Attribute
    public boolean MemoryOverflowFN;

    /**
     * Признак исчерпания ресурса ФН
     */
    @Attribute
    public boolean ResourcesExhaustionFN;

    /**
     * Признак того, что подтверждение оператора для переданного фискального документа
     * отсутствует более двух дней. Для ФД с версией ФФД 1.0 более 5 дней.
     */
    @Attribute
    public boolean OFDtimeout;
}
