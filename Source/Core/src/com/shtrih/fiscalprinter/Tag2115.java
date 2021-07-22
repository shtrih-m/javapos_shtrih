/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

/**
 *
 * @author Виталий
 */
import java.util.zip.CRC32;

/*
 Реквизит «контрольный код КМ» (тег 2115) должен принимать 
 значение младших четырех цифр от десятичного значения контрольной 
 суммы CRC-32, рассчитанной на базе значения реквизита «код маркировки» 
 (тег 2000). Вычисление значения CRC-32 выполняются в соответствии с 
 параметрами:

 Рассчитывается в соответствии с CRC-32 IEEE 802.3
 Вычисление значения CRC-32 выполняется с параметрами алгоритма:
 Width = 32 bits;
 Truncated polynomial = 0x04C11DB7  
 (x^32 + x^26 + x^23 + x^22 + x^16 + x^12 + x^11+ x^10 + x^8 + x^7 + x^5 + x^4 + x^2 + x + 1);
 Initial value = 0xFFFFFFFF

 */
public class Tag2115 {

    public Tag2115() {
    }

    public static final String getValue(byte[] mc) throws Exception {
        CRC32 crc = new CRC32();
        crc.reset();
        crc.update(mc);
        String s = String.format("%04d", crc.getValue());
        return s.substring(s.length() - 4);
    }
    
    public static final String getValue(String mc) throws Exception {
        return getValue(mc.getBytes());
    }
}
