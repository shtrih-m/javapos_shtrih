package com.shtrih.jpos1c.xml;

import java.math.BigDecimal;

public class Money {
    public static long toLong(BigDecimal value) {
        return value.multiply(new BigDecimal(100)).intValueExact();
    }
}
