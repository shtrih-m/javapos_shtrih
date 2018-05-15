package com.shtrih.fiscalprinter;

/**
 * Генетарор код товарной номенклатуры(КТН)
 */
public class GCNGenerator {

    private static final int gtinLength = 6;

    /**
     * Изделия из меха, 02
     *
     * @param gtin Идентификатор продукта GTIN.
     *             Используется 14 разрядный GTIN, при записи в ККТ, GTIN представляется как
     *             десятичное 14 знаковое число и преобразуется в BIN (big endian), размером 6 байт.
     * @param kiz  Контрольный (идентификационный) знак (КиЗ)
     *             КиЗ содержит в себе цифры и заглавные буквы латинского алфавита.
     *             Строковое значение, в кодировке CPP 866, размер – 20 байт.
     * @return Код товарной номенклатуры
     */
    public static byte[] generate02(long gtin, String kiz) {

        final int kizLength = 20;

        if (kiz.length() != kizLength)
            throw new IllegalArgumentException("Incorrect stamp length, expected " + kizLength + ", but was " + kiz.length());

        return new TLVWriter()
                .add(new byte[]{0x00, 0x02}) // два байта код (2)
                .addBE(gtin, gtinLength) // GTIN 6 байт Big Endian
                .add(kiz, kizLength) // КиЗ 20 байт
                .getBytes();
    }

    /**
     * Лекарственные препараты, 03
     *
     * @param gtin         Идентификатор продукта GTIN.
     *                     Используется 14 разрядный GTIN, при записи в ККТ, GTIN представляется как
     *                     десятичное 14 знаковое число и преобразуется в BIN (big endian), размером 6 байт.
     * @param serialNumber Серийный номер.
     *                     Serial содержит в себе цифры, заглавные и строчные буквы латинского алфавита.
     *                     Строкове значение, в кодировке CPP 866, размер – 13 байт.
     * @return Код товарной номенклатуры
     */
    public static byte[] generate03(long gtin, String serialNumber) {

        final int serialNumberLength = 13;

        if (serialNumber.length() != serialNumberLength)
            throw new IllegalArgumentException("Incorrect stamp length, expected " + serialNumberLength + ", but was " + serialNumber.length());

        return new TLVWriter()
                .add(new byte[]{0x00, 0x03}) // два байта код (3)
                .add(serialNumber, serialNumberLength) // 13 байт зав. номера
                .addBE(gtin, gtinLength) // GTIN 6 байт Big Endian
                .getBytes();
    }
}
