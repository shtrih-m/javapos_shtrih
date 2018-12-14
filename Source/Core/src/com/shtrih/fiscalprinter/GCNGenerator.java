package com.shtrih.fiscalprinter;

/**
 * Генетарор код товарной номенклатуры(КТН)
 */
public class GCNGenerator {

    private static final int codeLength = 2;
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

        return generate(2, gtin, kiz);
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

        return generate(3, gtin, serialNumber);
    }

    /**
     * Табачная продукция, 05
     *
     * @param gtin Идентификатор продукта GTIN.
     *             Используется 14 разрядный GTIN, при записи в ККТ, GTIN представляется как
     *             десятичное 14 знаковое число и преобразуется в BIN (big endian), размером 6 байт.
     * @param id   Код идентификации экземпляра товара, строка до 24 символов..
     * @return Код товарной номенклатуры
     */
    public static byte[] generate5408(long gtin, String id) {
        return generate(5408, gtin, id);
    }

    /**
     * Обувные твары, 5408
     *
     * @param gtin Идентификатор продукта GTIN.
     *             Используется 14 разрядный GTIN, при записи в ККТ, GTIN представляется как
     *             десятичное 14 знаковое число и преобразуется в BIN (big endian), размером 6 байт.
     * @param id   Код идентификации экземпляра товара, строка до 24 символов..
     * @return Код товарной номенклатуры
     */
    public static byte[] generate05(long gtin, String id) {
        return generate(5, gtin, id);
    }

    /**
     * Универсальный метод генерации КТН
     *
     * @param code  Код маркировки, число 2 байта.
     * @param gtin  Идентификатор продукта GTIN.
     *              Используется 14 разрядный GTIN, при записи в ККТ, GTIN представляется как
     *              десятичное 14 знаковое число и преобразуется в BIN (big endian), размером 6 байт.
     * @param value Строка до 24 символов.
     * @return Код товарной номенклатуры
     */
    public static byte[] generate(int code, long gtin, String value) {
        return new TLVWriter()
                .addBE(code, codeLength) // Код 2 байта Big Endian
                .addBE(gtin, gtinLength) // GTIN 6 байт Big Endian
                .add(value) // Значение до 24 байт
                .getBytes();
    }
}
