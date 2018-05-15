package com.shtrih.jpos1c.xml.check;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Код товарной номенклатуры. XML Структура.
 */
@Root
public class GoodCodeData {

    /**
     * Тип маркировки. Список значений:
     *  "02" – изделия из меха
     *  "03" – лекарственные препараты
     */
    @Attribute
    public String StampType;

    /**
     * Контрольный идентификационный знак (КиЗ)
     */
    @Attribute
    public String Stamp;

    /**
     * Глобальный идентификатор торговой единицы (GTIN)
     */
    @Attribute
    public long GTIN;
}
