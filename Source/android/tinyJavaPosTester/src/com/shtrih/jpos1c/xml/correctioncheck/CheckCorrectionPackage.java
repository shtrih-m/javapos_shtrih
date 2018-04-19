package com.shtrih.jpos1c.xml.correctioncheck;

import com.shtrih.jpos1c.xml.Xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Текст в формате XML содержит описание передаваемого для печати чека коррекции.
 */
@Root(name = "CheckCorrectionPackage")
public class CheckCorrectionPackage {
    
    /**
     * Формирование нового чека с заданным атрибутами
     */
    @Element
    public Parameters Parameters;

    /**
     * Параметры закрытия чека. Чек коррекции может быть оплачен только одним видом оплаты
     * и без сдачи.
     */
    @Element
    public Payments Payments;

    public CheckCorrectionPackage(){
        Parameters = new Parameters();
        Payments = new Payments();
    }

    public String toXml() throws Exception {
        return Xml.serialize(this);
    }
}
