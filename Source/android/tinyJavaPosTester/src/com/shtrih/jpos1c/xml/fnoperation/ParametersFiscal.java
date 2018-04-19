package com.shtrih.jpos1c.xml.fnoperation;

import com.shtrih.jpos1c.xml.Xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "Parameters")
public class ParametersFiscal {

    /**
     * ФИО и должность уполномоченного лица для проведения операции
     */
    @Attribute
    public String CashierName;

    /**
     * ИНН уполномоченного лица для проведения операции
     */
    @Attribute
    public String CashierVATIN;

    /**
     * Регистрационный номер ККТ
     */
    @Attribute(required = false)
    public String KKTNumber;

    /**
     * Название организации
     */
    @Attribute(required = false)
    public String OrganizationName;

    /**
     * ИНН организации
     */
    @Attribute(required = false)
    public String VATIN;

    /**
     * Адрес установки ККТ для проведения расчетов
     */
    @Attribute(required = false)
    public String AddressSettle;

    /**
     * Место проведения расчетов
     */
    @Attribute(required = false)
    public String PlaceSettle;

    /**
     * Коды системы налогообложения через разделитель ",".
     * Коды системы налогообложения приведены в таблице "Системы налогообложения".
     */
    @Attribute(required = false)
    public String TaxVariant;

    /**
     * Признак автономного режима
     */
    @Attribute(required = false)
    public boolean OfflineMode;

    /**
     * Признак шифрование данных
     */
    @Attribute(required = false)
    public boolean DataEncryption;

    /**
     * Признак расчетов за услуги
     */
    @Attribute(required = false)
    public boolean ServiceSign;

    /**
     * Продажа подакцизного товара
     */
    @Attribute(required = false)
    public boolean SaleExcisableGoods;

    /**
     * Признак проведения азартных игр
     */
    @Attribute(required = false)
    public boolean SignOfGambling;

    /**
     * Признак проведения лотереи
     */
    @Attribute(required = false)
    public boolean SignOfLottery;

    /**
     * Коды признаков агента через разделитель ",".
     * (Коды приведены в таблице 10 форматов фискальных данных)
     */
    @Attribute
    public String SignOfAgent;

    /**
     * Признак формирования только БСО
     */
    @Attribute(required = false)
    public boolean BSOSing;

    /**
     * Признак ККТ для расчетов только в Интернет
     */
    @Attribute(required = false)
    public boolean CalcOnlineSign;

    /**
     * Признак установки принтера в автомате
     */
    @Attribute(required = false)
    public boolean PrinterAutomatic;

    /**
     * Признак автоматического режима
     */
    @Attribute(required = false)
    public boolean AutomaticMode;

    /**
     * Номер автомата для автоматического режима
     */
    @Attribute(required = false)
    public String AutomaticNumber;

    /**
     * Код причины перерегистрации
     * * указывается только для операции "Изменение параметров регистрации"
     * (Коды приведены в таблице 15 форматов фискальных данных)
     */
    @Attribute(required = false)
    public int ReasonCode;

    /**
     * Коды причин изменения сведений о ККТ через разделитель ".
     * (Коды приведены в таблице 16 форматов фискальных данных)
     */
    @Attribute(required = false)
    public String InfoChangesReasonsCodes;

    /**
     * Название организации ОФД
     */
    @Attribute
    public String OFDOrganizationName;

    /**
     * ИНН организации ОФД
     */
    @Attribute
    public String OFDVATIN;

    /**
     * Адрес сайта уполномоченного органа (ФНС) в сети «Интернет»
     */
    @Attribute(required = false)
    public String FNSWebSite;

    /**
     * Адрес электронной почты отправителя чека
     */
    @Attribute(required = false)
    public String SenderEmail;

    public String toXml() throws Exception {
        return Xml.serialize(this);
    }
}
