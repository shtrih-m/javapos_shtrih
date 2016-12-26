/*
 * CashRegister.java
 *
 * Created on 11 Март 2010 г., 17:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 * @author V.Kravtsov
 */
public class CashRegister {

    private final int number;
    private long value = 0;

    public CashRegister(int number) {
        this.number = number;
    }

    public CashRegister(int number, long value) {
        this.number = number;
        this.value = value;
    }
    
    public int getNumber() {
        return number;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public static String getName(int number) {
        return getRusName(number);
    }

    private static String SCashRegisterRus00 = "Накопление продаж в 1 отдел в чеке";
    private static String SCashRegisterRus01 = "Накопление покупок в 1 отдел в чеке";
    private static String SCashRegisterRus02 = "Накопление возврата продаж в 1 отдел в чеке";
    private static String SCashRegisterRus03 = "Накопление возврата покупок в 1 отдел в чеке";
    private static String SCashRegisterRus04 = "Накопление продаж в 2 отдел в чеке";
    private static String SCashRegisterRus05 = "Накопление покупок в 2 отдел в чеке";
    private static String SCashRegisterRus06 = "Накопление возврата продаж в 2 отдел в чеке";
    private static String SCashRegisterRus07 = "Накопление возврата покупок в 2 отдел в чеке";
    private static String SCashRegisterRus08 = "Накопление продаж в 3 отдел в чеке";
    private static String SCashRegisterRus09 = "Накопление покупок в 3 отдел в чеке";
    private static String SCashRegisterRus0A = "Накопление возврата продаж в 3 отдел в чеке";
    private static String SCashRegisterRus0B = "Накопление возврата покупок в 3 отдел в чеке";
    private static String SCashRegisterRus0C = "Накопление продаж в 4 отдел в чеке";
    private static String SCashRegisterRus0D = "Накопление покупок в 4 отдел в чеке";
    private static String SCashRegisterRus0E = "Накопление возврата продаж в 4 отдел в чеке";
    private static String SCashRegisterRus0F = "Накопление возврата покупок в 4 отдел в чеке";
    private static String SCashRegisterRus10 = "Накопление продаж в 5 отдел в чеке";
    private static String SCashRegisterRus11 = "Накопление покупок в 5 отдел в чеке";
    private static String SCashRegisterRus12 = "Накопление возврата продаж в 5 отдел в чеке";
    private static String SCashRegisterRus13 = "Накопление возврата покупок в 5 отдел в чеке";
    private static String SCashRegisterRus14 = "Накопление продаж в 6 отдел в чеке";
    private static String SCashRegisterRus15 = "Накопление покупок в 6 отдел в чеке";
    private static String SCashRegisterRus16 = "Накопление возврата продаж в 6 отдел в чеке";
    private static String SCashRegisterRus17 = "Накопление возврата покупок в 6 отдел в чеке";
    private static String SCashRegisterRus18 = "Накопление продаж в 7 отдел в чеке";
    private static String SCashRegisterRus19 = "Накопление покупок в 7 отдел в чеке";
    private static String SCashRegisterRus1A = "Накопление возврата продаж в 7 отдел в чеке";
    private static String SCashRegisterRus1B = "Накопление возврата покупок в 7 отдел в чеке";
    private static String SCashRegisterRus1C = "Накопление продаж в 8 отдел в чеке";
    private static String SCashRegisterRus1D = "Накопление покупок в 8 отдел в чеке";
    private static String SCashRegisterRus1E = "Накопление возврата продаж в 8 отдел в чеке";
    private static String SCashRegisterRus1F = "Накопление возврата покупок в 8 отдел в чеке";
    private static String SCashRegisterRus20 = "Накопление продаж в 9 отдел в чеке";
    private static String SCashRegisterRus21 = "Накопление покупок в 9 отдел в чеке";
    private static String SCashRegisterRus22 = "Накопление возврата продаж в 9 отдел в чеке";
    private static String SCashRegisterRus23 = "Накопление возврата покупок в 9 отдел в чеке";
    private static String SCashRegisterRus24 = "Накопление продаж в 10 отдел в чеке";
    private static String SCashRegisterRus25 = "Накопление покупок в 10 отдел в чеке";
    private static String SCashRegisterRus26 = "Накопление возврата продаж в 10 отдел в чеке";
    private static String SCashRegisterRus27 = "Накопление возврата покупок в 10 отдел в чеке";
    private static String SCashRegisterRus28 = "Накопление продаж в 11 отдел в чеке";
    private static String SCashRegisterRus29 = "Накопление покупок в 11 отдел в чеке";
    private static String SCashRegisterRus2A = "Накопление возврата продаж в 11 отдел в чеке";
    private static String SCashRegisterRus2B = "Накопление возврата покупок в 11 отдел в чеке";
    private static String SCashRegisterRus2C = "Накопление продаж в 12 отдел в чеке";
    private static String SCashRegisterRus2D = "Накопление покупок в 12 отдел в чеке";
    private static String SCashRegisterRus2E = "Накопление возврата продаж в 12 отдел в чеке";
    private static String SCashRegisterRus2F = "Накопление возврата покупок в 12 отдел в чеке";
    private static String SCashRegisterRus30 = "Накопление продаж в 13 отдел в чеке";
    private static String SCashRegisterRus31 = "Накопление покупок в 13 отдел в чеке";
    private static String SCashRegisterRus32 = "Накопление возврата продаж в 13 отдел в чеке";
    private static String SCashRegisterRus33 = "Накопление возврата покупок в 13 отдел в чеке";
    private static String SCashRegisterRus34 = "Накопление продаж в 14 отдел в чеке";
    private static String SCashRegisterRus35 = "Накопление покупок в 14 отдел в чеке";
    private static String SCashRegisterRus36 = "Накопление возврата продаж в 14 отдел в чеке";
    private static String SCashRegisterRus37 = "Накопление возврата покупок в 14 отдел в чеке";
    private static String SCashRegisterRus38 = "Накопление продаж в 15 отдел в чеке";
    private static String SCashRegisterRus39 = "Накопление покупок в 15 отдел в чеке";
    private static String SCashRegisterRus3A = "Накопление возврата продаж в 15 отдел в чеке";
    private static String SCashRegisterRus3B = "Накопление возврата покупок в 15 отдел в чеке";
    private static String SCashRegisterRus3C = "Накопление продаж в 16 отдел в чеке";
    private static String SCashRegisterRus3D = "Накопление покупок в 16 отдел в чеке";
    private static String SCashRegisterRus3E = "Накопление возврата продаж в 16 отдел в чеке";
    private static String SCashRegisterRus3F = "Накопление возврата покупок в 16 отдел в чеке";
    private static String SCashRegisterRus40 = "Накопление скидок с продаж в чеке";
    private static String SCashRegisterRus41 = "Накопление скидок с покупок в чеке";
    private static String SCashRegisterRus42 = "Накопление скидок с возврата продаж в чеке";
    private static String SCashRegisterRus43 = "Накопление скидок с возврата покупок в чеке";
    private static String SCashRegisterRus44 = "Накопление надбавок на продажи в чеке";
    private static String SCashRegisterRus45 = "Накопление надбавок на покупок в чеке";
    private static String SCashRegisterRus46 = "Накопление надбавок на возврата продаж в чеке";
    private static String SCashRegisterRus47 = "Накопление надбавок на возврата покупок в чеке";
    private static String SCashRegisterRus48 = "Накопление оплат продаж наличными в чеке";
    private static String SCashRegisterRus49 = "Накопление оплат покупок наличными в чеке";
    private static String SCashRegisterRus4A = "Накопление оплат возврата продаж наличными в чеке";
    private static String SCashRegisterRus4B = "Накопление оплат возврата покупок наличными в чеке";
    private static String SCashRegisterRus4C = "Накопление оплат продаж видом оплаты 2 в чеке";
    private static String SCashRegisterRus4D = "Накопление оплат покупок видом оплаты 2 в чеке";
    private static String SCashRegisterRus4E = "Накопление оплат возврата продаж видом оплаты 2 в чеке";
    private static String SCashRegisterRus4F = "Накопление оплат возврата покупок видом оплаты 2 в чеке";
    private static String SCashRegisterRus50 = "Накопление оплат продаж видом оплаты 3 в чеке";
    private static String SCashRegisterRus51 = "Накопление оплат покупок видом оплаты 3 в чеке";
    private static String SCashRegisterRus52 = "Накопление оплат возврата продаж видом оплаты 3 в чеке";
    private static String SCashRegisterRus53 = "Накопление оплат возврата покупок видом оплаты 3 в чеке";
    private static String SCashRegisterRus54 = "Накопление оплат продаж видом оплаты 4 в чеке";
    private static String SCashRegisterRus55 = "Накопление оплат покупок видом оплаты 4 в чеке";
    private static String SCashRegisterRus56 = "Накопление оплат возврата продаж видом оплаты 4 в чеке";
    private static String SCashRegisterRus57 = "Накопление оплат возврата покупок видом оплаты 4 в чеке";
    private static String SCashRegisterRus58 = "Оборот по налогу А с продаж в чеке";
    private static String SCashRegisterRus59 = "Оборот по налогу А с покупок в чеке";
    private static String SCashRegisterRus5A = "Оборот по налогу А с возврата продаж в чеке";
    private static String SCashRegisterRus5B = "Оборот по налогу А с возврата покупок в чеке";
    private static String SCashRegisterRus5C = "Оборот по налогу Б с продаж в чеке";
    private static String SCashRegisterRus5D = "Оборот по налогу Б с покупок в чеке";
    private static String SCashRegisterRus5E = "Оборот по налогу Б с возврата продаж в чеке";
    private static String SCashRegisterRus5F = "Оборот по налогу Б с возврата покупок в чеке";
    private static String SCashRegisterRus60 = "Оборот по налогу В с продаж в чеке";
    private static String SCashRegisterRus61 = "Оборот по налогу В с покупок в чеке";
    private static String SCashRegisterRus62 = "Оборот по налогу В с возврата продаж в чеке";
    private static String SCashRegisterRus63 = "Оборот по налогу В с возврата покупок в чеке";
    private static String SCashRegisterRus64 = "Оборот по налогу Г с продаж в чеке";
    private static String SCashRegisterRus65 = "Оборот по налогу Г с покупок в чеке";
    private static String SCashRegisterRus66 = "Оборот по налогу Г с возврата продаж в чеке";
    private static String SCashRegisterRus67 = "Оборот по налогу Г с возврата покупок в чеке";
    private static String SCashRegisterRus68 = "Накопления по налогу А с продаж в чеке";
    private static String SCashRegisterRus69 = "Накопления по налогу А с покупок в чеке";
    private static String SCashRegisterRus6A = "Накопления по налогу А с возврата продаж в чеке";
    private static String SCashRegisterRus6B = "Накопления по налогу А с возврата покупок в чеке";
    private static String SCashRegisterRus6C = "Накопления по налогу Б с продаж в чеке";
    private static String SCashRegisterRus6D = "Накопления по налогу Б с покупок в чеке";
    private static String SCashRegisterRus6E = "Накопления по налогу Б с возврата продаж в чеке";
    private static String SCashRegisterRus6F = "Накопления по налогу Б с возврата покупок в чеке";
    private static String SCashRegisterRus70 = "Накопления по налогу В с продаж в чеке";
    private static String SCashRegisterRus71 = "Накопления по налогу В с покупок в чеке";
    private static String SCashRegisterRus72 = "Накопления по налогу В с возврата продаж в чеке";
    private static String SCashRegisterRus73 = "Накопления по налогу В с возврата покупок в чеке";
    private static String SCashRegisterRus74 = "Накопления по налогу Г с продаж в чеке";
    private static String SCashRegisterRus75 = "Накопления по налогу Г с покупок в чеке";
    private static String SCashRegisterRus76 = "Накопления по налогу Г с возврата продаж в чеке";
    private static String SCashRegisterRus77 = "Накопления по налогу Г с возврата покупок в чеке";
    private static String SCashRegisterRus78 = "Наличность в кассе на момент закрытия чека";
    private static String SCashRegisterRus79 = "Накопление продаж в 1 отдел в смене";
    private static String SCashRegisterRus7A = "Накопление покупок в 1 отдел в смене";
    private static String SCashRegisterRus7B = "Накопление возврата продаж в 1 отдел в смене";
    private static String SCashRegisterRus7C = "Накопление возврата покупок в 1 отдел в смене";
    private static String SCashRegisterRus7D = "Накопление продаж в 2 отдел в смене";
    private static String SCashRegisterRus7E = "Накопление покупок в 2 отдел в смене";
    private static String SCashRegisterRus7F = "Накопление возврата продаж в 2 отдел в смене";
    private static String SCashRegisterRus80 = "Накопление возврата покупок в 2 отдел в смене";
    private static String SCashRegisterRus81 = "Накопление продаж в 3 отдел в смене";
    private static String SCashRegisterRus82 = "Накопление покупок в 3 отдел в смене";
    private static String SCashRegisterRus83 = "Накопление возврата продаж в 3 отдел в смене";
    private static String SCashRegisterRus84 = "Накопление возврата покупок в 3 отдел в смене";
    private static String SCashRegisterRus85 = "Накопление продаж в 4 отдел в смене";
    private static String SCashRegisterRus86 = "Накопление покупок в 4 отдел в смене";
    private static String SCashRegisterRus87 = "Накопление возврата продаж в 4 отдел в смене";
    private static String SCashRegisterRus88 = "Накопление возврата покупок в 4 отдел в смене";
    private static String SCashRegisterRus89 = "Накопление продаж в 5 отдел в смене";
    private static String SCashRegisterRus8A = "Накопление покупок в 5 отдел в смене";
    private static String SCashRegisterRus8B = "Накопление возврата продаж в 5 отдел в смене";
    private static String SCashRegisterRus8C = "Накопление возврата покупок в 5 отдел в смене";
    private static String SCashRegisterRus8D = "Накопление продаж в 6 отдел в смене";
    private static String SCashRegisterRus8E = "Накопление покупок в 6 отдел в смене";
    private static String SCashRegisterRus8F = "Накопление возврата продаж в 6 отдел в смене";
    private static String SCashRegisterRus90 = "Накопление возврата покупок в 6 отдел в смене";
    private static String SCashRegisterRus91 = "Накопление продаж в 7 отдел в смене";
    private static String SCashRegisterRus92 = "Накопление покупок в 7 отдел в смене";
    private static String SCashRegisterRus93 = "Накопление возврата продаж в 7 отдел в смене";
    private static String SCashRegisterRus94 = "Накопление возврата покупок в 7 отдел в смене";
    private static String SCashRegisterRus95 = "Накопление продаж в 8 отдел в смене";
    private static String SCashRegisterRus96 = "Накопление покупок в 8 отдел в смене";
    private static String SCashRegisterRus97 = "Накопление возврата продаж в 8 отдел в смене";
    private static String SCashRegisterRus98 = "Накопление возврата покупок в 8 отдел в смене";
    private static String SCashRegisterRus99 = "Накопление продаж в 9 отдел в смене";
    private static String SCashRegisterRus9A = "Накопление покупок в 9 отдел в смене";
    private static String SCashRegisterRus9B = "Накопление возврата продаж в 9 отдел в смене";
    private static String SCashRegisterRus9C = "Накопление возврата покупок в 9 отдел в смене";
    private static String SCashRegisterRus9D = "Накопление продаж в 10 отдел в смене";
    private static String SCashRegisterRus9E = "Накопление покупок в 10 отдел в смене";
    private static String SCashRegisterRus9F = "Накопление возврата продаж в 10 отдел в смене";
    private static String SCashRegisterRusA0 = "Накопление возврата покупок в 10 отдел в смене";
    private static String SCashRegisterRusA1 = "Накопление продаж в 11 отдел в смене";
    private static String SCashRegisterRusA2 = "Накопление покупок в 11 отдел в смене";
    private static String SCashRegisterRusA3 = "Накопление возврата продаж в 11 отдел в смене";
    private static String SCashRegisterRusA4 = "Накопление возврата покупок в 11 отдел в смене";
    private static String SCashRegisterRusA5 = "Накопление продаж в 12 отдел в смене";
    private static String SCashRegisterRusA6 = "Накопление покупок в 12 отдел в смене";
    private static String SCashRegisterRusA7 = "Накопление возврата продаж в 12 отдел в смене";
    private static String SCashRegisterRusA8 = "Накопление возврата покупок в 12 отдел в смене";
    private static String SCashRegisterRusA9 = "Накопление продаж в 13 отдел в смене";
    private static String SCashRegisterRusAA = "Накопление покупок в 13 отдел в смене";
    private static String SCashRegisterRusAB = "Накопление возврата продаж в 13 отдел в смене";
    private static String SCashRegisterRusAC = "Накопление возврата покупок в 13 отдел в смене";
    private static String SCashRegisterRusAD = "Накопление продаж в 14 отдел в смене";
    private static String SCashRegisterRusAE = "Накопление покупок в 14 отдел в смене";
    private static String SCashRegisterRusAF = "Накопление возврата продаж в 14 отдел в смене";
    private static String SCashRegisterRusB0 = "Накопление возврата покупок в 14 отдел в смене";
    private static String SCashRegisterRusB1 = "Накопление продаж в 15 отдел в смене";
    private static String SCashRegisterRusB2 = "Накопление покупок в 15 отдел в смене";
    private static String SCashRegisterRusB3 = "Накопление возврата продаж в 15 отдел в смене";
    private static String SCashRegisterRusB4 = "Накопление возврата покупок в 15 отдел в смене";
    private static String SCashRegisterRusB5 = "Накопление продаж в 16 отдел в смене";
    private static String SCashRegisterRusB6 = "Накопление покупок в 16 отдел в смене";
    private static String SCashRegisterRusB7 = "Накопление возврата продаж в 16 отдел в смене";
    private static String SCashRegisterRusB8 = "Накопление возврата покупок в 16 отдел в смене";
    private static String SCashRegisterRusB9 = "Накопление скидок с продаж в смене";
    private static String SCashRegisterRusBA = "Накопление скидок с покупок в смене";
    private static String SCashRegisterRusBB = "Накопление скидок с возврата продаж в смене";
    private static String SCashRegisterRusBC = "Накопление скидок с возврата покупок в смене";
    private static String SCashRegisterRusBD = "Накопление надбавок на продажи в смене";
    private static String SCashRegisterRusBE = "Накопление надбавок на покупок в смене";
    private static String SCashRegisterRusBF = "Накопление надбавок на возврата продаж в смене";
    private static String SCashRegisterRusC0 = "Накопление надбавок на возврата покупок в смене";
    private static String SCashRegisterRusC1 = "Накопление оплат продаж наличными в смене";
    private static String SCashRegisterRusC2 = "Накопление оплат покупок наличными в смене";
    private static String SCashRegisterRusC3 = "Накопление оплат возврата продаж наличными в смене";
    private static String SCashRegisterRusC4 = "Накопление оплат возврата покупок наличными в смене";
    private static String SCashRegisterRusC5 = "Накопление оплат продаж видом оплаты 2 в смене";
    private static String SCashRegisterRusC6 = "Накопление оплат покупок видом оплаты 2 в смене";
    private static String SCashRegisterRusC7 = "Накопление оплат возврата продаж видом оплаты 2 в смене";
    private static String SCashRegisterRusC8 = "Накопление оплат возврата покупок видом оплаты 2 в смене";
    private static String SCashRegisterRusC9 = "Накопление оплат продаж видом оплаты 3 в смене";
    private static String SCashRegisterRusCA = "Накопление оплат покупок видом оплаты 3 в смене";
    private static String SCashRegisterRusCB = "Накопление оплат возврата продаж видом оплаты 3 в смене";
    private static String SCashRegisterRusCC = "Накопление оплат возврата покупок видом оплаты 3 в смене";
    private static String SCashRegisterRusCD = "Накопление оплат продаж видом оплаты 4 в смене";
    private static String SCashRegisterRusCE = "Накопление оплат покупок видом оплаты 4 в смене";
    private static String SCashRegisterRusCF = "Накопление оплат возврата продаж видом оплаты 4 в смене";
    private static String SCashRegisterRusD0 = "Накопление оплат возврата покупок видом оплаты 4 в смене";
    private static String SCashRegisterRusD1 = "Оборот по налогу А с продаж в смене";
    private static String SCashRegisterRusD2 = "Оборот по налогу А с покупок в смене";
    private static String SCashRegisterRusD3 = "Оборот по налогу А с возврата продаж в смене";
    private static String SCashRegisterRusD4 = "Оборот по налогу А с возврата покупок в смене";
    private static String SCashRegisterRusD5 = "Оборот по налогу Б с продаж в смене";
    private static String SCashRegisterRusD6 = "Оборот по налогу Б с покупок в смене";
    private static String SCashRegisterRusD7 = "Оборот по налогу Б с возврата продаж в смене";
    private static String SCashRegisterRusD8 = "Оборот по налогу Б с возврата покупок в смене";
    private static String SCashRegisterRusD9 = "Оборот по налогу В с продаж в смене";
    private static String SCashRegisterRusDA = "Оборот по налогу В с покупок в смене";
    private static String SCashRegisterRusDB = "Оборот по налогу В с возврата продаж в смене";
    private static String SCashRegisterRusDC = "Оборот по налогу В с возврата покупок в смене";
    private static String SCashRegisterRusDD = "Оборот по налогу Г с продаж в смене";
    private static String SCashRegisterRusDE = "Оборот по налогу Г с покупок в смене";
    private static String SCashRegisterRusDF = "Оборот по налогу Г с возврата продаж в смене";
    private static String SCashRegisterRusE0 = "Оборот по налогу Г с возврата покупок в смене";
    private static String SCashRegisterRusE1 = "Накопления по налогу А с продаж в смене";
    private static String SCashRegisterRusE2 = "Накопления по налогу А с покупок в смене";
    private static String SCashRegisterRusE3 = "Накопления по налогу А с возврата продаж в смене";
    private static String SCashRegisterRusE4 = "Накопления по налогу А с возврата покупок в смене";
    private static String SCashRegisterRusE5 = "Накопления по налогу Б с продаж в смене";
    private static String SCashRegisterRusE6 = "Накопления по налогу Б с покупок в смене";
    private static String SCashRegisterRusE7 = "Накопления по налогу Б с возврата продаж в смене";
    private static String SCashRegisterRusE8 = "Накопления по налогу Б с возврата покупок в смене";
    private static String SCashRegisterRusE9 = "Накопления по налогу В с продаж в смене";
    private static String SCashRegisterRusEA = "Накопления по налогу В с покупок в смене";
    private static String SCashRegisterRusEB = "Накопления по налогу В с возврата продаж в смене";
    private static String SCashRegisterRusEC = "Накопления по налогу В с возврата покупок в смене";
    private static String SCashRegisterRusED = "Накопления по налогу Г с продаж в смене";
    private static String SCashRegisterRusEE = "Накопления по налогу Г с покупок в смене";
    private static String SCashRegisterRusEF = "Накопления по налогу Г с возврата продаж в смене";
    private static String SCashRegisterRusF0 = "Накопления по налогу Г с возврата покупок в смене";
    private static String SCashRegisterRusF1 = "Накопление наличности в кассе";
    private static String SCashRegisterRusF2 = "Накопление внесений за смену";
    private static String SCashRegisterRusF3 = "Накопление выплат за смену";
    private static String SCashRegisterRusF4 = "Необнуляемая сумма до фискализации";
    private static String SCashRegisterRusF5 = "Сумма продаж в смене из ЭКЛЗ";
    private static String SCashRegisterRusF6 = "Сумма покупок в смене из ЭКЛЗ";
    private static String SCashRegisterRusF7 = "Сумма возвратов продаж в смене из ЭКЛЗ";
    private static String SCashRegisterRusF8 = "Сумма возвратов покупок в смене из ЭКЛЗ";
    private static String SCashRegisterRusF9 = "Сумма аннулированных продаж в смене";
    private static String SCashRegisterRusFA = "Сумма аннулированных покупок в смене";
    private static String SCashRegisterRusFB = "Сумма аннулированных возвратов продаж в смене";
    private static String SCashRegisterRusFC = "Сумма аннулированных возвратов покупок в смене";

    public static String getEngName(int number) {
        switch (number) {
            case 0:
                return "Sales accumulation in 1 department in receipt";
            case 1:
                return "Buys accumulation in 1 department in receipt";
            case 2:
                return "Sales refund accumulation in 1 department in receipt";
            case 3:
                return "Buys refund accumulation in 1 department in receipt";
            case 4:
                return "Sales accumulation in 2 department in receipt";
            case 5:
                return "Buys accumulation in 2 department in receipt";
            case 6:
                return "Sales refund accumulation in 2 department in receipt";
            case 7:
                return "Buys refund accumulation in 2 department in receipt";
            case 8:
                return "Sales accumulation in 3 department in receipt";
            case 9:
                return "Buys accumulation in 3 department in receipt";
            case 10:
                return "Sales refund accumulation in 3 department in receipt";
            case 11:
                return "Buys refund accumulation in 3 department in receipt";
            case 12:
                return "Sales accumulation in 4 department in receipt";
            case 13:
                return "Buys accumulation in 4 department in receipt";
            case 14:
                return "Sales refund accumulation in 4 department in receipt";
            case 15:
                return "Buys refund accumulation in 4 department in receipt";
            case 16:
                return "Sales accumulation in 5 department in receipt";
            case 17:
                return "Buys accumulation in 5 department in receipt";
            case 18:
                return "Sales refund accumulation in 5 department in receipt";
            case 19:
                return "Buys refund accumulation in 5 department in receipt";
            case 20:
                return "Sales accumulation in 6 department in receipt";
            case 21:
                return "Buys accumulation in 6 department in receipt";
            case 22:
                return "Sales refund accumulation in 6 department in receipt";
            case 23:
                return "Buys refund accumulation in 6 department in receipt";
            case 24:
                return "Sales accumulation in 7 department in receipt";
            case 25:
                return "Buys accumulation in 7 department in receipt";
            case 26:
                return "Sales refund accumulation in 7 department in receipt";
            case 27:
                return "Buys refund accumulation in 7 department in receipt";
            case 28:
                return "Sales accumulation in 8 department in receipt";
            case 29:
                return "Buys accumulation in 8 department in receipt";
            case 30:
                return "Sales refund accumulation in 8 department in receipt";
            case 31:
                return "Buys refund accumulation in 8 department in receipt";
            case 32:
                return "Sales accumulation in 9 department in receipt";
            case 33:
                return "Buys accumulation in 9 department in receipt";
            case 34:
                return "Sales refund accumulation in 9 department in receipt";
            case 35:
                return "Buys refund accumulation in 9 department in receipt";
            case 36:
                return "Sales accumulation in 10 department in receipt";
            case 37:
                return "Buys accumulation in 10 department in receipt";
            case 38:
                return "Sales refund accumulation in 10 department in receipt";
            case 39:
                return "Buys refund accumulation in 10 department in receipt";
            case 40:
                return "Sales accumulation in 11 department in receipt";
            case 41:
                return "Buys accumulation in 11 department in receipt";
            case 42:
                return "Sales refund accumulation in 11 department in receipt";
            case 43:
                return "Buys refund accumulation in 11 department in receipt";
            case 44:
                return "Sales accumulation in 12 department in receipt";
            case 45:
                return "Buys accumulation in 12 department in receipt";
            case 46:
                return "Sales refund accumulation in 12 department in receipt";
            case 47:
                return "Buys refund accumulation in 12 department in receipt";
            case 48:
                return "Sales accumulation in 13 department in receipt";
            case 49:
                return "Buys accumulation in 13 department in receipt";
            case 50:
                return "Sales refund accumulation in 13 department in receipt";
            case 51:
                return "Buys refund accumulation in 13 department in receipt";
            case 52:
                return "Sales accumulation in 14 department in receipt";
            case 53:
                return "Buys accumulation in 14 department in receipt";
            case 54:
                return "Sales refund accumulation in 14 department in receipt";
            case 55:
                return "Buys refund accumulation in 14 department in receipt";
            case 56:
                return "Sales accumulation in 15 department in receipt";
            case 57:
                return "Buys accumulation in 15 department in receipt";
            case 58:
                return "Sales refund accumulation in 15 department in receipt";
            case 59:
                return "Buys refund accumulation in 15 department in receipt";
            case 60:
                return "Sales accumulation in 16 department in receipt";
            case 61:
                return "Buys accumulation in 16 department in receipt";
            case 62:
                return "Sales refund accumulation in 16 department in receipt";
            case 63:
                return "Buys refund accumulation in 16 department in receipt";
            case 64:
                return "Discounts accumulation from sales in receipt";
            case 65:
                return "Discounts accumulation from buys in receipt";
            case 66:
                return "Discounts accumulation from sale refunds in receipt";
            case 67:
                return "Discounts accumulation from buy refunds in receipt";
            case 68:
                return "Charges accumulation on sales in receipt";
            case 69:
                return "Charges accumulation on buys in receipt";
            case 70:
                return "Charges accumulation on sale refunds in receipt";
            case 71:
                return "Charges accumulation on buy refunds in receipt";
            case 72:
                return "Cash payment accumulation of sales in receipt";
            case 73:
                return "Cash payment accumulation of buys in receipt";
            case 74:
                return "Cash payment accumulation of sale refunds in receipt";
            case 75:
                return "Cash payment accumulation of buy refunds in receipt";
            case 76:
                return "Payment type 2 accumulation of sales in receipt";
            case 77:
                return "Payment type 2 accumulation of buys in receipt";
            case 78:
                return "Payment type 2 accumulation of sale refunds in receipt";
            case 79:
                return "Payment type 2 accumulation of buy refunds in receipt";
            case 80:
                return "Payment type 3 accumulation of sales in receipt";
            case 81:
                return "Payment type 3 accumulation of buys in receipt";
            case 82:
                return "Payment type 3 accumulation of sale refunds in receipt";
            case 83:
                return "Payment type 3 accumulation of buy refunds in receipt";
            case 84:
                return "Payment type 4 accumulation of sales in receipt";
            case 85:
                return "Payment type 4 accumulation of buys in receipt";
            case 86:
                return "Payment type 4 accumulation of sale refunds in receipt";
            case 87:
                return "Payment type 4 accumulation of buy refunds in receipt";
            case 88:
                return "Tax A turnover of sales in receipt";
            case 89:
                return "Tax A turnover of buys in receipt";
            case 90:
                return "Tax A turnover of sale refunds in receipt";
            case 91:
                return "Tax A turnover of buy refunds in receipt";
            case 92:
                return "Tax B turnover of sales in receipt";
            case 93:
                return "Tax B turnover of buys in receipt";
            case 94:
                return "Tax B turnover of sale refunds in receipt";
            case 95:
                return "Tax B turnover of buy refunds in receipt";
            case 96:
                return "Tax C turnover of sales in receipt";
            case 97:
                return "Tax C turnover of buys in receipt";
            case 98:
                return "Tax C turnover of sale refunds in receipt";
            case 99:
                return "Tax C turnover of buy refunds in receipt";
            case 100:
                return "Tax D turnover of sales in receipt";
            case 101:
                return "Tax D turnover of buys in receipt";
            case 102:
                return "Tax D turnover of sale refunds in receipt";
            case 103:
                return "Tax D turnover of buy refunds in receipt in receipt";
            case 104:
                return "Tax A accumulations of sales in receipt";
            case 105:
                return "Tax A accumulations of buys in receipt";
            case 106:
                return "Tax A accumulations of sale refunds in receipt";
            case 107:
                return "Tax A accumulations of buy refunds in receipt";
            case 108:
                return "Tax B accumulations of sales in receipt";
            case 109:
                return "Tax B accumulations of buys in receipt";
            case 110:
                return "Tax B accumulations of sale refunds in receipt";
            case 111:
                return "Tax B accumulations of buy refunds in receipt";
            case 112:
                return "Tax C accumulations of sales in receipt";
            case 113:
                return "Tax C accumulations of buys in receipt";
            case 114:
                return "Tax C accumulations of sale refunds in receipt";
            case 115:
                return "Tax C accumulations of buy refunds in receipt";
            case 116:
                return "Tax D accumulations of sales in receipt";
            case 117:
                return "Tax D accumulations of buys in receipt";
            case 118:
                return "Tax D accumulations of sale refunds in receipt";
            case 119:
                return "Tax D accumulations of buy refunds in receipt";
            case 120:
                return "Cash total in ECR at receipt closing moment";
            case 121:
                return "Sales accumulation in 1 department in shift";
            case 122:
                return "Buys accumulation in 1 department in shift";
            case 123:
                return "Sales refund accumulation in 1 department in shift";
            case 124:
                return "Buys refund accumulation in 1 department in shift";
            case 125:
                return "Sales accumulation in 2 department in shift";
            case 126:
                return "Buys accumulation in 2 department in shift";
            case 127:
                return "Sales refund accumulation in 2 department in shift";
            case 128:
                return "Buys refund accumulation in 2 department in shift";
            case 129:
                return "Sales accumulation in 3 department in shift";
            case 130:
                return "Buys accumulation in 3 department in shift";
            case 131:
                return "Sales refund accumulation in 3 department in shift";
            case 132:
                return "Buys refund accumulation in 3 department in shift";
            case 133:
                return "Sales accumulation in 4 department in shift";
            case 134:
                return "Buys accumulation in 4 department in shift";
            case 135:
                return "Sales refund accumulation in 4 department in shift";
            case 136:
                return "Buys refund accumulation in 4 department in shift";
            case 137:
                return "Sales accumulation in 5 department in shift";
            case 138:
                return "Buys accumulation in 5 department in shift";
            case 139:
                return "Sales refund accumulation in 5 department in shift";
            case 140:
                return "Buys refund accumulation in 5 department in shift";
            case 141:
                return "Sales accumulation in 6 department in shift";
            case 142:
                return "Buys accumulation in 6 department in shift";
            case 143:
                return "Sales refund accumulation in 6 department in shift";
            case 144:
                return "Buys refund accumulation in 6 department in shift";
            case 145:
                return "Sales accumulation in 7 department in shift";
            case 146:
                return "Buys accumulation in 7 department in shift";
            case 147:
                return "Sales refund accumulation in 7 department in shift";
            case 148:
                return "Buys refund accumulation in 7 department in shift";
            case 149:
                return "Sales accumulation in 8 department in shift";
            case 150:
                return "Buys accumulation in 8 department in shift";
            case 151:
                return "Sales refund accumulation in 8 department in shift";
            case 152:
                return "Buys refund accumulation in 8 department in shift";
            case 153:
                return "Sales accumulation in 9 department in shift";
            case 154:
                return "Buys accumulation in 9 department in shift";
            case 155:
                return "Sales refund accumulation in 9 department in shift";
            case 156:
                return "Buys refund accumulation in 9 department in shift";
            case 157:
                return "Sales accumulation in 10 department in shift";
            case 158:
                return "Buys accumulation in 10 department in shift";
            case 159:
                return "Sales refund accumulation in 10 department in shift";
            case 160:
                return "Buys refund accumulation in 10 department in shift";
            case 161:
                return "Sales accumulation in 11 department in shift";
            case 162:
                return "Buys accumulation in 11 department in shift";
            case 163:
                return "Sales refund accumulation in 11 department in shift";
            case 164:
                return "Buys refund accumulation in 11 department in shift";
            case 165:
                return "Sales accumulation in 12 department in shift";
            case 166:
                return "Buys accumulation in 12 department in shift";
            case 167:
                return "Sales refund accumulation in 12 department in shift";
            case 168:
                return "Buys refund accumulation in 12 department in shift";
            case 169:
                return "Sales accumulation in 13 department in receipt";
            case 170:
                return "Buys accumulation in 13 department in receipt";
            case 171:
                return "Sales refund accumulation in 13 department in receipt";
            case 172:
                return "Buys refund accumulation in 13 department in receipt";
            case 173:
                return "Sales accumulation in 14 department in shift";
            case 174:
                return "Buys accumulation in 14 department in shift";
            case 175:
                return "Sales refund accumulation in 14 department in shift";
            case 176:
                return "Buys refund accumulation in 14 department in shift";
            case 177:
                return "Sales accumulation in 15 department in shift";
            case 178:
                return "Buys accumulation in 15 department in shift";
            case 179:
                return "Sales refund accumulation in 15 department in shift";
            case 180:
                return "Buys refund accumulation in 15 department in shift";
            case 181:
                return "Sales accumulation in 16 department in shift";
            case 182:
                return "Buys accumulation in 16 department in shift";
            case 183:
                return "Sales refund accumulation in 16 department in shift";
            case 184:
                return "Buys refund accumulation in 16 department in shift";
            case 185:
                return "Discounts accumulation on sales in shift";
            case 186:
                return "Discounts accumulation on buys in shift";
            case 187:
                return "Discounts accumulation on sale refunds in shift";
            case 188:
                return "Discounts accumulation on buy refunds in shift";
            case 189:
                return "Charges accumulation on sales in shift";
            case 190:
                return "Charges accumulation on buys in shift";
            case 191:
                return "Charges accumulation on sale refunds in shift";
            case 192:
                return "Charges accumulation on buy refunds in shift";
            case 193:
                return "Cash payment accumulation of sales in shift";
            case 194:
                return "Cash payment accumulation of buys in shift";
            case 195:
                return "Cash payment accumulation of sale refunds shift";
            case 196:
                return "Cash payment accumulation of buy refunds in shift";
            case 197:
                return "Payment type 2 accumulation of sales in shift";
            case 198:
                return "Payment type 2 accumulation of buys in shift";
            case 199:
                return "Payment type 2 accumulation of sale refunds in shift";
            case 200:
                return "Payment type 2 accumulation of buy refunds in shift";
            case 201:
                return "Payment type 3 accumulation of sales in shift";
            case 202:
                return "Payment type 3 accumulation of buys in shift";
            case 203:
                return "Payment type 3 accumulation of sale refunds in shift";
            case 204:
                return "Payment type 3 accumulation of buy refunds in shift";
            case 205:
                return "Payment type 4 accumulation of sales in shift";
            case 206:
                return "Payment type 4 accumulation of buys in shift";
            case 207:
                return "Payment type 4 accumulation of sale refunds in shift";
            case 208:
                return "Payment type 4 accumulation of buy refunds in shift";
            case 209:
                return "Tax A turnover of sales in shift";
            case 210:
                return "Tax A turnover of buys in shift";
            case 211:
                return "Tax A turnover of sale refunds in shift";
            case 212:
                return "Tax A turnover of buy refunds in shift";
            case 213:
                return "Tax B turnover of sales in shift";
            case 214:
                return "Tax B turnover of buys in shift";
            case 215:
                return "Tax B turnover of sale refunds in shift";
            case 216:
                return "Tax B turnover of buy refunds in shift";
            case 217:
                return "Tax C turnover of sales in shift";
            case 218:
                return "Tax C turnover of buys in shift";
            case 219:
                return "Tax C turnover of sale refunds in shift";
            case 220:
                return "Tax C turnover of buy refunds in shift";
            case 221:
                return "Tax D turnover of sales in shift";
            case 222:
                return "Tax D turnover of buys in shift";
            case 223:
                return "Tax D turnover of sale refunds in shift";
            case 224:
                return "Tax D turnover of buy refunds in shift";
            case 225:
                return "Tax A accumulations of sales in shift";
            case 226:
                return "Tax A accumulations of buys in shift";
            case 227:
                return "Tax A accumulations of sale refunds in shift";
            case 228:
                return "Tax A accumulations of buy refunds in shift";
            case 229:
                return "Tax B accumulations of sales in shift";
            case 230:
                return "Tax B accumulations of buys in shift";
            case 231:
                return "Tax B accumulations of sale refunds in shift";
            case 232:
                return "Tax B accumulations of buy refunds in shift";
            case 233:
                return "Tax C accumulations of sales in shift";
            case 234:
                return "Tax C accumulations of buys in shift";
            case 235:
                return "Tax C accumulations of sale refunds in shift";
            case 236:
                return "Tax C accumulations of buy refunds in shift";
            case 237:
                return "Tax D accumulations of sales in shift";
            case 238:
                return "Tax D accumulations of buys in shift";
            case 239:
                return "Tax D accumulations of sale refunds in shift";
            case 240:
                return "Tax D accumulations of buy refunds in shift";
            case 241:
                return "Cash total in ECR accumulation";
            case 242:
                return "Cash in accumulation in shift";
            case 243:
                return "Cash out accumulation in shift";
            case 244:
                return "Non-zeroise sum before fiscalization";
            case 245:
                return "Sales total in shift from EJ";
            case 246:
                return "Buys total in shift from EJ";
            case 247:
                return "Sale refunds total in shift from EJ";
            case 248:
                return "Buy refunds total in shift from EJ";
            case 249:
                return "Daily voided sale receipts total";
            case 250:
                return "Daily voided buy receipts total";
            case 251:
                return "Daily voided sale return receipts total";
            case 252:
                return "Daily voided buy return receipts total";
            default:
                return "Unknown register";
        }
    }

    public static String getRusName(int number) {
        switch (number) {
            case 0x00:
                return SCashRegisterRus00;
            case 0x01:
                return SCashRegisterRus01;
            case 0x02:
                return SCashRegisterRus02;
            case 0x03:
                return SCashRegisterRus03;
            case 0x04:
                return SCashRegisterRus04;
            case 0x05:
                return SCashRegisterRus05;
            case 0x06:
                return SCashRegisterRus06;
            case 0x07:
                return SCashRegisterRus07;
            case 0x08:
                return SCashRegisterRus08;
            case 0x09:
                return SCashRegisterRus09;
            case 0x0A:
                return SCashRegisterRus0A;
            case 0x0B:
                return SCashRegisterRus0B;
            case 0x0C:
                return SCashRegisterRus0C;
            case 0x0D:
                return SCashRegisterRus0D;
            case 0x0E:
                return SCashRegisterRus0E;
            case 0x0F:
                return SCashRegisterRus0F;
            case 0x10:
                return SCashRegisterRus10;
            case 0x11:
                return SCashRegisterRus11;
            case 0x12:
                return SCashRegisterRus12;
            case 0x13:
                return SCashRegisterRus13;
            case 0x14:
                return SCashRegisterRus14;
            case 0x15:
                return SCashRegisterRus15;
            case 0x16:
                return SCashRegisterRus16;
            case 0x17:
                return SCashRegisterRus17;
            case 0x18:
                return SCashRegisterRus18;
            case 0x19:
                return SCashRegisterRus19;
            case 0x1A:
                return SCashRegisterRus1A;
            case 0x1B:
                return SCashRegisterRus1B;
            case 0x1C:
                return SCashRegisterRus1C;
            case 0x1D:
                return SCashRegisterRus1D;
            case 0x1E:
                return SCashRegisterRus1E;
            case 0x1F:
                return SCashRegisterRus1F;
            case 0x20:
                return SCashRegisterRus20;
            case 0x21:
                return SCashRegisterRus21;
            case 0x22:
                return SCashRegisterRus22;
            case 0x23:
                return SCashRegisterRus23;
            case 0x24:
                return SCashRegisterRus24;
            case 0x25:
                return SCashRegisterRus25;
            case 0x26:
                return SCashRegisterRus26;
            case 0x27:
                return SCashRegisterRus27;
            case 0x28:
                return SCashRegisterRus28;
            case 0x29:
                return SCashRegisterRus29;
            case 0x2A:
                return SCashRegisterRus2A;
            case 0x2B:
                return SCashRegisterRus2B;
            case 0x2C:
                return SCashRegisterRus2C;
            case 0x2D:
                return SCashRegisterRus2D;
            case 0x2E:
                return SCashRegisterRus2E;
            case 0x2F:
                return SCashRegisterRus2F;
            case 0x30:
                return SCashRegisterRus30;
            case 0x31:
                return SCashRegisterRus31;
            case 0x32:
                return SCashRegisterRus32;
            case 0x33:
                return SCashRegisterRus33;
            case 0x34:
                return SCashRegisterRus34;
            case 0x35:
                return SCashRegisterRus35;
            case 0x36:
                return SCashRegisterRus36;
            case 0x37:
                return SCashRegisterRus37;
            case 0x38:
                return SCashRegisterRus38;
            case 0x39:
                return SCashRegisterRus39;
            case 0x3A:
                return SCashRegisterRus3A;
            case 0x3B:
                return SCashRegisterRus3B;
            case 0x3C:
                return SCashRegisterRus3C;
            case 0x3D:
                return SCashRegisterRus3D;
            case 0x3E:
                return SCashRegisterRus3E;
            case 0x3F:
                return SCashRegisterRus3F;
            case 0x40:
                return SCashRegisterRus40;
            case 0x41:
                return SCashRegisterRus41;
            case 0x42:
                return SCashRegisterRus42;
            case 0x43:
                return SCashRegisterRus43;
            case 0x44:
                return SCashRegisterRus44;
            case 0x45:
                return SCashRegisterRus45;
            case 0x46:
                return SCashRegisterRus46;
            case 0x47:
                return SCashRegisterRus47;
            case 0x48:
                return SCashRegisterRus48;
            case 0x49:
                return SCashRegisterRus49;
            case 0x4A:
                return SCashRegisterRus4A;
            case 0x4B:
                return SCashRegisterRus4B;
            case 0x4C:
                return SCashRegisterRus4C;
            case 0x4D:
                return SCashRegisterRus4D;
            case 0x4E:
                return SCashRegisterRus4E;
            case 0x4F:
                return SCashRegisterRus4F;
            case 0x50:
                return SCashRegisterRus50;
            case 0x51:
                return SCashRegisterRus51;
            case 0x52:
                return SCashRegisterRus52;
            case 0x53:
                return SCashRegisterRus53;
            case 0x54:
                return SCashRegisterRus54;
            case 0x55:
                return SCashRegisterRus55;
            case 0x56:
                return SCashRegisterRus56;
            case 0x57:
                return SCashRegisterRus57;
            case 0x58:
                return SCashRegisterRus58;
            case 0x59:
                return SCashRegisterRus59;
            case 0x5A:
                return SCashRegisterRus5A;
            case 0x5B:
                return SCashRegisterRus5B;
            case 0x5C:
                return SCashRegisterRus5C;
            case 0x5D:
                return SCashRegisterRus5D;
            case 0x5E:
                return SCashRegisterRus5E;
            case 0x5F:
                return SCashRegisterRus5F;
            case 0x60:
                return SCashRegisterRus60;
            case 0x61:
                return SCashRegisterRus61;
            case 0x62:
                return SCashRegisterRus62;
            case 0x63:
                return SCashRegisterRus63;
            case 0x64:
                return SCashRegisterRus64;
            case 0x65:
                return SCashRegisterRus65;
            case 0x66:
                return SCashRegisterRus66;
            case 0x67:
                return SCashRegisterRus67;
            case 0x68:
                return SCashRegisterRus68;
            case 0x69:
                return SCashRegisterRus69;
            case 0x6A:
                return SCashRegisterRus6A;
            case 0x6B:
                return SCashRegisterRus6B;
            case 0x6C:
                return SCashRegisterRus6C;
            case 0x6D:
                return SCashRegisterRus6D;
            case 0x6E:
                return SCashRegisterRus6E;
            case 0x6F:
                return SCashRegisterRus6F;
            case 0x70:
                return SCashRegisterRus70;
            case 0x71:
                return SCashRegisterRus71;
            case 0x72:
                return SCashRegisterRus72;
            case 0x73:
                return SCashRegisterRus73;
            case 0x74:
                return SCashRegisterRus74;
            case 0x75:
                return SCashRegisterRus75;
            case 0x76:
                return SCashRegisterRus76;
            case 0x77:
                return SCashRegisterRus77;
            case 0x78:
                return SCashRegisterRus78;
            case 0x79:
                return SCashRegisterRus79;
            case 0x7A:
                return SCashRegisterRus7A;
            case 0x7B:
                return SCashRegisterRus7B;
            case 0x7C:
                return SCashRegisterRus7C;
            case 0x7D:
                return SCashRegisterRus7D;
            case 0x7E:
                return SCashRegisterRus7E;
            case 0x7F:
                return SCashRegisterRus7F;
            case 0x80:
                return SCashRegisterRus80;
            case 0x81:
                return SCashRegisterRus81;
            case 0x82:
                return SCashRegisterRus82;
            case 0x83:
                return SCashRegisterRus83;
            case 0x84:
                return SCashRegisterRus84;
            case 0x85:
                return SCashRegisterRus85;
            case 0x86:
                return SCashRegisterRus86;
            case 0x87:
                return SCashRegisterRus87;
            case 0x88:
                return SCashRegisterRus88;
            case 0x89:
                return SCashRegisterRus89;
            case 0x8A:
                return SCashRegisterRus8A;
            case 0x8B:
                return SCashRegisterRus8B;
            case 0x8C:
                return SCashRegisterRus8C;
            case 0x8D:
                return SCashRegisterRus8D;
            case 0x8E:
                return SCashRegisterRus8E;
            case 0x8F:
                return SCashRegisterRus8F;
            case 0x90:
                return SCashRegisterRus90;
            case 0x91:
                return SCashRegisterRus91;
            case 0x92:
                return SCashRegisterRus92;
            case 0x93:
                return SCashRegisterRus93;
            case 0x94:
                return SCashRegisterRus94;
            case 0x95:
                return SCashRegisterRus95;
            case 0x96:
                return SCashRegisterRus96;
            case 0x97:
                return SCashRegisterRus97;
            case 0x98:
                return SCashRegisterRus98;
            case 0x99:
                return SCashRegisterRus99;
            case 0x9A:
                return SCashRegisterRus9A;
            case 0x9B:
                return SCashRegisterRus9B;
            case 0x9C:
                return SCashRegisterRus9C;
            case 0x9D:
                return SCashRegisterRus9D;
            case 0x9E:
                return SCashRegisterRus9E;
            case 0x9F:
                return SCashRegisterRus9F;
            case 0xA0:
                return SCashRegisterRusA0;
            case 0xA1:
                return SCashRegisterRusA1;
            case 0xA2:
                return SCashRegisterRusA2;
            case 0xA3:
                return SCashRegisterRusA3;
            case 0xA4:
                return SCashRegisterRusA4;
            case 0xA5:
                return SCashRegisterRusA5;
            case 0xA6:
                return SCashRegisterRusA6;
            case 0xA7:
                return SCashRegisterRusA7;
            case 0xA8:
                return SCashRegisterRusA8;
            case 0xA9:
                return SCashRegisterRusA9;
            case 0xAA:
                return SCashRegisterRusAA;
            case 0xAB:
                return SCashRegisterRusAB;
            case 0xAC:
                return SCashRegisterRusAC;
            case 0xAD:
                return SCashRegisterRusAD;
            case 0xAE:
                return SCashRegisterRusAE;
            case 0xAF:
                return SCashRegisterRusAF;
            case 0xB0:
                return SCashRegisterRusB0;
            case 0xB1:
                return SCashRegisterRusB1;
            case 0xB2:
                return SCashRegisterRusB2;
            case 0xB3:
                return SCashRegisterRusB3;
            case 0xB4:
                return SCashRegisterRusB4;
            case 0xB5:
                return SCashRegisterRusB5;
            case 0xB6:
                return SCashRegisterRusB6;
            case 0xB7:
                return SCashRegisterRusB7;
            case 0xB8:
                return SCashRegisterRusB8;
            case 0xB9:
                return SCashRegisterRusB9;
            case 0xBA:
                return SCashRegisterRusBA;
            case 0xBB:
                return SCashRegisterRusBB;
            case 0xBC:
                return SCashRegisterRusBC;
            case 0xBD:
                return SCashRegisterRusBD;
            case 0xBE:
                return SCashRegisterRusBE;
            case 0xBF:
                return SCashRegisterRusBF;
            case 0xC0:
                return SCashRegisterRusC0;
            case 0xC1:
                return SCashRegisterRusC1;
            case 0xC2:
                return SCashRegisterRusC2;
            case 0xC3:
                return SCashRegisterRusC3;
            case 0xC4:
                return SCashRegisterRusC4;
            case 0xC5:
                return SCashRegisterRusC5;
            case 0xC6:
                return SCashRegisterRusC6;
            case 0xC7:
                return SCashRegisterRusC7;
            case 0xC8:
                return SCashRegisterRusC8;
            case 0xC9:
                return SCashRegisterRusC9;
            case 0xCA:
                return SCashRegisterRusCA;
            case 0xCB:
                return SCashRegisterRusCB;
            case 0xCC:
                return SCashRegisterRusCC;
            case 0xCD:
                return SCashRegisterRusCD;
            case 0xCE:
                return SCashRegisterRusCE;
            case 0xCF:
                return SCashRegisterRusCF;
            case 0xD0:
                return SCashRegisterRusD0;
            case 0xD1:
                return SCashRegisterRusD1;
            case 0xD2:
                return SCashRegisterRusD2;
            case 0xD3:
                return SCashRegisterRusD3;
            case 0xD4:
                return SCashRegisterRusD4;
            case 0xD5:
                return SCashRegisterRusD5;
            case 0xD6:
                return SCashRegisterRusD6;
            case 0xD7:
                return SCashRegisterRusD7;
            case 0xD8:
                return SCashRegisterRusD8;
            case 0xD9:
                return SCashRegisterRusD9;
            case 0xDA:
                return SCashRegisterRusDA;
            case 0xDB:
                return SCashRegisterRusDB;
            case 0xDC:
                return SCashRegisterRusDC;
            case 0xDD:
                return SCashRegisterRusDD;
            case 0xDE:
                return SCashRegisterRusDE;
            case 0xDF:
                return SCashRegisterRusDF;
            case 0xE0:
                return SCashRegisterRusE0;
            case 0xE1:
                return SCashRegisterRusE1;
            case 0xE2:
                return SCashRegisterRusE2;
            case 0xE3:
                return SCashRegisterRusE3;
            case 0xE4:
                return SCashRegisterRusE4;
            case 0xE5:
                return SCashRegisterRusE5;
            case 0xE6:
                return SCashRegisterRusE6;
            case 0xE7:
                return SCashRegisterRusE7;
            case 0xE8:
                return SCashRegisterRusE8;
            case 0xE9:
                return SCashRegisterRusE9;
            case 0xEA:
                return SCashRegisterRusEA;
            case 0xEB:
                return SCashRegisterRusEB;
            case 0xEC:
                return SCashRegisterRusEC;
            case 0xED:
                return SCashRegisterRusED;
            case 0xEE:
                return SCashRegisterRusEE;
            case 0xEF:
                return SCashRegisterRusEF;
            case 0xF0:
                return SCashRegisterRusF0;
            case 0xF1:
                return SCashRegisterRusF1;
            case 0xF2:
                return SCashRegisterRusF2;
            case 0xF3:
                return SCashRegisterRusF3;
            case 0xF4:
                return SCashRegisterRusF4;
            case 0xF5:
                return SCashRegisterRusF5;
            case 0xF6:
                return SCashRegisterRusF6;
            case 0xF7:
                return SCashRegisterRusF7;
            case 0xF8:
                return SCashRegisterRusF8;
            case 0xF9:
                return SCashRegisterRusF9;
            case 0xFA:
                return SCashRegisterRusFA;
            case 0xFB:
                return SCashRegisterRusFB;
            case 0xFC:
                return SCashRegisterRusFC;
            default:
                return "Unknown register";
        }
    }
}
