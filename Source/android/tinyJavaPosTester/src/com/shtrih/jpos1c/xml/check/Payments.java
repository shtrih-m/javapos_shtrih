package com.shtrih.jpos1c.xml.check;

import com.shtrih.jpos1c.xml.Money;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.math.BigDecimal;

/**
 * Параметры закрытия чека. Чек коррекции может быть оплачен только одним видом оплаты и без сдачи.
 */
@Root
public class Payments {

    /**
     * Сумма наличной оплаты
     */
    @Attribute(required = false)
    public BigDecimal Cash = BigDecimal.ZERO;

    /**
     * Сумма электронной оплаты
     */
    @Attribute(required = false)
    public BigDecimal ElectronicPayment = BigDecimal.ZERO;

    /**
     * Сумма предоплатой (зачетом аванса)
     */
    @Attribute(required = false)
    public BigDecimal AdvancePayment = BigDecimal.ZERO;

    /**
     * Сумма постоплатой (в кредит)
     */
    @Attribute(required = false)
    public BigDecimal Credit = BigDecimal.ZERO;

    /**
     * Сумма встречным предоставлением
     */
    @Attribute(required = false)
    public BigDecimal CashProvision = BigDecimal.ZERO;

    public long getCash() {
        return Money.toLong(Cash);
    }

    public long getElectronicPayment() {
        return Money.toLong(ElectronicPayment);
    }

    public long getAdvancePayment() {
        return Money.toLong(AdvancePayment);
    }

    public long getCredit() {
        return Money.toLong(Credit);
    }

    public long getCashProvision() {
        return Money.toLong(CashProvision);
    }
}
