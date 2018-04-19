package com.shtrih.jpos1c.xml.check;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Формирование нового чека с заданным атрибутами.
 * При формирование чека ККТ должен проверять, что передаваемый код системы налогообложения
 * доступен для данного фискализированного ФН.
 */
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

    /**
     * Тип расчета
     * 1 - Приход
     * 2 - Возврат прихода
     * 3 - Расход
     * 4 - Возврат расхода
     */
    @Attribute
    public long PaymentType;

    /**
     * Код системы налогообложения
     */
    @Attribute
    public long TaxVariant;

    /**
     * Email покупателя
     */
    @Attribute(required = false)
    public String CustomerEmail;

    /**
     * Телефонный номер покупателя
     */
    @Attribute(required = false)
    public String CustomerPhone;

    /**
     * Адрес электронной почты отправителя чека
     */
    @Attribute(required = false)
    public String SenderEmail;

    /**
     * Адрес проведения расчетов
     */
    @Attribute(required = false)
    public String AddressSettle;

    /**
     * Место проведения расчетов
     */
    @Attribute(required = false)
    public String PlaceSettle;

    /**
     * Признак агента
     */
    @Attribute(required = false)
    public int AgentSign;

    /**
     * Данные агента
     */
    @Element(required = false)
    public AgentData AgentData = new AgentData();

    /**
     * Данные поставщика
     */
    @Element(required = false)
    public PurveyorData PurveyorData = new PurveyorData();

    public int getTaxVariant() {
        int result = 1;
        return result << TaxVariant;
    }
}

