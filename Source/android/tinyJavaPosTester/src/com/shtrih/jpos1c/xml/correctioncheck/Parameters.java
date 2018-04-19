package com.shtrih.jpos1c.xml.correctioncheck;

import com.shtrih.jpos1c.xml.Money;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Текст в формате XML содержит описание передаваемого для печати чека коррекции.
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
     * Тип коррекции
     *   0 - самостоятельно
     *   1 - по предписанию
     */
    @Attribute
    public int CorrectionType;

    /**
     * Тип расчета
     *  1 - Приход
     *  2 - Возврат прихода
     *  3 - Расход
     *  4 - Возврат расхода
     */
    @Attribute
    public int PaymentType;

    /**
     * Код системы налогообложения
     */
    @Attribute
    public long TaxVariant;

    /**
     * Наименование основания для коррекции
     */
    @Attribute(required = false)
    public String CorrectionBaseName;

    /**
     * Дата документа основания для коррекции
     */
    @Attribute(required = false)
    public String CorrectionBaseDate;

    /**
     * Номер документа основания для коррекции
     */
    @Attribute(required = false)
    public String CorrectionBaseNumber;

    /**
     * Сумма расчета, указанного в чеке
     */
    @Attribute
    public BigDecimal Sum;

    /**
     * Сумма НДС чека по ставке 18%
     */
    @Attribute(required = false)
    public BigDecimal SumTAX18;

    /**
     * Сумма НДС чека по ставке 10%
     */
    @Attribute(required = false)
    public BigDecimal SumTAX10;

    /**
     * Сумма НДС чека по ставке 0%
     */
    @Attribute(required = false)
    public BigDecimal SumTAX0;

    /**
     * Сумма НДС чека по без НДС
     */
    @Attribute(required = false)
    public BigDecimal SumTAXNone;

    /**
     * Сумма НДС чека по расч. ставке 10/110
     */
    @Attribute(required = false)
    public BigDecimal SumTAX110;

    /**
     * Сумма НДС чека по расч. ставке 18/118
     */
    @Attribute(required = false)
    public BigDecimal SumTAX118;
    private long correctionBaseDate;
    private int taxVarian;

    public long getSumTAX18() {
        return Money.toLong(SumTAX18);
    }

    public long getSumTAX10() {
        return Money.toLong(SumTAX10);
    }

    public long getSumTAX0() {
        return Money.toLong(SumTAX0);
    }

    public long getSumTAXNone() {
        return Money.toLong(SumTAXNone);
    }

    public long getSumTAX118() {
        return Money.toLong(SumTAX118);
    }

    public long getSumTAX110() {
        return Money.toLong(SumTAX110);
    }

    public long getSum() {
        return Money.toLong(Sum);
    }

    public long getCorrectionBaseDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date dateStr = sdf.parse(CorrectionBaseDate);

        return dateStr.getTime() / 1000L;
        //return new Date(dateStr.getYear(), dateStr.getMonth(), dateStr.getDay()).getTime() / 1000L;
    }

    public int getTaxVariant() {
        int result = 1;
        return result << TaxVariant;
    }
}