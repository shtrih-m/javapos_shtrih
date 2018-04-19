package com.shtrih.jpos1c.xml.inputparameters;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class Parameters {

    /**
     * ФИО и должность уполномоченного лица для проведения операции
     */
    @Attribute
    public String CashierName;

    /**
     * ИНН уполномоченного лица для проведения операции
     */
    @Attribute(required = false)
    public String CashierVATIN;
}


