package com.shtrih.jpos1c.xml.check;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Печать штрихкода. Осуществляется с автоматическим размером с выравниванием по центру чека.
 * Тип штрихкода может иметь одно из следующих значений: EAN8, EAN13, CODE39, QR.
 * В случае, если модель устройства не поддерживает печать штрихкода, выдается ошибка.
 */
@Root
public class Barcode {

    /**
     * Строка, определяющая тип штрихкода
     */
    @Attribute
    public String BarcodeType;

    /**
     * Значение штрихкода
     */
    @Attribute
    public String Barcode;
}
