package com.shtrih.jpos1c.xml.check;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Данные агента. XML Структура.
 */
@Root
public class AgentData {

    /**
     * Операция платежного агента
     */
    @Attribute(required = false)
    public String PayingAgentOperation;

    /**
     * Телефон платежного агента
     */
    @Attribute(required = false)
    public String PayingAgentPhone;

    /**
     * Телефон оператора по приему платежей
     */
    @Attribute(required = false)
    public String ReceivePaymentsOperatorPhone;

    /**
     * Телефон оператора перевода
     */
    @Attribute(required = false)
    public String MoneyTransferOperatorPhone;

    /**
     * Наименование оператора перевода
     */
    @Attribute(required = false)
    public String MoneyTransferOperatorName;

    /**
     * Адрес оператора перевода
     */
    @Attribute(required = false)
    public String MoneyTransferOperatorAddress;

    /**
     * ИНН оператора перевода
     */
    @Attribute(required = false)
    public String MoneyTransferOperatorVATIN;
}
