package com.shtrih.jpos1c.xml.check;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Данные поставщика. XML Структура.
 */
@Root
public class PurveyorData {

    /**
     * Телефон поставщика
     */
    @Attribute(required = false)
    public String PurveyorPhone;

    /**
     * Наименование поставщика
     */
    @Attribute(required = false)
    public String PurveyorName;

    /**
     * ИНН поставщика
     */
    @Attribute(required = false)
    public String PurveyorVATIN;
}

