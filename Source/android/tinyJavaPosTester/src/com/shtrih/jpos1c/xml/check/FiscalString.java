package com.shtrih.jpos1c.xml.check;

import com.shtrih.jpos1c.xml.Money;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.math.BigDecimal;

/**
 * Регистрирует фискальную строку с переданными реквизитами.
 * При печати длинных фискальных строк необходимо делать перенос на следующую строку.
 */
@Root
public class FiscalString {
    /**
     * Наименование товара
     */
    @Attribute
    public String Name;

    /**
     * Количество товара
     */
    @Attribute
    public BigDecimal Quantity;

    /**
     * Цена единицы товара с учетом скидок/наценок
     */
    @Attribute
    public BigDecimal PriceWithDiscount;

    /**
     * Конечная сумма по позиции чека с учетом всех скидок/наценок
     */
    @Attribute
    public BigDecimal SumWithDiscount;

    /**
     * Сумма скидок и наценок
     */
//    @Attribute(required = false)
//    public BigDecimal DiscountSum;

    /**
     * Отдел, по которому ведется продажа
     */
    @Attribute(required = false)
    public int Department;

    /**
     * Ставка НДС. Список значений:
     *  "none" - БЕЗ НДС
     *  "18" - НДС 18
     *  "10" - НДС 10
     *  "0" - НДС 0
     *  "10/110" - расч. ставка 10/110
     *  "18/118" - расч. ставка 18/118
     */
    @Attribute
    public String Tax;

    /**
     * Признак способа расчета
     */
    @Attribute(required = false)
    public int SignMethodCalculation;

    /**
     * Признак предмета расчета
     */
    @Attribute(required = false)
    public int SignCalculationObject;

    /**
     * Признак агента по предмету расчета
     */
    @Attribute(required = false)
    public int SignSubjectCalculationAgent;

    /**
     * Данные агента
     */
    @Element(required = false)
    public AgentData AgentData = new AgentData();

    /**
     * Данные агента
     */
    @Element(required = false)
    public PurveyorData PurveyorData = new PurveyorData();

    /**
     * Единица измерения предмета расчета
     */
    @Element(required = false)
    public String MeasurementUnit;

    /**
     * Данные кода товарной номенклатуры
     */
    @Element(required = false)
    public GoodCodeData GoodCodeData;

    public long getAmount() {
        return Money.toLong(SumWithDiscount);
    }

    public long getPrice() {
        return Money.toLong(PriceWithDiscount);
    }

    public long getQuantity() {
        return Quantity.multiply(new BigDecimal(1000)).intValueExact();
    }

    public int getTax() {
        switch (Tax) {
            case "18":
                return 1;
            case "10":
                return 2;
            case "0":
                return 3;
            case "none":
                return 4;
            case "18/118":
                return 5;
            case "10/110":
                return 6;
            default:
                throw new UnsupportedOperationException("Неизвестный тип налоговой ставки: " + Tax);
        }
    }
}
